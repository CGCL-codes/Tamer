package org.fudaa.dodico.h2d.telemac;

import gnu.trove.TIntHashSet;
import gnu.trove.TObjectIntHashMap;
import org.fudaa.ctulu.CtuluCommandContainer;
import org.fudaa.ctulu.CtuluRange;
import org.fudaa.ctulu.collection.CtuluArrayDouble;
import org.fudaa.ctulu.collection.CtuluCollectionDouble;
import org.fudaa.ctulu.collection.CtuluDoubleVisitor;

/**
 * Definition d'un siphon pour telemac.
 * 
 * @author Fred Deniger
 * @version $Id: H2dtelemacSiphon.java,v 1.14 2007-05-04 13:46:37 deniger Exp $
 */
public class H2dtelemacSiphon implements H2dTelemacSiphonInterface {

    private H2dTelemacSource b1_;

    private H2dTelemacSource b2_;

    private int i1_;

    private int i2_;

    private final CtuluArrayDouble values_;

    protected H2dtelemacSiphon(final int _i1, final int _i2, final H2dTelemacSource _b1, final H2dTelemacSource _b2, final CtuluCollectionDouble _l) {
        values_ = new CtuluArrayDouble(_l);
        b1_ = _b1;
        b2_ = _b2;
        i1_ = _i1;
        i2_ = _i2;
    }

    protected H2dtelemacSiphon(final int _i1, final int _i2, final H2dTelemacSource _b1, final H2dTelemacSource _b2, final double[] _values) {
        values_ = new CtuluArrayDouble(_values.length);
        for (int i = _values.length - 1; i >= 0; i--) {
            if (_values[i] >= 0) {
                values_.set(i, _values[i]);
            }
        }
        b1_ = _b1;
        b2_ = _b2;
        i1_ = _i1;
        i2_ = _i2;
    }

    /**
   * @param _idxValue les indices des valeurs a recuperer
   * @param _dest le tableau de meme taille que _idxValue qui recevra les valeurs demandees.
   */
    protected void getValues(final int[] _idxValue, final double[] _dest) {
        for (int i = _idxValue.length - 1; i >= 0; i--) {
            _dest[i] = getValue(_idxValue[i]);
        }
    }

    /**
   * @param _idxValue les indices des valeurs a recuperer
   * @param _dest le tableau de meme taille que _idxValue qui contient les valeurs a tester
   */
    protected void keepCommonValues(final int[] _idxValue, final double[] _dest) {
        for (int i = _idxValue.length - 1; i >= 0; i--) {
            if (_dest[i] != getValue(_idxValue[i])) {
                _dest[i] = -1;
            }
        }
    }

    protected final void setB1(final H2dTelemacSource _b1) {
        b1_ = _b1;
    }

    protected final void setB2(final H2dTelemacSource _b2) {
        b2_ = _b2;
    }

    protected void setValues(final int[] _idx, final double[] _val, final CtuluCommandContainer _cmd) {
        values_.set(_idx, _val, _cmd);
    }

    /**
   * @param _srcIdx le tableau H2DTelemacSource -> Indice
   */
    protected void updateNumerotation(final TObjectIntHashMap _srcIdx) {
        int newI1 = _srcIdx.get(b1_);
        if (newI1 != i1_) {
            i1_ = newI1;
        }
        newI1 = _srcIdx.get(b2_);
        if (newI1 != i2_) {
            i2_ = newI1;
        }
    }

    /**
   * @param _toFill la liste a remplire avec les indices utilises
   */
    public void fillWithUsedSrc(final TIntHashSet _toFill) {
        _toFill.add(i1_);
        _toFill.add(i2_);
    }

    /**
   * @return la buse 1
   */
    public final H2dTelemacSource getB1() {
        return b1_;
    }

    /**
   * @return la buse 2
   */
    public final H2dTelemacSource getB2() {
        return b2_;
    }

    public Double getCommonValue(final int[] _idx) {
        return values_.getCommonValue(_idx);
    }

    /**
   * @return l'indice de la source 1
   */
    public final int getI1() {
        return i1_;
    }

    /**
   * @return l'indice de la source 2
   */
    public final int getI2() {
        return i2_;
    }

    public double getMax() {
        return values_.getMax();
    }

    public void expandTo(final CtuluRange _rangeToExpand) {
        values_.expandTo(_rangeToExpand);
    }

    public void getRange(final CtuluRange _r) {
        values_.getRange(_r);
    }

    public double getMin() {
        return values_.getMin();
    }

    public int getSize() {
        return values_.getSize();
    }

    public int getSource1() {
        return i1_;
    }

    public int getSource2() {
        return i2_;
    }

    /**
   * @param _i l'indice de la variable demande [0,getNbVariable[
   * @return la valeur
   */
    public double getValue(final int _i) {
        return values_.getValue(_i);
    }

    public Object getObjectValueAt(final int _i) {
        return values_.getObjectValueAt(_i);
    }

    public double[] getValues() {
        return values_.getValues();
    }

    public void iterate(final CtuluDoubleVisitor _visitor) {
        values_.iterate(_visitor);
    }

    /**
   * @param _srcIdx les indices des sources a tester
   * @return true une des sources est utilisee
   */
    public boolean useOneSource(final int[] _srcIdx) {
        if (_srcIdx != null) {
            for (int i = _srcIdx.length - 1; i >= 0; i--) {
                if (useSource(_srcIdx[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
   * @param _srcIdx les indices des sources a tester
   * @return O si aucune extremite utilise. 1 si seule l'extremite un est utilise. 2 si uniquement la deuxieme. 3 si les
   *         deux.
   */
    public int useOneSourceAndWhichEnd(final int[] _srcIdx) {
        if (_srcIdx != null) {
            boolean i1found = false;
            boolean i2found = false;
            for (int i = _srcIdx.length - 1; i >= 0; i--) {
                final int idx = _srcIdx[i];
                if (idx == i1_) {
                    i1found = true;
                } else if (idx == i2_) {
                    i2found = true;
                }
            }
            if (i1found) {
                if (i2found) {
                    return 3;
                }
                return 1;
            } else if (i2found) {
                return 2;
            }
        }
        return 0;
    }

    /**
   * @param _srcIdx l'indice de la source
   * @return true si la source est utilisee
   */
    public boolean useSource(final int _srcIdx) {
        return i1_ == _srcIdx || i2_ == _srcIdx;
    }
}
