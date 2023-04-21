package net.sourceforge.originalsynth.tab.listener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sourceforge.originalsynth.global.SynProperties;
import net.sourceforge.originalsynth.tab.SynTabbedPane;
import net.sourceforge.originalsynth.tab.Tab;

/**
 * Listens to the tabs. Helps setup the popup menu on the tabs. Keeps track of
 * the current tab.
 * 
 * 
 */
public class TabListener implements ChangeListener {

    /**
     * 
     * Empty Constructor
     */
    public TabListener() {
    }

    /**
     * 
     * @return The currently selected tab. Identified by its index.
     */
    public int getSelectedTab() {
        return SynProperties.getCurTabbedPane().getSelectedIndex();
    }

    public void stateChanged(ChangeEvent e) {
        SynTabbedPane curTabbedPane = SynProperties.getCurTabbedPane();
        if (curTabbedPane != null) {
            Tab tab = (Tab) (curTabbedPane.getSelectedComponent());
            if (tab != null) {
                tab.getCanvas().setToCurTabContent();
            } else {
                curTabbedPane.setSelectedIndex(-1);
            }
        }
    }
}
