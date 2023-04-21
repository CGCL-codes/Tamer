package org.columba.addressbook.gui.list;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.columba.addressbook.gui.util.ToolTipFactory;
import org.columba.addressbook.model.ContactModelPartial;
import org.columba.addressbook.model.GroupModelPartial;
import org.columba.addressbook.model.BasicModelPartial;
import org.columba.addressbook.resourceloader.IconKeys;
import org.columba.core.resourceloader.ImageLoader;

@SuppressWarnings({ "serial", "serial" })
public class AddressbookListRenderer extends JLabel implements ListCellRenderer {

    ImageIcon image1 = ImageLoader.getSmallIcon(IconKeys.EDIT_CONTACT);

    ImageIcon image2 = ImageLoader.getSmallIcon(org.columba.core.resourceloader.IconKeys.USER);

    public AddressbookListRenderer() {
        setOpaque(true);
        setHorizontalAlignment(LEFT);
        setVerticalAlignment(CENTER);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        BasicModelPartial item = (BasicModelPartial) value;
        setText(item.getName());
        if (item.isContact()) {
            setIcon(image1);
            setToolTipText(ToolTipFactory.createToolTip((ContactModelPartial) item));
        } else {
            setIcon(image2);
            setToolTipText(ToolTipFactory.createToolTip((GroupModelPartial) item));
        }
        return this;
    }
}
