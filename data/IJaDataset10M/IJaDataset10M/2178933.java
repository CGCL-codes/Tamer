package ch.intertec.storybook.view.combo;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import ch.intertec.storybook.model.hbn.entity.Location;

/**
 * @author martin
 * 
 */
@SuppressWarnings("serial")
public class LocationListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        try {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (!(value instanceof Location)) {
                String str = value.toString();
                if (str.isEmpty()) {
                    label.setText(" ");
                    return label;
                }
                label.setText(value.toString());
                return label;
            }
            Location location = (Location) value;
            label.setIcon(location.getIcon());
            label.setText(location.getFullName(true));
            return label;
        } catch (Exception e) {
            return new JLabel("");
        }
    }
}
