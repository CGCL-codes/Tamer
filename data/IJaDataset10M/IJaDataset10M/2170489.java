package org.fudaa.fudaa.sig.layer;

import java.awt.Color;
import org.fudaa.ebli.calque.BArbreCalqueModel;
import org.fudaa.ebli.calque.ZCalqueLigneBrisee;
import org.fudaa.ebli.calque.ZEbliCalquesPanel;
import org.fudaa.ebli.geometrie.GrBoite;
import org.fudaa.ebli.trace.TraceIcon;
import org.fudaa.ebli.trace.TraceIconModel;
import org.fudaa.ebli.trace.TraceLigne;
import org.fudaa.ebli.trace.TraceLigneModel;
import org.fudaa.fudaa.sig.FSigLineSingleModel;
import com.vividsolutions.jts.geom.LineString;

/**
 * Une classe permettant d'afficher temporaire une ligne dans le panel
 * 
 * @author deniger
 */
public class FSigTempLineInLayer {

    private Color colorLineTempo_ = Color.RED;

    GrBoite initZoom_;

    boolean isZoomChanged_;

    final ZEbliCalquesPanel panel_;

    ZCalqueLigneBrisee tmp_;

    public FSigTempLineInLayer(ZEbliCalquesPanel _panel) {
        super();
        panel_ = _panel;
    }

    public void close() {
        if (tmp_ != null) {
            tmp_.detruire();
            panel_.getVueCalque().getCalque().repaint();
            panel_.getVueCalque().changeRepere(this, initZoom_);
            tmp_ = null;
        }
    }

    /**
   * @return the colorLineTempo
   */
    public Color getColorLineTempo() {
        return colorLineTempo_;
    }

    /**
   * @return the isZoomChanged
   */
    public boolean isZoomChanged() {
        return isZoomChanged_;
    }

    /**
   * @param _colorLineTempo the colorLineTempo to set
   */
    public void setColorLineTempo(Color _colorLineTempo) {
        colorLineTempo_ = _colorLineTempo;
    }

    /**
   * @param _s la ligne a afficher temporairement. si null n'affiche rien
   * @param _zoom true si on doit zoomer sur la ligne.
   */
    public void display(final LineString _s, final boolean _zoom) {
        if (panel_ == null) {
            return;
        }
        if (_s == null) {
            if (tmp_ != null) {
                tmp_.setVisible(false);
            }
            return;
        }
        final FSigLineSingleModel modele = new FSigLineSingleModel(_s);
        if (tmp_ == null) {
            initZoom_ = panel_.getVueCalque().getViewBoite();
            tmp_ = new ZCalqueLigneBrisee();
            tmp_.setDestructible(true);
            final TraceIconModel model = new TraceIconModel(TraceIcon.PLUS_DOUBLE, 4, colorLineTempo_);
            tmp_.setIconModel(0, model);
            tmp_.setIconModel(1, model);
            final TraceLigneModel ligne = new TraceLigneModel(TraceLigne.MIXTE, 2, colorLineTempo_);
            tmp_.setLineModel(0, ligne);
            tmp_.setLineModel(1, ligne);
            panel_.getVueCalque().getCalque().enPremier(tmp_);
            panel_.getCqInfos().enPremier();
        }
        tmp_.setVisible(true);
        tmp_.modele(modele);
        if (_zoom) {
            BArbreCalqueModel.actionCenter(tmp_, panel_);
            isZoomChanged_ = true;
        }
    }

    public void zoomInitial() {
        panel_.getVueCalque().setViewBoite(initZoom_);
    }

    public void restaurer() {
        panel_.restaurer();
    }
}
