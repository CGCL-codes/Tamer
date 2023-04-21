package com.iver.cit.gvsig.fmap.layers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Vector;
import javax.print.attribute.PrintRequestAttributeSet;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.fmap.ViewPort;
import com.iver.cit.gvsig.fmap.layers.layerOperations.ComposedLayer;
import com.iver.utiles.swing.threads.Cancellable;

/**
 * Group WMS layers to make a single request to the
 * server for all layers.
 *
 * It is posible only if almost all params are the same. For this
 * comparasion, ComposedLayerWMS uses the method
 * {@link com.iver.cit.gvsig.fmap.layers.FLyrWMS#isComposedLayerCompatible(com.iver.cit.gvsig.fmap.layers.FLayer)}
 *
 *
 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.ComposedLayer
 * @see com.iver.cit.gvsig.fmap.layers.FLyrWMS
 */
public class ComposedLayerWMS extends ComposedLayer {

    private FLyrWMS layer = null;

    public boolean canAdd(FLayer layer) {
        if (this.layer != null) {
            return this.layer.isComposedLayerCompatible(layer);
        }
        return false;
    }

    public void doAdd(FLayer layer) throws Exception {
        FLyrWMS aLayer = (FLyrWMS) layer;
        if (this.layer == null) {
            this.layer = new FLyrWMS();
            this.layer.setXMLEntity(aLayer.getXMLEntity());
            return;
        }
        this.layer.setLayerQuery(this.layer.getLayerQuery() + "," + aLayer.getLayerQuery());
        Vector aStyles = aLayer.getStyles();
        if (aStyles != null) {
            Vector myStyles = this.layer.getStyles();
            if (myStyles == null) {
                this.layer.setStyles(aStyles);
            } else {
                myStyles.addAll(aStyles);
                this.layer.setStyles(myStyles);
            }
        }
        this.layer.getFullExtent().add(aLayer.getFullExtent());
    }

    public void doDraw(BufferedImage image, Graphics2D g, ViewPort viewPort, Cancellable cancel, double scale) throws ReadDriverException {
        this.layer.draw(image, g, viewPort, cancel, scale);
    }

    protected void doPrint(Graphics2D g, ViewPort viewPort, Cancellable cancel, double scale, PrintRequestAttributeSet properties) throws ReadDriverException {
        this.layer.print(g, viewPort, cancel, scale, properties);
    }
}
