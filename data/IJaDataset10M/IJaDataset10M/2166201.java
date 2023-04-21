package game.cannonMode;

import game.GUIObject;

/**
 * The CannonHelp displays the possible note names in the GUI when the player is performing badly
 * It keeps track of the misses and hits, and appears and disappears according to these values
 * 
 * @author P. Curet
 */
public class CannonHelp extends GUIObject {

    private int showWithMistakes = 3;

    private int mistakeCounter = showWithMistakes * 2;

    private boolean isVisible = true;

    PlaceStaff staffToHelp;

    /**
	 * Constructor for the cannonHelp
	 */
    public CannonHelp(PlaceStaff staff) {
        super(staff.getX(), staff.getY());
        staffToHelp = staff;
    }

    /**
	 * Keeps track of the mistakes made
	 * @param mistake
	 */
    public void madeMistake(boolean mistake) {
        if (mistake) {
            this.mistakeCounter++;
        } else if (this.mistakeCounter > 0) {
            this.mistakeCounter--;
        }
        this.isVisible = this.mistakeCounter >= 3;
    }

    /**
	 * @return the visibility of the help
	 */
    public boolean isVisible() {
        return this.isVisible;
    }
}
