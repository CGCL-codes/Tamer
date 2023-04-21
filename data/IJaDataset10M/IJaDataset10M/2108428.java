package game.graph;

public class GraphLayerCrossEvent extends GraphLayerEvent {

    GraphLayerCross layer;

    public GraphLayerCrossEvent(GraphLayerCross l) {
        super(l);
        layer = l;
    }

    public int getX() {
        return layer.get(GraphLayerCross.SET_POS_X);
    }

    public int getY() {
        return layer.get(GraphLayerCross.SET_POS_Y);
    }
}
