package net.assimilator.tools.webster.http;

import net.assimilator.tools.webster.WebsterImpl;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;

/**
 * inner class for parsing a HTTP header
 *
 * @author Jim Clark
 * @author Mike T. Miller
 * @author Larry J. Mitchell
 * @version $Id$
 */
public class Head implements Runnable {

    /**
     * the component name for webster
     */
    static final String COMPONENT = "net.assimilator.tools.webster";

    /**
     * the logger we use to tell people what is happening
     */
    private static final Logger logger = Logger.getLogger(COMPONENT);

    private Socket client;

    private String fileName, header;

    private int fileLength;

    private WebsterImpl webster;

    public Head(WebsterImpl webster, Socket s, String name, Properties hdr) {
        this.webster = webster;
        client = s;
        this.fileName = name;
    }

    public void run() {
        StringBuffer dirData = new StringBuffer();
        try {
            File getFile = webster.parseFileName(fileName);
            if (logger.isLoggable(Level.FINE)) logger.fine("getFile = " + getFile);
            if (getFile.isDirectory()) {
                printHeaderFilesInDirectory(getFile, dirData);
            } else if (getFile.exists()) {
                setupHeaderFileForDownload(getFile);
            } else {
                header = "HTTP/1.1 404 Not Found\r\n\r\n";
            }
            DataOutputStream clientStream = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
            clientStream.writeBytes(header);
            clientStream.flush();
            clientStream.close();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error closing Socket", e);
        } finally {
            try {
                client.close();
            } catch (IOException e2) {
                logger.log(Level.WARNING, "Closing incoming socket", e2);
            }
        }
    }

    private void setupHeaderFileForDownload(File getFile) throws IOException {
        DataInputStream requestedFile = new DataInputStream(new BufferedInputStream(new FileInputStream(getFile)));
        fileLength = requestedFile.available();
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        fileType = WebsterImpl.getMimeTypes().getProperty(fileType);
        header = "HTTP/1.1 200 OK\n" + "Allow: GET\nMIME-Version: 1.0\n" + "Server : Webster: a Java HTTP Server\n" + "Content-Type: " + fileType + "\n" + "Content-Length: " + fileLength + "\r\n\r\n";
    }

    private void printHeaderFilesInDirectory(File getFile, StringBuffer dirData) {
        String files[] = getFile.list();
        for (String file : files) {
            File f = new File(getFile, file);
            dirData.append(f.toString().substring(getFile.getParent().length()));
            dirData.append("\t");
            if (f.isDirectory()) dirData.append("d"); else dirData.append("f");
            dirData.append("\t");
            dirData.append(f.length());
            dirData.append("\t");
            dirData.append(f.lastModified());
            dirData.append("\n");
        }
        fileLength = dirData.length();
        String fileType = WebsterImpl.getMimeTypes().getProperty("txt");
        header = "HTTP/1.1 200 OK\n" + "Allow: GET\nMIME-Version: 1.0\n" + "Server : Webster: a Java HTTP Server\n" + "Content-Type: " + fileType + "\n" + "Content-Length: " + fileLength + "\r\n\r\n";
    }
}
