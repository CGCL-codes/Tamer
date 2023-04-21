package za.co.OO7J.utils;

/**
 * Based on the db4o examples
 *  
 * @author pvz
 * 29 Jan 2009
 *
 */
public interface ServerConfiguration {

    /**
	    * the host to be used.
	    * <br>If you want to run the client server examples on two computers,
	    * enter the computer name of the one that you want to use as server. 
	    */
    public String HOST = "localhost";

    /**
	    * the database file to be used by the server.
	    */
    public String FILE = "oo7jsmall.yap";

    /**
	    * the port to be used by the server.
	    */
    public int PORT = 9000;

    /**
	    * the user name for access control.
	    */
    public String USER = "OO7";

    /**
	    * the pasword for access control.
	    */
    public String PASS = "OO7";
}
