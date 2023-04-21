package org.openXpertya.grid;

import java.awt.Component;
import javax.swing.JTabbedPane;
import org.compiere.swing.CTabbedPane;
import org.openXpertya.apps.APanel;
import org.openXpertya.util.Language;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class VTabbedPane extends CTabbedPane {

    /**
     * Constructor de la clase ...
     *
     *
     * @param isWorkbench
     */
    public VTabbedPane(boolean isWorkbench) {
        super();
        setWorkbench(isWorkbench);
        setFocusable(false);
    }

    /** Descripción de Campos */
    private boolean m_workbenchTab;

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String toString() {
        return (m_workbenchTab ? "WorkbenchTab" : "WindowTab") + " - selected " + getSelectedIndex() + " of " + getTabCount();
    }

    /**
     * Descripción de Método
     *
     *
     * @param isWorkbench
     */
    public void setWorkbench(boolean isWorkbench) {
        m_workbenchTab = isWorkbench;
        if (m_workbenchTab) {
            super.setTabPlacement(JTabbedPane.BOTTOM);
        } else {
            super.setTabPlacement(Language.getLoginLanguage().isLeftToRight() ? JTabbedPane.LEFT : JTabbedPane.RIGHT);
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public boolean isWorkbench() {
        return m_workbenchTab;
    }

    /**
     * Descripción de Método
     *
     *
     * @param notUsed
     */
    public void setTabPlacement(int notUsed) {
        new java.lang.IllegalAccessError("Do not use VTabbedPane.setTabPlacement directly");
    }

    /**
     * Descripción de Método
     *
     *
     * @param aPanel
     */
    public void dispose(APanel aPanel) {
        Component[] comp = getComponents();
        for (int i = 0; i < comp.length; i++) {
            if (comp[i] instanceof VTabbedPane) {
                VTabbedPane tp = (VTabbedPane) comp[i];
                tp.removeChangeListener(aPanel);
                tp.dispose(aPanel);
            } else if (comp[i] instanceof GridController) {
                GridController gc = (GridController) comp[i];
                gc.addDataStatusListener(aPanel);
                gc.dispose();
            }
        }
        removeAll();
    }
}
