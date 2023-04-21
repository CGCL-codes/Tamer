package org.fudaa.fudaa.meshviewer.layer;

import java.awt.Graphics;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.ctulu.gis.GISLib;
import org.fudaa.dodico.ef.EfFrontierInterface;
import org.fudaa.dodico.ef.EfGridInterface;
import org.fudaa.ebli.calque.BGroupeCalque;
import org.fudaa.ebli.calque.ZModeleDonnees;
import org.fudaa.ebli.geometrie.GrBoite;
import org.fudaa.ebli.geometrie.GrPoint;
import org.fudaa.ebli.geometrie.GrPolygone;
import org.fudaa.ebli.palette.BPaletteInfo.InfoData;
import org.fudaa.fudaa.commun.FudaaLib;
import org.fudaa.fudaa.meshviewer.MvResource;
import org.fudaa.fudaa.meshviewer.model.MvElementModel;
import org.fudaa.fudaa.meshviewer.model.MvElementModelDefault;
import org.fudaa.fudaa.meshviewer.model.MvInfoDelegate;
import org.fudaa.fudaa.meshviewer.model.MvNodeModel;
import org.fudaa.fudaa.meshviewer.model.MvNodeModelDefault;

/**
 * @author deniger
 * @version $Id: MvGridLayerGroup.java,v 1.28 2007-03-30 15:38:10 deniger Exp $
 */
public class MvGridLayerGroup extends BGroupeCalque {

    protected MvElementLayer cqPoly_;

    protected MvNodeLayer cqPt_;

    protected EfGridInterface mail_;

    public MvGridLayerGroup(final EfGridInterface _maill) {
        this(_maill, null);
    }

    public MvGridLayerGroup(final EfGridInterface _maill, final MvInfoDelegate _d) {
        this(_maill, new MvNodeModelDefault(_maill, _d), new MvElementModelDefault(_maill, _d));
    }

    public MvGridLayerGroup(final EfGridInterface _maill, final MvNodeLayer _ptModel, final MvElementLayer _eltModel) {
        setTitle(MvResource.getS("Maillage"));
        setName("cqMaillage");
        setDestructible(false);
        mail_ = _maill;
        cqPt_ = _ptModel;
        cqPt_.setName("cqPts");
        cqPt_.setTitle(MvResource.getS("Noeuds"));
        cqPt_.setDestructible(false);
        add(cqPt_);
        cqPoly_ = _eltModel;
        cqPoly_.setDestructible(false);
        cqPoly_.setName("cqPolygon");
        cqPoly_.setTitle(MvResource.getS("El�ments"));
        add(cqPoly_);
    }

    public MvGridLayerGroup(final EfGridInterface _maill, final MvNodeModel _ptModel, final MvElementModel _eltModel) {
        this(_maill, new MvNodeLayer(_ptModel), new MvElementLayer(_eltModel));
    }

    protected void determinePaintState() {
        if ((!cqPoly_.isVisible()) || (cqPoly_.getPaletteCouleur() != null) || ((cqPt_.getIconModel(0) != null) && ((cqPt_.getIconModel(0).getTaille() > 1) || (cqPt_.getPaletteCouleur() != null) || (cqPoly_.getForeground() != cqPt_.getForeground())))) {
            cqPt_.setPainted(true);
        } else {
            cqPt_.setPainted(false);
        }
    }

    public final void clearSelection() {
        cqPoly_.clearSelection();
        cqPt_.clearSelection();
    }

    @Override
    public void fillWithInfo(final InfoData _m) {
        _m.setTitle(MvResource.getS("Maillage"));
        _m.put(MvResource.getS("Nombre de noeuds"), CtuluLibString.getString(mail_.getPtsNb()));
        _m.put(MvResource.getS("Nombre d'�l�ments"), CtuluLibString.getString(mail_.getEltNb()));
        final EfFrontierInterface fr = mail_.getFrontiers();
        _m.put("X min", CtuluLib.DEFAULT_NUMBER_FORMAT.format(getGrid().getMinX()));
        _m.put("X max", CtuluLib.DEFAULT_NUMBER_FORMAT.format(getGrid().getMaxX()));
        _m.put("Y min", CtuluLib.DEFAULT_NUMBER_FORMAT.format(getGrid().getMinY()));
        _m.put("Y max", CtuluLib.DEFAULT_NUMBER_FORMAT.format(getGrid().getMaxY()));
        if (fr != null) {
            _m.put(FudaaLib.getS("Nombre de fronti�res"), CtuluLibString.getString(fr.getNbFrontier()));
            _m.put(FudaaLib.getS("Nombre de fronti�res externes"), CtuluLibString.getString(fr.getNbFrontierExt()));
            _m.put(MvResource.getS("Nombre de noeuds sur les fronti�res"), CtuluLibString.getString(fr.getNbTotalPt()));
            _m.put(MvResource.getS("Emprise"), CtuluLib.DEFAULT_NUMBER_FORMAT.format(GISLib.getAire(getGrid().getEnvelope(null))));
        }
    }

    @Override
    public GrBoite getDomaine() {
        return MvGridLayerGroup.getDomaine(mail_);
    }

    public final EfGridInterface getGrid() {
        return mail_;
    }

    public int getNbPointsForElt(final int _indexElt) {
        return mail_.getElement(_indexElt).getPtNb();
    }

    public MvNodeLayer getPointLayer() {
        return cqPt_;
    }

    public MvElementLayer getPolygonLayer() {
        return cqPoly_;
    }

    public ZModeleDonnees modeleDonnees() {
        return cqPt_.modeleDonnees();
    }

    @Override
    public final void paint(final Graphics _g) {
        determinePaintState();
        super.paint(_g);
    }

    /**
   * @param _p le point a initialiser
   * @param _i l'inidice
   * @return true si ok.
   */
    public boolean point(final GrPoint _p, final int _i) {
        _p.setCoordonnees(mail_.getPtX(_i), mail_.getPtY(_i), mail_.getPtZ(_i));
        return true;
    }

    public boolean polygone(final GrPolygone _poly, final int _i) {
        MvElementModelDefault.initGrPolygone(this.mail_, mail_.getElement(_i), _poly);
        return true;
    }

    private boolean polyOldValue_ = true;

    @Override
    public void setRapide(final boolean _v) {
        if (rapide_ != _v) {
            final boolean vp = rapide_;
            rapide_ = _v;
            cqPt_.setRapide(_v);
            if (_v) {
                polyOldValue_ = cqPoly_.isVisible();
                cqPoly_.setVisible(false);
            } else {
                cqPoly_.setVisible(polyOldValue_);
            }
            firePropertyChange("ajustement", vp, rapide_);
        }
    }

    public static GrBoite getDomaine(final EfGridInterface _g) {
        final GrBoite b = new GrBoite();
        b.o_ = new GrPoint(_g.getMinX(), _g.getMinY(), 0);
        b.e_ = new GrPoint(_g.getMaxX(), _g.getMaxY(), 0);
        return b;
    }
}
