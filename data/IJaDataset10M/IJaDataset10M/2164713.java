package org.fudaa.fudaa.refonde;

import java.awt.print.PageFormat;
import java.util.Vector;
import org.fudaa.ebli.calque.BVueCalque;
import org.fudaa.ebli.geometrie.GrBoite;
import org.fudaa.ebli.geometrie.GrPoint;
import org.fudaa.ebli.geometrie.GrPolygone;
import org.fudaa.ebli.geometrie.VecteurGrContour;
import org.fudaa.ebli.repere.RepereMouseKeyController;
import org.fudaa.ebli.repere.RepereMouseKeyTarget;

/**
 * Un modele de vues. Il renferme des objets de type BVueCalque.
 *
 * @version      $Id: RefondeModeleFenetres.java,v 1.7 2006-12-05 10:18:14 deniger Exp $
 * @author       Bertrand Marchand
 */
public class RefondeModeleFenetres {

    /**
   * La liste des vues. Le vecteur contient des objets de type
   * BVueCalque.
   */
    private Vector vfns_;

    /**
   * Vecteur des g�om�tries des vues.
   */
    private Vector vfnsGeo_;

    /**
   * Cr�ation d'un mod�le de vues.
   */
    public RefondeModeleFenetres() {
        vfns_ = new Vector();
        vfnsGeo_ = new Vector();
    }

    /**
   * D�finition du format de page dans lequel la vue doit �tre ajout�e. Par
   * d�faut le format de page vaut PrinterJob.getPrinterJob().defautPage().
   */
    public void setPageFormat(PageFormat _pf) {
    }

    /**
   * Ajoute une vue au mod�le en pr�cisant ses dimensions.
   *
   * @param _vc La vue de post traitement.
   * @param _x  Position x de la vue.
   * @param _y  Position y de la vue.
   * @param _w  Largeur de la vue.
   * @param _h  Hauteur de la vue.
   */
    public void ajoute(BVueCalque _vc, double _x, double _y, double _w, double _h) {
        if (vfns_.contains(_vc)) return;
        GrPolygone pg = new GrPolygone();
        pg.sommets_.ajoute(new GrPoint(_x, _y, 0));
        pg.sommets_.ajoute(new GrPoint(_x + _w, _y, 0));
        pg.sommets_.ajoute(new GrPoint(_x + _w, _y + _h, 0));
        pg.sommets_.ajoute(new GrPoint(_x, _y + _h, 0));
        vfns_.add(_vc);
        vfnsGeo_.add(pg);
    }

    /**
   * Enl�ve une vue au mod�le.
   */
    public void enleve(BVueCalque _vc) {
        int i = vfns_.indexOf(_vc);
        vfns_.remove(i);
        vfnsGeo_.remove(i);
    }

    /**
   * Supprime tous les objets du mod�le.
   */
    public void enleveTout() {
        vfns_.clear();
        vfnsGeo_.clear();
    }

    /**
   * Retourne les vues du mod�le sous forme de vecteur.
   */
    public Vector getObjets() {
        return vfns_;
    }

    /**
   * Retourne la g�om�trie associ�e � une vue.
   * @return La g�om�trie ou <i>null</i> si la vue n'est pas dans le mod�le.
   */
    public GrPolygone getGeometrie(BVueCalque _vc) {
        int i = vfns_.indexOf(_vc);
        return i != -1 ? (GrPolygone) vfnsGeo_.get(i) : null;
    }

    /**
   * Boite englobante des objets contenus dans le mod�le.
   * @return Boite englobante. Si le mod�le est vide,
   * la boite englobante retourn�e est <I>null</I>
   */
    public GrBoite getBoite() {
        GrBoite r = null;
        if (vfnsGeo_.size() > 0) {
            if (r == null) r = new GrBoite();
            for (int i = 0; i < vfnsGeo_.size(); i++) r.ajuste(((GrPolygone) vfnsGeo_.get(i)).boite());
        }
        return r;
    }

    /**
   * Renvoi la liste des �l�ments s�lectionnables, soit tous les objets.
   * <p>
   * Cette liste est retourn�e dans le sens inverse de cr�ation des objets, de
   * fa�on que le dernier cr�� soit le premier � �tre test� lors de la s�lection.
   *
   * @return La liste des objets.
   */
    public VecteurGrContour getSelectionnables() {
        VecteurGrContour r = new VecteurGrContour();
        for (int i = vfnsGeo_.size() - 1; i >= 0; i--) r.ajoute((GrPolygone) vfnsGeo_.get(i));
        return r;
    }
}
