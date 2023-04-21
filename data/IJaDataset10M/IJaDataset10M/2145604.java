package de.fhkl.helloWorld.interfaces.util.ftp;

/**
 * This class provides methods to establish a ftp connection for up- and
 * downloading files
 */
public interface IFTPClient {

    /**
	 * @param url
	 * @param username
	 * @param password
	 * @return true if a connection is succesful established
	 */
    public boolean connect(Object url, Object username, Object password);

    /**
	 * @param source
	 * @param target
	 * @return true if upload is succesful
	 */
    public boolean uploadFile(Object source, Object target);

    /**
	 * @param source
	 * @return null if download is not succesful
	 */
    public boolean downloadFile(Object source);

    /**
	 * disconnects the ftp client
	 */
    public void disconnect();
}
