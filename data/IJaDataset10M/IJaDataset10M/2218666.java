package au.gov.naa.digipres.xena.plugin.archive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import au.gov.naa.digipres.xena.kernel.XenaException;
import au.gov.naa.digipres.xena.kernel.XenaInputSource;
import au.gov.naa.digipres.xena.kernel.filenamer.AbstractFileNamer;
import au.gov.naa.digipres.xena.kernel.filenamer.FileNamerManager;
import au.gov.naa.digipres.xena.kernel.metadatawrapper.AbstractMetaDataWrapper;
import au.gov.naa.digipres.xena.kernel.normalise.AbstractNormaliser;
import au.gov.naa.digipres.xena.kernel.normalise.BinaryToXenaBinaryNormaliser;
import au.gov.naa.digipres.xena.kernel.normalise.NormaliserResults;
import au.gov.naa.digipres.xena.kernel.type.Type;
import au.gov.naa.digipres.xena.util.FileUtils;

/**
 * Base class for normalising archives. An extension of this class will need to be created for each type of archive (eg Zip, GZip, 7z, RAR etc),
 * implementing the getArchiveHandler method. This method should return an instance of an ArchiveHandler implementation, which will allow
 * this class to retrieve entries in the archive one by one.
 * created 28/03/2007
 * archive
 * Short desc of class:
 */
public abstract class ArchiveNormaliser extends AbstractNormaliser {

    public static final String ARCHIVE_PREFIX = "archive";

    public static final String ARCHIVE_TAG = "archive";

    public static final String ENTRY_TAG = "entry";

    public static final String ENTRY_ORIGINAL_PATH_ATTRIBUTE = "original_path";

    public static final String ENTRY_ORIGINAL_FILE_DATE_ATTRIBUTE = "original_file_date";

    public static final String ENTRY_ORIGINAL_SIZE_ATTRIBUTE = "original_size";

    public static final String ENTRY_OUTPUT_FILENAME = "output_filename";

    public static final String ARCHIVE_URI = "http://preservation.naa.gov.au/archive/1.0";

    public static final String DATE_FORMAT_STRING = "yyyyMMdd'T'HHmmssZ";

