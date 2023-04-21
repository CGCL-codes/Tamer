package org.zaproxy.zap.extension.history;

import java.awt.Component;
import java.sql.SQLException;
import javax.swing.JList;
import org.parosproxy.paros.extension.ExtensionPopupMenu;
import org.parosproxy.paros.extension.history.ExtensionHistory;
import org.parosproxy.paros.model.HistoryReference;
import org.parosproxy.paros.network.HttpMalformedHeaderException;
import org.parosproxy.paros.network.HttpMessage;

/**
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class PopupMenuNote extends ExtensionPopupMenu {

    private static final long serialVersionUID = 1L;

    private ExtensionHistory extension = null;

    /**
     * 
     */
    public PopupMenuNote() {
        super();
        initialize();
    }

    /**
     * @param label
     */
    public PopupMenuNote(String label) {
        super(label);
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setText("Note...");
        this.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                JList listLog = extension.getLogPanel().getListLog();
                HistoryReference ref = (HistoryReference) listLog.getSelectedValue();
                HttpMessage msg = null;
                try {
                    msg = ref.getHttpMessage();
                    extension.showNotesAddDialog(ref, msg.getNote());
                } catch (HttpMalformedHeaderException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public boolean isEnableForComponent(Component invoker) {
        if (invoker.getName() != null && invoker.getName().equals("ListLog")) {
            try {
                JList list = (JList) invoker;
                if (list.getSelectedIndex() >= 0) {
                    this.setEnabled(true);
                } else {
                    this.setEnabled(false);
                }
            } catch (Exception e) {
            }
            return true;
        }
        return false;
    }

    public void setExtension(ExtensionHistory extension) {
        this.extension = extension;
    }
}
