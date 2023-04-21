package org.logview4j.ui.split;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JSplitPane;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 * A JSplitPane with all of the borders removed
 * 
 * Concepts from com.jgoodies.uif_lite.component.UIFSplitPane
 */
public class MinimalJSplitPane extends JSplitPane {

    /**
   * Builds a minimal JSplitPane
   * @param orientation the orientation of the split
   * @param left the left component
   * @param right the right component
   */
    public MinimalJSplitPane(int orientation, Component left, Component right) {
        super(orientation, false, left, right);
    }

    /**
   * Builds a minimal JSplitPane
   * @param orientation the orientation of the split
   */
    public MinimalJSplitPane(int orientation) {
        this(orientation, null, null);
    }

    /**
   * Factory method for creating a clean split pane
   * 
   * @param orientation the orientation (from JSplitPane)
   * @param left the left component
   * @param right the right component
   * @param location the proportional location, between 0.0 and 1.0
   * @return the newly constructed CleanSplitPane
   */
    public static MinimalJSplitPane createCleanSplitPane(int orientation, Component left, Component right) {
        MinimalJSplitPane split = new MinimalJSplitPane(orientation, left, right);
        split.setBorder(BorderFactory.createEmptyBorder());
        split.setOneTouchExpandable(false);
        split.setDividerSize(5);
        return split;
    }

    /**
   * Updates the UI to remove the divider border if possible
   */
    public void updateUI() {
        super.updateUI();
        removeDividerBorder();
    }

    /**
   * Removes the divider border if this is a BasicSplitPaneUI
   */
    private void removeDividerBorder() {
        SplitPaneUI splitPaneUI = getUI();
        if (splitPaneUI instanceof BasicSplitPaneUI) {
            BasicSplitPaneUI basicUI = (BasicSplitPaneUI) splitPaneUI;
            basicUI.getDivider().setBorder(BorderFactory.createEmptyBorder());
        }
    }
}
