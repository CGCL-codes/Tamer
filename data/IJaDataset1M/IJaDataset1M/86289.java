package co.edu.unal.ungrid.services.client.applet.view.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;

public abstract class SuperAction extends AbstractAction {

    public static final long serialVersionUID = 1L;

    public void actionPerformed(ActionEvent ae) {
    }

    public JMenuItem getMenuItem() {
        if (m_menuItem == null) {
            m_menuItem = new JMenuItem(this);
        }
        return m_menuItem;
    }

    public JCheckBoxMenuItem getCheckBoxMenuItem() {
        if (m_menuItem == null) {
            m_menuItem = new JCheckBoxMenuItem(this);
        }
        return (JCheckBoxMenuItem) m_menuItem;
    }

    public void setChecked(boolean b) {
        if (m_menuItem instanceof JCheckBoxMenuItem) {
            ((JCheckBoxMenuItem) m_menuItem).setSelected(b);
        }
    }

    protected JMenuItem m_menuItem;
}
