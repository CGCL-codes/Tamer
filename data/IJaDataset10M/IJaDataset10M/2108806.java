package info.metlos.jdc.fileshare;

import info.metlos.jdc.fileshare.list.FileEntry;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Implementors of this interface can create hashes of files using a message
 * digest algorithm implementation supplied.
 * 
 * @author metlos
 * 
 * @version $Id: IHasher.java 112 2007-08-12 00:52:45Z metlos $
 */
public interface IHasher {

    /**
	 * Sets the message digest algorithm that will be used to create the hashes
	 * of the files.
	 * 
	 * @param messageDigest
	 */
    void setMessageDigest(MessageDigest messageDigest);

    /**
	 * Computes the hash for a single file and sets the digest of the file using
	 * {@link FileEntry#setContentDigest(String)}.
	 * 
	 * @param fe
	 *            the file entry in the file list
	 * @param fileData
	 *            the contents of the file.
	 */
    void createHashFor(FileEntry fe, InputStream fileData);

    /**
	 * The listener will be informed when a hash for some file is finished.
	 * 
	 * @param listener
	 */
    void registerHasherListener(IHasherListener listener);

    /**
	 * The listener will no longer be notified about finished hashing
	 * operations.
	 * 
	 * @param listener
	 */
    void deregisterHasherListener(IHasherListener listener);
}
