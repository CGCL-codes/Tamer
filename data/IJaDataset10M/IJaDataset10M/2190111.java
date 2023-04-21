package demo.view.layout;

import demo.model.*;
import higraph.view.layout.NestedTreeLayoutManager;

public class DemoBoxesInBoxesLayout extends NestedTreeLayoutManager<DemoPayload, DemoEdgeLabel, DemoHigraph, DemoWholeGraph, DemoSubgraph, DemoNode, DemoEdge> {

    final int LEFT_MARGIN = 5;

    final int RIGHT_MARGIN = 5;

    final int TOP_MARGIN = 5;

    final int BOTTOM_MARGIN = 5;

    final int VERTICAL_GAP = 5;

    final int HORIZONTAL_GAP = 5;

    final int LEAF_WIDTH = 10;

    final int LEAF_HEIGHT = 10;

    public DemoBoxesInBoxesLayout getThis() {
        return this;
    }
}
