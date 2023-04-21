package org.dcm4chee.xero.wado;

import static org.dcm4chee.xero.wado.WadoParams.CONTENT_DISPOSITION;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.UID;
import org.dcm4chee.xero.instrumentation.IInstrumentationObject;
import org.dcm4chee.xero.instrumentation.InstrumentorFactory;
import org.dcm4chee.xero.metadata.MetaData;
import org.dcm4chee.xero.metadata.filter.Filter;
import org.dcm4chee.xero.metadata.filter.FilterItem;
import org.dcm4chee.xero.metadata.servlet.ErrorResponseItem;
import org.dcm4chee.xero.metadata.servlet.ServletResponseItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EncodeImage transforms a WadoImage object into a JPEG, PNG or any other
 * supported image format.
 * 
 * @author bwallace
 * 
 */
public class EncodeImage implements Filter<ServletResponseItem> {

    private static final Logger log = LoggerFactory.getLogger(EncodeImage.class);

    public static final String MAX_BITS = "maxBits";

    private static final float DEFAULT_QUALITY = -1.0f;

    protected static Map<String, EncodeResponseInfo> contentTypeMap = new HashMap<String, EncodeResponseInfo>();

    static {
        new EncodeResponseInfo("image/jp12", "image/jpeg", true, 12, "JPEG", UID.JPEGExtended24);
        new EncodeResponseInfo("image/jpls", "image/jpeg", false, 16, "JPEG-LS", UID.JPEGLSLossless, UID.JPEGLSLossyNearLossless);
        new EncodeResponseInfo("image/jpll", "image/jpeg", false, 16, "JPEG-LOSSLESS", UID.JPEGLossless);
        new EncodeResponseInfo("image/png", null, false, 0, null);
        new EncodeResponseInfo("image/png16", "image/png", false, 16, null);
        new EncodeResponseInfo("image/jpeg", "image/jpeg", true, 0, "JPEG", UID.JPEGBaseline1, "image/*");
        new EncodeResponseInfo("image/jp2", null, false, 16, "JPEG2000", UID.JPEG2000, UID.JPEG2000LosslessOnly);
        new EncodeResponseInfo("image/gif", null, false, 0, null);
        new EncodeResponseInfo("image/bmp", null, false, 0, null);
    }

    ;

    String[] wadoParameters = new String[] { "windowCenter", "windowWidth", "imageUID", "studyUID", "seriesUID", "objectUID", "frameNumber", "rgb", "region", "rows", "cols", "presentationUID" };

