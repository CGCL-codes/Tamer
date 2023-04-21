package org.fudaa.fudaa.refonde;

import java.util.Vector;
import org.fudaa.ebli.calque.dessin.DeForme;
import org.fudaa.ebli.geometrie.GrBoite;
import org.fudaa.ebli.geometrie.GrContour;
import org.fudaa.ebli.geometrie.VecteurGrContour;

/**
 * Un modele de dessin. Il renferme des objets de type DeForme.
 *
 * @version      $Id: RefondeModeleDessin.java,v 1.5 2006-09-08 16:04:28 opasteur Exp $
 * @author       Bertrand Marchand
 */
public class RefondeModeleDessin {

    /**
   * La liste des dessins. Le vecteur contient des objets de type forme.
   */
    private Vector dessins_;

    /**
   * Cr�ation d'un mod�le de dessin.
   */
    public RefondeModeleDessin() {
        dessins_ = new Vector();
    }

    /**
   * Ajoute une forme au mod�le.
   */
    public void ajoute(DeForme _forme) {
        if (!dessins_.contains(_forme)) dessins_.add(_forme);
    }

    /**
   * Enl�ve une forme au mod�le.
   */
    public void enleve(DeForme _forme) {
        dessins_.remove(_forme);
    }

    /**
   * Supprime tous les objets du mod�le.
   */
    public void enleveTout() {
        dessins_.clear();
    }

    /**
   * Retourne les formes du mod�le sous forme de vecteur.
   */
    public Vector getObjets() {
        return dessins_;
    }

    /**
   * Boite englobante des objets contenus dans le mod�le.
   * @return Boite englobante. Si le mod�le est vide,
   * la boite englobante retourn�e est <I>null</I>
   */
    public GrBoite getBoite() {
        GrBoite r = null;
        if (dessins_.size() > 0) {
            if (r == null) r = new GrBoite();
            for (int i = 0; i < dessins_.size(); i++) r.ajuste(((DeForme) dessins_.get(i)).boite());
        }
        return r;
    }

    /**
   * Renvoi la liste des �l�ments s�lectionnables, soit tous les objets.
   * <p>
   * Cette liste est retourn�e dans le sens inverse de cr�ation des objets, de
   * fa�on que le dernier cr�� soit le premier � �tre test� lors de la s�lection.
   *
   * @return La liste des formes.
   */
    public VecteurGrContour getSelectionnables() {
        VecteurGrContour r = new VecteurGrContour();
        for (int i = dessins_.size() - 1; i >= 0; i--) r.ajoute((GrContour) dessins_.get(i));
        return r;
    }
}
