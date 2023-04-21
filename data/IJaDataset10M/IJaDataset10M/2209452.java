package fr.cpbrennestt.metier.entite.comparateurs;

import java.io.Serializable;
import fr.cpbrennestt.metier.entite.MembreBureau;

public class ComparateurBureauOrdre extends ComparateurBureau implements Serializable {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    public int comparer(MembreBureau m1, MembreBureau m2) {
        if (m1.getOrdreAffichage() > m2.getOrdreAffichage()) return 1;
        if (m1.getOrdreAffichage() == m2.getOrdreAffichage()) return 0;
        return -1;
    }
}
