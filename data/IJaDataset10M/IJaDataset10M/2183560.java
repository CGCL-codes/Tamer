package com.generalynx.common.mail;

import javax.activation.DataSource;
import java.io.*;

/**
 * This class implements a typed DataSource from:<br>
 * <p/>
 * - an InputStream<br>
 * - a byte array<br>
 * - a String<br>
 *
 * @author <a href="mailto:colin.chalmers@maxware.nl">Colin Chalmers</a>
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:bmclaugh@algx.net">Brett McLaughlin</a>
 * @version $Id: ByteArrayDataSource.java,v 1.1 2004/11/29 09:59:11 epugh Exp $
 */
public class ByteArrayDataSource implements DataSource {

    /**
     * Stream containg the Data
     */
    private ByteArrayOutputStream baos = null;

    /**
     * Content-type.
     */
    private String type = "application/octet-stream";

    /**
     * define the buffer size
     */
    public static final int BUFFER_SIZE = 512;

    /**
     * Create a datasource from a byte array.
     *
     * @param data  A byte[].
     * @param aType A String.
     * @throws IOException IOException
     */
    public ByteArrayDataSource(byte[] data, String aType) throws IOException {
        ByteArrayInputStream bis = null;
        try {
            bis = new ByteArrayInputStream(data);
            this.byteArrayDataSource(bis, aType);
        } catch (IOException ioex) {
            throw ioex;
        } finally {
            if (bis != null) {
                bis.close();
            }
        }
    }

    /**
     * Create a datasource from an input stream.
     *
     * @param aIs   An InputStream.
     * @param aType A String.
     * @throws IOException IOException
     */
    public ByteArrayDataSource(InputStream aIs, String aType) throws IOException {
        this.byteArrayDataSource(aIs, aType);
    }

    /**
     * Create a datasource from an input stream.
     *
     * @param aIs   An InputStream.
     * @param aType A String.
     * @throws IOException IOException
     */
    private void byteArrayDataSource(InputStream aIs, String aType) throws IOException {
        this.type = aType;
        BufferedInputStream bis = null;
        BufferedOutputStream osWriter = null;
        try {
            int length = 0;
            byte[] buffer = new byte[ByteArrayDataSource.BUFFER_SIZE];
            bis = new BufferedInputStream(aIs);
            baos = new ByteArrayOutputStream();
            osWriter = new BufferedOutputStream(baos);
            while ((length = bis.read(buffer)) != -1) {
                osWriter.write(buffer, 0, length);
            }
            osWriter.flush();
            osWriter.close();
        } catch (IOException ioex) {
            throw ioex;
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (baos != null) {
                baos.close();
            }
            if (osWriter != null) {
                osWriter.close();
            }
        }
    }

    /**
     * Create a datasource from a String.
     *
     * @param data  A String.
     * @param aType A String.
     * @throws IOException IOException
     */
    public ByteArrayDataSource(String data, String aType) throws IOException {
        this.type = aType;
        try {
            baos = new ByteArrayOutputStream();
            baos.write(data.getBytes("iso-8859-1"));
            baos.flush();
            baos.close();
        } catch (UnsupportedEncodingException uex) {
            throw new IOException("The Character Encoding is not supported.");
        } finally {
            if (baos != null) {
                baos.close();
            }
        }
    }

    /**
     * Get the content type.
     *
     * @return A String.
     */
    public String getContentType() {
        return (type == null ? "application/octet-stream" : type);
    }

    /**
     * Get the input stream.
     *
     * @return An InputStream.
     * @throws IOException IOException
     */
    public InputStream getInputStream() throws IOException {
        if (baos == null) {
            throw new IOException("no data");
        }
        return new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * Get the name.
     *
     * @return A String.
     */
    public String getName() {
        return "ByteArrayDataSource";
    }

    /**
     * Get the OutputStream to write to
     *
     * @return An OutputStream
     * @throws IOException IOException
     */
    public OutputStream getOutputStream() throws IOException {
        baos = new ByteArrayOutputStream();
        return baos;
    }
}
