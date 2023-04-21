package org.fudaa.ctulu.modele;

import org.fudaa.ctulu.CtuluCommandContainer;

/**
 * Une classe permettant de gerer un vecteur de double. en ajoutant des fonctionnalites sup: <br>
 * Undo/Redo Max/min enregistrer automatiquement
 *
 * @author Fred Deniger
 * @version $Id: CtuluModelAbstract.java,v 1.2 2006-07-13 13:34:37 deniger Exp $
 */
public abstract class CtuluModelAbstract implements CtuluModelInterface {

    /**
   * Initialise avec une liste de taille de 20 par defaut.
   */
    protected CtuluModelAbstract() {
    }

    protected void fireObjectChanged() {
    }

    public boolean addAllObject(final Object _dataArray, final CtuluCommandContainer _c) {
        return false;
    }

    public Object[] getObjectValues() {
        final Object[] r = new Object[getSize()];
        for (int i = r.length - 1; i >= 0; i--) {
            r[i] = getObjectValueAt(i);
        }
        return r;
    }

    public boolean addObject(final Object _data, final CtuluCommandContainer _c) {
        return false;
    }

    public boolean insertObject(final int _i, final Object _data, final CtuluCommandContainer _c) {
        return false;
    }

    public boolean remove(final int _i, final CtuluCommandContainer _c) {
        return false;
    }

    public boolean remove(final int[] _i, final CtuluCommandContainer _c) {
        return false;
    }

    public void removeAll(final CtuluCommandContainer _c) {
    }
}
