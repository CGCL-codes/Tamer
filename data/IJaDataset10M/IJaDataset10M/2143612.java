package org.openscience.jchempaint.renderer.elements;

import java.awt.geom.AffineTransform;

/**
 * @cdk.module  render
 */
public interface IRenderingVisitor {

    public abstract void visit(IRenderingElement element);

    public abstract void setTransform(AffineTransform transform);
}
