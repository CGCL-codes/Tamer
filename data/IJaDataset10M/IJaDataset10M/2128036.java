package org.ocd.dcf.property.editor;

import java.awt.Rectangle;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RectangleEditor extends EditorSupport {

    private JTextField xTF;

    private JTextField yTF;

    private JTextField widthTF;

    private JTextField heightTF;

    public RectangleEditor() {
        xTF = new JTextField();
        xTF.setDocument(new NumberDocument());
        yTF = new JTextField();
        yTF.setDocument(new NumberDocument());
        widthTF = new JTextField();
        widthTF.setDocument(new NumberDocument());
        heightTF = new JTextField();
        heightTF.setDocument(new NumberDocument());
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel("X: "));
        panel.add(xTF);
        panel.add(new JLabel("Y: "));
        panel.add(yTF);
        panel.add(new JLabel("Width: "));
        panel.add(widthTF);
        panel.add(new JLabel("Height: "));
        panel.add(heightTF);
    }

    public void setValue(Object value) {
        super.setValue(value);
        Rectangle rect = (Rectangle) value;
        xTF.setText(Integer.toString(rect.x));
        yTF.setText(Integer.toString(rect.y));
        widthTF.setText(Integer.toString(rect.width));
        heightTF.setText(Integer.toString(rect.height));
    }

    public Object getValue() {
        int x = Integer.parseInt(xTF.getText());
        int y = Integer.parseInt(yTF.getText());
        int width = Integer.parseInt(widthTF.getText());
        int height = Integer.parseInt(heightTF.getText());
        return new Rectangle(x, y, width, height);
    }
}
