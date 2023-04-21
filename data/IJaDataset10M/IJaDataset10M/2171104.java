package nz.ac.vuw.ecs.kcassell.callgraph.gui.transformers;

import java.awt.BasicStroke;
import java.awt.Stroke;
import nz.ac.vuw.ecs.kcassell.callgraph.CallGraphNode;
import org.apache.commons.collections15.Transformer;

/**
 * This class adjusts the width of the line around the boundary of the nodes.
 * @author Keith Cassell
 */
public class NodeStrokeTransformer implements Transformer<CallGraphNode, Stroke> {

    protected static BasicStroke defaultStroke = new BasicStroke();

    protected static BasicStroke boldStroke = null;

    protected static BasicStroke dashedStroke = null;

    {
        float lineWidth = defaultStroke.getLineWidth();
        float boldWidth = lineWidth * 3;
        boldStroke = new BasicStroke(boldWidth);
        boldStroke = new BasicStroke(boldWidth);
        dashedStroke = new BasicStroke(boldWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, new float[] { 2.0f, 4.0f }, 0.0f);
    }

    public NodeStrokeTransformer() {
    }

    public Stroke transform(CallGraphNode node) {
        Stroke stroke = defaultStroke;
        if (node.isInherited()) {
            stroke = defaultStroke;
        } else if (node.isInner()) {
            stroke = dashedStroke;
        } else {
            stroke = boldStroke;
        }
        return stroke;
    }
}
