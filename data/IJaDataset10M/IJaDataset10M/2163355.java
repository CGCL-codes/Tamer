package verilogx;

import rpi.goldsd.graph.*;

/**
 * Title:        Agere cell lib verilog parser
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      Pittsburgh Simulation
 * @author Charles Havener
 * @version 1.0
 */
public class LabeledEdge extends DirectedEdge {

    String label;

    public LabeledEdge(Vertex startVertex, Vertex endVertex, String label) {
        super(startVertex, endVertex);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
