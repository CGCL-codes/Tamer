package org.fudaa.ctulu.interpolation.bilinear;

import gnu.trove.TIntArrayList;
import java.util.Arrays;
import org.fudaa.ctulu.CtuluAnalyze;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.ctulu.CtuluPermanentList;
import org.fudaa.ctulu.CtuluVariable;
import org.fudaa.ctulu.ProgressionUpdater;
import org.fudaa.ctulu.gis.CtuluLibGeometrie;
import org.fudaa.ctulu.interpolation.InterpolationParameters;
import org.fudaa.ctulu.interpolation.InterpolationResultsHolderI;
import org.fudaa.ctulu.interpolation.SupportLocationI;
import org.fudaa.ctulu.interpolation.InterpolationSupportValuesI;
import org.fudaa.ctulu.interpolation.InterpolationTarget;
import org.fudaa.ctulu.interpolation.Interpolator;

/**
 * @author deniger
 * @version $Id: InterpolatorBilinear.java,v 1.2 2007-06-11 13:03:48 deniger Exp $
 */
public final class InterpolatorBilinear extends Interpolator {

    double distance_;

    InterpolationBilinearSupportSorted ref_;

    final SupportLocationI init_;

    boolean stop_;

    /**
   * @param _target
   * @param _ref
   * @param _results
   */
    public InterpolatorBilinear(final SupportLocationI _ref) {
        super();
        init_ = _ref;
        distance_ = -1;
    }

    /**
   * @return la distance minimal par rapport � la frontiere
   */
    public double getDistance() {
        return distance_;
    }

    protected boolean doInterpolate(InterpolationParameters _params) {
        if (_params == null || _params.getTarget() == null || (init_ == null)) {
            return false;
        }
        _params.createDefaultResults();
        if (ref_ == null) {
            ref_ = InterpolationBilinearSupportSorted.buildSortedSrc(init_, _params.getProg());
        }
        final InterpolationTarget target = _params.getTarget();
        final InterpolationResultsHolderI results = _params.getResults();
        final InterpolationSupportValuesI srcValues = _params.getValues();
        final int nb = target.getPtsNb();
        final ProgressionUpdater prog = new ProgressionUpdater(_params.getProg());
        prog.setValue(3, nb, 0, 90);
        final int[] quad = new int[4];
        boolean made;
        int sameIdx;
        TIntArrayList pointNotSet = null;
        double x, y, xref, yref;
        CtuluPermanentList variable = _params.getVariable();
        final int nbValues = variable.size();
        final double[] numerateur = new double[nbValues];
        final double[] denominateur = new double[nbValues];
        final double[] values = new double[nbValues];
        for (int i = nb - 1; i >= 0; i--) {
            if (stop_) {
                return false;
            }
            x = target.getPtX(i);
            y = target.getPtY(i);
            ref_.getQuadrantIdx(x, y, quad);
            made = false;
            sameIdx = -1;
            Arrays.fill(numerateur, 0);
            Arrays.fill(denominateur, 0);
            for (int j = 3; j >= 0; j--) {
                final int temp = quad[j];
                if (temp >= 0) {
                    xref = init_.getPtX(temp);
                    yref = init_.getPtY(temp);
                    if ((distance_ < 0) || (target.isInBuffer(xref, yref, distance_))) {
                        made = true;
                        double dist = CtuluLibGeometrie.getD2(xref, yref, x, y);
                        if (CtuluLib.isZero(dist, _params.getEps())) {
                            sameIdx = temp;
                            break;
                        }
                        for (int k = nbValues - 1; k >= 0; k--) {
                            numerateur[k] += srcValues.getV((CtuluVariable) variable.get(k), temp) / dist;
                            denominateur[k] += 1d / dist;
                        }
                    }
                }
            }
            if (sameIdx >= 0) {
                for (int k = nbValues - 1; k >= 0; k--) {
                    values[k] = srcValues.getV((CtuluVariable) variable.get(k), sameIdx);
                }
                results.setResult(i, values);
            } else if (made) {
                for (int k = nbValues - 1; k >= 0; k--) {
                    values[k] = numerateur[k] / denominateur[k];
                }
                results.setResult(i, values);
            } else {
                if (pointNotSet == null) {
                    pointNotSet = new TIntArrayList();
                }
                pointNotSet.add(i);
            }
            prog.majAvancement();
        }
        if (pointNotSet != null) {
            manageBadPoints(target, _params.getAnalyze(), nb, pointNotSet);
            if (_params.getAnalyze().containsFatalError()) {
                return false;
            }
            extrapolateValues(target, results, pointNotSet);
        }
        return true;
    }

    private void extrapolateValues(final InterpolationTarget _target, InterpolationResultsHolderI _res, final TIntArrayList _pointNotSet) {
        int nbPt = _target.getPtsNb();
        for (int i = _pointNotSet.size() - 1; i >= 0; i--) {
            double dmin = Double.MAX_VALUE;
            final int temp = _pointNotSet.getQuick(i);
            final double x = _target.getPtX(temp);
            final double y = _target.getPtY(temp);
            int nearest = -1;
            for (int j = nbPt - 1; j >= 0; j--) {
                if (_res.isDone(j)) {
                    final double d = CtuluLibGeometrie.getD2(x, y, _target.getPtX(j), _target.getPtY(j));
                    if (d < dmin) {
                        dmin = d;
                        nearest = j;
                    }
                }
            }
            if (nearest >= 0) {
                _res.setResult(temp, nearest);
            }
        }
    }

    private void manageBadPoints(final InterpolationTarget _target, final CtuluAnalyze _analyze, final int _nbToInterpolate, final TIntArrayList _pointNotSet) {
        _pointNotSet.sort();
        final int nbBad = _pointNotSet.size();
        if (nbBad == _nbToInterpolate) {
            _analyze.addFatalError(CtuluLib.getS("Aucun point n'a pu �tre interpol�"), 0);
        } else if (nbBad > 15) {
            _analyze.addWarn(CtuluLib.getS("{0} points n'ont pas �t� interpol�s correctement", CtuluLibString.getString(nbBad)), 0);
        } else {
            final StringBuffer b = new StringBuffer();
            for (int i = 0; i < nbBad; i++) {
                final int temp = _pointNotSet.getQuick(i);
                b.append(CtuluLibString.getString(temp)).append(CtuluLibString.ESPACE).append("(").append(_target.getPtX(temp)).append(";").append(_target.getPtY(temp)).append(")");
            }
            _analyze.addWarn(CtuluLib.getS("Points non interpol�s correctement: {0}", b.toString()), 0);
        }
    }

    public void setDistance(final double _distance) {
        distance_ = _distance;
    }

    public void stop() {
        stop_ = true;
    }
}
