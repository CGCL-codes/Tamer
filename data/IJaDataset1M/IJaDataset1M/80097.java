package util;

import state.*;

/**
 * Packing class.
 */
public class Unpacker {

    public static Packable unpack(int messagetype, byte[] data) throws PackingException {
        return unpack(messagetype, data, -1);
    }

    public static Packable unpack(int messagetype, byte[] data, int actor) throws PackingException {
        Packable p = null;
        switch(messagetype) {
            case Packable.TEXT_MESSAGE:
                p = new TextMessage();
                p.unpack(data);
                if (actor != -1) {
                    ((TextMessage) p).setActor(actor);
                }
                break;
            case Packable.ACTION:
                p = new Action();
                p.unpack(data);
                if (actor != -1) {
                    ((Action) p).setActor(actor);
                }
                break;
            case Packable.GAME_EVENT:
                p = new GameEvent();
                p.unpack(data);
                break;
            case Packable.GAME_STATE:
                System.err.println("GAME STATE RECEIVED");
                p = new GameState();
                p.unpack(data);
                break;
            case Packable.PLAYER:
                System.err.println("PLAYER RECEIVED");
                p = new Player();
                p.unpack(data);
                break;
            default:
                throw new PackingException("Unknown message type: " + messagetype);
        }
        return p;
    }
}
