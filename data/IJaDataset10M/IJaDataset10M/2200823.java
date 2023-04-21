package com.ecmdeveloper.plugin.cmis.model.tasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import com.ecmdeveloper.plugin.core.model.tasks.AbstractTask;
import com.ecmdeveloper.plugin.core.model.tasks.IGetContentAsFileTask;
import com.ecmdeveloper.plugin.cmis.model.Document;

/**
 * @author Ricardo.Belfor
 *
 */
public class GetContentAsFileTask extends AbstractTask implements IGetContentAsFileTask {

    private Document document;

    private int index;

    private String outputPath;

    private String filePrefix;

    public GetContentAsFileTask(Document document, String outputPath, int index) {
        super();
        this.document = document;
        this.index = index;
        this.outputPath = outputPath;
    }

    public GetContentAsFileTask(Document document, String outputPath) {
        this(document, outputPath, 0);
    }

    public String getFilePrefix() {
        return filePrefix;
    }

    @Override
    public void setFilePrefix(String filePrefix) {
        this.filePrefix = filePrefix;
    }

    @Override
    public Object call() throws Exception {
        org.apache.chemistry.opencmis.client.api.Document internalDocument = document.getInternalDocument();
        if (index >= 1) {
            return null;
        }
        return copyContentElementToFile(internalDocument.getContentStream());
    }

    private String copyContentElementToFile(ContentStream contentStream) throws FileNotFoundException, IOException {
        InputStream inputStream = contentStream.getStream();
        String outputFilename = getOutputFilename(contentStream);
        copyInputStreamToOutputFile(inputStream, outputFilename);
        return outputFilename;
    }

    private String getOutputFilename(ContentStream contentStream) {
        StringBuffer outputFilename = new StringBuffer();
        outputFilename.append(outputPath);
        outputFilename.append(File.separator);
        if (filePrefix != null) {
            outputFilename.append(filePrefix);
        }
        outputFilename.append(contentStream.getFileName());
        return outputFilename.toString();
    }

    private void copyInputStreamToOutputFile(InputStream contentStream, String outputFilename) throws FileNotFoundException, IOException {
        byte buffer[] = new byte[1000];
        int bytesRead;
        OutputStream out = new FileOutputStream(getWritableFile(outputFilename));
        while ((bytesRead = contentStream.read(buffer)) > 0) {
            out.write(buffer, 0, bytesRead);
        }
        out.close();
        contentStream.close();
    }

    private File getWritableFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            file.setWritable(true);
        }
        return file;
    }
}
