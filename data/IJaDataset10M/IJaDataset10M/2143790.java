package org.fudaa.fudaa.albe;

import java.awt.event.KeyEvent;

/**
 * Panneau s'affichant lorsque l'utilisateur choisit la courbe du fascicule 62
 * Titre V longue dur�e dans l'onglet 'Param�tres de sol'.
 * 
 * @author Sabrina Delattre
 */
public class AlbeSolParametresCourbeTVLongueDuree extends AlbeAbstractSolCourbeElastoSetraFascicule {

    /**
   * Constructeur.
   */
    public AlbeSolParametresCourbeTVLongueDuree(final AlbeSolParametres _sol) {
        super(_sol, 5, true, false);
        initButton_.addActionListener(this);
    }

    /**
   * 
   */
    public void afficheGraphique(int _indexCouche) {
        afficheGraphiqueCourbe5Pts(_indexCouche);
    }

    /**
   *
   */
    public void keyReleased(KeyEvent _evt) {
        genereTableauxCourteEtLongueDuree(AlbeLib.TABLEAU_LONGUE_DUREE, getCurrentTab());
    }
}
