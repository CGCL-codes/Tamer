package com.paregos.danoftp;

/**
 * This class acts as an interface to the server. It is
 * used to get references to the server-object and the
 * user validator.
 *
 * @author  Erik Eklund, Johan "Spocke" S�rlin
 * @version $Revision: 1.1.1.1 $
 */
public class FTPServerContext {

    /**
	 * The only constructor. Needs a server reference as argument.
	 *
	 * @param server The ftp server.
	 */
    FTPServerContext(FTPServer server) {
        this.server = server;
    }

    /**
	 * This method returns a reference to the ftp server object.
	 *
	 * @return a reference to the server
	 */
    public FTPServer getServerRef() {
        return server;
    }

    /**
	 * This method returns a reference to the user validator object.
	 * If a validator has not been set, the server acts as a user
	 * validator, and you will recieve a reference to the server.
	 *
	 * @return a reference to the user validator
	 */
    public FTPUserValidator getUserValidatorRef() {
        return userValidator;
    }

    /**
	 * This sets a new user validator object.
	 *
	 * @param uv a reference to the user validator
	 */
    public void setUserValidatorRef(FTPUserValidator uv) {
        userValidator = uv;
    }

    private FTPServer server;

    private FTPUserValidator userValidator = null;
}
