package org.fudaa.fudaa.meshviewer.layer;

import gnu.trove.TIntHashSet;
import gnu.trove.TIntObjectIterator;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;
import org.fudaa.ctulu.CtuluCommandContainer;
import org.fudaa.ctulu.CtuluListSelection;
import org.fudaa.ctulu.CtuluListSelectionInterface;
import org.fudaa.ctulu.CtuluUI;
import org.fudaa.ctulu.collection.CtuluCollectionDouble;
import org.fudaa.ctulu.collection.CtuluCollectionInteger;
import org.fudaa.ctulu.gis.GISGeometryFactory;
import org.fudaa.ebli.calque.BCalquePersistenceInterface;
import org.fudaa.ebli.calque.ZCalqueAffichageDonneesMultiSpecial;
import org.fudaa.ebli.calque.ZModeleDonnees;
import org.fudaa.ebli.calque.ZSelectionTrace;
import org.fudaa.ebli.commun.EbliActionInterface;
import org.fudaa.ebli.commun.EbliActionSimple;
import org.fudaa.ebli.commun.EbliListeSelectionMulti;
import org.fudaa.ebli.commun.EbliListeSelectionMultiInterface;
import org.fudaa.ebli.find.EbliFindExpressionContainerInterface;
import org.fudaa.ebli.geometrie.GrBoite;
import org.fudaa.ebli.geometrie.GrMorphisme;
import org.fudaa.ebli.geometrie.GrPoint;
import org.fudaa.ebli.trace.TraceIcon;
import org.fudaa.ebli.trace.TraceIconModel;
import org.fudaa.ebli.trace.TraceLigne;
import org.fudaa.fudaa.meshviewer.MvLayerGrid;
import org.fudaa.fudaa.meshviewer.MvResource;
import org.fudaa.fudaa.meshviewer.model.Mv3DFrontierData;
import org.fudaa.fudaa.meshviewer.model.MvExpressionSupplierFrNode;
import org.fudaa.fudaa.meshviewer.model.MvFrontierModel;
import org.fudaa.fudaa.meshviewer.persistence.MvFrontiere3DPersistence;
import org.fudaa.fudaa.sig.FSigLib;
import com.vividsolutions.jts.algorithm.SIRtreePointInRing;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;

/**
 * @author deniger
 * @version $Id: MvFrontierLayerAbstract.java,v 1.24.6.1 2008-02-20 10:11:50 bmarchan Exp $
 */
public abstract class MvFrontierLayerAbstract extends ZCalqueAffichageDonneesMultiSpecial implements MvLayerGrid, MvFrontierLayer {

    public static class EditAction extends EbliActionSimple {

        final MvFrontierLayer layer_;

        final CtuluCommandContainer cmd_;

        final CtuluUI ui_;

        public EditAction(final MvFrontierLayer _layer, final CtuluCommandContainer _cmd, final CtuluUI _ui) {
            super(MvResource.getS("3D: Editer les hauteurs des b�timents"), null, "EDIT_3D_ELEVATION");
            layer_ = _layer;
            setDefaultToolTip(MvResource.getS("Permet d'editer la hauteur des b�timents pour la vue 3D"));
            super.putValue(EbliActionInterface.UNABLE_TOOLTIP, FSigLib.getS("S�lectionner au moins un noeud dans une fronti�re interne"));
            ui_ = _ui;
            cmd_ = _cmd;
        }

        @Override
        public void updateStateBeforeShow() {
            if (layer_.isSelectionEmpty()) {
                setEnabled(false);
            } else {
                final int[] idx = layer_.getSelectedFrontier();
                for (int i = idx.length - 1; i >= 0; i--) {
                    if (layer_.isInternFr(idx[i])) {
                        setEnabled(true);
                        return;
                    }
                }
                setEnabled(false);
            }
        }

        @Override
        public void actionPerformed(final ActionEvent _e) {
            if (layer_.isSelectionEmpty()) {
                return;
            }
            layer_.createData().edit(new CtuluListSelection(layer_.getSelectedFrontier()), cmd_, ui_);
        }
    }

    Mv3DFrontierData data3D_;

    boolean isT6_;

    final GrPoint p_ = new GrPoint();

    protected MvFrontierModel m_;

    public MvFrontierLayerAbstract(final MvFrontierModel _cl) {
        setTitle(MvResource.getS("Noeuds de bords"));
        m_ = _cl;
        isT6_ = m_.isT6();
        if (isT6_) {
            iconModel_ = new TraceIconModel(TraceIcon.CROIX, 1, Color.GREEN);
        }
    }

