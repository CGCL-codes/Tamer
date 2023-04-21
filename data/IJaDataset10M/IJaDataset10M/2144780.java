package ingenias.editor.cell;

import java.awt.*;
import javax.swing.*;
import java.awt.Graphics;
import java.util.Map;
import org.jgraph.graph.*;
import org.jgraph.*;
import ingenias.editor.entities.*;

public class ActivityFinalView extends VertexView {

    public static ActivityFinalRenderer renderer = new ActivityFinalRenderer();

    public ActivityFinalView(Object cell) {
        super(cell);
    }

    public CellViewRenderer getRenderer() {
        try {
            return this.renderer;
        } catch (Exception e) {
            e.printStackTrace();
            ingenias.editor.Log.getInstance().log(e.getMessage());
        }
        return renderer;
    }

    public java.awt.Component getRendererComponent(JGraph jg, boolean b1, boolean b2, boolean b3) {
        CellViewRenderer renderer = null;
        try {
            ingenias.editor.entities.ActivityFinal ent = (ingenias.editor.entities.ActivityFinal) ((DefaultGraphCell) this.getCell()).getUserObject();
            this.renderer.setEntity(ent);
            JPanel uop = (JPanel) this.renderer.getRendererComponent(null, this, false, false, false);
            return (Component) uop;
        } catch (Exception e) {
            e.printStackTrace();
            ingenias.editor.Log.getInstance().log("WARNING!!!" + e.getMessage());
        }
        return super.getRendererComponent(jg, b1, b2, b3);
    }

    public static Dimension getSize() {
        return renderer.getSize();
    }

    public static Dimension getSize(ActivityFinal ent) {
        renderer.setEntity(ent);
        return renderer.getSize();
    }
}
