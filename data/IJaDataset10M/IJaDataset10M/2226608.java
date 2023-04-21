package mwt.xml.xdbforms.xformlayer.transactions.exception;

/**
 * Progetto Master Web Technology
 * @author Gianfranco Murador, Cristian Castiglia, Matteo Ferri
 * @copyright (C) 2009, MCG08
 */
public class CommitterServiceValidateEx extends Exception {

    public CommitterServiceValidateEx(Throwable cause) {
        super(cause);
    }

    public CommitterServiceValidateEx(String message, Throwable cause) {
        super(message, cause);
    }

    public CommitterServiceValidateEx(String message) {
        super(message);
    }

    public CommitterServiceValidateEx() {
    }
}
