package monster;

import java.util.Stack;

/**
 * Monster tracker
 * @author dkimnach
 */
public class Tracker {

    private int enctotal;

    private int encnum;

    private Monster current;

    private Stack<Monster> encounters = new Stack<Monster>();

    /**
     * Constructs a new monster tracker
     */
    public Tracker() {
        enctotal = 9;
        encnum = 1;
        initEncounters();
    }

    /**
     * Constructs a new monster tracker for [enc] encounters
     * @param enc encounters
     */
    public Tracker(int enc) {
        enctotal = enc;
        encnum = 1;
    }

    /**
     * Add another monster encounter (e.g. bloodmage special)
     * @return total encounters
     */
    public int addEnc() {
        enctotal++;
        drawRandomMonster();
        return enctotal;
    }

    /**
     * Begin an encounter
     * @return encounter number
     */
    public int beginEnc() {
        current = encounters.pop();
        return encnum;
    }

    /**
     * End encounter
     * @return encounter number
     */
    public int endEnc() {
        encnum++;
        return encnum;
    }

    /**
     * Damage monster
     * @param dmg
     * @return monster health
     */
    public int dmgEnc(int dmg) {
        int h = current.damage(dmg);
        return h;
    }

    /**
     * Heal monster
     * @param heal
     * @return monster health
     */
    public int healEnc(int heal) {
        int h = current.heal(heal);
        return h;
    }

    /**
     * Get encounter number
     * @return encunter number
     */
    public int getEncNum() {
        return encnum;
    }

    /**
     * Get total encounters
     * @return total encounters
     */
    public int getEncTotal() {
        return enctotal;
    }

    /**
     * Get current encounter
     * @return current encounter
     */
    public Monster getCurrentEnc() {
        return current;
    }

    private void initEncounters() {
        for (int i = 0; i < enctotal; i++) {
            drawRandomMonster();
        }
    }

    private void drawRandomMonster() {
    }
}
