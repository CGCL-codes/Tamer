package game.graph;

public class GraphLayerEvent {

    GraphLayer layer;

    public GraphLayerEvent(GraphLayer l) {
        layer = l;
    }

    public int getDX() {
        return layer.get(GraphLayer.SET_DX);
    }

    public int getDY() {
        return layer.get(GraphLayer.SET_DY);
    }

    public int getHeight() {
        return layer.get(GraphLayer.SET_HEIGHT);
    }

    public int getWidth() {
        return layer.get(GraphLayer.SET_WIDTH);
    }
}
