package net.sourceforge.cridmanager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

/**
 * Eine Implementierung der <code>IFile</code> -Schnittstelle f�r ein Verzeichniss auf einem FTP -
 * Server.
 */
public class FtpFile extends FtpResource implements IFile {

    private static Logger logger = Logger.getLogger("net.sourceforge.cridmanager.FtpFile");

    private FTPClient myftp = null;

    private OutputStream outputStream = null;

    private InputStream inputStream = null;

    private FTPFile file = null;

    private int existFlag = 0;

    public static final int EXISTANCE_UNKNOWN = 0;

    public static final int EXISTS = 1;

    public static final int EXISTS_NOT = 2;

    private String compareString;

    /**
	 * @param host
	 * @param user
	 * @param password
	 * @param filename
	 */
    public FtpFile(String host, String user, String password, FTPFile file) {
        this(host, user, password, file.getName(), EXISTS);
        this.file = file;
    }

    /**
	 * Erstellt ein <code>FtpFile</code>- Objekt aus einem URI
	 */
    public FtpFile(URI uri) {
        setHost(uri.getHost());
        path = uri.getPath().substring(1);
        String[] userInfos = uri.getUserInfo().split(":");
        if (userInfos.length == 2) {
            setUser(userInfos[0]);
            setPassword(userInfos[1]);
        } else {
            setUser(uri.getUserInfo());
            setPassword("");
        }
    }

    /**
	 * @param host
	 * @param user
	 * @param password
	 * @param filename
	 */
    public FtpFile(String host, String user, String password, String filename) {
        this(host, user, password, filename, EXISTANCE_UNKNOWN);
    }

    /**
	 * @param host
	 * @param user
	 * @param password
	 * @param filename
	 * @param existFlag
	 */
    public FtpFile(String host, String user, String password, String filename, int existFlag) {
        super(host, user, password);
        this.path = filename;
        this.existFlag = existFlag;
        this.compareString = this.getHost().concat(this.getAbsolutePath());
    }

    public String getName() {
        if (logger.isDebugEnabled()) {
            logger.debug("getName() - start");
        }
        String parts[] = path.split("/");
        String returnString = parts[parts.length - 1];
        if (logger.isDebugEnabled()) {
            logger.debug("getName() - end");
        }
        return returnString;
    }

    public String getAbsolutePath() {
        return path;
    }

    public String[] getPathElements() {
        if (logger.isDebugEnabled()) {
            logger.debug("getPathElements() - start");
        }
        String[] result;
        ILocation parent = getParent();
        if (parent != null) {
            String[] parents = parent.getPathElements();
            result = new String[parents.length + 1];
            for (int i = 0; i < parents.length; i++) result[i] = parents[i];
        } else result = new String[1];
        result[result.length - 1] = getName();
        if (logger.isDebugEnabled()) {
            logger.debug("getPathElements() - end");
        }
        return result;
    }

