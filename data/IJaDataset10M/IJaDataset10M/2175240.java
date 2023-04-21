package au.edu.qut.yawl.editor.actions.view;

import java.awt.event.ActionEvent;
import java.util.prefs.Preferences;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.ToolTipManager;
import au.edu.qut.yawl.editor.YAWLEditor;
import au.edu.qut.yawl.editor.actions.YAWLBaseAction;

public class ToolTipToggleAction extends YAWLBaseAction {

    /**
   * 
   */
    private static final long serialVersionUID = 1L;

    private boolean selected;

    private Preferences prefs = Preferences.userNodeForPackage(YAWLEditor.class);

    {
        putValue(Action.SHORT_DESCRIPTION, " Toggle showing tooltips. ");
        putValue(Action.NAME, "Show Tooltips");
        putValue(Action.LONG_DESCRIPTION, "Validate currently selected net.");
        putValue(Action.MNEMONIC_KEY, new Integer(java.awt.event.KeyEvent.VK_T));
    }

    public ToolTipToggleAction() {
        selected = prefs.getBoolean("showToolTips", true);
        ToolTipManager.sharedInstance().setEnabled(selected);
    }

    public void actionPerformed(ActionEvent event) {
        selected = !selected;
        ToolTipManager.sharedInstance().setEnabled(selected);
        JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) event.getSource();
        menuItem.setSelected(selected);
        prefs.putBoolean("showToolTips", selected);
    }

    public boolean isSelected() {
        return selected;
    }
}
