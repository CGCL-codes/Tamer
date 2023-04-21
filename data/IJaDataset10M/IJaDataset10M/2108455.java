package org.ungoverned.radical.action;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import org.ungoverned.radical.InstanceDescriptor;
import org.ungoverned.radical.ListenerDescriptor;
import org.ungoverned.radical.ui.dialog.ListenerDialog;
import org.ungoverned.radical.util.GuiUtility;

public class ListenerAction extends AbstractAction {

    private InstanceDescriptor m_id = null;

    public ListenerAction(InstanceDescriptor id) {
        this("Event listeners...", id);
    }

    public ListenerAction(String name, InstanceDescriptor id) {
        super(name);
        m_id = id;
    }

    public void actionPerformed(ActionEvent event) {
        ListenerDialog dlg = new ListenerDialog(null, m_id);
        GuiUtility.positionWindow(dlg, GuiUtility.CENTER);
        dlg.show();
        if (dlg.isOkay()) {
            while (m_id.getListenerCount() > 0) {
                m_id.removeListener(m_id.getListener(0));
            }
            List list = dlg.getSelectedDescriptors();
            for (int i = 0; i < list.size(); i++) {
                ListenerDescriptor ld = (ListenerDescriptor) list.get(i);
                m_id.addListener(ld);
            }
        }
    }
}
