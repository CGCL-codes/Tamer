package org.jsens.editors.internal.simulation;

import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.VertexView;

public class SimulatableVariableView extends VertexView {

    private static final CellViewRenderer RENDERER = new SimulatableVariableRenderer();

    public SimulatableVariableView() {
        super();
    }

    public SimulatableVariableView(Object cell) {
        super(cell);
    }

    @Override
    public CellViewRenderer getRenderer() {
        return RENDERER;
    }
}
