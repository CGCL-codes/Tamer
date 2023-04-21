package repast.model.heatbugs;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import repast.simphony.valueLayer.ValueLayer;
import repast.simphony.visualizationOGL2D.ValueLayerStyleOGL;

/**
 * @author Nick Collier
 */
public class HeatStyle implements ValueLayerStyleOGL {

    private ValueLayer layer;

    private Map<Integer, Color> colorMap = new HashMap<Integer, Color>();

    public HeatStyle() {
        for (int i = 0; i < 64; i++) {
            colorMap.put(i, new Color(i / 63.0f, 0f, 0f));
        }
    }

    public float getCellSize() {
        return 10;
    }

    public Color getColor(double... coordinates) {
        double val = layer.get(coordinates);
        Color color = colorMap.get((int) (val / 512));
        if (color == null) {
            color = Color.RED;
        }
        return color;
    }

    public void init(ValueLayer layer) {
        this.layer = layer;
    }
}