    public long length() {
        if (logger.isDebugEnabled()) {
            logger.debug("length() - start");
        }
        long result = 0;
        try {
            logger.debug("Bestimme FtpFile.length");
            if (file == null) {
                getInternalFile();
            }
            if (file != null) {
                result = file.getSize();
            }
        } catch (Exception e) {
            logger.error("length(): Fehler beim auslesen der L�nge der Datei " + getName() + ".", e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("length() - end");
        }
        return result;
    }

    /**
	 * @throws IOException
	 */
    private void getInternalFile() {
        if (logger.isDebugEnabled()) {
            logger.debug("getInternalFile() - start");
        }
        try {
            FTPClient ftp = getFtpClient();
            FTPFile files[] = ftp.listFiles(getAbsolutePath());
            releaseFtpClient(ftp);
            if (files.length == 1) {
                file = files[0];
                existFlag = EXISTS;
            } else existFlag = EXISTS_NOT;
        } catch (IOException e) {
            logger.error("getInternalFile()", e);
            e.printStackTrace();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getInternalFile() - end");
        }
    }

    public InputStream getInput() throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("getInput() - start");
        }
        if (inputStream == null && outputStream == null) {
            logger.debug("FtpFile.getInput");
            myftp = getFtpClient();
            inputStream = myftp.retrieveFileStream(getAbsolutePath());
            if (logger.isDebugEnabled()) {
                logger.debug("getInput() - end");
            }
            return inputStream;
        } else throw new IllegalStateException("");
    }

    public OutputStream createOutput() throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("createOutput() - start");
        }
        if (inputStream == null && outputStream == null) {
            logger.debug("Bestimme FtpFile.createOutput");
            myftp = getFtpClient();
            myftp.setBufferSize(4096);
            outputStream = myftp.storeFileStream(getAbsolutePath());
            existFlag = EXISTS;
            if (logger.isDebugEnabled()) {
                logger.debug("createOutput() - end");
            }
            return outputStream;
        } else throw new IllegalStateException("");
    }

    public OutputStream getOutput() throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("getOutput() - start");
        }
        if (inputStream == null && outputStream == null) {
            logger.debug("Bestimme FtpFile.createOutput");
            myftp = getFtpClient();
            myftp.setBufferSize(4096);
            outputStream = myftp.appendFileStream(getAbsolutePath());
            existFlag = EXISTS;
            if (logger.isDebugEnabled()) {
                logger.debug("getOutput() - end");
            }
            return outputStream;
        } else throw new IllegalStateException("");
    }

    public ILocation getParent() {
        logger.debug("Bestimme FtpFile.getLocation");
        int index = path.lastIndexOf("/");
        String s;
        if (index >= 0) {
            s = path.substring(0, index);
        } else s = "";
        ILocation returnILocation = new FtpLocation(getHost(), getUser(), getPassword(), s);
        if (logger.isDebugEnabled()) {
            logger.debug("getParent() - end");
        }
        return returnILocation;
    }

    public boolean delete() {
        if (logger.isDebugEnabled()) {
            logger.debug("delete() - start");
        }
        try {
            logger.debug("FtpFile.delete");
            FTPClient ftp = getFtpClient();
            ftp.deleteFile(getAbsolutePath());
            existFlag = EXISTS_NOT;
            releaseFtpClient(ftp);
            if (logger.isDebugEnabled()) {
                logger.debug("delete() - end");
            }
            return true;
        } catch (Exception e) {
            logger.error("delete()", e);
            logger.error("delete(): Fehler beim l�schen der Datei " + getName() + ".", e);
            return false;
        }
    }

    public void close() {
        if (logger.isDebugEnabled()) {
            logger.debug("close() - start");
        }
        try {
            logger.debug("FtpFile.close");
            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
                myftp.completePendingCommand();
            }
        } catch (IOException ioe1) {
            logger.error("close(): Fehler beim Schliessen des inputStream  mit filename = " + getName() + ".", ioe1);
        }
        try {
            if (outputStream != null) {
                outputStream.close();
                outputStream = null;
                myftp.completePendingCommand();
            }
        } catch (IOException ioe2) {
            logger.error("close(): Fehler beim Schliessen des outputStream mit filename= " + getName() + ".", ioe2);
        }
        if (myftp != null) releaseFtpClient(myftp);
        myftp = null;
        if (logger.isDebugEnabled()) {
            logger.debug("close() - end");
        }
    }

    public boolean exists() {
        logger.debug("Bestimme FtpFile.exists");
        if (existFlag == EXISTANCE_UNKNOWN) {
            if (file == null) getInternalFile();
            if (file == null) existFlag = EXISTS_NOT; else existFlag = EXISTS;
        }
        boolean returnboolean = existFlag == EXISTS;
        if (logger.isDebugEnabled()) {
            logger.debug("exists() - end");
        }
        return returnboolean;
    }

    public URI getURI() {
        if (logger.isDebugEnabled()) {
            logger.debug("getURI() - start");
        }
        try {
            URI returnURI = new URI("ftp", getUser() + ":" + getPassword(), getHost(), 21, "/" + path, null, null);
            if (logger.isDebugEnabled()) {
                logger.debug("getURI() - end");
            }
            return returnURI;
        } catch (URISyntaxException e) {
            logger.error("getURI()", e);
            System.out.println(e.toString());
            System.out.println("Uri Error!");
            System.out.println("ftp://" + getUser() + ":" + getPassword() + "@" + getHost() + "/" + path);
            if (logger.isDebugEnabled()) {
                logger.debug("getURI() - end");
            }
            return null;
        }
    }

    public boolean equals(Object obj) {
        if (logger.isDebugEnabled()) {
            logger.debug("equals(Object) - start");
        }
        if (obj instanceof FtpFile) {
            FtpFile other = (FtpFile) obj;
            boolean returnboolean = compareString.equals(other.compareString);
            if (logger.isDebugEnabled()) {
                logger.debug("equals(Object) - end");
            }
            return returnboolean;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("equals(Object) - end");
            }
            return false;
        }
    }

    public int hashCode() {
        if (logger.isDebugEnabled()) {
            logger.debug("hashCode() - start");
        }
        int returnint = compareString.hashCode();
        if (logger.isDebugEnabled()) {
            logger.debug("hashCode() - end");
        }
        return returnint;
    }

    public String toString() {
        if (logger.isDebugEnabled()) {
            logger.debug("toString() - start");
        }
        String returnString = getAbsolutePath();
        if (logger.isDebugEnabled()) {
            logger.debug("toString() - end");
        }
        return returnString;
    }

    public int compareTo(Object obj) {
        if (logger.isDebugEnabled()) {
            logger.debug("compareTo(Object) - start");
        }
        if (obj != null && obj instanceof FtpFile) {
            FtpFile other = (FtpFile) obj;
            int returnint = compareString.compareTo(other.compareString);
            if (logger.isDebugEnabled()) {
                logger.debug("compareTo(Object) - end");
            }
            return returnint;
        } else throw new ClassCastException("�bergebenes Objekt ist kein FtpFile");
    }

    public boolean isFile() {
        return true;
    }

    public boolean isLocation() {
        return false;
    }
}
