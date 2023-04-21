package org.fudaa.fudaa.tr.post;

import com.vividsolutions.jts.geom.Coordinate;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.ctulu.CtuluRange;
import org.fudaa.dodico.ef.EfGridInterface;
import org.fudaa.dodico.h2d.rubar.H2dRubarArete;
import org.fudaa.dodico.h2d.rubar.H2dRubarGrid;
import org.fudaa.dodico.h2d.type.H2dVariableType;
import org.fudaa.ebli.commun.EbliFormatterInterface;
import org.fudaa.ebli.palette.BPaletteInfo.InfoData;
import org.fudaa.fudaa.meshviewer.MvResource;
import org.fudaa.fudaa.tr.common.TrResource;
import org.fudaa.fudaa.tr.data.TrInfoSenderDelegate;

/**
 * @author Fred Deniger
 * @version $Id: TrPostInfoDelegate.java,v 1.21 2007-05-04 14:01:52 deniger Exp $
 */
public class TrPostInfoDelegate extends TrInfoSenderDelegate {

    TrPostSource src_;

    public TrPostInfoDelegate(final TrPostSource _src, final EbliFormatterInterface _fmt) {
        super(_fmt);
        src_ = _src;
    }

    @Override
    public EfGridInterface getGrid() {
        return src_.getGrid();
    }

    /**
   * @param _ptIdx
   * @param _idxVar
   * @param _timeStep
   * @param _d
   * @param _value
   */
    public void fillInfoWithVar(final int _ptIdx, final H2dVariableType _idxVar, final int _timeStep, final InfoData _d, final double _value) {
        if (_idxVar != null) {
            final H2dVariableType t = _idxVar;
            if (_timeStep >= 0) {
                _d.put(TrResource.getS("Pas de temps"), Double.toString(src_.getTimeStep(_timeStep)));
                if (_ptIdx >= 0) {
                    _d.put(t.getName(), Double.toString(_value));
                } else {
                    _d.put(TrResource.getS("Variable"), t.getName());
                }
            }
            CtuluRange r = new CtuluRange();
            if (_timeStep >= 0 && src_.isMinMaxCompute(t, _timeStep)) {
                r = src_.getExtremaForTimeStep(r, t, _timeStep, null, false);
                _d.put(TrResource.getS("Min sur le pas de temps"), Double.toString(r.min_));
                _d.put(TrResource.getS("Max sur le pas de temps"), Double.toString(r.max_));
            }
            if (src_.isMinMaxCompute(t)) {
                r = src_.getExtrema(r, t, null, null, false);
                _d.put(TrResource.getS("Min global"), Double.toString(r.min_));
                _d.put(TrResource.getS("Max global"), Double.toString(r.max_));
            }
        }
    }

    public void fillWithAreteInfo(final int _idx, final InfoData _m) {
        final H2dRubarArete a = ((H2dRubarGrid) src_.getGrid()).getRubarArete(_idx);
        final String nd = TrResource.getS("Noeud") + CtuluLibString.ESPACE;
        _m.setTitle(TrResource.getS("Ar�te") + CtuluLibString.ESPACE + CtuluLibString.getString(_idx + 1));
        final Coordinate c = getGrid().getCoor(a.getPt1Idx());
        final String sep = "; ";
        _m.put(nd + "1 (" + CtuluLibString.getString(a.getPt1Idx() + 1) + ')', CtuluLib.DEFAULT_NUMBER_FORMAT.format(c.x) + sep + CtuluLib.DEFAULT_NUMBER_FORMAT.format(c.y) + sep + CtuluLib.DEFAULT_NUMBER_FORMAT.format(c.z));
        final Coordinate c2 = getGrid().getCoor(a.getPt2Idx());
        _m.put(nd + "2 (" + CtuluLibString.getString(a.getPt2Idx() + 1) + ')', CtuluLib.DEFAULT_NUMBER_FORMAT.format(c2.x) + sep + CtuluLib.DEFAULT_NUMBER_FORMAT.format(c2.y) + sep + CtuluLib.DEFAULT_NUMBER_FORMAT.format(c2.z));
        _m.put(MvResource.getS("Milieu"), CtuluLib.DEFAULT_NUMBER_FORMAT.format((c.x + c2.x) / 2) + sep + CtuluLib.DEFAULT_NUMBER_FORMAT.format((c.y + c2.y) / 2) + sep + CtuluLib.DEFAULT_NUMBER_FORMAT.format((c.z + c2.z) / 2));
        if (a.isExtern()) {
            _m.put(TrResource.getS("Type"), a.getType().toString());
        }
        final TrPostRubarEdgesResults r = ((TrPostSourceRubar) src_).getEdgesRes();
        if (r != null && r.isLocalEdgeSet(new int[] { _idx })) {
            _m.put(TrResource.getS("R�sultats"), TrResource.getS("Oui"));
        }
    }
}
