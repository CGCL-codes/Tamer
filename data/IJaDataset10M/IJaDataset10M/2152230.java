package org.fudaa.fudaa.meshviewer.layer;

import java.awt.Color;
import java.awt.Graphics2D;
import org.fudaa.ebli.commun.EbliLib;
import org.fudaa.ebli.find.EbliFindActionInterface;
import org.fudaa.ebli.geometrie.GrBoite;
import org.fudaa.ebli.geometrie.GrMorphisme;
import org.fudaa.ebli.geometrie.GrPoint;
import org.fudaa.ebli.trace.TraceIcon;
import org.fudaa.ebli.trace.TraceIconModel;
import org.fudaa.fudaa.meshviewer.model.MvFrontierModel;

/**
 * @author deniger
 * @version $Id: MvFrontierPointLayer.java,v 1.18 2007-01-19 13:14:07 deniger Exp $
 */
public class MvFrontierPointLayer extends MvFrontierLayerAbstract {

    /**
   * @param _cl
   */
    public MvFrontierPointLayer(final MvFrontierModel _cl) {
        super(_cl);
    }

    public EbliFindActionInterface getFinder() {
        return new MVFindActionFrontierPt(this);
    }

    /**
   * @param _g
   */
    @Override
    public void paintDonnees(final Graphics2D _g, final GrMorphisme _versEcran, final GrMorphisme _versReel, final GrBoite _clipReel) {
        if (!isT6_) {
            return;
        }
        final GrBoite clip = _clipReel;
        final GrMorphisme versEcran = _versEcran;
        final GrPoint p = new GrPoint();
        final Color old = _g.getColor();
        final TraceIconModel ic = iconModel_.cloneData();
        final GrBoite b = getDomaine().applique(versEcran);
        int taille = (int) (b.e_.y_ - b.o_.y_) / 10;
        if (taille > 3) {
            taille = 3;
        }
        ic.setTaille(taille);
        if (isAttenue()) {
            ic.setCouleur(attenueCouleur(ic.getCouleur()));
        } else if (EbliLib.isAlphaChanged(alpha_)) {
            ic.setCouleur(EbliLib.getAlphaColor(ic.getCouleur(), alpha_));
        }
        final TraceIcon traceIc = new TraceIcon(ic);
        for (int i = m_.getNbFrontier() - 1; i >= 0; i--) {
            for (int j = m_.getNbPointInFrontier(i) - 1; j >= 0; j -= 2) {
                m_.point(p, i, j);
                if (clip.contientXY(p)) {
                    p.autoApplique(versEcran);
                    traceIc.paintIconCentre(this, _g, p.x_, p.y_);
                }
            }
        }
        _g.setColor(old);
    }
}
