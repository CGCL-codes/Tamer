package net.sf.logshark.simple;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.JTabbedPane;

class ActivatePreviousTabAction implements Action {

    private JTabbedPane m_tabbedPane;

    public void addPropertyChangeListener(PropertyChangeListener pListener) {
    }

    public Object getValue(String pKey) {
        return null;
    }

    public boolean isEnabled() {
        return true;
    }

    public void putValue(String pKey, Object pValue) {
    }

    public void removePropertyChangeListener(PropertyChangeListener pListener) {
    }

    public void setEnabled(boolean pEnabled) {
    }

    public void actionPerformed(ActionEvent event) {
        if (m_tabbedPane.getSelectedIndex() != -1 && m_tabbedPane.getComponentCount() > 1) {
            int newSelectedIndex = m_tabbedPane.getSelectedIndex() - 1;
            if (newSelectedIndex == -1) {
                newSelectedIndex += m_tabbedPane.getComponentCount();
            }
            m_tabbedPane.setSelectedIndex(newSelectedIndex);
        }
    }

    ActivatePreviousTabAction(JTabbedPane tabbedPane) {
        super();
        m_tabbedPane = tabbedPane;
    }
}
