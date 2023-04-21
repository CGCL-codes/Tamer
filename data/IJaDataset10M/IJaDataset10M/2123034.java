package KwiKoL;

/**
 * Occurs when an attempt is made to cast a buff on a player and the 
 * casting player does not have enough MP to do so
 */
public class KoLNotEnoughMPException extends Exception {

    public KoLNotEnoughMPException(String skillID) {
        super("Not enough MP to cast Buff " + skillID);
    }
}