    @Override
    public void parse(InputSource input, NormaliserResults results, boolean convertOnly) throws SAXException, java.io.IOException {
        FileNamerManager fileNamerManager = normaliserManager.getPluginManager().getFileNamerManager();
        AbstractFileNamer fileNamer = fileNamerManager.getActiveFileNamer();
        OutputStream entryOutputStream = null;
        InputStream archiveStream = null;
        try {
            ContentHandler ch = getContentHandler();
            AttributesImpl att = new AttributesImpl();
            ch.startElement(ARCHIVE_URI, ARCHIVE_TAG, ARCHIVE_PREFIX + ":" + ARCHIVE_TAG, att);
            archiveStream = input.getByteStream();
            ArchiveHandler archiveHandler = getArchiveHandler(archiveStream);
            ArchiveEntry entry = archiveHandler.getNextEntry();
            while (entry != null) {
                File tempFile = new File(entry.getFilename());
                XenaInputSource childXis = new XenaInputSource(tempFile);
                Type fileType = normaliserManager.getPluginManager().getGuesserManager().mostLikelyType(childXis);
                childXis.setType(fileType);
                AbstractNormaliser entryNormaliser = normaliserManager.lookup(fileType);
                File entryOutputFile;
                if (convertOnly) {
                    if (entryNormaliser.isConvertible()) {
                        entryNormaliser.setProperty("http://xena/input", childXis);
                        entryOutputFile = fileNamer.makeNewOpenFile(childXis, entryNormaliser);
                    } else {
                        FileUtils.fileCopy(tempFile, results.getDestinationDirString() + File.separator + tempFile.getName(), false);
                        tempFile.delete();
                        entry = archiveHandler.getNextEntry();
                        continue;
                    }
                } else {
                    entryOutputFile = fileNamer.makeNewXenaFile(childXis, entryNormaliser);
                }
                childXis.setOutputFileName(entryOutputFile.getName());
                NormaliserResults childResults;
                try {
                    entryOutputStream = new FileOutputStream(entryOutputFile);
                    childResults = normaliseArchiveEntry(childXis, entryNormaliser, entryOutputFile, entryOutputStream, fileNamerManager, fileType, convertOnly);
                } catch (Exception ex) {
                    System.out.println("Normalisation of archive entry failed, switching to binary.\n" + ex);
                    if (entryOutputFile.exists()) {
                        entryOutputFile.delete();
                    }
                    entryNormaliser = normaliserManager.lookup(BinaryToXenaBinaryNormaliser.BINARY_NORMALISER_NAME);
                    entryOutputFile = fileNamer.makeNewXenaFile(childXis, entryNormaliser);
                    childXis.setOutputFileName(entryOutputFile.getName());
                    entryOutputStream = new FileOutputStream(entryOutputFile);
                    childResults = normaliseArchiveEntry(childXis, entryNormaliser, entryOutputFile, entryOutputStream, fileNamerManager, fileType, convertOnly);
                } finally {
                    if (entryOutputStream != null) {
                        entryOutputStream.close();
                    }
                }
                results.addChildAIPResult(childResults);
                String entryOutputFilename = entryOutputFile.getName();
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute(ARCHIVE_URI, ENTRY_ORIGINAL_PATH_ATTRIBUTE, ARCHIVE_PREFIX + ":" + ENTRY_ORIGINAL_PATH_ATTRIBUTE, "CDATA", entry.getName());
                SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_STRING);
                atts.addAttribute(ARCHIVE_URI, ENTRY_ORIGINAL_FILE_DATE_ATTRIBUTE, ARCHIVE_PREFIX + ":" + ENTRY_ORIGINAL_FILE_DATE_ATTRIBUTE, "CDATA", formatter.format(entry.getOriginalFileDate()));
                atts.addAttribute(ARCHIVE_URI, ENTRY_ORIGINAL_SIZE_ATTRIBUTE, ARCHIVE_PREFIX + ":" + ENTRY_ORIGINAL_SIZE_ATTRIBUTE, "CDATA", String.valueOf(entry.getOriginalSize()));
                atts.addAttribute(ARCHIVE_URI, ENTRY_OUTPUT_FILENAME, ARCHIVE_PREFIX + ":" + ENTRY_OUTPUT_FILENAME, "CDATA", entryOutputFilename);
                ch.startElement(ARCHIVE_URI, ENTRY_TAG, ARCHIVE_PREFIX + ":" + ENTRY_TAG, atts);
                ch.endElement(ARCHIVE_URI, ENTRY_TAG, ARCHIVE_PREFIX + ":" + ENTRY_TAG);
                tempFile.delete();
                entry = archiveHandler.getNextEntry();
                if (isArchiveFile(fileType.toString()) && convertOnly) {
                    entryOutputFile.delete();
                }
            }
            ch.endElement(ARCHIVE_URI, ARCHIVE_TAG, ARCHIVE_PREFIX + ":" + ARCHIVE_TAG);
        } catch (XenaException x) {
            throw new SAXException("Problem parseing Xena file", x);
        } catch (TransformerException e) {
            throw new SAXException("Problem creating XML transformer", e);
        } finally {
            if (entryOutputStream != null) {
                entryOutputStream.close();
            }
            if (archiveStream != null) {
                archiveStream.close();
            }
        }
    }

    private NormaliserResults normaliseArchiveEntry(XenaInputSource childXis, AbstractNormaliser entryNormaliser, File entryOutputFile, OutputStream entryOutputStream, FileNamerManager fileNamerManager, Type fileType, boolean convertOnly) throws TransformerConfigurationException, XenaException, SAXException, IOException {
        SAXTransformerFactory transformFactory = (SAXTransformerFactory) TransformerFactory.newInstance();
        TransformerHandler transformerHandler = transformFactory.newTransformerHandler();
        entryNormaliser.setProperty("http://xena/url", childXis.getSystemId());
        AbstractMetaDataWrapper wrapper = null;
        if (convertOnly) {
            wrapper = normaliserManager.getPluginManager().getMetaDataWrapperManager().getEmptyWrapper().getWrapper();
            transformerHandler.getTransformer().setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        } else {
            wrapper = normaliserManager.getPluginManager().getMetaDataWrapperManager().getWrapNormaliser();
            wrapper = getNormaliserManager().wrapTheNormaliser(entryNormaliser, childXis, wrapper);
        }
        wrapper.setContentHandler(transformerHandler);
        wrapper.setLexicalHandler(transformerHandler);
        wrapper.setParent(entryNormaliser);
        entryNormaliser.setContentHandler(wrapper);
        entryNormaliser.setLexicalHandler(wrapper);
        entryNormaliser.setProperty("http://xena/file", entryOutputFile);
        entryNormaliser.setProperty("http://xena/normaliser", entryNormaliser);
        OutputStreamWriter osw = new OutputStreamWriter(entryOutputStream, "UTF-8");
        StreamResult streamResult = new StreamResult(osw);
        transformerHandler.setResult(streamResult);
        NormaliserResults childResults = new NormaliserResults(childXis, entryNormaliser, fileNamerManager.getDestinationDir(), fileNamerManager.getActiveFileNamer(), wrapper);
        childResults.setInputType(fileType);
        childResults.setOutputFileName(entryOutputFile.getName());
        normaliserManager.parse(entryNormaliser, childXis, wrapper, childResults, convertOnly);
        childResults.setNormalised(true);
        childResults.setId(wrapper.getSourceId(new XenaInputSource(entryOutputFile)));
        return childResults;
    }

    @Override
    public String getVersion() {
        return ReleaseInfo.getVersion() + "b" + ReleaseInfo.getBuildNumber();
    }

    protected abstract ArchiveHandler getArchiveHandler(InputStream archiveStream);

    private boolean isArchiveFile(String fileType) {
        boolean result = false;
        if (fileType.equalsIgnoreCase("gzip") || fileType.equalsIgnoreCase("zip") || fileType.equalsIgnoreCase("tar") || fileType.equalsIgnoreCase("Image Magick Normaliser") || fileType.equalsIgnoreCase("Image Tiff Normaliser") || fileType.equalsIgnoreCase("Website")) {
            result = true;
        }
        return result;
    }
}
