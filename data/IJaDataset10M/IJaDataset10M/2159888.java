package org.fudaa.ebli.courbe;

import gnu.trove.TDoubleArrayList;
import java.util.Map;
import com.hexidec.ekit.FudaaAlignAction;
import com.memoire.fu.FuEmptyArrays;
import com.memoire.fu.FuLib;
import org.fudaa.ctulu.CtuluCommandContainer;
import org.fudaa.ctulu.CtuluLibArray;
import org.fudaa.ctulu.CtuluListSelectionInterface;
import org.fudaa.ctulu.CtuluRange;
import org.fudaa.ctulu.CtuluUI;
import org.fudaa.ebli.palette.BPaletteInfo.InfoData;

/**
 * @author Fred Deniger
 * @version $Id: EGCourbeModelDefault.java,v 1.17 2007-05-22 14:19:04 deniger Exp $
 */
public class EGCourbeModelDefault implements EGModel {

    public static boolean deplace(final EGModel _model, final int[] _select, final double _deltaX, final double _deltaY, final CtuluCommandContainer _cmd) {
        if (_select == null || _model == null) {
            return false;
        }
        final double[] x = new double[_select.length];
        final double[] y = new double[_select.length];
        for (int i = x.length - 1; i >= 0; i--) {
            x[i] = _model.getX(_select[i]) + _deltaX;
            y[i] = _model.getY(_select[i]) + _deltaY;
        }
        return _model.setValues(_select, x, y, _cmd);
    }

    public static CtuluRange getLocalMinMax(final EGAxe _h, final EGModel _m) {
        CtuluRange r = null;
        for (int i = _m.getNbValues() - 1; i >= 0; i--) {
            final double x = _m.getX(i);
            if (_h.containsPoint(x)) {
                if (r == null) {
                    r = new CtuluRange();
                    r.min_ = _m.getY(i);
                    r.max_ = r.min_;
                } else {
                    final double val = _m.getY(i);
                    if (val > r.max_) {
                        r.max_ = val;
                    } else if (val < r.min_) {
                        r.min_ = val;
                    }
                }
            }
        }
        return r;
    }

    String nom_;

    double[] x_;

    double[] y_;

    double ymax_;

    double ymin_;

    boolean yrangeComute_;

    /**
   * @param _x les x
   * @param _y les y
   */
    public EGCourbeModelDefault(final double[] _x, final double[] _y) {
        super();
        x_ = CtuluLibArray.copy(_x);
        y_ = CtuluLibArray.copy(_y);
    }

    /**
   * ACHTUNG: Constructeur uniquement utilis� pour la persistance des donn�es.
   */
    public EGCourbeModelDefault() {
        y_ = FuEmptyArrays.DOUBLE0;
        x_ = FuEmptyArrays.DOUBLE0;
    }

    private void computeRangeY() {
        if (!yrangeComute_) {
            ymax_ = y_[0];
            ymin_ = ymax_;
            for (int i = y_.length - 1; i > 0; i--) {
                final double d = y_[i];
                if (d > ymax_) {
                    ymax_ = d;
                } else if (d < ymin_) {
                    ymin_ = d;
                }
            }
            yrangeComute_ = true;
        }
    }

    public boolean addValue(final double _x, final double _y, final CtuluCommandContainer _cmd) {
        return false;
    }

    public boolean addValue(final double[] _x, final double[] _y, final CtuluCommandContainer _cmd) {
        if (CtuluLibArray.isEmpty(x_)) {
            x_ = CtuluLibArray.copy(_x);
        } else {
            TDoubleArrayList list = new TDoubleArrayList();
            list.add(x_);
            list.add(_x);
            x_ = list.toNativeArray();
        }
        if (CtuluLibArray.isEmpty(y_)) {
            y_ = CtuluLibArray.copy(_y);
        } else {
            TDoubleArrayList list = new TDoubleArrayList();
            list.add(y_);
            list.add(_y);
            y_ = list.toNativeArray();
        }
        return true;
    }

    public boolean deplace(final int[] _select, final double _deltaX, final double _deltaY, final CtuluCommandContainer _cmd) {
        return false;
    }

    public void fillWithInfo(final InfoData _table, final CtuluListSelectionInterface _selectedPt) {
    }

    public int getActiveTimeIdx() {
        return 0;
    }

    public String getPointLabel(int _i) {
        return null;
    }

    public int getNbValues() {
        return x_.length;
    }

    public final String getTitle() {
        return nom_;
    }

    public double getX(final int _idx) {
        return x_[_idx];
    }

    public double getXMax() {
        return x_[x_.length - 1];
    }

    public double getXMin() {
        return x_[0];
    }

    public double getY(final int _idx) {
        return y_[_idx];
    }

    public double getYMax() {
        computeRangeY();
        return ymax_;
    }

    public double getYMin() {
        computeRangeY();
        return ymin_;
    }

    public boolean isActiveTimeEnable() {
        return false;
    }

    public boolean isDuplicatable() {
        return true;
    }

    public boolean isModifiable() {
        return false;
    }

    public boolean isPointDrawn(final int _i) {
        return true;
    }

    public boolean isRemovable() {
        return true;
    }

    public boolean isSegmentDrawn(final int _i) {
        return true;
    }

    public boolean isTitleModifiable() {
        return true;
    }

    public boolean isVisibleLong() {
        return false;
    }

    public boolean isXModifiable() {
        return false;
    }

    public boolean removeValue(final int _i, final CtuluCommandContainer _cmd) {
        return false;
    }

    public boolean removeValue(final int[] _i, final CtuluCommandContainer _cmd) {
        return false;
    }

    public final boolean setTitle(final String _nom) {
        if (_nom != null && !_nom.equals(nom_)) {
            nom_ = _nom;
            return true;
        }
        return false;
    }

    public boolean setValue(final int _i, final double _x, final double _y, final CtuluCommandContainer _cmd) {
        return false;
    }

    public boolean setValues(final int[] _idx, final double[] _x, final double[] _y, final CtuluCommandContainer _cmd) {
        return false;
    }

    public EGModel duplicate() {
        EGCourbeModelDefault duplic = new EGCourbeModelDefault(this.x_, this.y_);
        if (this.nom_ != null) duplic.nom_ = this.nom_;
        duplic.ymax_ = this.ymax_;
        duplic.ymin_ = this.ymin_;
        duplic.yrangeComute_ = this.yrangeComute_;
        return duplic;
    }

    public void viewGenerationSource(Map infos, CtuluUI impl) {
    }

    public Object savePersistSpecificDatas() {
        return null;
    }

    public void restoreFromSpecificDatas(Object _data, Map _infos) {
    }

    public void replayData(EGGrapheModel model, Map infos, CtuluUI impl) {
    }
}
