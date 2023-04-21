package de.robowars.server;

/**
 * @author Helge
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class GameFinishedException extends ServerException {

    /**
	 * Constructor for GameFinishedException.
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
    public GameFinishedException(String arg0, Throwable arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    /**
	 * Constructor for GameFinishedException.
	 * @param arg0
	 * @param arg1
	 */
    public GameFinishedException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
	 * Constructor for GameFinishedException.
	 * @param arg0
	 */
    public GameFinishedException(Throwable arg0) {
        super(arg0);
    }

    /**
	 * Constructor for GameFinishedException.
	 * @param arg0
	 */
    public GameFinishedException(String arg0) {
        super(arg0);
    }

    /**
	 * Constructor for GameFinishedException.
	 * @param arg0
	 * @param arg1
	 */
    public GameFinishedException(String arg0, int arg1) {
        super(arg0, arg1);
    }
}
