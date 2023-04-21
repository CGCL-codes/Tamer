package org.dyno.visual.swing.widgets.delegate;

import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;

public class JCheckBoxMouseDelegate extends WidgetMouseInputAdapter {

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        if (e.isConsumed()) return;
        if (e.getButton() == MouseEvent.BUTTON1) {
            JCheckBox checkBox = (JCheckBox) adaptable.getWidget();
            boolean selected = checkBox.isSelected();
            checkBox.setSelected(!selected);
            adaptable.setDirty(true);
            adaptable.repaintDesigner();
        }
        e.setSource(null);
    }
}
