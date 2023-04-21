package org.fudaa.fudaa.meshviewer.layer;

import com.memoire.bu.BuTable;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.CtuluLibArray;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.ctulu.CtuluNumberFormatI;
import org.fudaa.ctulu.gui.CtuluTable;
import org.fudaa.dodico.ef.EfGridVolumeInterface;
import org.fudaa.dodico.ef.EfSegment;
import org.fudaa.ebli.calque.ZCalqueAffichageDonneesInterface;
import org.fudaa.ebli.calque.ZCalqueSegment;
import org.fudaa.ebli.calque.ZModeleSegment;
import org.fudaa.ebli.geometrie.GrBoite;
import org.fudaa.ebli.geometrie.GrPoint;
import org.fudaa.ebli.geometrie.GrSegment;
import org.fudaa.ebli.palette.BPaletteInfo.InfoData;
import org.fudaa.fudaa.meshviewer.MvResource;

/**
 * @author Fred Deniger
 * @version $Id: MvEdgeModelDefault.java,v 1.11 2006-10-02 15:13:22 deniger Exp $
 */
public class MvEdgeModelDefault implements ZModeleSegment {

    EfGridVolumeInterface g_;

    /**
   * @param _g le maillage
   */
    public MvEdgeModelDefault(final EfGridVolumeInterface _g) {
        super();
        g_ = _g;
    }

    public final double getNorme(final int _i) {
        return getArete(_i).getNorme(g_);
    }

    public final double getVx(final int _i) {
        return getArete(_i).getVx(g_);
    }

    public double getZ1(final int _i) {
        return g_.getPtZ(getArete(_i).getPt1Idx());
    }

    public double getZ2(final int _i) {
        return g_.getPtZ(getArete(_i).getPt2Idx());
    }

    public final double getVy(final int _i) {
        return getArete(_i).getVy(g_);
    }

    public final double getX(final int _i) {
        return g_.getPtX(getArete(_i).getPt1Idx());
    }

    public final double getY(final int _i) {
        return g_.getPtY(getArete(_i).getPt1Idx());
    }

    public BuTable createValuesTable(final ZCalqueAffichageDonneesInterface _layer) {
        final BuTable b = new CtuluTable();
        b.setModel(new ZCalqueSegment.SegmentValueTableModel(this));
        return b;
    }

    public CtuluNumberFormatI getXYFormat() {
        return CtuluLib.CTULU_THREE_DIGITS_FORMAT;
    }

    public void fillWithInfo(final InfoData _m, final ZCalqueAffichageDonneesInterface _layer) {
        fillWithInfo(_m, _layer, false);
    }

    public void fillWithInfo(final InfoData _m, final ZCalqueAffichageDonneesInterface _layer, final boolean _addZ) {
        final int nbSelected = CtuluLibArray.getSelectedIdxNb(_layer.getLayerSelection());
        final int selected = nbSelected == 1 ? _layer.getLayerSelection().getMaxIndex() : -1;
        final EfSegment a = selected < 0 ? null : getArete(selected);
        final ZCalqueSegment.InfoString info = new ZCalqueSegment.InfoString();
        info.nbSegment_ = MvResource.getS("Nombre d'ar�tes");
        info.nbSegmentSelectionnees_ = MvResource.getS("Nombre d'ar�tes s�lectionn�es");
        info.fmt_ = getXYFormat();
        info.addZinfo_ = _addZ;
        if (selected >= 0) {
            info.pt1_ = MvResource.getS("Noeud") + CtuluLibString.ESPACE + CtuluLibString.UN + " (" + CtuluLibString.getString(a.getPt1Idx() + 1) + " )";
            info.pt2_ = MvResource.getS("Noeud") + CtuluLibString.ESPACE + CtuluLibString.DEUX + " (" + CtuluLibString.getString(a.getPt2Idx() + 1) + " )";
            info.titreIfOne_ = MvResource.getS("Ar�te n�");
        } else {
            info.titre_ = MvResource.getS("Ar�tes");
        }
        ZCalqueSegment.fillWithInfo(_m, nbSelected, selected, this, info);
        if (selected < 0) {
            return;
        }
        final int realIdx = getGlobalIdx(selected);
        if (realIdx != selected) {
            _m.setTitle(info.titreIfOne_ + CtuluLibString.ESPACE + CtuluLibString.getString(realIdx + 1));
        }
    }

    public int[] getGlobalIdx(final int[] _selectedIdx) {
        return _selectedIdx;
    }

    public int getGlobalIdx(final int _selectedIdx) {
        return _selectedIdx;
    }

    public boolean isValuesTableAvailable() {
        return true;
    }

    public EfSegment getArete(final int _idx) {
        return g_.getArete(_idx);
    }

    public final boolean segment(final GrSegment _s, final int _i, final boolean _force) {
        final EfSegment a = getArete(_i);
        if (a.getPt1Idx() < 0 && a.getPt2Idx() < 0) {
            return false;
        }
        if (_s.e_ == null) {
            _s.e_ = new GrPoint();
        }
        if (_s.o_ == null) {
            _s.o_ = new GrPoint();
        }
        int idx = a.getPt1Idx();
        if (idx < 0) {
            idx = a.getPt2Idx();
        }
        _s.o_.setCoordonnees(g_.getPtX(idx), g_.getPtY(idx), g_.getPtZ(idx));
        idx = a.getPt2Idx();
        if (idx < 0) {
            idx = a.getPt1Idx();
        }
        _s.e_.setCoordonnees(g_.getPtX(idx), g_.getPtY(idx), g_.getPtZ(idx));
        return true;
    }

    public GrBoite getDomaine() {
        final GrBoite b = new GrBoite();
        b.e_ = new GrPoint(g_.getMaxX(), g_.getMaxY(), 0);
        b.o_ = new GrPoint(g_.getMinX(), g_.getMinY(), 0);
        return b;
    }

    public int getNombre() {
        return g_.getNbAretes();
    }

    public final Object getObject(final int _ind) {
        return null;
    }

    protected EfGridVolumeInterface getG() {
        return g_;
    }
}