    protected void buildActions() {
    }

    public LineString getSelectedLine() {
        if (isSelectionEmpty()) {
            return null;
        }
        final EbliListeSelectionMultiInterface select = getLayerSelectionMulti();
        if (select.getNbListSelected() == 1) {
            final int i = select.getIdxSelected()[0];
            final CtuluListSelectionInterface selection = select.getSelection(i);
            final int nbSelected = selection.getNbSelectedIndex();
            if (nbSelected == 2) {
                return createLineFromSelection(i, selection, nbSelected);
            } else if (nbSelected >= 2) {
                final int nbPointForLigne = m_.getNbPointInFrontier(i);
                final int[] res = CtuluListSelection.isSelectionContiguous(select.getSelection(i), nbPointForLigne);
                if (res == null) {
                    return null;
                }
                if (res[0] < res[1]) {
                    return createLineFromSelection(i, selection, nbSelected);
                }
                final Coordinate[] cs = new Coordinate[nbSelected];
                int csi = 0;
                final GrPoint pt = new GrPoint();
                for (int j = res[0]; j < nbPointForLigne; j++) {
                    m_.point(pt, i, j);
                    cs[csi++] = new Coordinate(pt.x_, pt.y_);
                }
                for (int j = 0; j <= res[1]; j++) {
                    m_.point(pt, i, j);
                    cs[csi++] = new Coordinate(pt.x_, pt.y_);
                }
                return GISGeometryFactory.INSTANCE.createLineString(cs);
            }
        } else if (select.getNbListSelected() == 2) {
            final int[] idx = select.getIdxSelected();
            final CtuluListSelectionInterface selection1 = select.getSelection(idx[0]);
            final CtuluListSelectionInterface selection2 = select.getSelection(idx[1]);
            if (selection1.getNbSelectedIndex() == 1 && selection2.getNbSelectedIndex() == 1) {
                final Coordinate[] cs = new Coordinate[2];
                final GrPoint pt = new GrPoint();
                m_.point(pt, idx[0], selection1.getMaxIndex());
                cs[0] = new Coordinate(pt.x_, pt.y_);
                m_.point(pt, idx[1], selection2.getMaxIndex());
                cs[1] = new Coordinate(pt.x_, pt.y_);
                return GISGeometryFactory.INSTANCE.createLineString(cs);
            }
        }
        return null;
    }

    private LineString createLineFromSelection(final int _i, final CtuluListSelectionInterface _selection, final int _nbSelected) {
        final GrPoint pt = new GrPoint();
        final Coordinate[] cs = new Coordinate[_nbSelected];
        final int[] idx = _selection.getSelectedIndex();
        for (int j = 0; j < _nbSelected; j++) {
            m_.point(pt, _i, idx[j]);
            cs[j] = new Coordinate(pt.x_, pt.y_);
        }
        return GISGeometryFactory.INSTANCE.createLineString(cs);
    }

    @Override
    protected final void select(final int _i, final int _idx1, final int _idx2) {
        if (m_.isExternFrontier(_i)) {
            super.select(_i, _idx1, _idx2);
        } else {
            super.select(_i, _idx2, _idx1);
        }
    }

    public boolean changeSelectionFromFrIdx(final CtuluListSelection _frIdx, final int _selOption) {
        if (_frIdx == null || _frIdx.isEmpty()) {
            return false;
        }
        final int[] frIdx = new int[2];
        final EbliListeSelectionMulti newOne = createSelection();
        final int maxIdx = _frIdx.getMaxIndex();
        for (int i = _frIdx.getMinIndex(); i <= maxIdx; i++) {
            if (_frIdx.isSelected(i) && m_.getIdxFrIdxOnFrFromFrontier(i, frIdx)) {
                newOne.add(frIdx[0], frIdx[1]);
            }
        }
        return super.changeSelection(newOne, _selOption);
    }

    public boolean changeSelectionFromGlobalIdx(final CtuluListSelection _frIdx, final int _selOption) {
        if (_frIdx == null || _frIdx.isEmpty()) {
            return false;
        }
        final int[] frIdx = new int[2];
        final EbliListeSelectionMulti newOne = createSelection();
        final int maxIdx = _frIdx.getMaxIndex();
        for (int i = _frIdx.getMinIndex(); i <= maxIdx; i++) {
            if (_frIdx.isSelected(i) && m_.getIdxFrIdxOnFrFromGeneral(i, frIdx)) {
                newOne.add(frIdx[0], frIdx[1]);
            }
        }
        return super.changeSelection(newOne, _selOption);
    }

