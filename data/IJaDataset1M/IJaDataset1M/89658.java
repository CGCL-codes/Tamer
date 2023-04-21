package de.offis.semanticmm4u.compositors.variables.operators.complex;

import component_interfaces.semanticmm4u.realization.compositor.realization.IElement;
import component_interfaces.semanticmm4u.realization.compositor.realization.IOperator;
import component_interfaces.semanticmm4u.realization.generator.realization.IPresentationFragmentBoundaries;
import de.offis.semanticmm4u.compositors.projectors.SpatialProjector;
import de.offis.semanticmm4u.generators.GeneratorToolkit;

public class Border extends AbstractComplexOperator {

    private Border() {
    }

    public Border(IOperator myOperator) {
        this.createNewBorder(myOperator, 4);
    }

    public Border(IOperator myOperator, int myBorder) {
        this.createNewBorder(myOperator, myBorder);
    }

    private void createNewBorder(IOperator myRootOperator, int border) {
        this.setRootOperator(myRootOperator);
        IPresentationFragmentBoundaries fragmentBoundaries = GeneratorToolkit.getFragmentBoundaries(this.getRootOperator());
        int x = fragmentBoundaries.getX();
        int y = fragmentBoundaries.getY();
        int width = fragmentBoundaries.getWidth();
        int height = fragmentBoundaries.getHeight();
        this.getRootOperator().addProjector(new SpatialProjector(x + border, y + border, width + border, height + border, 0));
    }

    /**
     * Clone the object recursive.
     * 
     * @return a copy of the Object.
     * @see de.offis.semanticmm4u.compositors.AbstractElement#recursiveClone()
     */
    public IElement recursiveClone() {
        Border object = new Border();
        super.recursiveClone(object);
        return object;
    }
}
