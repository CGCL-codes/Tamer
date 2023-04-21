package edu.ucdavis.genomics.metabolomics.util.io.dest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.Scanner;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import edu.ucdavis.genomics.metabolomics.exception.ConfigurationException;
import edu.ucdavis.genomics.metabolomics.util.io.Copy;
import edu.ucdavis.genomics.metabolomics.util.io.source.FTPSource;

/**
 * sends files to a given ftp server
 * 
 * @author wohlgemuth
 * @version Feb 27, 2006
 * 
 */
public class FTPDestination implements Destination {

    public static final String FTP_PASSWORD = "FTP_PASSWORD";

    public static final String FTP_USER = "FTP_USER";

    public static final String FTP_PORT = "FTP_PORT";

    public static final String FTP_SERVER = "FTP_SERVER";

    public static final String FTP_BINAERY = "FTP_BINAERY";

    private Logger logger = Logger.getLogger(FTPDestination.class);

    /**
	 * servername
	 */
    private String server = null;

    /**
	 * port
	 */
    private String port = null;

    /**
	 * username
	 */
    private String username = null;

    /**
	 * password
	 */
    private String password = null;

    private String mainDir = null;

    /**
	 * file including path and so one like /test/sample/File.txt
	 */
    private String file = null;

    private boolean binaery = false;

    private Map properties;

    public FTPDestination() {
        super();
    }

    /**
	 * provides us with the servername
	 * 
	 * @author wohlgemuth
	 * @version Feb 27, 2006
	 * @param servername
	 */
    public FTPDestination(String servername, boolean binaery) {
        this.server = servername;
        this.binaery = binaery;
    }

    /**
	 * provides us with the servername
	 * 
	 * @author wohlgemuth
	 * @version Feb 27, 2006
	 * @param servername
	 */
    public FTPDestination(String servername) {
        this.server = servername;
    }

    public OutputStream getOutputStream() throws IOException {
        return new TempFileOutputStream();
    }

    public void setIdentifier(Object o) throws ConfigurationException {
        this.file = o.toString();
    }

    public void configure(Map p) throws ConfigurationException {
        this.server = (String) p.get(FTPDestination.FTP_SERVER);
        this.port = (String) p.get(FTPDestination.FTP_PORT);
        this.username = (String) p.get(FTPDestination.FTP_USER);
        this.password = (String) p.get(FTPDestination.FTP_PASSWORD);
        this.binaery = ((Boolean) p.get(FTPDestination.FTP_BINAERY)).booleanValue();
        this.properties = p;
    }

    /**
	 * creates a temproraery file and stores it on the ftp server
	 * 
	 * @author wohlgemuth
	 * @version Feb 27, 2006
	 * 
	 */
    private class TempFileOutputStream extends OutputStream {

        private File tempFile;

        private FileOutputStream output = null;

        public TempFileOutputStream() throws IOException {
            tempFile = File.createTempFile("binbase", ".tmp");
            output = new FileOutputStream(tempFile);
        }

        public void close() throws IOException {
            output.flush();
            output.close();
            FTPClient client = new FTPClient();
            if (server == null) {
                throw new IOException("FTP_SERVER property is missing");
            } else {
                if (port != null) {
                    client.connect(server, Integer.parseInt(port));
                } else {
                    client.connect(server);
                }
            }
            if (username != null) {
                logger.debug("log in as specified user");
                client.login(username, password);
            } else {
                logger.debug("log in as anonymous");
                client.login("anonymous", this.getClass().getName());
            }
            if (binaery) {
                logger.debug("use binaery mode");
                client.setFileType(FTP.BINARY_FILE_TYPE);
            } else {
                logger.debug("use ascii mode");
                client.setFileType(FTP.ASCII_FILE_TYPE);
            }
            client.enterLocalPassiveMode();
            logger.debug("store file on server: " + tempFile + " under name: " + file);
            InputStream stream = new FileInputStream(tempFile);
            String dir = file.substring(0, file.lastIndexOf("/")) + "/";
            String split[] = dir.split("/");
            String last = "";
            logger.debug("creating dir: " + dir);
            for (int i = 0; i < split.length; i++) {
                last = last + "/" + split[i];
                logger.debug(last + " --> " + client.makeDirectory(last));
            }
            logger.debug("storing file: " + file);
            client.deleteFile(file);
            client.storeFile(file, stream);
            client.disconnect();
            tempFile.delete();
            try {
                FTPSource source = new FTPSource();
                source.configure(properties);
                source.setIdentifier(file);
                if (source.exist()) {
                    logger.debug("done");
                } else {
                    throw new IOException("can't find file I just wrote, something went wrong!");
                }
            } catch (ConfigurationException e) {
                throw new IOException(e.getMessage());
            }
        }

        public boolean equals(Object arg0) {
            return output.equals(arg0);
        }

        public void flush() throws IOException {
            output.flush();
        }

        public FileChannel getChannel() {
            return output.getChannel();
        }

        public int hashCode() {
            return output.hashCode();
        }

        public String toString() {
            return output.toString();
        }

        public void write(byte[] arg0, int arg1, int arg2) throws IOException {
            output.write(arg0, arg1, arg2);
        }

        public void write(byte[] arg0) throws IOException {
            output.write(arg0);
        }

        public void write(int arg0) throws IOException {
            output.write(arg0);
        }
    }

    public String getMainDir() {
        return mainDir;
    }

    public void setMainDir(String mainDir) {
        this.mainDir = mainDir;
    }
}
