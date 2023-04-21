package ingenias.editor.cell;

import java.awt.*;
import javax.swing.*;
import java.awt.Graphics;
import java.util.Map;
import org.jgraph.graph.*;
import org.jgraph.*;
import ingenias.editor.entities.*;

public class RuntimeFactView extends VertexView {

    public static RuntimeFactRenderer renderer = new RuntimeFactRenderer();

    public RuntimeFactView(Object cell) {
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
            ingenias.editor.entities.RuntimeFact ent = (ingenias.editor.entities.RuntimeFact) ((DefaultGraphCell) this.getCell()).getUserObject();
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

    public static Dimension getSize(RuntimeFact ent) {
        renderer.setEntity(ent);
        return renderer.getSize();
    }
}