    /**
	 * Filter the image by returning an JPEG type image object
	 * 
	 * @param filterItem
	 *            is the information about what to filter.
	 * @param map
	 *            contains the parameters used to determine the encoding type.
	 * @return A response that can be used to write the image to a stream in the
	 *         provided encoding type, or image/jpeg if none.
	 */
    public ServletResponseItem filter(FilterItem<ServletResponseItem> filterItem, Map<String, Object> map) {
        String contentType = (String) map.get("contentType");
        boolean containsRelative = map.containsKey("relative");
        if (contentType == null) contentType = (containsRelative ? "image/png" : "image/jpeg");
        log.info("Encoding image in content type={}", contentType);
        DicomObject ds = dicomImageHeader.filter(null, map);
        if (ds != null && !(ds.contains(Tag.PixelRepresentation) || containsRelative)) {
            log.info("DICOM does not contain pixel representation.");
            return filterItem.callNextFilter(map);
        }
        float quality = DEFAULT_QUALITY;
        String sQuality = (String) map.get("imageQuality");
        if (sQuality != null) quality = Float.parseFloat(sQuality);
        String queryStr = computeQueryStr(map);
        map.put(org.dcm4chee.xero.metadata.filter.MemoryCacheFilter.KEY_NAME, queryStr);
        ArrayList<EncodeResponseInfo> possibleResponses = getAllowedContentTypes(contentType);
        EncodeResponseInfo eri = null;
        if (possibleResponses.size() > 0) {
            eri = possibleResponses.get(0);
        }
        boolean tryReturningRawBytes = ((eri != null && eri.maxBits > 8));
        if (tryReturningRawBytes) {
            ArrayList<String> allowedTransferSyntaxList = getAllowedTransferSyntaxesFromContentType(possibleResponses);
            if (!allowedTransferSyntaxList.isEmpty()) {
                map.put(WadoImage.IMG_AS_BYTES_ONLY_FOR_TRANSFER_SYNTAXES, allowedTransferSyntaxList);
            }
            map.put(MAX_BITS, eri.maxBits);
        }
        if (eri == null) {
            log.warn("No EncodeResponeItem for contentType: " + contentType);
            return new ErrorResponseItem(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Content type " + contentType + " isn't supported.");
        }
        WadoImage image = null;
        try {
            image = wadoImageFilter.filter(null, map);
        } catch (Exception e) {
            log.error("Error running WadoImage filter chain.", e);
            return new ErrorResponseItem(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
        }
        if (image == null || image.hasError()) return new ErrorResponseItem(HttpServletResponse.SC_NO_CONTENT, "No content found.");
        if (image.getValue() == null) {
            if (image.getParameter(WadoImage.IMG_AS_BYTES) != null) {
                String rawTransferSyntax = (String) image.getParameter(WadoImage.AS_BYTES_RETURNED_TRANSFER_SYNTAX);
                EncodeResponseInfo tsEri = contentTypeMap.get(rawTransferSyntax);
                if (tsEri == null) {
                    return new ErrorResponseItem(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Internal error, retrieved an unknown content type that doesn't match " + contentType);
                }
                if (!eri.transferSyntaxUIDs.contains(rawTransferSyntax)) {
                    eri = tsEri;
                }
                log.info("Source tsuid=" + rawTransferSyntax);
            }
        }
        return new ImageServletResponseItem(image, eri, quality);
    }

    protected ArrayList<EncodeResponseInfo> getAllowedContentTypes(String contentType) {
        ArrayList<EncodeResponseInfo> contentTypeResponeInfo = new ArrayList<EncodeResponseInfo>();
        for (String testType : contentType.split(",")) {
            int semi = testType.indexOf(';');
            if (semi > 0) testType = testType.substring(0, semi);
            testType = testType.trim();
            EncodeResponseInfo response = contentTypeMap.get(contentType);
            if (response != null) {
                contentTypeResponeInfo.add(response);
            }
        }
        return contentTypeResponeInfo;
    }

    protected ArrayList<String> getAllowedTransferSyntaxesFromContentType(List<EncodeResponseInfo> responseList) {
        ArrayList<String> UidList = new ArrayList<String>();
        for (EncodeResponseInfo ri : responseList) {
            UidList.addAll(ri.transferSyntaxUIDs);
        }
        if (UidList.contains(UID.JPEGExtended24)) {
            if (!UidList.contains(UID.JPEGBaseline1)) {
                UidList.add(UID.JPEGBaseline1);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Accept raw bytes for the following UIDs." + UidList.toString());
        }
        return UidList;
    }

    /**
	 * Figures out what the query string should be - in a fixed order, using
	 * just the handled items.
	 * 
	 * @param map
	 *            to get the WADO parameters from
	 * @param A
	 *            URI parameter string with the relevant WADO parameters in it,
	 *            as defined by wadoParameters
	 */
    String computeQueryStr(Map<String, ?> map) {
        StringBuffer ret = new StringBuffer();
        for (String key : wadoParameters) {
            Object value = map.get(key);
            if (value != null) {
                ret.append("&").append(key).append("=").append(value);
            }
        }
        return ret.toString();
    }

    /** Adds encoding information to the params map based on the transfer syntax or (single)
	 * contentType tsuid.
	 * 
	 * @param tsuid  The transfer syntax UID or single mime type with no parameters.
	 * @param params
	 */
    public static void addEncodingInfo(String tsuid, Map<String, Object> params) {
        EncodeResponseInfo eri = contentTypeMap.get(tsuid);
        if (eri == null) return;
        params.put(MAX_BITS, eri.maxBits);
    }

    /** Returns the transferSyntax corresponding to the file format of an image. Returns null
	 * when the image format is not recognized or the compression type is not jpeg. 
	 * 
	 * @param fileFormat The file format of an image file.
	 * @return
	 */
    public static String getTransferSyntaxFromFileFormat(String fileFormat) {
        String mimeType = "image/" + fileFormat;
        String transferSyntax;
        Set<Map.Entry<String, EncodeResponseInfo>> set = contentTypeMap.entrySet();
        for (Map.Entry<String, EncodeResponseInfo> entry : set) {
            EncodeResponseInfo eri = entry.getValue();
            if (mimeType.equals(eri.mimeType)) {
                transferSyntax = entry.getKey();
                if (Character.isDigit(transferSyntax.charAt(0))) {
                    return transferSyntax;
                }
            }
        }
        log.warn("The file format " + fileFormat + " does not correspond to a transfer syntax UID");
        return null;
    }

    private Filter<DicomObject> dicomImageHeader;

    /** Gets the filter that returns the dicom object image header */
    public Filter<DicomObject> getDicomImageHeader() {
        return dicomImageHeader;
    }

    @MetaData(out = "${ref:dicomImageHeader}")
    public void setDicomImageHeader(Filter<DicomObject> dicomImageHeader) {
        this.dicomImageHeader = dicomImageHeader;
    }

    private Filter<WadoImage> wadoImageFilter;

    public Filter<WadoImage> getWadoImageFilter() {
        return wadoImageFilter;
    }

    /**
	 * Sets the filter to use for the wado image data.
	 * @param wadoImageFilter
	 */
    @MetaData(out = "${ref:wadoImg}")
    public void setWadoImageFilter(Filter<WadoImage> wadoImageFilter) {
        this.wadoImageFilter = wadoImageFilter;
    }
}

/** Does the actual writing to the stream */
class ImageServletResponseItem implements ServletResponseItem {

    private static Logger log = LoggerFactory.getLogger(ImageServletResponseItem.class);

    String contentType;

    ImageWriter writer;

    ImageWriteParam imageWriteParam;

    IIOMetadata iiometadata;

    WadoImage wadoImage;

    private int maxAge = 3600;

    static String preferred_name_start2 = "com.sun.media.imageioimpl.plugins";

    static String preferred_name_start = "com.agfa";

    /**
	 * Create an image servlet response to write the given image to the response
	 * stream.
	 * 
	 * @param image
	 *            is the data to write to the stream
	 * @param contentType
	 *            is the type of image encoding to use (image/jpeg etc) - any
	 *            available encoder will be used
	 * @param quality
	 *            is the JPEG lossy quality (may eventually be other types as
	 *            well, but currently that is the only one available)
	 */
    public ImageServletResponseItem(WadoImage image, EncodeResponseInfo eri, float quality) {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType(eri.lookupMimeType);
        ImageWriter writerIt = writers.next();
        while (writers.hasNext()) {
            String name = writerIt.getClass().getName();
            if (name.startsWith(preferred_name_start)) {
                writer = writerIt;
                break;
            } else if (name.startsWith(preferred_name_start2)) {
                writer = writerIt;
            }
            log.debug("Skipping {}", name);
            writerIt = writers.next();
        }
        if (writer == null) {
            writer = writerIt;
            log.warn("Couldn't find preferred writer, using " + writer.getClass() + " instead.");
        }
        this.contentType = eri.mimeType;
        this.wadoImage = image;
        if (eri != null && quality >= 0f && quality <= 1f && eri.isLossyQuality) {
            imageWriteParam = writer.getDefaultWriteParam();
            imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            imageWriteParam.setCompressionType("JPEG");
            imageWriteParam.setCompressionQuality(quality);
        }
        if (eri.compressionType != null) {
            if (imageWriteParam == null) imageWriteParam = writer.getDefaultWriteParam();
            imageWriteParam.setCompressionType(eri.compressionType);
        }
    }

    /**
	 * Write the response to the provided stream. Sets the content type and
	 * writes to the output stream.
	 * 
	 * @param httpRequest
	 *            unused
	 * @param response
	 *            that the image is written to. Also sets the content type.
	 */
    @SuppressWarnings("unchecked")
    public void writeResponse(HttpServletRequest httpRequest, HttpServletResponse response) throws IOException {
        if (wadoImage == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found.");
            log.warn("Image not found.");
            return;
        }
        String filename = wadoImage.getFilename();
        if (wadoImage.hasError()) {
            log.warn("Writing an error response, name:" + filename);
            response.setHeader(CONTENT_DISPOSITION, "inline;filename=" + filename + ".error");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter pw = response.getWriter();
            pw.println("Image has internal problem:" + wadoImage.getError());
            pw.println("Filename:" + filename);
            pw.close();
            return;
        }
        IInstrumentationObject instrumentAction = InstrumentorFactory.getInstrumentor().start("EncodeImage", new Object[] { filename });
        long start = System.nanoTime();
        response.setContentType(contentType);
        response.setHeader("Cache-Control", "max-age=" + maxAge);
        response.setHeader("Pragma", null);
        response.setHeader("Expires", null);
        if (filename != null) {
            int extPos = 1 + contentType.indexOf("/");
            response.setHeader(CONTENT_DISPOSITION, "inline;filename=" + filename + "." + contentType.substring(extPos));
        }
        Collection<String> headers = (Collection<String>) wadoImage.getParameter("responseHeaders");
        if (headers != null) {
            for (String key : headers) {
                response.setHeader(key, (String) wadoImage.getParameter(key));
            }
        }
        byte[] rawImage;
        String msg;
        if (wadoImage.getValue() == null) {
            rawImage = (byte[]) wadoImage.getParameter(WadoImage.IMG_AS_BYTES);
            msg = "Raw image ";
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageOutputStream ios = new MemoryCacheImageOutputStream(baos);
            writer.setOutput(ios);
            IIOImage iioimage = new IIOImage(wadoImage.getValue(), null, null);
            writer.write(iiometadata, iioimage, imageWriteParam);
            writer.dispose();
            ios.close();
            rawImage = baos.toByteArray();
            baos.close();
            msg = "Encoding with " + writer.getClass();
        }
        long mid = System.nanoTime();
        response.setContentLength(rawImage.length);
        OutputStream os = response.getOutputStream();
        os.write(rawImage);
        os.flush();
        if (log.isInfoEnabled()) {
            log.info(msg + " took " + ((mid - start) / 1e6) + " Writing took " + ((System.nanoTime() - mid) / 1e6));
        }
        InstrumentorFactory.getInstrumentor().stop(instrumentAction);
    }
}

class EncodeResponseInfo {

    private static final List<String> EMPTY_STRING_LIST = new ArrayList<String>(0);

    public String mimeType;

    public String lookupMimeType;

    public boolean isLossyQuality;

    public int maxBits;

    public String compressionType;

    public List<String> transferSyntaxUIDs = EMPTY_STRING_LIST;

    public EncodeResponseInfo(String mimeType, String lookupMimeType, boolean isLossyQuality, int maxBits, String compressionType, String... transferSyntaxes) {
        this.mimeType = mimeType;
        if (lookupMimeType == null) lookupMimeType = mimeType;
        this.lookupMimeType = lookupMimeType;
        this.isLossyQuality = isLossyQuality;
        this.maxBits = maxBits;
        this.compressionType = compressionType;
        if (transferSyntaxes != null) {
            transferSyntaxUIDs = new ArrayList<String>();
            for (String ts : transferSyntaxes) {
                EncodeImage.contentTypeMap.put(ts, this);
                transferSyntaxUIDs.add(ts);
            }
        }
        EncodeImage.contentTypeMap.put(mimeType, this);
    }
}
