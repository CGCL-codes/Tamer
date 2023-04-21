package ingenias.editor.cell;

import java.awt.*;
import javax.swing.*;
import java.awt.Graphics;
import java.util.Map;
import java.util.Hashtable;
import org.jgraph.graph.*;
import org.jgraph.*;
import java.awt.geom.Point2D;

public class GTCreatesView extends NAryView {

    static GTCreatesRenderer renderer1 = new GTCreatesRenderer();

    public GTCreatesView(Object cell) {
        super(cell);
    }

    public static Dimension getSize() {
        return new Dimension(80, 120);
    }

    public CellViewRenderer getRenderer() {
        return renderer1;
    }

    public java.awt.Component getRendererComponent(JGraph jg, boolean b1, boolean b2, boolean b3) {
        CellViewRenderer renderer = null;
        try {
            ingenias.editor.entities.GTCreates ent = (ingenias.editor.entities.GTCreates) ((DefaultGraphCell) this.getCell()).getUserObject();
            this.renderer1.setEntity(ent);
            JPanel uop = (JPanel) this.renderer1.getRendererComponent(null, null, false, false, false);
            if (ent.getPrefs().getView() == ingenias.editor.entities.ViewPreferences.ViewType.LABEL) {
                NAryEdge naryedge = (NAryEdge) this.getCell();
                DefaultEdge[] edge = naryedge.getRepresentation();
                AttributeMap am = edge[0].getAttributes();
                GraphConstants.setLabelAlongEdge(am, true);
                GraphConstants.setExtraLabels(am, new Object[] { ent.getLabel() });
                GraphConstants.setExtraLabelPositions(am, new Point2D[] { new Point2D.Double(GraphConstants.PERMILLE * 7 / 8, -20) });
                edge[0].setAttributes(am);
            }
            return (Component) uop;
        } catch (Exception e) {
            e.printStackTrace();
            ingenias.editor.Log.getInstance().log("WARNING!!!" + e.getMessage());
        }
        return super.getRendererComponent(jg, b1, b2, b3);
    }
}
