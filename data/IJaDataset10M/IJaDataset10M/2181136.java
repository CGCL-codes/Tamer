package dpgui.view;

import java.awt.Component;
import java.awt.SystemColor;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import mil.army.usace.ehlschlaeger.digitalpopulations.censusgen.filerelationship.Regions;
import mil.army.usace.ehlschlaeger.rgik.util.ObjectUtil;

/**
 * Custom display of Regions objects.  Intended for use in a JComboBox.
 *
 * @author William R. Zwicky
 */
public class RegionsListRenderer extends JLabel implements ListCellRenderer {

    public RegionsListRenderer() {
        setOpaque(true);
        setHorizontalAlignment(LEFT);
        setVerticalAlignment(CENTER);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof String) {
            setText((String) value);
        } else if (value == null) {
            setText("");
        } else {
            Regions rgn = (Regions) value;
            String buf;
            if (ObjectUtil.isBlank(rgn.id)) {
                String map = "-";
                if (!ObjectUtil.isBlank(rgn.map)) map = new File(rgn.map).getName();
                String tbl = "-";
                if (!ObjectUtil.isBlank(rgn.table)) tbl = new File(rgn.table).getName();
                buf = String.format("%s / %s", map, tbl);
            } else buf = rgn.id;
            setText(buf);
        }
        if (isSelected) setBackground(SystemColor.textHighlight); else setBackground(SystemColor.text);
        return this;
    }
}
