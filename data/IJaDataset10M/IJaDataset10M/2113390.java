package org.xmi.graph.jgraph.uml;

import org.jgraph.graph.CellViewRenderer;

public class ClassView extends UMLVertexView {

    private static final ClassRenderer RENDERER = new ClassRenderer();

    public ClassView(Object cell) {
        super(cell);
    }

    public CellViewRenderer getRenderer() {
        return RENDERER;
    }
}
