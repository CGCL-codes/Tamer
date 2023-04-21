package org.ws4d.java.attachment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import org.ws4d.java.communication.http.HTTPHeader;
import org.ws4d.java.constants.HTTPConstants;
import org.ws4d.java.constants.MIMEConstants;
import org.ws4d.java.modules.attachment.IAttachmentData;
import org.ws4d.java.service.AbstractAction;
import org.ws4d.java.service.Parameter;
import org.ws4d.java.util.MIMEUtil;
import org.ws4d.java.util.Properties;

/**
 * Utility class for handling attachments.
 */
public class AttachmentUtil {

    public static final boolean DIRECTION_RESPONSE = false;

    public static final boolean DIRECTION_REQUEST = true;

    private static int attachmentDuration = 5 * 1000 * 60;

    /**
	 * We are shy!
	 */
    private AttachmentUtil() {
    }

    /**
	 * Initializes internal parameters of class.
	 */
    public static void init() {
        attachmentDuration = Properties.getInstance().getGlobalIntProperty(Properties.PROP_PRESENTATION_URL_ATTACHMENT_DURATION);
    }

    /**
	 * Get boundary for mime usage. Each call probably returns a different
	 * boundary.
	 * 
	 * @return boundary
	 */
    public static String getBoundary() {
        long creationTime = System.currentTimeMillis();
        return "---------------------------" + creationTime;
    }

    /**
	 * Get estimated Content-Type via filename.
	 * 
	 * @param filename fileName with extension.
	 * @return Content-Type estimated Content-Type based on the file extension.
	 */
    public static String estimateContentType(String filename) {
        int last = 0;
        last = filename.lastIndexOf('.');
        String fileExt = filename.substring(last + 1);
        return extentionContentType(fileExt);
    }

    /**
	 * Get Content-Type via file extension.
	 * 
	 * @param fileExt file extension.
	 * @return Content-Type (type and subtype).
	 */
    public static String extentionContentType(String fileExt) {
        if (fileExt.equalsIgnoreCase("jpg") || fileExt.equalsIgnoreCase("jpeg")) {
            return MIMEConstants.MEDIATYPE_IMAGE + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_JPEG;
        } else if (fileExt.equalsIgnoreCase("txt")) {
            return MIMEConstants.MEDIATYPE_TEXT + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_PLAIN;
        } else if (fileExt.equalsIgnoreCase("gif")) {
            return MIMEConstants.MEDIATYPE_IMAGE + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_GIF;
        } else if (fileExt.equalsIgnoreCase("png")) {
            return MIMEConstants.MEDIATYPE_IMAGE + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_PNG;
        } else if (fileExt.equalsIgnoreCase("tiff")) {
            return MIMEConstants.MEDIATYPE_IMAGE + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_TIFF;
        } else if (fileExt.equalsIgnoreCase("tif")) {
            return MIMEConstants.MEDIATYPE_IMAGE + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_TIFF;
        } else if (fileExt.equalsIgnoreCase("htm") || fileExt.equalsIgnoreCase("html")) {
            return MIMEConstants.MEDIATYPE_TEXT + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_HTML;
        } else if (fileExt.equalsIgnoreCase("xml")) {
            return MIMEConstants.MEDIATYPE_TEXT + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_XML;
        } else if (fileExt.equalsIgnoreCase("js")) {
            return MIMEConstants.MEDIATYPE_TEXT + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_JAVASCRIPT;
        } else if (fileExt.equalsIgnoreCase("css")) {
            return MIMEConstants.MEDIATYPE_TEXT + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_CSS;
        } else if (fileExt.equalsIgnoreCase("zip")) {
            return MIMEConstants.MEDIATYPE_APPLICATION + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_ZIP;
        } else if (fileExt.equalsIgnoreCase("pdf")) {
            return MIMEConstants.MEDIATYPE_APPLICATION + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_PDF;
        } else if (fileExt.equalsIgnoreCase("wmv")) {
            return MIMEConstants.MEDIATYPE_VIDEO + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_WINDOWSMEDIA;
        } else if (fileExt.equalsIgnoreCase("rar")) {
            return MIMEConstants.MEDIATYPE_APPLICATION + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_RAR;
        } else if (fileExt.equalsIgnoreCase("swf")) {
            return MIMEConstants.MEDIATYPE_APPLICATION + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_SHOCKWAVEFLASH;
        } else if (fileExt.equalsIgnoreCase("exe")) {
            return MIMEConstants.MEDIATYPE_APPLICATION + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_WINDOWSEXECUTEABLE;
        } else if (fileExt.equalsIgnoreCase("avi")) {
            return MIMEConstants.MEDIATYPE_VIDEO + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_AVI;
        } else if (fileExt.equalsIgnoreCase("doc") || fileExt.equalsIgnoreCase("dot")) {
            return MIMEConstants.MEDIATYPE_APPLICATION + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_MSWORD;
        } else if (fileExt.equalsIgnoreCase("ico")) {
            return MIMEConstants.MEDIATYPE_IMAGE + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_ICON;
        } else if (fileExt.equalsIgnoreCase("mp2") || fileExt.equalsIgnoreCase("mp3")) {
            return MIMEConstants.MEDIATYPE_AUDIO + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_MPEG3;
        } else if (fileExt.equalsIgnoreCase("rtf")) {
            return MIMEConstants.MEDIATYPE_TEXT + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_RICHTEXT;
        } else if (fileExt.equalsIgnoreCase("xls") || fileExt.equalsIgnoreCase("xla")) {
            return MIMEConstants.MEDIATYPE_APPLICATION + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_MSEXCEL;
        }
        return MIMEConstants.MEDIATYPE_TEXT + MIMEConstants.SEPARATOR + MIMEConstants.SUBTYPE_PLAIN;
    }

