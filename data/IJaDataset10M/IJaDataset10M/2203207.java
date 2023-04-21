package org.fudaa.ctulu.collection;

import java.util.Arrays;
import org.fudaa.ctulu.CtuluCommandContainer;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.CtuluRange;

/**
 * @author Fred Deniger
 * @version $Id: CtuluArrayDoubleUnique.java,v 1.2 2007-04-16 16:33:53 deniger Exp $
 */
public class CtuluArrayDoubleUnique implements CtuluCollectionDoubleEdit {

    int nb_;

    double v_;

    public CtuluArrayDoubleUnique() {
        this(0);
    }

    public CtuluArrayDoubleUnique(final double _v) {
        v_ = _v;
    }

    public void expandTo(final CtuluRange _rangeToExpand) {
        if (_rangeToExpand != null) {
            _rangeToExpand.expandTo(v_);
        }
    }

    public CtuluArrayDoubleUnique(final double _v, final int _nb) {
        v_ = _v;
        nb_ = _nb;
    }

    public boolean addAllObject(final Object _dataArray, final CtuluCommandContainer _c) {
        return false;
    }

    public boolean addObject(final Object _data, final CtuluCommandContainer _c) {
        return false;
    }

    public CtuluArrayDoubleUnique createMemento() {
        return new CtuluArrayDoubleUnique(v_);
    }

    public Double getCommonValue(final int[] _i) {
        return CtuluLib.getDouble(v_);
    }

    public final double getMax() {
        return v_;
    }

    public final double getMin() {
        return v_;
    }

    public Object getObjectValueAt(final int _i) {
        return CtuluLib.getDouble(getValue(_i));
    }

    public Object[] getObjectValues() {
        final Object[] r = new Double[getSize()];
        for (int i = r.length - 1; i >= 0; i--) {
            r[i] = getObjectValueAt(i);
        }
        return r;
    }

    public void getRange(final CtuluRange _r) {
        _r.max_ = v_;
        _r.min_ = v_;
    }

    public int getSize() {
        return nb_;
    }

    public double getValue(final int _i) {
        return v_;
    }

    public double[] getValues() {
        final double[] r = new double[nb_];
        Arrays.fill(r, v_);
        return r;
    }

    public void initWithDouble(final CtuluCollectionDouble _m, final boolean _throwEvent) {
    }

    public void initWith(final CtuluCollection _model, final boolean _throwEvent) {
    }

    public boolean insertObject(final int _i, final Object _data, final CtuluCommandContainer _c) {
        return false;
    }

    public boolean isSameValues(final int[] _i) {
        return true;
    }

    /**
   * Visite de i=0 a i=nbElement.
   */
    public void iterate(final CtuluDoubleVisitor _visitor) {
        for (int i = 0; i < nb_; i++) {
            if (!_visitor.accept(i, v_)) {
                return;
            }
        }
    }

    public boolean remove(final int _i, final CtuluCommandContainer _c) {
        return false;
    }

    public boolean remove(final int[] _i, final CtuluCommandContainer _c) {
        return false;
    }

    public void removeAll(final CtuluCommandContainer _c) {
    }

    public boolean set(final int _i, final double _newV) {
        return true;
    }

    public boolean set(final int _i, final double _newV, final CtuluCommandContainer _c) {
        return true;
    }

    public boolean set(final int[] _i, final double _newV, final CtuluCommandContainer _c) {
        return true;
    }

    public boolean set(final int[] _i, final double[] _newV) {
        return true;
    }

    public boolean set(final int[] _i, final double[] _newV, final CtuluCommandContainer _c) {
        return true;
    }

    public boolean setAll(final double _val, final CtuluCommandContainer _cmd) {
        return false;
    }

    public boolean setObject(final int _i, final double _data, final CtuluCommandContainer _c) {
        return false;
    }

    public boolean setAll(final double[] _val, final CtuluCommandContainer _cmd) {
        return true;
    }

    public boolean setAll(final Object _data, final CtuluCommandContainer _c) {
        new Throwable().printStackTrace();
        return false;
    }

    public boolean setAllObject(final int[] _i, final Object[] _data, final CtuluCommandContainer _c) {
        new Throwable().printStackTrace();
        return false;
    }

    public boolean setObject(final int _i, final Object _data, final CtuluCommandContainer _c) {
        new Throwable().printStackTrace();
        return false;
    }

    public boolean setObject(final int[] _i, final Object _data, final CtuluCommandContainer _c) {
        new Throwable().printStackTrace();
        return false;
    }
}
