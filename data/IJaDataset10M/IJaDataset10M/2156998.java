package Networking.server;

public class GameIsFullException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1144359626083409177L;

    public GameIsFullException() {
    }

    public GameIsFullException(String arg0) {
        super(arg0);
    }

    public GameIsFullException(Throwable arg0) {
        super(arg0);
    }

    public GameIsFullException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