    public EbliActionInterface create3DAction(final CtuluUI _ui, final CtuluCommandContainer _cmd) {
        return new MvFrontierLayerAbstract.EditAction(this, _cmd, _ui);
    }

    public Mv3DFrontierData createData() {
        if (data3D_ == null) {
            data3D_ = new Mv3DFrontierData(((MvFrontierModel) modeleDonnees()).getFr());
            data3D_.addObserver(new Observer() {

                public void update(final Observable _o, final Object _arg) {
                    firePropertyChange("modeld", true, false);
                }
            });
        }
        return data3D_;
    }

    public CtuluCollectionInteger get3DType() {
        return data3D_ == null ? null : data3D_.getType();
    }

    public CtuluCollectionDouble getBuildingHeight() {
        return data3D_ == null ? null : data3D_.getElevation();
    }

    public Mv3DFrontierData getData() {
        return data3D_;
    }

    public EbliFindExpressionContainerInterface getExpressionContainer() {
        return new MvExpressionSupplierFrNode(m_);
    }

    public MvFrontierModel getModeleCl() {
        return (MvFrontierModel) modeleDonnees();
    }

    @Override
    public final BCalquePersistenceInterface getPersistenceMng() {
        return new MvFrontiere3DPersistence();
    }

    public int[] getSelectedEdgeIdx() {
        return null;
    }

    public int[] getSelectedElementIdx() {
        return null;
    }

    public int[] getSelectedFrontier() {
        return getLayerSelectionMulti().getIdxSelected();
    }

    /**
   * @return les indices de frontieres selectionnes.
   */
    @Override
    public int[] getSelectedObjectInTable() {
        if (!isSelectionEmpty()) {
            final TIntObjectIterator it = selection_.getIterator();
            final TIntHashSet r = new TIntHashSet(m_.getNbTotalPoint());
            final int max = selection_.getNbListSelected();
            for (int i = 0; i < max; i++) {
                it.advance();
                final int fr = it.key();
                final CtuluListSelection s = (CtuluListSelection) it.value();
                final int maxFr = s.getMaxIndex();
                for (int j = s.getMinIndex(); j <= maxFr; j++) {
                    if (s.isSelected(j)) {
                        r.add(m_.getFrontiereIndice(fr, j));
                    }
                }
            }
            return r.toArray();
        }
        return null;
    }

    /**
   * @return les indices globaux selectionne
   */
    public int[] getSelectedPtIdx() {
        if (!isSelectionEmpty()) {
            final TIntObjectIterator it = selection_.getIterator();
            final TIntHashSet r = new TIntHashSet(m_.getNbTotalPoint());
            for (int i = selection_.getNbListSelected(); i-- > 0; ) {
                it.advance();
                final int fr = it.key();
                final CtuluListSelection s = (CtuluListSelection) it.value();
                final int max = s.getMaxIndex();
                for (int j = s.getMinIndex(); j <= max; j++) {
                    if (s.isSelected(j)) {
                        r.add(m_.getGlobalIdx(fr, j));
                    }
                }
            }
            return r.toArray();
        }
        return null;
    }

    public CtuluListSelection getSelectionInFrNum() {
        if (isSelectionEmpty()) {
            return null;
        }
        final CtuluListSelection r = new CtuluListSelection(m_.getNbTotalPoint());
        int idx = 0;
        final int nbFr = m_.getNbFrontier();
        for (int i = 0; i < nbFr; i++) {
            final int nbPt = m_.getNbPointInFrontier(i);
            final CtuluListSelection s = selection_.get(i);
            if (s == null || (s.isEmpty())) {
                idx += nbPt;
            } else {
                for (int j = 0; j < nbPt; j++) {
                    if (s.isSelected(j)) {
                        r.add(idx);
                    }
                    idx++;
                }
            }
        }
        return r;
    }

    public GrBoite getDomaineOnSelected() {
        if (isSelectionEmpty()) {
            return null;
        }
        final GrBoite r = new GrBoite();
        final GrPoint p = new GrPoint();
        final TIntObjectIterator it = selection_.getIterator();
        for (int i = selection_.getNbListSelected(); i-- > 0; ) {
            it.advance();
            final int fr = it.key();
            final CtuluListSelection s = (CtuluListSelection) it.value();
            int max = s.getMaxIndex();
            if (max >= m_.getNbPointInFrontier(fr)) {
                max = m_.getNbPointInFrontier(fr) - 1;
            }
            for (int j = s.getMinIndex(); j <= max; j++) {
                if (s.isSelected(j)) {
                    m_.point(p, fr, j);
                    r.ajuste(p);
                }
            }
        }
        return r;
    }

