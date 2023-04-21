package org.openscience.jchempaint.renderer.elements;

/**
 * @cdk.module render
 */
public interface IRenderingElement {

    public void accept(IRenderingVisitor v);
}