    /**
	 * Creates a MIME definiton with the given attachments and writes it to
	 * stream.
	 * 
	 * @param out Output stream.
	 * @param act Action which contains the binary parameter.
	 * @param boundary String representing the MIME boundary.
	 * @param direction Indicates whether the output parameter <code>true</code>,
	 *            or the input parameter <code>false</code> should be used.
	 * @throws IOException.
	 */
    public static void writeMIMEtoStream(OutputStream out, AbstractAction act, String boundary, boolean direction) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Enumeration params = null;
        if (direction) {
            if (act.hasBinaryOutputParameter()) {
                params = act.getOutputParameters().elements();
            }
        } else {
            if (act.hasBinaryInputParameter()) {
                params = act.getInputParameters().elements();
            }
        }
        for (; params.hasMoreElements(); ) {
            Parameter para = (Parameter) params.nextElement();
            Hashtable table = para.getAttachmentTable();
            Enumeration enu = table.elements();
            for (; enu.hasMoreElements(); ) {
                IAttachmentData data = (IAttachmentData) enu.nextElement();
                if (data != null) {
                    HTTPHeader header = new HTTPHeader();
                    if (data.getContentType() != null) header.addHeader(HTTPConstants.HTTP_CONTENT_TYPE, data.getContentType());
                    if (data.getContentTransferEncoding() != null) header.addHeader(HTTPConstants.HTTP_CONTENT_TRANSFER_ENCODING, data.getContentTransferEncoding());
                    if (data.getContentID() != null) header.addHeader(HTTPConstants.HTTP_CONTENT_ID, MIMEUtil.createReference(data.getContentID()));
                    buffer.write(HTTPConstants.CRLF.getBytes());
                    buffer.write(MIMEConstants.BOUNDARY_FIX.getBytes());
                    buffer.write(boundary.getBytes());
                    buffer.write(HTTPConstants.CRLF.getBytes());
                    buffer.write(header.toString().getBytes());
                    buffer.write(HTTPConstants.CRLF.getBytes());
                    out.write((HTTPConstants.CRLF + Integer.toHexString(buffer.toByteArray().length) + HTTPConstants.CRLF).getBytes());
                    out.write(buffer.toByteArray());
                    out.flush();
                    buffer = new ByteArrayOutputStream();
                    out.write((HTTPConstants.CRLF + Integer.toHexString(data.size()) + HTTPConstants.CRLF).getBytes());
                    data.writeTo(out);
                    out.flush();
                    data.dispose();
                }
            }
        }
    }

    public static int getAttachmentDuration() {
        return attachmentDuration;
    }
}
