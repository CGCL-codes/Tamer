package org.fudaa.fudaa.sipor;

/**
 * Classe qui d�crit lun navire g�n�r� par l'executable fortran g�narr.
 *@version $Version$
 * @author hadoux
 *
 */
public class GenarrNavire {

    int navire;

    int categorie;

    int jour;

    int heure;

    int minute;

    public GenarrNavire(int navire, int categorie, int jour, int heure, int minute) {
        super();
        this.navire = navire;
        this.categorie = categorie;
        this.jour = jour;
        this.heure = heure;
        this.minute = minute;
    }

    public GenarrNavire(GenarrNavire clone) {
        navire = clone.navire;
        categorie = clone.categorie;
        jour = clone.jour;
        heure = clone.heure;
        minute = clone.minute;
    }
}
