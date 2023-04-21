package org.fudaa.fudaa.meshviewer.profile;

import org.fudaa.ctulu.CtuluCommandContainer;
import org.fudaa.ctulu.CtuluListSelectionInterface;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.dodico.ef.operation.EfLineIntersectionsResultsBuilder;
import org.fudaa.dodico.ef.operation.EfLineIntersectionsResultsI;
import org.fudaa.ebli.palette.BPaletteInfo.InfoData;

/**
 * @author fred deniger
 * @version $Id: MvProfileCoordinatesModel.java,v 1.8 2007-06-13 12:58:08 deniger Exp $
 */
public class MvProfileCoordinatesModel implements MvProfileCourbeModelInterface {

    final boolean isX_;

    EfLineIntersectionsResultsI res_;

    String title_;

    double ymax_;

    double ymin_;

    public MvProfileCoordinatesModel(final EfLineIntersectionsResultsI _res, final boolean _isX) {
        super();
        res_ = _res;
        isX_ = _isX;
        title_ = isX_ ? "X" : "Y";
        updateRes();
    }

    public int getActiveTimeIdx() {
        return 0;
    }

    public boolean isActiveTimeEnable() {
        return false;
    }

    private void updateRes() {
        if (res_.getNbIntersect() > 0) {
            double min = isX_ ? res_.getIntersect(0).getX() : res_.getIntersect(0).getY();
            double max = min;
            for (int i = res_.getNbIntersect() - 1; i > 0; i--) {
                final double val = isX_ ? res_.getIntersect(i).getX() : res_.getIntersect(i).getY();
                if (val < min) {
                    min = val;
                }
                if (val > max) {
                    max = val;
                }
            }
            ymin_ = min;
            ymax_ = max;
        } else {
            ymin_ = 0;
            ymax_ = 0;
        }
    }

    public boolean addValue(final double _x, final double _y, final CtuluCommandContainer _cmd) {
        return false;
    }

    public boolean addValue(final double[] _x, final double[] _y, final CtuluCommandContainer _cmd) {
        return false;
    }

    public boolean deplace(final int[] _selectIdx, final double _deltaX, final double _deltaY, final CtuluCommandContainer _cmd) {
        return false;
    }

    public int getNbValues() {
        return res_.getNbIntersect();
    }

    public EfLineIntersectionsResultsI getRes() {
        return res_;
    }

    public String getTitle() {
        return title_;
    }

    public double getX(final int _idx) {
        return res_.getDistFromDeb(_idx);
    }

    public double getXMax() {
        return res_.getDistFromDeb(getNbValues() - 1);
    }

    public double getXMin() {
        return 0;
    }

    public double getY(final int _idx) {
        return isX_ ? res_.getIntersect(_idx).getX() : res_.getIntersect(_idx).getY();
    }

    public double getYMax() {
        return ymax_;
    }

    public double getYMin() {
        return ymin_;
    }

    public boolean isDuplicatable() {
        return false;
    }

    public boolean isModifiable() {
        return true;
    }

    public boolean isPointDrawn(final int _i) {
        return true;
    }

    public boolean isRemovable() {
        return false;
    }

    public boolean isSegmentDrawn(final int _i) {
        return true;
    }

    public boolean isTitleModifiable() {
        return true;
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

    public void reupdateY() {
    }

    public void setRes(final EfLineIntersectionsResultsBuilder _res, ProgressionInterface _prog) {
        res_ = _res.getInitRes();
        updateRes();
    }

    public boolean setTitle(final String _newName) {
        if (_newName != null && _newName != title_) {
            title_ = _newName;
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

    public void fillWithInfo(final InfoData _table, final CtuluListSelectionInterface _selectedPt) {
    }
}
