package de.sonivis.tool.view.prefuse;

import java.util.Iterator;
import prefuse.Visualization;
import prefuse.action.Action;
import prefuse.data.tuple.TupleSet;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

public class NodeStrokeColorAction extends Action {

    public NodeStrokeColorAction(final Visualization visualization) {
        super(visualization);
    }

    @Override
    public final void run(final double frac) {
        final TupleSet nodes = m_vis.getGroup(StringIds.NODES);
        final Iterator<?> nodeItemIterator = nodes.tuples();
        while (nodeItemIterator.hasNext()) {
            final VisualItem nodeItem = (VisualItem) nodeItemIterator.next();
            final int fillColor = nodeItem.getFillColor();
            nodeItem.setStrokeColor(ColorLib.darker(fillColor));
        }
    }
}
