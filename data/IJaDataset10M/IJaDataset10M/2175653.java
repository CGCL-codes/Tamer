package TransmitterS;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

/**
 * @author mschneider
 * <p>TODO documentation
 */
public interface NetworkCenter extends Remote {

    boolean sendMessage(String sender, String nachricht, Set<String> receiver) throws RemoteException;

    /**
	 * Returns a {@link java.util.Set} containing the names of the avaible parsers located on the Server.
	 * @return	the avaible parsers
	 * @throws	RemoteException if an error in the client-server connection occured.
	 */
    public Set<String> avaibleParsers() throws RemoteException;

    /**
	 * Returns a {@code byte[]} containing the file which was specified.
	 * @param	fileName	the requested parser's name.
	 * @return	the specified file
	 * @throws	RemoteException	if an error in the client-server connection occured.
	 */
    public byte[] downloadFile(String fileName) throws RemoteException;

    /**
	 * Returns a {@code byte[]} containing the md5 hashsum of the specified file.
	 * @param	fileName	the requested parser's name.
	 * @return	the specified file's md5 hashsum
	 * @throws	RemoteException	if an error in the client-server connection occured.
	 */
    public byte[] verifyFile(String fileName) throws RemoteException;
}
