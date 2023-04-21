package org.fudaa.ebli.visuallibrary;

import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import org.fudaa.ctulu.CtuluCommandContainer;
import org.fudaa.ebli.trace.TraceLigne;

/**
 * @author deniger
 */
public class EbliWidgetWithBordure extends EbliWidget {

    protected Insets insets_;

    /**
   * @param _scene
   */
    public EbliWidgetWithBordure(final EbliScene _scene) {
        super(_scene);
    }

    /**
   * @param _scene
   * @param _controllerDefaut
   */
    public EbliWidgetWithBordure(final EbliScene _scene, final boolean _controllerDefaut) {
        super(_scene, _controllerDefaut);
    }

    @Override
    protected void paintWidget() {
        final Graphics2D g = getGraphics();
        final Rectangle rec = getClientArea();
        g.translate(rec.x, rec.y);
        final TraceLigne l = new TraceLigne(getTraceLigneModel());
        if (this.getIntern().canColorBackground()) {
            getGraphics().setColor(getColorFond());
            getGraphics().fillRect(0, 0, (int) (rec.width - l.getEpaisseur()), (int) (rec.height - l.getEpaisseur()));
        }
        l.setCouleur(getColorContour());
        l.dessineRectangle(g, (int) (l.getEpaisseur() / 2), (int) (l.getEpaisseur() / 2), (int) (rec.width - l.getEpaisseur()), (int) (rec.height - l.getEpaisseur()));
        g.translate(-rec.x, -rec.y);
    }

    @Override
    protected void setPropertyCmd(final String _key, final Object _prop, final CtuluCommandContainer _cmd) {
        super.setPropertyCmd(_key, _prop, _cmd);
        if (LINEMODEL.equals(_prop)) {
            updateTraceLigne();
        } else if (COLORFOND.equals(_prop)) repaint();
    }

    protected void updateTraceLigne() {
        final int oldEp = insets_.bottom;
        if (insets_.bottom != getTraceLigneModel().getEpaisseur()) {
            insets_.bottom = (int) getTraceLigneModel().getEpaisseur();
            insets_.top = insets_.bottom;
            insets_.left = insets_.bottom;
            insets_.right = insets_.bottom;
            final Rectangle newBounds = getBounds();
            newBounds.height += (getTraceLigneModel().getEpaisseur() - oldEp) * 2;
            newBounds.width += (getTraceLigneModel().getEpaisseur() - oldEp) * 2;
            setPreferredBounds(newBounds);
        }
    }
}
