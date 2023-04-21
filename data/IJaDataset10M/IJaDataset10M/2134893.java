package Grafikus;

/**
 * Ez  az  osztály  az  absztakt  ősosztálya  a  különleges  bogyókat  reprezentáló  osztályoknak, 
 * amelyek pozícióikat nem képesek megváltoztatni. 
 */
public abstract class ActionBerry extends Berry implements IAction {

    /*** 
     * a hátralévő idő, ameddig az adott bogyó a pályán marad
     */
    protected int timeLimit;

    /*** 
     * a bogyó állapotát jelzi, hogy felvették, vagy sem
     */
    protected boolean pickedUp;

    public ActionBerry(int timeLimit) {
        this.timeLimit = timeLimit;
        pickedUp = false;
    }

    public ActionBerry(FieldElement field) {
        super(field);
        pickedUp = false;
    }

    /**
     * @return A hátralévő időt adja vissza.
     */
    public int getTimeLimit() {
        return timeLimit;
    }

    /**
     * 
     * @param timeLimit A hátralévő időt állítja be.
     */
    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    /**
     * 
     * @return Visszaadja, hogy felvették-e már a bogyót.
     */
    public boolean getPickedUp() {
        return pickedUp;
    }
}
