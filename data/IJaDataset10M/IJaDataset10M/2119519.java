package br.unb.syntainia.gui.graphicinteraction.jgraph;

import br.unb.syntainia.gui.graphicinteraction.jgraph.rendering.GeneCell;
import br.unb.syntainia.gui.graphicinteraction.jgraph.rendering.GeneCellView;
import br.unb.syntainia.gui.graphicinteraction.jgraph.rendering.GeneEdgeCell;
import br.unb.syntainia.gui.graphicinteraction.jgraph.rendering.GeneEdgeCellView;
import br.unb.syntainia.gui.graphicinteraction.jgraph.rendering.GeneGroupCell;
import br.unb.syntainia.gui.graphicinteraction.jgraph.rendering.GeneGroupCellView;
import br.unb.syntainia.gui.graphicinteraction.jgraph.rendering.GenomeCell;
import br.unb.syntainia.gui.graphicinteraction.jgraph.rendering.GenomeCellView;
import br.unb.syntainia.gui.graphicinteraction.jgraph.rendering.TitleCell;
import br.unb.syntainia.gui.graphicinteraction.jgraph.rendering.TitleCellView;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.Edge;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.VertexView;

/**
 *
 * @author luizaugusto
 */
public class SyntainiaCellViewFactory extends DefaultCellViewFactory {

    private static final long serialVersionUID = -2858907421354782779L;

    @Override
    protected VertexView createVertexView(Object cell) {
        if (cell instanceof GeneGroupCell) return new GeneGroupCellView((GeneGroupCell) cell);
        if (cell instanceof GeneCell) return new GeneCellView((GeneCell) cell);
        if (cell instanceof TitleCell) return new TitleCellView((TitleCell) cell);
        if (cell instanceof GenomeCell) return new GenomeCellView((GenomeCell) cell);
        return super.createVertexView(cell);
    }

    @Override
    protected EdgeView createEdgeView(Edge cell) {
        return new GeneEdgeCellView((GeneEdgeCell) cell);
    }

    @Override
    protected EdgeView createEdgeView(Object cell) {
        if (cell instanceof Edge) return createEdgeView((Edge) cell); else return new EdgeView(cell);
    }
}
