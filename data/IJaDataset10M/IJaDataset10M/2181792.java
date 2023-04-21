package org.fudaa.dodico.ef.impl;

import org.fudaa.dodico.ef.EfElement;
import org.fudaa.dodico.ef.EfGridInterface;

/**
 * Un adapteur permettant de translater les coordonn�es selon deux offset.
 * 
 * @author fred deniger
 * @version $Id: EfGridTranslate.java,v 1.2 2007-01-22 13:34:20 deniger Exp $
 */
public class EfGridTranslate extends EfGridDefaultAbstract {

    final EfGridInterface init_;

    final double offsetX_;

    final double offsetY_;

    public static EfGridInterface translateToOrigin(final EfGridInterface _grid) {
        if (_grid == null) {
            return null;
        }
        return new EfGridTranslate(_grid, -_grid.getMinX(), -_grid.getMinY());
    }

    public EfGridTranslate(final EfGridInterface _init, final double _offsetX, final double _offsetY) {
        init_ = _init;
        offsetX_ = _offsetX;
        offsetY_ = _offsetY;
    }

    protected boolean setZIntern(final int _i, final double _newV) {
        return false;
    }

    public double getPtX(final int _i) {
        return init_.getPtX(_i) + offsetX_;
    }

    public double getPtY(final int _i) {
        return init_.getPtY(_i) + offsetY_;
    }

    public double getPtZ(final int _i) {
        return init_.getPtZ(_i);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void setPt(int _i, double _x, double _y) {
        this.setPt(_i, _x, _y, 0.0);
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public void setPt(int _i, double _x, double _y, double _z) {
        init_.setPt(_i, _x - offsetX_, _y - offsetY_, _z);
    }

    public int getPtsNb() {
        return init_.getPtsNb();
    }

    public EfElement getElement(final int _i) {
        return init_.getElement(_i);
    }

    public int getEltNb() {
        return init_.getEltNb();
    }
}
