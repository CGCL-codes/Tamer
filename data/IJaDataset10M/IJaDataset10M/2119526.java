package org.fudaa.fudaa.dimduc;

/**
 * @version      $Revision: 1.5 $ $Date: 2006-09-19 15:02:07 $ by $Author: deniger $
 * @author       Christian Barou
 */
public class DimducArrondis {

    protected double a_arrondir_;

    protected double arrondi_;

    public DimducArrondis(final double _a_arrondir) {
        a_arrondir_ = _a_arrondir;
    }

    public void arrondi2Decimales() {
        arrondi_ = a_arrondir_ * 100;
        arrondi_ = Math.round(arrondi_);
        arrondi_ = (arrondi_ * 0.01);
    }

    public double arrondi() {
        return arrondi_;
    }
}
