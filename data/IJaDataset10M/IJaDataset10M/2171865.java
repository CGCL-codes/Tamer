package com.dcivision.dms.client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
  HtmlFormFile.java

  This class sends the file through HTTP

    @author          Rollo Chan
    @company         DCIVision Limited
    @creation date   24/06/2003
    @version         $Revision: 1.3.32.1 $
    */
public class HtmlFormFile implements HtmlFormElement {

    public static final String REVISION = "$Revision: 1.3.32.1 $";

    private String name;

    private String contentType;

    private int contentLength;

    private String file;

    private static final Log log = LogFactory.getLog(HtmlFormFile.class);

    public HtmlFormFile() {
    }

    public HtmlFormFile(String name, String file) {
        this.name = name;
        this.file = file;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }

    public byte[] getTranslated() {
        File f = new File(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StringBuffer header = new StringBuffer();
        header.append("Content-Disposition: form-data; name=\"").append(name).append("\"; filename=\"").append(file).append("\"").append(HttpSender.returnChar);
        String contentType = URLConnection.guessContentTypeFromName(f.getName()) != null ? URLConnection.guessContentTypeFromName(f.getName()) : "application/octet-stream";
        header.append("Content-Type: ").append(contentType);
        header.append(HttpSender.returnChar);
        header.append(HttpSender.returnChar);
        FileInputStream fis = null;
        try {
            baos.write(header.toString().getBytes());
            fis = new FileInputStream(f);
            int length = -1;
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            while ((length = fis.read(buffer, 0, bufferSize)) != -1) {
                baos.write(buffer, 0, length);
            }
            baos.write(HttpSender.returnChar.getBytes());
        } catch (IOException ioe) {
            log.error(ioe, ioe);
        } finally {
            try {
                fis.close();
            } catch (IOException ioe) {
            }
        }
        return baos.toByteArray();
    }
}
