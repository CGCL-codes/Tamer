package org.fudaa.ebli.calque;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.table.AbstractTableModel;
import com.vividsolutions.jts.algorithm.SIRtreePointInRing;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.CtuluListSelection;
import org.fudaa.ctulu.gis.GISGeometryFactory;
import org.fudaa.ctulu.gis.GISLib;
import org.fudaa.ebli.commun.EbliLib;
import org.fudaa.ebli.geometrie.GrBoite;
import org.fudaa.ebli.geometrie.GrMorphisme;
import org.fudaa.ebli.geometrie.GrPoint;
import org.fudaa.ebli.palette.BPalettePlage;
import org.fudaa.ebli.palette.BPaletteInfo.InfoData;
import org.fudaa.ebli.trace.TraceIcon;
import org.fudaa.ebli.trace.TraceIconModel;

/**
 * Un calque d'affichage de point avec un icone.
 * 
 * @version $Id: ZCalquePoint.java,v 1.51.6.1 2008-02-20 10:16:01 bmarchan Exp $
 * @author Guillaume Desnoix
 */
public class ZCalquePoint extends ZCalqueAffichageDonnees {

    /**
   * @author Fred Deniger
   * @version $Id: ZCalquePoint.java,v 1.51.6.1 2008-02-20 10:16:01 bmarchan Exp $
   */
    public static class DefaultTableModel extends AbstractTableModel {

        final ZModelePoint model_;

        /**
     * @param _model
     */
        public DefaultTableModel(final ZModelePoint _model) {
            super();
            model_ = _model;
        }

        public int getColumnCount() {
            return 3;
        }

        public String getColumnName(final int _column) {
            if (_column == 0) {
                return EbliLib.getS("Indice");
            }
            if (_column == 1) {
                return "X";
            }
            return "Y";
        }

        public int getRowCount() {
            return model_.getNombre();
        }

        public Class getColumnClass(final int _columnIndex) {
            return _columnIndex == 0 ? Integer.class : Double.class;
        }

        public Object getValueAt(final int _rowIndex, final int _columnIndex) {
            if (_columnIndex == 0) {
                return new Integer(_rowIndex + 1);
            }
            if (_columnIndex == 1) {
                return CtuluLib.getDouble(model_.getX(_rowIndex));
            }
            return CtuluLib.getDouble(model_.getY(_rowIndex));
        }
    }

    public void fillWithInterpolateInfo(final InfoData _m) {
    }

    /**
   * @author Fred Deniger
   * @version $Id: ZCalquePoint.java,v 1.51.6.1 2008-02-20 10:16:01 bmarchan Exp $
   */
    public static class DefaultTableModelZ extends DefaultTableModel {

        /**
     * @param _model
     */
        public DefaultTableModelZ(final ZModelePoint.ThreeDim _model) {
            super(_model);
        }

        public int getColumnCount() {
            return 4;
        }

        public String getColumnName(final int _column) {
            if (_column == 3) {
                return "Z";
            }
            return super.getColumnName(_column);
        }

        public Object getValueAt(final int _rowIndex, final int _columnIndex) {
            if (_columnIndex == 3) {
                return CtuluLib.getDouble(((ZModelePoint.ThreeDim) super.model_).getZ(_rowIndex));
            }
            return super.getValueAt(_rowIndex, _columnIndex);
        }
    }

    protected ZModelePoint modele_;

    /** */
    public ZCalquePoint() {
        this(null);
    }

    public GrBoite getDomaineOnSelected() {
        if (isSelectionEmpty()) {
            return null;
        }
        int m = selection_.getMaxIndex();
        if (m > modele_.getNombre()) {
            m = modele_.getNombre() - 1;
        }
        final GrBoite r = new GrBoite();
        final GrPoint p = new GrPoint();
        for (int i = selection_.getMinIndex(); i <= m; i++) {
            if (selection_.isSelected(i)) {
                modele_.point(p, i, true);
                r.ajuste(p);
            }
        }
        return r;
    }

