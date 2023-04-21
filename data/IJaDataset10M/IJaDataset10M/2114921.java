package Visitables;

import Composite.*;
import fichiers.*;

public class Visiteur_MiseAJourSupprimer implements Visiteur {

    public Visiteur_MiseAJourSupprimer() {
    }

    public void visiteDossier(Dossier d) {
        if (d.getVerification() == true && !d.getFichierCourant().exists()) {
            ((Dossier) d.getPere()).removeChild(d);
        }
    }

    public void visitePhoto(Photo p) {
        if (!p.getFichierCourant().exists()) p.getPere().removeChild(p);
    }

    public void visiteRacineDossier(RacineDossier racineDossier) {
    }
}
