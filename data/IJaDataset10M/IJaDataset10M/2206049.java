package freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mindmaps.freemind.modes.mindmapmode;

import java.awt.Color;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mindmaps.freemind.main.FreeMindMain;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mindmaps.freemind.main.Tools;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mindmaps.freemind.main.XMLElement;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mindmaps.freemind.modes.EdgeAdapter;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mindmaps.freemind.modes.MindMapNode;

public class MindMapEdgeModel extends EdgeAdapter {

    public MindMapEdgeModel(MindMapNode node, FreeMindMain frame) {
        super(node, frame);
    }

    public XMLElement save() {
        if (style != null || color != null || width != WIDTH_PARENT) {
            XMLElement edge = new XMLElement();
            edge.setName("edge");
            if (style != null) {
                edge.setAttribute("style", style);
            }
            if (color != null) {
                edge.setAttribute("color", Tools.colorToXml(color));
            }
            if (width != WIDTH_PARENT) {
                if (width == WIDTH_THIN) edge.setAttribute("width", "thin"); else edge.setAttribute("width", Integer.toString(width));
            }
            return edge;
        }
        return null;
    }

    public void setColor(Color color) {
        super.setColor(color);
    }

    public void setWidth(int width) {
        super.setWidth(width);
    }

    public void setStyle(String style) {
        super.setStyle(style);
    }
}
