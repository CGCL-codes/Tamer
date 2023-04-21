package org.openscience.jchempaint.renderer.generators;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.List;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.jchempaint.renderer.Renderer;
import org.openscience.jchempaint.renderer.RendererModel;
import org.openscience.jchempaint.renderer.elements.ElementGroup;
import org.openscience.jchempaint.renderer.elements.IRenderingElement;
import org.openscience.jchempaint.renderer.elements.TextElement;

/**
 * @cdk.module rendercontrol
 */
public class AtomContainerTitleGenerator implements IGenerator {

    public AtomContainerTitleGenerator() {
    }

    public IRenderingElement generate(IAtomContainer ac, RendererModel model) {
        if (ac.getProperty(CDKConstants.TITLE) == null) return null;
        double d = model.getBondLength() / model.getScale() / 2;
        Rectangle2D totalBounds = Renderer.calculateBounds(ac);
        ElementGroup diagram = new ElementGroup();
        double minX = totalBounds.getMinX();
        double minY = totalBounds.getMinY();
        double maxX = totalBounds.getMaxX();
        double maxY = totalBounds.getMaxY();
        Color c = model.getForeColor();
        diagram.add(new TextElement((minX + maxX) / 2, minY - d, (String) ac.getProperty(CDKConstants.TITLE), c));
        return diagram;
    }

    public List<IGeneratorParameter> getParameters() {
        return null;
    }
}
