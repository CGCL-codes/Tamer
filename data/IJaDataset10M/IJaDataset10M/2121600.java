package Grafikus;

import java.awt.*;

/**
 * A  kígyó testének  elemeit  reprezentáló  osztály.
 */
public class BodyStepElement extends StepElement {

    /**
     * Harapás érte az adott testelemet. Jelez saját kígyójának, hogy egy fűrészes kígyó nekiütközött annak biteOnMe() metódusának meghívásával. 
     */
    public void biteOnMe() {
        mySnake.biteOnMe(this);
    }

    /**
     * Hatás kérése. Másképp hív vissza a testelem ha van benne kőbogyó
     * és másképp, ha nincs.
     * @param affectable A hatásban résztvevő másik objektum.
     */
    public void affect(IAffectable affectable) {
        if (mySnake != null) {
            if (hasStoneBerry()) {
                affectable.runIntoStoneStepElement();
            } else {
                affectable.runIntoBodyStepElement();
            }
        }
    }

    public BodyStepElement(Snake mySnake) {
        this.mySnake = mySnake;
    }

    public Color getColor() {
        return mySnake.getColor();
    }
}
