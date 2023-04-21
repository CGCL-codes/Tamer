package praktikumid.kaug1;

import java.util.Formatter;

/**
 * M�ngija andmete hoidmine.
 * 
 * @author A
 *
 */
public class Player extends Object {

    /**
	 * M�ngija nimi.
	 */
    private String nimi;

    /**
	 * Skoor palju m�ngija on saanud triki
	 * "�hed" eest.
	 */
    private int yhed;

    /**
	 * Konstruktor m�ngija objekti loomiseks.
	 * 
	 * @param nimi - M�ngija nimi.
	 */
    public Player(String nimi) {
        this.nimi = nimi;
    }

    /**
	 * M�ngija nime k�ttesaamiseks.
	 * 
	 * @return - m�ngija nimi.
	 */
    public String getNimi() {
        return nimi;
    }

    /**
	 * Triki "�hed" skoori v��rtustamine.
	 * 
	 * @param yhed - mitu punkti
	 */
    public void setYhed(int yhed) {
        this.yhed = yhed;
    }

    /**
	 * "�hed" triki punktide k�tte saamine.
	 * @return - mitu punkti on
	 */
    public int getYhed() {
        return yhed;
    }

    public int summa() {
        return getYhed();
    }

    @Override
    public String toString() {
        Formatter f = new Formatter();
        String result = f.format("%10s | %4s |", nimi, getYhed()).toString();
        return result;
    }
}
