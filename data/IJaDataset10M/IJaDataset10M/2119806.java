package src.gladiatormanager;

import java.util.ArrayList;

/**
 *
 * @author Antti
 */
public class Joukkue {

    public static final int SININEN = 2;

    public static final int PUNAINEN = 1;

    public static final int TYHJA = 0;

    ArrayList<Gladiaattori> gList;

    public Joukkue() {
        gList = new ArrayList<Gladiaattori>();
    }

    public void addGladiator(Gladiaattori gladiaattori) {
        gList.add(gladiaattori);
    }
}
