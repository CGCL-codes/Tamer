package org.goodoldai.jeff.report.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import org.dom4j.Document;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ImageData;
import org.goodoldai.jeff.report.ReportChunkBuilder;
import org.dom4j.Element;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.ImageExplanationChunk;

/**
 * A concrete builder for transforming image explanation chunks into pieces
 * of XML report
 *
 * @author Ivan Milenkovic
 */
public class HTMLImageChunkBuilder implements ReportChunkBuilder {

    /**
     * Initializes the builder
     */
    public HTMLImageChunkBuilder() {
    }

    /**
     * This method transforms an image explanation chunk into an HTML report piece
     * and writes this piece into the provided xml document which is, in this
     * case, an instance of org.dom4j.Document. The method first collects
     * all general chunk data (context, rule, group, tags) and inserts them
     * into the report, and then retrieves the chunk content.
     *
     * @param echunk image explanation chunk that needs to be transformed
     * @param stream output stream to which the transformed chunk will be
     * written in as an xml document (in this case org.dom4j.Document)
     * @param insertHeaders denotes if chunk headers should be inserted into the
     * report (true) or not (false)
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are
     * null, if the entered chunk is not an ImageExplanationChunk instance or
     * if the entered output stream type is not org.dom4j.Document
     */
    public void buildReportChunk(ExplanationChunk echunk, Object stream, boolean insertHeaders) {
        if (echunk == null && stream == null) {
            throw new ExplanationException("All of the arguments are mandatory, so they can not be null");
        }
        if (echunk == null) {
            throw new ExplanationException("The argument 'echunk' is mandatory, so it can not be null");
        }
        if (stream == null) {
            throw new ExplanationException("The argument 'stream' is mandatory, so it can not be null");
        }
        if (!(echunk instanceof ImageExplanationChunk)) {
            throw new ExplanationException("The ExplanationChunk must be type of ImageExplanationChunk");
        }
        if (!(stream instanceof Document)) {
            throw new ExplanationException("The stream must be the type of org.dom4j.Document");
        }
        Document document = (Document) stream;
        Element bodyElement = document.getRootElement().element("body");
        Element imageParagraphElement = bodyElement.addElement("p");
        if (insertHeaders) {
            HTMLChunkUtility.insertExplanationInfo(echunk, imageParagraphElement);
        }
        ImageExplanationChunk imageExplanationChunk = (ImageExplanationChunk) echunk;
        insertContent((ImageData) imageExplanationChunk.getContent(), imageParagraphElement);
    }

    /**
     * This is a private method that is used to insert content into the document
     * This method also creates a new folder htmlReportFiles and copies all image files
     * referenced in the htmlReport to that folder
     *
     * @param imageExplanationChunk image explanation chunk which holds the content
     * that needs to be transformed
     * @param element element in which the content of the transformed chunk will be
     * written in as an xml document (in this case org.dom4j.Document)
     */
    private void insertContent(ImageData imageData, Element element) {
        URL url = getClass().getResource(imageData.getURL());
        try {
            File imageFileRead = new File(url.toURI());
            FileInputStream inputStream = new FileInputStream(imageFileRead);
            String imageFileWritePath = "htmlReportFiles" + "/" + imageData.getURL();
            File imageFileWrite = new File(imageFileWritePath);
            String[] filePathTokens = imageFileWritePath.split("/");
            String directoryPathCreate = filePathTokens[0];
            int i = 1;
            while (i < filePathTokens.length - 1) {
                directoryPathCreate = directoryPathCreate + "/" + filePathTokens[i];
                i++;
            }
            File fileDirectoryPathCreate = new File(directoryPathCreate);
            if (!fileDirectoryPathCreate.exists()) {
                boolean successfulFileCreation = fileDirectoryPathCreate.mkdirs();
                if (successfulFileCreation == false) {
                    throw new ExplanationException("Unable to create folders in path " + directoryPathCreate);
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(imageFileWrite);
            byte[] data = new byte[1024];
            int readDataNumberOfBytes = 0;
            while (readDataNumberOfBytes != -1) {
                readDataNumberOfBytes = inputStream.read(data, 0, data.length);
                if (readDataNumberOfBytes != -1) {
                    fileOutputStream.write(data, 0, readDataNumberOfBytes);
                }
            }
            inputStream.close();
            fileOutputStream.close();
        } catch (Exception ex) {
            throw new ExplanationException(ex.getMessage());
        }
        String caption = imageData.getCaption();
        Element imageElement = element.addElement("img");
        if (imageData.getURL().charAt(0) != '/') imageElement.addAttribute("src", "htmlReportFiles" + "/" + imageData.getURL()); else imageElement.addAttribute("src", "htmlReportFiles" + imageData.getURL());
        imageElement.addAttribute("alt", "image not available");
        if (caption != null) {
            element.addElement("br");
            element.addText(caption);
        }
    }
}
