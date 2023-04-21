package com.neptuny.xgrapher.cli.render;

import java.awt.BasicStroke;
import java.awt.Stroke;
import com.neptuny.xgrapher.cli.controller.GraphAttributeType;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.decorators.EdgeStrokeFunction;
import edu.uci.ics.jung.visualization.PluggableRenderer;

/**
 * The edge stroke function for collaborative mode - Use edge userDatum(SIZE) to
 * define the strokeness of the line
 * 
 * @author Luigi Bianchi [luigi_bianchi@katamail.com]
 * @since 11/ott/07
 */
public class DefaultEdgeStrokeFunction implements EdgeStrokeFunction {

    protected final Stroke THIN = new BasicStroke(1);

    protected final Stroke HEAVY = new BasicStroke(5);

    protected final Stroke DOTTED = PluggableRenderer.DOTTED;

    public Stroke getStroke(Edge e) {
        if ((Double) e.getUserDatum(GraphAttributeType.SIZE) < 0.7) return null; else if ((Double) e.getUserDatum(GraphAttributeType.SIZE) < 0.8) return DOTTED; else if ((Double) e.getUserDatum(GraphAttributeType.SIZE) < 0.9) return THIN; else return HEAVY;
    }
}
