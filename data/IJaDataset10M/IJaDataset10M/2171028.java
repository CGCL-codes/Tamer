package coursimpl;

import cours.*;

public class DejaInscritException extends Exception {

    private Adherent adherent;

    public DejaInscritException(Adherent adherent) {
        this.adherent = (Adherent) adherent;
    }

    public String toString() {
        return this.adherent.toString() + " est d�j� inscrit � ce cours";
    }
}
