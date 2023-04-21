package main;

import java.util.Vector;
import communication.UmbraPrimaryKey;
import logic.Player;
import logic.Unit;
import main.constants.Constants;

public class SourceOfAllThings {

    private static int maximumIDs = 100000;

    private static Creatable[] everything = null;

    private static Vector<Integer> pks = null;

    public static void initialize() {
        pks = new Vector<Integer>();
        everything = new Creatable[maximumIDs];
        for (int i = 0; i < maximumIDs; i++) {
            pks.add(new Integer(i));
        }
    }

    public static Unit getUnit(int pk) {
        return (Unit) everything[pk];
    }

    public static void createID(Unit object) {
        if (pks.isEmpty()) {
            System.out.println("OMG! There are no more avaiable Identifications! Sorry.");
        } else {
            object.setPrimaryKey(new UmbraPrimaryKey(Constants.thisPlayer, object.getType().ordinal(), pks.remove(0).intValue()));
        }
    }

    public static void register(Unit object, UmbraPrimaryKey pk) {
        everything[pk.id] = object;
    }

    public static void unregister(UmbraPrimaryKey pk) {
        everything[pk.id] = null;
        pks.add(new Integer(pk.id));
    }

    public static Player getActualPlayer() {
        return new Player(Constants.thisPlayer);
    }

    public static Player getPlayer(int i) {
        return new Player(i);
    }
}
