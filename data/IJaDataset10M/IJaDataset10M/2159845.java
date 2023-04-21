package org.fudaa.dodico.rubar.io;

import org.fudaa.ctulu.fileformat.FileFormat;
import org.fudaa.dodico.ef.EfGridInterface;
import org.fudaa.dodico.ef.EfGridSourcesAbstract;
import org.fudaa.dodico.h2d.rubar.H2dRubarGrid;
import org.fudaa.dodico.h2d.rubar.H2dRubarGridAreteSource;

/**
 * @author Fred Deniger
 * @version $Id: RubarDatSource.java,v 1.9 2006-09-19 14:45:51 deniger Exp $
 */
public class RubarDatSource extends EfGridSourcesAbstract implements H2dRubarGridAreteSource {

    H2dRubarGrid grid_;

    /**
   * les elements voisins par aretes.
   */
    int[][] iaElementVoisin_;

    int[] idxAreteLimiteEntrante_;

    /**
   * les elements voisins par elements.
   */
    int[][] ieElementVoisin_;

    int nbDecimal_;

    /**
   * @param _g le maillage volumique
   * @param _ieEltVoisin le tableau des elt voisins pour chaque elt
   * @param _iaEltVoisin le tableau des elt voisins pour chaque arete
   */
    public RubarDatSource(final H2dRubarGrid _g, final int[][] _ieEltVoisin, final int[][] _iaEltVoisin) {
        grid_ = _g;
        ieElementVoisin_ = _ieEltVoisin;
        iaElementVoisin_ = _iaEltVoisin;
    }

    /**
   * @see org.fudaa.dodico.h2d.rubar.H2dRubarGridAreteSource#elementsVoisinsParArete()
   */
    public int[][] elementsVoisinsParArete() {
        return iaElementVoisin_;
    }

    /**
   * @see org.fudaa.dodico.h2d.rubar.H2dRubarGridAreteSource#elementsVoisinsParElement()
   */
    public int[][] elementsVoisinsParElement() {
        return ieElementVoisin_;
    }

    public FileFormat getFileFormat() {
        return RubarDATFileFormat.getInstance();
    }

    /**
   * @see org.fudaa.dodico.h2d.rubar.H2dRubarGridAreteSource#getGrid()
   */
    public EfGridInterface getGrid() {
        return grid_;
    }

    public int[] getIdxAreteLimiteEntrante() {
        return idxAreteLimiteEntrante_;
    }

    public int getNbDecimal() {
        return nbDecimal_;
    }

    public H2dRubarGrid getRubarGrid() {
        return grid_;
    }

    /**
   * L'ordre est important car utilise dans d'autre fichier.
   * 
   * @param _idxAreteLimiteEntrante les indices des aretes ouvertes.
   */
    public void setIdxAreteLimiteEntrante(final int[] _idxAreteLimiteEntrante) {
        idxAreteLimiteEntrante_ = _idxAreteLimiteEntrante;
    }
}
