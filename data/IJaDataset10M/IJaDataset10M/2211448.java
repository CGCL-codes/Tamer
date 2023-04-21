package org.fudaa.dodico.h2d;

import gnu.trove.TDoubleArrayList;
import org.fudaa.ctulu.CtuluRange;

/**
 * @author Fred Deniger
 * @version $Id: H2dSpatialPropertyAbstract.java,v 1.5 2006-09-19 14:43:25 deniger Exp $
 */
public abstract class H2dSpatialPropertyAbstract implements H2dSpatialPropertyInterface {

    protected TDoubleArrayList x_;

    protected TDoubleArrayList y_;

    /**
   * Initialise les vecteus internes � une capacite de 10.
   */
    public H2dSpatialPropertyAbstract() {
        this(10);
    }

    /**
   * Initialise les vecteus internes � une capacite de _nbValue.
   *
   * @param _nbValue la capacit� initiales des vecteurs internes
   */
    public H2dSpatialPropertyAbstract(final int _nbValue) {
        x_ = new TDoubleArrayList(_nbValue);
        y_ = new TDoubleArrayList(_nbValue);
    }

    protected H2dSpatialPropertyAbstract(final H2dSpatialPropertyAbstract _a) {
        if (_a.x_.size() != _a.y_.size()) {
            throw new IllegalArgumentException("bad sizes");
        }
        x_ = new TDoubleArrayList(_a.x_.toNativeArray());
        y_ = new TDoubleArrayList(_a.y_.toNativeArray());
    }

    boolean contains(final double _x, final double _y) {
        return x_.contains(_x) && y_.contains(_y);
    }

    /**
   * Ajoute un point. Attention ne fait pas de test pour savoir si le point est deja contenu.
   *
   * @param _x le x a ajouter
   * @param _y le y a ajouter
   */
    void addPoint(final double _x, final double _y) {
        x_.add(_x);
        y_.add(_y);
        if ((xR_ != null) && (!xR_.isValueContained(_x))) {
            if (_x < xR_.min_) {
                xR_.min_ = _x;
            }
            if (_x > xR_.max_) {
                xR_.max_ = _x;
            }
        }
        if ((yR_ != null) && (!yR_.isValueContained(_y))) {
            if (_y < yR_.min_) {
                yR_.min_ = _y;
            }
            if (_y > yR_.max_) {
                yR_.max_ = _y;
            }
        }
    }

    double[] remove(final int _i) {
        if ((_i >= 0) && (_i < x_.size())) {
            final double xRemoved = x_.remove(_i);
            final double yRemoved = y_.remove(_i);
            if ((yR_ != null) && (yR_.isValueStrictlyContained(yRemoved))) {
                yR_ = null;
            }
            if ((xR_ != null) && (xR_.isValueStrictlyContained(yRemoved))) {
                yR_ = null;
            }
            return new double[] { xRemoved, yRemoved };
        }
        return null;
    }

    CtuluRange xR_;

    CtuluRange yR_;

    public int getNbPoint() {
        return x_.size();
    }

    public double getX(final int _i) {
        return x_.getQuick(_i);
    }

    public void getXRange(final CtuluRange _r) {
        if (xR_ == null) {
            xR_ = new CtuluRange();
            CtuluRange.getRangeFor(x_, xR_);
        }
        _r.initWith(xR_);
    }

    public double getY(final int _i) {
        return y_.getQuick(_i);
    }

    public void getYRange(final CtuluRange _r) {
        if (yR_ == null) {
            yR_ = new CtuluRange();
            CtuluRange.getRangeFor(y_, yR_);
        }
        _r.initWith(yR_);
    }

    public final boolean isDefinedOnGrilleReguliere() {
        return false;
    }
}
