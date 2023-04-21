package com.oozinoz.carousel2;

/**
 * Model the behavior of a carousel door when it's closing. This refactoring
 * uses constant state values.
 * 
 * @author Steven J. Metsker
 */
public class DoorClosing extends DoorState {

    /**
     * Stop closing and start opening the door.
     */
    public void touch(Door door) {
        door.setState(OPENING);
    }

    /**
     * Stop closing and close the door.
     */
    public void complete(Door door) {
        door.setState(DoorConstants.CLOSED);
    }
}