    public boolean isConfigurable() {
        return true;
    }

    public boolean isFontModifiable() {
        return false;
    }

    public boolean isForegroundColorModifiable() {
        return true;
    }

    /**
   * @param _generalIdx les indices fronti�res a tester
   * @return true si tous les indices sont correctes
   */
    public boolean isFrontierSelectionCorrect(final int[] _generalIdx) {
        if (_generalIdx == null) {
            return false;
        }
        final int[] frIdx = new int[2];
        for (int i = _generalIdx.length - 1; i >= 0; i--) {
            if (!m_.getIdxFrIdxOnFrFromFrontier(_generalIdx[i], frIdx)) {
                return false;
            }
        }
        return true;
    }

    /**
   * @param _generalIdx les indices g�n�raux a tester
   * @return true si tous les points seront s�lectionne
   */
    public boolean isGeneralSelectionCorrect(final int[] _generalIdx) {
        if (_generalIdx == null) {
            return false;
        }
        final int[] frIdx = new int[2];
        for (int i = _generalIdx.length - 1; i >= 0; i--) {
            if (!m_.getIdxFrIdxOnFrFromGeneral(_generalIdx[i], frIdx)) {
                return false;
            }
        }
        return true;
    }

    public boolean isInternFr(final int _idxFr) {
        return !m_.getFr().isExtern(_idxFr);
    }

    @Override
    public boolean isPaletteModifiable() {
        return false;
    }

    @Override
    public int[] isSelectionContiguous(final int _i) {
        return super.isSelectionContiguous(_i, m_.getNbPointInFrontier(_i));
    }

    public boolean isSelectionEdgeEmpty() {
        return true;
    }

    public boolean isSelectionElementEmpty() {
        return true;
    }

    public boolean isSelectionPointEmpty() {
        return isSelectionEmpty();
    }

    @Override
    public ZModeleDonnees modeleDonnees() {
        return m_;
    }

    @Override
    public void paintIcon(final Component _c, final Graphics _g, final int _x, final int _y) {
        super.paintIcon(_c, _g, _x, _y);
        final TraceLigne l = new TraceLigne();
        l.setEpaisseur(0.5f);
        l.setCouleur(Color.LIGHT_GRAY);
        final int h = getIconHeight();
        final int w = getIconWidth();
        int x1 = _x + 3;
        int y1 = _y + h / 2;
        int x2 = _y + w / 4;
        int y2 = _y + 3;
        l.dessineTrait((Graphics2D) _g, x1, y1, x2, y2);
        l.setCouleur(Color.BLACK);
        l.dessineTrait((Graphics2D) _g, x1 - 1, y1, x1 + 1, y1);
        l.dessineTrait((Graphics2D) _g, x1, y1 - 1, x1, y1 + 1);
        l.setCouleur(Color.LIGHT_GRAY);
        x1 = x2;
        y1 = y2;
        y2 = _y + h / 4;
        x2 = _x + w / 2;
        l.dessineTrait((Graphics2D) _g, x1, y1, x2, y2);
        l.setCouleur(Color.BLACK);
        l.dessineTrait((Graphics2D) _g, x1 - 1, y1, x1 + 1, y1);
        l.dessineTrait((Graphics2D) _g, x1, y1 - 1, x1, y1 + 1);
        l.setCouleur(Color.LIGHT_GRAY);
        x1 = x2;
        y1 = y2;
        y2 = _y + 3;
        x2 = _x + w / 2 + 2;
        l.dessineTrait((Graphics2D) _g, x1, y1, x2, y2);
        l.setCouleur(Color.BLACK);
        l.dessineTrait((Graphics2D) _g, x1 - 1, y1, x1 + 1, y1);
        l.dessineTrait((Graphics2D) _g, x1, y1 - 1, x1, y1 + 1);
        l.setCouleur(Color.LIGHT_GRAY);
        x1 = x2;
        y1 = y2;
        y2 = _y + h / 2;
        x2 = _x + w - 2;
        l.dessineTrait((Graphics2D) _g, x1, y1, x2, y2);
        l.setCouleur(Color.BLACK);
        l.dessineTrait((Graphics2D) _g, x1 - 1, y1, x1 + 1, y1);
        l.dessineTrait((Graphics2D) _g, x1, y1 - 1, x1, y1 + 1);
        l.setCouleur(Color.LIGHT_GRAY);
        x1 = x2;
        y1 = y2;
        y2 = _y + h - 2;
        x2 = _x + w - 5;
        l.dessineTrait((Graphics2D) _g, x1, y1, x2, y2);
        l.setCouleur(Color.BLACK);
        l.dessineTrait((Graphics2D) _g, x1 - 1, y1, x1 + 1, y1);
        l.dessineTrait((Graphics2D) _g, x1, y1 - 1, x1, y1 + 1);
        l.setCouleur(Color.LIGHT_GRAY);
        x1 = x2;
        y1 = y2;
        x2 = _x + 3;
        y2 = _y + h / 2;
        l.dessineTrait((Graphics2D) _g, x1, y1, x2, y2);
        l.setCouleur(Color.BLACK);
        l.dessineTrait((Graphics2D) _g, x1 - 1, y1, x1 + 1, y1);
        l.dessineTrait((Graphics2D) _g, x1, y1 - 1, x1, y1 + 1);
        l.setCouleur(Color.LIGHT_GRAY);
    }

