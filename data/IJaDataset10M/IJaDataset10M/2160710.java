package org.fudaa.ebli.calque;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import org.fudaa.ebli.commun.EbliLib;
import org.fudaa.ebli.geometrie.GrBoite;
import org.fudaa.ebli.geometrie.GrPoint;
import org.fudaa.ebli.ressource.EbliResource;

/**
 * @author deniger
 * @version $Id: ZCalqueRepereInteraction.java,v 1.18 2006-09-19 14:55:48 deniger Exp $
 */
public class ZCalqueRepereInteraction extends BCalqueInteraction implements MouseListener, MouseMotionListener, KeyListener {

    private Point pointDeb_, pointFinPrec_;

    private final BVueCalque vc_;

    private double coefZoomClick_ = 1.5;

    private final BGroupeCalque gcDonnees_;

    public ZCalqueRepereInteraction(final BVueCalque _vc, BGroupeCalque _gcDonnees) {
        super();
        vc_ = _vc;
        pointDeb_ = null;
        pointFinPrec_ = null;
        _vc.addKeyListener(this);
        gcDonnees_ = _gcDonnees;
    }

    public boolean alwaysPaint() {
        return true;
    }

    public Cursor getSpecificCursor() {
        return EbliResource.EBLI.getCursor("loupe-etendue", -1, new Point(8, 8));
    }

    public String getDescription() {
        return EbliLib.getS("Zoom") + (zoomMoins_ ? " -" : " +");
    }

    /**
   * Dessin de l'icone.
   * 
   * @param _c composant dont l'icone peut deriver des proprietes (couleur, ...). Ce parametre peut etre <I>null</I>. Il
   *          est ignore ici.
   * @param _g le graphics sur lequel dessiner l'icone
   * @param _x lieu cible de l'icone (x)
   * @param _y lieu cible de l'icone (y)
   */
    public void paintIcon(final Component _c, final Graphics _g, final int _x, final int _y) {
        super.paintIcon(_c, _g, _x, _y);
        final int w = getIconWidth();
        final int h = getIconHeight();
        _g.setColor(Color.black);
        _g.drawRect(_x + 10, _y + 6, w - 15, h - 13);
        _g.setColor(Color.lightGray);
        _g.drawRect(_x + 2, _y + 2, w - 4, h - 4);
    }

    /**
   * Affectation du coefficient de zoom avant/arriere.
   * <p>
   * Ce coefficient est utilis� dans le zoom par simple clic.
   * 
   * @param _coef Le coefficient multiplicateur du zoom autre que 0. Si 0, il est d�fini par d�faut.
   */
    public void setCoefficientZoom(final double _coef) {
        if (_coef == 0) {
            coefZoomClick_ = 1.5;
        } else {
            coefZoomClick_ = _coef;
        }
    }

    /**
   * Retour du coefficient de zoom avant/arriere.
   * 
   * @return La valeur du coefficient. <I>1.5</I> par d�faut.
   */
    public double getCoefficientZoom() {
        return coefZoomClick_;
    }

    public void mouseClicked(final MouseEvent _evt) {
    }

    public void mouseEntered(final MouseEvent _evt) {
    }

    public void mouseExited(final MouseEvent _evt) {
    }

    public void mousePressed(final MouseEvent _evt) {
        if (!isOkLeftEvent(_evt)) {
            return;
        }
        gcDonnees_.setUseCache(true);
        pointDeb_ = _evt.getPoint();
    }

    public void mouseReleased(final MouseEvent _evt) {
        gcDonnees_.setUseCache(false);
        if (!isOkLeftEvent(_evt)) {
            return;
        }
        if (pointDeb_ == null) {
            return;
        }
        GrPoint pointO;
        GrPoint pointE;
        if (pointDeb_.distance(_evt.getPoint()) > 7) {
            final Point pointFin = _evt.getPoint();
            pointE = new GrPoint(Math.min(pointDeb_.x, pointFin.x), Math.max(pointDeb_.y, pointFin.y), 0.);
            pointO = new GrPoint(Math.max(pointDeb_.x, pointFin.x), Math.min(pointDeb_.y, pointFin.y), 0.);
            pointE.autoApplique(getVersReel());
            pointO.autoApplique(getVersReel());
            final GrBoite boite = new GrBoite(pointO, pointE);
            vc_.changeRepere(this, boite);
        } else {
            vc_.zoomOnMouse(_evt);
        }
        pointFinPrec_ = null;
        pointDeb_ = null;
    }

    public void mouseDragged(final MouseEvent _evt) {
        final Point pointFin = _evt.getPoint();
        if (!contains(pointFin)) {
            EbliLib.setIn(pointFin, this);
        }
        pointFinPrec_ = pointFin;
        repaint();
    }

    @Override
    public void paintComponent(Graphics _g) {
        if (pointDeb_ == null || pointFinPrec_ == null) {
            return;
        }
        _g.setXORMode(getBackground());
        _g.setColor(Color.black);
        _g.drawLine(pointDeb_.x, pointDeb_.y, pointFinPrec_.x, pointDeb_.y);
        _g.drawLine(pointFinPrec_.x, pointDeb_.y, pointFinPrec_.x, pointFinPrec_.y);
        _g.drawLine(pointFinPrec_.x, pointFinPrec_.y, pointDeb_.x, pointFinPrec_.y);
        _g.drawLine(pointDeb_.x, pointFinPrec_.y, pointDeb_.x, pointDeb_.y);
    }

    public void mouseMoved(final MouseEvent _evt) {
    }

    private boolean zoomMoins_;

    private void setZoomMoins(final boolean _b) {
        if (_b != zoomMoins_) {
            zoomMoins_ = _b;
            firePropertyChange("state", zoomMoins_ ? Boolean.FALSE : Boolean.TRUE, zoomMoins_ ? Boolean.TRUE : Boolean.FALSE);
        }
    }

    public void keyPressed(final KeyEvent _e) {
        if ((!isGele()) && (_e.getKeyCode() == KeyEvent.VK_SHIFT)) {
            setZoomMoins(_e.isShiftDown());
        }
    }

    public void keyReleased(final KeyEvent _e) {
        if ((!isGele()) && (_e.getKeyCode() == KeyEvent.VK_SHIFT)) {
            setZoomMoins(false);
        }
    }

    public void keyTyped(final KeyEvent _e) {
    }
}
