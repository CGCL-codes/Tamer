package org.ximtec.igesture.tool.gesturevisualisation;

import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.ximtec.igesture.core.Gesture;

/**
 * Comment
 * @version 1.0 16.12.2008
 * @author Ueli Kurmann
 */
public class NotSupportedGesturePanel implements GesturePanel {

    Gesture<?> gesture;

    @Override
    public void init(Gesture<?> gesture) {
        this.gesture = gesture;
    }

    @Override
    public JPanel getPanel(Dimension dimension) {
        JLabel label = new JLabel(gesture.getName());
        JPanel panel = new JPanel();
        panel.setPreferredSize(dimension);
        panel.add(label);
        return panel;
    }
}
