package ingenias.editor.cell;

import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import ingenias.editor.entities.*;

public class UserObjectPanel extends javax.swing.JPanel {

    Entity uo = null;

    public UserObjectPanel(Entity uo) {
        super();
        this.setUserObject(uo);
        this.setBackground(Color.WHITE);
    }

    public void setUserObject(Entity uo) {
        this.uo = uo;
    }

    public Entity getUserObject() {
        return this.uo;
    }

    public void paint(Graphics g) {
        g.setXORMode(Color.WHITE);
        super.paint(g);
    }
}
