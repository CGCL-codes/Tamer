package de.robowars.server;

import de.robowars.util.RobowarsException;

/**
 *
 * @author Kai
 * 
 **/
public class ServerFactory {

    private static ServerFactory instance;

    private static GameMaster gameMaster = null;

    /**
	 * Constructor for ServerFactory.
	 */
    protected ServerFactory() {
        super();
    }

    /**
	 * @return CommFactory's singleton instance.
	 */
    public static ServerFactory getInstance() {
        if (instance == null) {
            instance = new ServerFactory();
        }
        return instance;
    }

    /**
     * @return Get implementation of ClientFacade.
     * @see de.robowars.comm.ClientFacade
     */
    public GameMaster startServer(int port) {
        try {
            if (gameMaster == null) return (GameMaster) new GameServer(port); else return gameMaster;
        } catch (RobowarsException re) {
            re.printStackTrace();
            return null;
        }
    }
}
