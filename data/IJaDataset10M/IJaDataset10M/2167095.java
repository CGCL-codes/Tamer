package com.doculibre.intelligid.entites.ddv;

/**
 * Choix dans une liste de référence. Chaque organisme fournit la sienne.
 * 
 * TODO La demander à Richard Parent.
 * 
 * @author Vincent Dussault
 */
public class StatutVersion extends ElementDomaineValeurs {

    private static final long serialVersionUID = 1L;

    /**
	 * Cette méthode définie si l'élément est utilisé statiquement dans le code. Si c'est le cas, il
	 * sera impossible de modifier son code
	 */
    @Override
    public boolean estUtiliseStatiquement() {
        return false;
    }
}
