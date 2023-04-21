package fmglemmings;

import java.util.logging.*;

/**
 *innen megy ki a lemming
 */
public class Spawn extends MapObject {

    private static Logger logger = Logger.getLogger("fmglemmings");

    /**
	 * A megszuletendo lemmingek szama
	 */
    private int bornnumber;

    /**
	 * A varakozasi ido ket lemming szuletese kozott
	 */
    private int wait;

    /**
	 * A Spawnban a tickEvent hatasara szuletnek a lemmingek
	 */
    public void tickEvent() {
        wait++;
        if (wait == 2) {
            if (bornnumber != 0) {
                logger.info("Uj lemming szuletese");
                gf.bornRequest(new Lemming(X, Y, 6));
                bornnumber--;
            }
            wait = 0;
        }
    }

    /**
	 * Konstruktor
	 * @param a X koordinata
	 * @param b Y koordinata
	 * @param c irany
	 * @param n Kiadando lemmingek szama
	 */
    public Spawn(int a, int b, int c, int n) {
        super(a, b, c);
        wait = 0;
        bornnumber = n;
        destructable = false;
        logger.info("Konstruktor kesz X:" + X + " Y: " + Y);
    }
}
