package ai;

import game.Element;

/**
 * 
 * @author Daniel
 * Manejador de la inteligencia artificial. La clase implementa el patr�n singleton para 
 * poder ser accedida desde cualquier parte del programa. Esta clase se encarga de las 
 * operaciones con almacenamiento y carga de informaci�n en memoria.
 */
public class AIHandler {

    private static AIHandler aiHandler;

    private AIHandler() {
    }

    public static synchronized AIHandler getInstance() {
        if (aiHandler == null) {
            aiHandler = new AIHandler();
        }
        return aiHandler;
    }

    public void update() {
        EventManager.getInstance().update();
        IssuerEvent.getInstance().sendDelayedMessages();
    }

    public void registerObject(double id, String type, Element e) {
        AIObject o = null;
        if (type.equals(AIObject.PLAYER)) {
            o = new AIPlayer(id, e);
        } else if (type.equals(AIObject.BUG)) {
        } else if (type.equals(AIObject.SPIDER)) {
        } else if (type.equals(AIObject.SNAKE)) {
        } else if (type.equals(AIObject.MINOTAUR)) {
            o = new AIMinotaur(id, e);
        }
        if (o == null) {
            System.err.println("AIType " + type + " not found.");
            System.exit(-1);
        }
        EventManager.getInstance().registerObject(o);
    }

    public void deleteObject(double id) {
        EventManager.getInstance().deleteObject(id);
    }

    public void deleteAllObjects() {
        EventManager.getInstance().deleteAllObjects();
        IssuerEvent.getInstance().deleteAllMessages();
    }

    public AIObject getObject(double id) {
        return EventManager.getInstance().getObject(id);
    }

    public void sendMessage(double delay, double senderId, double reciverId, int type) {
        IssuerEvent.getInstance().sendMessage(delay, senderId, reciverId, type);
    }
}
