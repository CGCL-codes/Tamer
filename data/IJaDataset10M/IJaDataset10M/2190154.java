package org.hardtokenmgmt.admin.ui.panels.mantokens;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import org.hardtokenmgmt.common.Constants;
import org.hardtokenmgmt.core.ui.UIHelper;

/**
 * 
 * Used to render a JList with an icon and a display name.
 * 
 * @author Philip Vendil 21 Feb 2010
 *
 * @version $Id$
 */
public class UserStatusListCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 1L;

    public UserStatusListCellRenderer() {
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean selected, boolean cellHasFocus) {
        String userStatus = (String) value;
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, selected, cellHasFocus);
        label.setText(UIHelper.getText(Constants.userStatusToLanguageResourceMap.get(userStatus)));
        return label;
    }
}
