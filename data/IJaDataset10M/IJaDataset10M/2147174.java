package empruntable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class EmpruntableManager {

    private static ArrayList<Empruntable> tousLesEmpruntables = new ArrayList<Empruntable>();

    public static void add(Empruntable E) {
        tousLesEmpruntables.add(E);
    }

    public static boolean containsId(int id) {
        for (Empruntable E : tousLesEmpruntables) if (E.getId() == id) return true;
        return false;
    }

    public static Empruntable getById(int id) {
        for (Empruntable E : tousLesEmpruntables) if (E.getId() == id) return E;
        return (Empruntable) new Object();
    }

    public static int nextId() {
        int size = tousLesEmpruntables.size();
        return size > 0 ? tousLesEmpruntables.get(size - 1).getId() + 1 : 1;
    }

    public static String empruntablesPossiblesPour(Abonnable A) {
        String liste = "";
        for (Empruntable E : tousLesEmpruntables) {
            if (E.estDisponible() && E.estEmpruntablePar(A)) liste += E + "\n";
        }
        return liste;
    }

    public static String getFullString() {
        String fullString = "";
        for (Empruntable E : tousLesEmpruntables) fullString += E + " (ID:" + E.getId() + ")\n";
        return fullString;
    }

    public static void serializeArray(ObjectOutputStream out) {
        try {
            out.writeObject(tousLesEmpruntables);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
