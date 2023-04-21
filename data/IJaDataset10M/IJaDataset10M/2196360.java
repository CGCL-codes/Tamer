package client.gui;

/**
 * Datentyp der die 3 Skillwerte enthält.
 * @author aqualuk
 *
 */
public class SkillInfo {

    public int life;

    public int force;

    public int freq;

    /**
	 * Konstruktor.
	 * @param life Um wieviel Life erhöhen.
	 * @param force Um wieviel Schusskraft erhöhen.
	 * @param freq Um wieviel Schussfrequenz verringern.
	 */
    public SkillInfo(int life, int force, int freq) {
        this.life = life;
        this.force = force;
        this.freq = freq;
    }
}