    public void paintSelection(final Graphics2D _g, final ZSelectionTrace _trace, final GrMorphisme _versEcran, final GrBoite _clipReel) {
        if (isSelectionEmpty()) {
            return;
        }
        final GrBoite clip = _clipReel;
        Color cs = _trace.getColor();
        if (isAttenue()) {
            cs = attenueCouleur(cs);
        }
        _g.setColor(cs);
        final TraceIcon ic = _trace.getIcone();
        if (isAttenue()) {
            cs = attenueCouleur(cs);
        }
        _g.setColor(cs);
        final TIntObjectIterator it = selection_.getIterator();
        final GrMorphisme versEcran = _versEcran;
        for (int i = selection_.getNbListSelected(); i-- > 0; ) {
            it.advance();
            final int fr = it.key();
            final CtuluListSelection l = (CtuluListSelection) it.value();
            final int max = l.getMaxIndex();
            for (int j = Math.max(0, l.getMinIndex()); j <= max; j++) {
                if (l.isSelected(j)) {
                    m_.point(p_, fr, j);
                    if (clip.contientXY(p_)) {
                        p_.autoApplique(versEcran);
                        ic.paintIconCentre(this, _g, p_.x_, p_.y_);
                    }
                }
            }
        }
    }

    @Override
    public EbliListeSelectionMulti selection(final GrPoint _pt, final int _tolerance) {
        GrBoite bClip = getDomaine();
        final double distanceReel = GrMorphisme.convertDistanceXY(getVersReel(), _tolerance);
        if ((!bClip.contientXY(_pt)) && (bClip.distanceXY(_pt) > distanceReel)) {
            return null;
        }
        bClip = getClipReel(getGraphics());
        final GrPoint p = new GrPoint();
        for (int i = m_.getNbFrontier() - 1; i >= 0; i--) {
            for (int j = m_.getNbPointInFrontier(i) - 1; j >= 0; j--) {
                m_.point(p, i, j);
                if (bClip.contientXY(p) && p.distanceXY(_pt) < distanceReel) {
                    final EbliListeSelectionMulti r = new EbliListeSelectionMulti(1);
                    r.set(i, j);
                    return r;
                }
            }
        }
        return null;
    }

    @Override
    public EbliListeSelectionMulti selection(final LinearRing _poly) {
        if (m_.getNombre() == 0 || !isVisible()) {
            return null;
        }
        final Envelope polyEnv = _poly.getEnvelopeInternal();
        final GrBoite domaineBoite = getDomaine();
        final Envelope domaine = new Envelope(domaineBoite.e_.x_, domaineBoite.o_.x_, domaineBoite.e_.y_, domaineBoite.o_.y_);
        if (!polyEnv.intersects(domaine)) {
            return null;
        }
        final EbliListeSelectionMulti r = createSelection();
        final GrPoint p = new GrPoint();
        final SIRtreePointInRing tester = new SIRtreePointInRing(_poly);
        final Coordinate c = new Coordinate();
        for (int i = m_.getNbFrontier() - 1; i >= 0; i--) {
            CtuluListSelection l = null;
            final int nbPt = m_.getNbPointInFrontier(i);
            for (int j = nbPt - 1; j >= 0; j--) {
                m_.point(p, i, j);
                c.x = p.x_;
                c.y = p.y_;
                if ((polyEnv.contains(c)) && (tester.isInside(c))) {
                    if (l == null) {
                        l = new CtuluListSelection(nbPt);
                    }
                    l.add(j);
                }
            }
            if (l != null) {
                r.set(i, l);
            }
        }
        if (r.isEmpty()) {
            return null;
        }
        return r;
    }
}
