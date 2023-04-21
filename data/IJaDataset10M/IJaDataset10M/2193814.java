package de.laidback.racoon.logic.synus;

/**
 * @author Thomas
 * 
 */
public class SynusLogic {

    private static SynusLogic instance;

    private GameObjectFactory objectFactory;

    private ResponseFactory responseFactory;

    public static SynusLogic getInstance() {
        if (instance == null) {
            instance = new SynusLogic();
        }
        return instance;
    }

    private SynusLogic() {
        objectFactory = new GameObjectFactory();
        responseFactory = new ResponseFactory();
    }

    public SynusWorld createGame() {
        return objectFactory.createWorld();
    }

    /**
	 * @return the responseFactory
	 */
    public ResponseFactory getResponseFactory() {
        return responseFactory;
    }

    /**
	 * @return the objectFactory
	 */
    public GameObjectFactory getObjectFactory() {
        return objectFactory;
    }
}
