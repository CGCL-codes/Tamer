package org.fudaa.ebli.visuallibrary;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import org.fudaa.ebli.trace.TraceLigne;
import org.fudaa.ebli.trace.TraceLigneModel;
import org.fudaa.ebli.visuallibrary.creator.ShapeCreator;
import org.fudaa.ebli.visuallibrary.creator.ShapeCreatorLine;

/**
 * Classe qui permet de dessinner des shapes.
 * 
 * @author genesis
 */
public class EbliWidgetShape extends EbliWidget {

    Paint fg_ = Color.YELLOW;

    Map options_;

    ShapeCreator shaper_;

    public ShapeCreator getShaper_() {
        return shaper_;
    }

    public EbliWidgetShape(final EbliScene scene, final ShapeCreator _shaper, final Map _options) {
        super(scene, true);
        super.setCheckClipping(true);
        options_ = new HashMap();
        if (_options != null) {
            options_.putAll(_options);
        }
        shaper_ = _shaper;
        propGraphique.put("lineModel", new TraceLigneModel(TraceLigne.LISSE, 2, getColorContour()));
    }

    /**
     *
     */
    @Override
    protected void paintWidget() {
        final Graphics2D g = getGraphics();
        final Rectangle recInit = getClientArea();
        final TraceLigne tl = new TraceLigne(getTraceLigneModel());
        getTraceLigneModel().setCouleur(getColorContour());
        final AffineTransform oldTr = g.getTransform();
        g.translate(recInit.x, recInit.y);
        final float ep = tl.getEpaisseur();
        final Rectangle2D.Float rec = new Rectangle2D.Float(ep / 2, ep / 2, recInit.width - ep, recInit.height - ep);
        Shape shape = shaper_.createShapeFor(rec, options_, ep);
        if (getRotation() != 0 && !(shaper_ instanceof ShapeCreatorLine)) {
            AffineTransform tr = AffineTransform.getRotateInstance(getRotation(), rec.getCenterX(), rec.getCenterY());
            shape = tr.createTransformedShape(shape);
            final Rectangle2D newBound = shape.getBounds2D();
            final double wRatio = rec.width / newBound.getWidth();
            final double hRatio = rec.height / newBound.getHeight();
            tr = AffineTransform.getTranslateInstance(-newBound.getX(), -newBound.getY());
            shape = tr.createTransformedShape(shape);
            shape = AffineTransform.getScaleInstance(wRatio, hRatio).createTransformedShape(shape);
            tr = AffineTransform.getTranslateInstance(ep / 2, ep / 2);
            shape = tr.createTransformedShape(shape);
        } else if (getRotation() != 0) {
            AffineTransform tr = AffineTransform.getRotateInstance(getRotation(), rec.getCenterX(), rec.getCenterY());
            shape = tr.createTransformedShape(shape);
        }
        if (fg_ != null) {
            final Paint old = g.getPaint();
            g.setPaint(fg_);
            if (!this.getTransparent()) {
                g.setColor(getColorFond());
                g.fill(shape);
            }
            g.setPaint(old);
        }
        tl.dessineShape(g, shape);
        g.setTransform(oldTr);
    }

    /**
   * @return the fg
   */
    public Paint getFg() {
        return fg_;
    }

    /**
   * @param _fg the fg to set
   */
    public void setFg(final Paint _fg) {
        this.fg_ = _fg;
    }

    @Override
    public boolean canRotate() {
        return true;
    }

    @Override
    public boolean canColorForeground() {
        return true;
    }

    @Override
    public boolean canColorBackground() {
        return true;
    }

    @Override
    public boolean canTraceLigneModel() {
        return true;
    }

    @Override
    public boolean canFont() {
        return false;
    }
}
