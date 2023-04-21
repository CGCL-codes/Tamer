package repast.simphony.visualization.editedStyle;

import java.awt.Color;
import repast.simphony.valueLayer.ValueLayer;
import repast.simphony.visualizationOGL2D.ValueLayerStyleOGL;

/**
 * ValueLayer style loaded from xml serialized style data.
 * 
 * @author Nick Collier
 */
public class EditedValueLayerStyleOGL2D implements ValueLayerStyleOGL {

    private ValueLayer valueLayer;

    private EditedValueLayerStyleData data;

    public EditedValueLayerStyleOGL2D(String userStyleFile) {
        data = EditedStyleUtils.getValueLayerStyle(userStyleFile);
        if (data == null) data = new DefaultEditedValueLayerStyleData2D();
    }

    @Override
    public Color getColor(double... coordinates) {
        return EditedStyleUtils.getValueLayerColor(data, valueLayer.get(coordinates));
    }

    @Override
    public float getCellSize() {
        return data.getCellSize();
    }

    @Override
    public void init(ValueLayer layer) {
        this.valueLayer = layer;
    }
}
