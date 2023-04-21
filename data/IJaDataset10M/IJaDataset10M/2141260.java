package org.openscience.jchempaint.renderer.generators;

import java.awt.geom.Rectangle2D;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.jchempaint.renderer.Renderer;
import org.openscience.jchempaint.renderer.RendererModel;
import org.openscience.jchempaint.renderer.elements.ElementGroup;
import org.openscience.jchempaint.renderer.elements.IRenderingElement;
import org.openscience.jchempaint.renderer.elements.RectangleElement;
import org.openscience.jchempaint.renderer.elements.TextElement;

/**
 * Generate the symbols for radicals.
 * 
 * @author maclean
 * @cdk.module renderextra
 *
 */
public class ProductsBoxGenerator implements IReactionGenerator {

    private static double DISTANCE;

    public IRenderingElement generate(IReaction reaction, RendererModel model) {
        if (!model.getShowReactionBoxes()) return null;
        if (reaction.getProductCount() == 0) return new ElementGroup();
        DISTANCE = model.getBondLength() / model.getScale() / 2;
        Rectangle2D totalBounds = null;
        for (IAtomContainer molecule : reaction.getProducts().molecules()) {
            Rectangle2D bounds = Renderer.calculateBounds(molecule);
            if (totalBounds == null) {
                totalBounds = bounds;
            } else {
                totalBounds = totalBounds.createUnion(bounds);
            }
        }
        if (totalBounds == null) return null;
        ElementGroup diagram = new ElementGroup();
        diagram.add(new RectangleElement(totalBounds.getMinX() - DISTANCE, totalBounds.getMaxY() + DISTANCE, totalBounds.getMaxX() + DISTANCE, totalBounds.getMinY() - DISTANCE, model.getForeColor()));
        diagram.add(new TextElement((totalBounds.getMinX() + totalBounds.getMaxX()) / 2, totalBounds.getMinY() - DISTANCE, "Products", model.getForeColor()));
        return diagram;
    }
}
