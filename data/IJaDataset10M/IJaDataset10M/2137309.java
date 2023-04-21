package net.sf.opensftp;

import java.util.Hashtable;

/**
 * An <code>SftpSession</code> object represents the context of a communication
 * between a client and an SFTP server.
 * 
 * @author BurningXFlame@gmail.com
 */
public interface SftpSession {

    /**
	 * Returns the Date Time Pattern of the server.
	 * 
	 * @deprecated
	 */
    public String getServerDateTimePattern();

    /**
	 * Returns the user.
	 */
    public String getUser();

    /**
	 * Returns the host.
	 */
    public String getHost();

    /**
	 * Returns the current path.
	 */
    public String getCurrentPath();

    /**
	 * Returns a <code>Hashtable</code> which holds key-value pairs of
	 * additional/customized info.
	 */
    public Hashtable getExtras();
}
