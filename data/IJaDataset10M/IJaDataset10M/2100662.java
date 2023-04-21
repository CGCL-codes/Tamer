package org.mitre.rt.client.ui.recommendations;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.apache.log4j.Logger;
import org.mitre.rt.rtclient.IntIdValuePairType;

/**
 *
 * @author BAKERJ
 */
public class IntIdValuePairTypeRenderer extends DefaultListCellRenderer {

    private static Logger logger = Logger.getLogger(IntIdValuePairTypeRenderer.class.getPackage().getName());

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof IntIdValuePairType) {
            IntIdValuePairType item = (IntIdValuePairType) value;
            this.setText(item.getStringValue());
        } else {
            if (value == null) {
                this.setText("< Not Selected >");
            }
        }
        return this;
    }
}
