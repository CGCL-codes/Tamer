package graphlab.ui.components.GPropertyEditor.renderers;

import graphlab.ui.components.GPropertyEditor.GBasicCellRenderer;
import javax.swing.*;
import java.awt.*;

/**
 * User: root
 */
public class GStringRenderer implements GBasicCellRenderer {

    public Component getRendererComponent(Object value) {
        return new JLabel(value + "");
    }
}
