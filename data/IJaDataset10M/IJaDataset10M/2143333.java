package de.fhdarmstadt.fbi.dtree.ui.components;

import org.jgraph.JGraph;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.VertexView;
import org.jgraph.graph.DefaultCellViewFactory;

public final class DTreeViewer extends JGraph {

    private static class DTreeCellViewFactory extends DefaultCellViewFactory {

        public DTreeCellViewFactory() {
        }

        protected VertexView createVertexView(final Object cell) {
            return new DTreeCellRenderer(cell);
        }
    }

    public DTreeViewer(final GraphModel model) {
        super(model);
        getGraphLayoutCache().setFactory(new DTreeCellViewFactory());
        setEditable(false);
        setDisconnectable(false);
        setConnectable(false);
    }
}