    public void paintIcon(final Component _c, final Graphics _g, final int _x, final int _y) {
        super.paintIcon(_c, _g, _x, _y);
        if ((modele_ == null) || (modele_.getNombre() == 0)) {
            return;
        }
        if (isPaletteCouleurUsed_) {
            _g.setColor(Color.RED);
        } else {
            _g.setColor(getForeground());
        }
        final int w = getIconWidth();
        final int h = getIconHeight();
        int x1 = _x + 3;
        int y1 = _y + h / 2;
        final int x2 = _x + w / 2;
        final int y2 = _y + 3;
        final int x3 = _x + w / 4;
        final int y3 = _y + h - 3;
        final int x4 = x2;
        final int y4 = _y + h / 2 + 4;
        _g.drawLine(x1 - 1, y1, x1 + 1, y1);
        _g.drawLine(x1, y1 - 1, x1, y1 + 1);
        if (isPaletteCouleurUsed_) {
            _g.setColor(Color.BLUE);
        }
        _g.drawLine(x2 - 1, y2, x2 + 1, y2);
        _g.drawLine(x2, y2 - 1, x2, y2 + 1);
        if (isPaletteCouleurUsed_) {
            _g.setColor(Color.ORANGE);
        }
        _g.drawLine(x3 - 1, y3, x3 + 1, y3);
        _g.drawLine(x3, y3 - 1, x3, y3 + 1);
        if (isPaletteCouleurUsed_) {
            _g.setColor(Color.GREEN);
        }
        _g.drawLine(x4 - 1, y4, x4 + 1, y4);
        _g.drawLine(x4, y4 - 1, x4, y4 + 1);
        x1 = _x + w - 3;
        y1 = _y + h / 2 + 3;
        if (isPaletteCouleurUsed_) {
            _g.setColor(Color.yellow);
        }
        _g.drawLine(x1 - 1, y1, x1 + 1, y1);
        _g.drawLine(x1, y1 - 1, x1, y1 + 1);
    }

    /**
   * @param _modele le modele a afficher
   */
    public ZCalquePoint(final ZModelePoint _modele) {
        super();
        iconModel_ = new TraceIconModel(TraceIcon.CARRE_PLEIN, 1, Color.BLACK);
        modele_ = _modele;
        if (modele_ != null) {
            initSelection();
        }
    }

    /**
   * @param _modele Modele
   */
    public void setModele(final ZModelePoint _modele) {
        if (modele_ != _modele) {
            final ZModelePoint vp = modele_;
            modele_ = _modele;
            if (modele_ != null) {
                initSelection();
            }
            firePropertyChange("modele", vp, modele_);
        }
    }

    /**
   * @return Modele
   */
    public ZModelePoint modele() {
        return modele_;
    }

    public ZModeleDonnees modeleDonnees() {
        return modele();
    }

    GrPoint p_;

    public void setForeground(final Color _v) {
        if (iconModel_ != null) {
            iconModel_.setCouleur(_v);
        }
        super.setForeground(_v);
    }

    public void paintDonnees(final Graphics2D _g, final GrMorphisme _versEcran, final GrMorphisme _versReel, final GrBoite _clipReel) {
        if ((modele_ != null) && (modele_.getNombre() > 0)) {
            paintDonnees(_g, modele_.getNombre(), _versEcran, _clipReel);
        }
    }

    public void paintDonnees(final Graphics2D _g, final int _nbPt, final GrMorphisme _versEcran, final GrBoite _clipReel) {
        if ((modele_ == null) || (_nbPt <= 0)) {
            return;
        }
        final boolean attenue = isAttenue();
        final boolean rapide = isRapide();
        if (p_ == null) {
            p_ = new GrPoint();
        }
        final TraceIcon icone = iconModel_.buildCopy();
        if (icone == null) {
            return;
        }
        if (isAttenue()) {
            icone.setCouleur(attenueCouleur(icone.getCouleur()));
        }
        if (EbliLib.isAlphaChanged(alpha_)) {
            icone.setCouleur(EbliLib.getAlphaColor(icone.getCouleur(), alpha_));
        }
        for (int i = _nbPt - 1; i >= 0; i--) {
            if (!modele_.point(p_, i, false)) {
                continue;
            }
            if (!_clipReel.contientXY(p_)) {
                continue;
            }
            final double z = p_.z_;
            p_.autoApplique(_versEcran);
            if (!rapide && isPaletteCouleurUsed_ && (paletteCouleur_ != null)) {
                Color c = ((BPalettePlage) paletteCouleur_).getColorFor(z);
                if (attenue) {
                    c = attenueCouleur(c);
                }
                icone.setCouleur(c);
            }
            icone.paintIconCentre(this, _g, (int) p_.x_, (int) p_.y_);
        }
    }

