package mr.davidsanderson.uml.core.agt.render;

import mr.davidsanderson.uml.core.render.Style;
import org.modsl.core.agt.model.Graph;

public class GraphRenderVisitor extends AbstractRenderVisitor {

    @Override
    public void apply(Graph graph) {
        Style s = graph.getType().getStyle();
        g.setFillStyle(s.getGWTFillColor()).fillRectangle(0, 0, width, height);
    }
}
