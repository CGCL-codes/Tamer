package br.com.nix.editors;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import br.com.nix.framework.ImgUtils;
import br.com.nix.framework.SimpleWindow;
import com.bric.swing.ColorPicker;
import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.swing.ComponentFactory;
import com.l2fprod.common.swing.PercentLayout;
import com.l2fprod.common.swing.renderer.ColorCellRenderer;

/**
 * ColorPropertyEditor. <br>
 *  
 */
public class ColorPropertyEditor extends AbstractPropertyEditor {

    private ColorCellRenderer label;

    private JButton button;

    private Color color;

    public ColorPropertyEditor() {
        editor = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL, 0));
        ((JPanel) editor).add("*", label = new ColorCellRenderer());
        label.setOpaque(false);
        ((JPanel) editor).add(button = ComponentFactory.Helper.getFactory().createMiniButton());
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectColor();
            }
        });
        ((JPanel) editor).add(button = ComponentFactory.Helper.getFactory().createMiniButton());
        button.setText("X");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectNull();
            }
        });
        ((JPanel) editor).setOpaque(false);
    }

    public Object getValue() {
        return color;
    }

    public void setValue(Object value) {
        color = (Color) value;
        label.setValue(color);
    }

    protected void selectColor() {
        SimpleWindow win = new SimpleWindow("f:p:g", "f:p:g");
        win.setTitle("Select Color");
        ColorPicker cp = new ColorPicker(true, true);
        if (color != null) cp.setColor(color);
        win.addWidget("color", cp, 1, 1);
        win.setIcon(ImgUtils.getIcon(ImgUtils.IMG_EDITAR));
        HashMap<String, Object> retorno = win.show();
        if (retorno != null) {
            Color selectedColor = (Color) retorno.get("color");
            Color oldColor = color;
            Color newColor = selectedColor;
            label.setValue(newColor);
            color = newColor;
            firePropertyChange(oldColor, newColor);
        }
    }

    protected void selectNull() {
        Color oldColor = color;
        label.setValue(null);
        color = null;
        firePropertyChange(oldColor, null);
    }

    public static class AsInt extends ColorPropertyEditor {

        public void setValue(Object arg0) {
            if (arg0 instanceof Integer) {
                super.setValue(new Color(((Integer) arg0).intValue()));
            } else {
                super.setValue(arg0);
            }
        }

        public Object getValue() {
            Object value = super.getValue();
            if (value == null) {
                return null;
            } else {
                return new Integer(((Color) value).getRGB());
            }
        }

        protected void firePropertyChange(Object oldValue, Object newValue) {
            if (oldValue instanceof Color) {
                oldValue = new Integer(((Color) oldValue).getRGB());
            }
            if (newValue instanceof Color) {
                newValue = new Integer(((Color) newValue).getRGB());
            }
            super.firePropertyChange(oldValue, newValue);
        }
    }
}