    public LineString getSelectedLine() {
        if (getNbSelected() != 2) {
            return null;
        }
        final Coordinate[] cs = new Coordinate[2];
        cs[0] = new Coordinate(modele_.getX(getLayerSelection().getMinIndex()), modele_.getY(getLayerSelection().getMinIndex()));
        cs[1] = new Coordinate(modele_.getX(getLayerSelection().getMaxIndex()), modele_.getY(getLayerSelection().getMaxIndex()));
        if (cs[0].compareTo(cs[1]) > 0) {
            final Coordinate tmp = cs[0];
            cs[0] = cs[1];
            cs[1] = tmp;
        }
        return GISGeometryFactory.INSTANCE.createLineString(cs);
    }

    public void paintSelection(final Graphics2D _g, final ZSelectionTrace _trace, final GrMorphisme _versEcran, final GrBoite _clipReel) {
        if ((modele_ == null) || (modele_.getNombre() <= 0) || isSelectionEmpty() || isRapide()) {
            return;
        }
        final GrMorphisme versEcran = _versEcran;
        final GrBoite clip = _clipReel;
        if (!modele_.getDomaine().intersectXY(clip)) {
            return;
        }
        Color cs = _trace.getColor();
        if (isAttenue()) {
            cs = attenueCouleur(cs);
        }
        final TraceIcon ic = _trace.getIcone();
        _g.setColor(cs);
        int nb = selection_.getMaxIndex();
        final int max = modele_.getNombre();
        if (nb >= max) {
            nb = max - 1;
        }
        if (p_ == null) {
            p_ = new GrPoint();
        }
        for (int i = selection_.getMinIndex(); i <= nb; i++) {
            if (selection_.isSelected(i)) {
                modele_.point(p_, i, true);
                if (clip.contientXY(p_)) {
                    p_.autoApplique(versEcran);
                    ic.paintIconCentre(this, _g, p_.x_, p_.y_);
                }
            }
        }
    }

    public CtuluListSelection selection(final LinearRing _poly, final int _mode) {
        return selectionBasic(_poly, _mode);
    }

    public CtuluListSelection selectionBasic(final LinearRing _poly, final int _mode) {
        if (modele_ == null || modele_.getNombre() == 0 || !isVisible()) {
            return null;
        }
        final Envelope polyEnv = _poly.getEnvelopeInternal();
        final GrBoite domaineBoite = getDomaine();
        final Envelope domaine = new Envelope(domaineBoite.e_.x_, domaineBoite.o_.x_, domaineBoite.e_.y_, domaineBoite.o_.y_);
        if (!polyEnv.intersects(domaine)) {
            return null;
        }
        final CtuluListSelection r = creeSelection();
        final GrPoint p = new GrPoint();
        final Coordinate c = new Coordinate();
        final int nb = modele().getNombre() - 1;
        final SIRtreePointInRing tester = new SIRtreePointInRing(_poly);
        for (int i = nb; i >= 0; i--) {
            modele().point(p, i, true);
            c.x = p.x_;
            c.y = p.y_;
            if (GISLib.isSelectedEnv(c, _poly, polyEnv, tester)) {
                r.add(i);
            }
        }
        if (r.isEmpty()) {
            return null;
        }
        return r;
    }

    public static int getSelectedPoint(final ZModelePoint _modele, final GrPoint _p, final int _tolerance, final GrMorphisme _versReel, final GrBoite _clipReel) {
        final GrBoite bClip = _modele.getDomaine();
        final double toleranceReel = GrMorphisme.convertDistanceXY(_versReel, _tolerance);
        if ((!bClip.contientXY(_p)) && (bClip.distanceXY(_p) > toleranceReel)) {
            return -1;
        }
        final int nb = _modele.getNombre() - 1;
        final GrPoint p = new GrPoint();
        for (int i = nb; i >= 0; i--) {
            _modele.point(p, i, true);
            if (_clipReel.contientXY(p)) {
                if (_p.distanceXY(p) < toleranceReel) {
                    return i;
                }
            }
        }
        return -1;
    }

    public GrPoint getPoint(int i) {
        if (i >= modeleDonnees().getNombre()) return null;
        GrPoint p = new GrPoint();
        modele().point(p, i, true);
        return p;
    }

    protected CtuluListSelection selectionBasic(final GrPoint _pt, final int _tolerance) {
        if (modele() == null || modele().getNombre() == 0 || (!isVisible())) {
            return null;
        }
        final int i = getSelectedPoint(modele_, _pt, _tolerance, getVersReel(), getClipReel(getGraphics()));
        if (i >= 0) {
            final CtuluListSelection r = creeSelection();
            r.add(i);
            return r;
        }
        return null;
    }

    public CtuluListSelection selection(final GrPoint _pt, final int _tolerance) {
        return selectionBasic(_pt, _tolerance);
    }

    public boolean isConfigurable() {
        return true;
    }
}
