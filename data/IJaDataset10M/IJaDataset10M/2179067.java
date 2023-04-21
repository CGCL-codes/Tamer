package org.mitre.rt.client.ui.application.listview;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.apache.log4j.Logger;

/**
 *
 * @author BAKERJ
 */
public class ListViewListCellRenderer extends DefaultListCellRenderer {

    private static Logger logger = Logger.getLogger(ListViewListCellRenderer.class.getPackage().getName());

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof IListView) {
            IListView item = (IListView) value;
            this.setText(item.getListName());
        }
        return this;
    }
}
