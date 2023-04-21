package cours;

import java.util.ArrayList;

public class InscriptionManager {

    private static ArrayList<Inscription> lesInscriptions = new ArrayList<Inscription>();

    public static void add(Inscription i) {
        lesInscriptions.add(i);
    }
}
