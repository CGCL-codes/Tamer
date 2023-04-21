package org.jlense.uiworks.preference;

import java.util.Map;
import java.util.TreeMap;
import javax.swing.JComboBox;
import org.jlense.uiworks.forms.SelectFieldEditor;
import org.jlense.uiworks.internal.WorkbenchPlugin;
import org.jlense.uiworks.workbench.IWorkbenchPreferenceConstants;

/**
 * A field editor for selecting the toolbar button display options.
 * 
 * @author  ted stockwell [emorning@sourceforge.net]
 */
public class ToolBarButtonDisplayOptionsFieldEditor extends SelectFieldEditor {

    private static WorkbenchPlugin PLUGIN = WorkbenchPlugin.getDefault();

    private static IPreferenceStore _preferenceStore = null;

    private static Map DISPLAY_VALUES = null;

    /**
     * @param  The id of the default view.  If the perference value that 
     * corresponds to the Outlook workbench start view is not currently set 
     * then the view associated with the given id will be the default. 
     */
    public ToolBarButtonDisplayOptionsFieldEditor() {
        super(IWorkbenchPreferenceConstants.PREFERENCE_TOOLBAR_TEXT, WorkbenchPlugin.getDefault().bind("Label.ToolBarTextOptions") + ':', getToolbarTextOptionsDisplayMap(), IWorkbenchPreferenceConstants.PREFERENCE_TOOLBAR_TEXT_DEFAULT);
    }

    /**
     * Returns a map where the keys are the names of outlook shortcuts and the 
     * map values are the shortcut Ids.
     */
    public static Map getToolbarTextOptionsDisplayMap() {
        if (DISPLAY_VALUES == null) {
            TreeMap map = new TreeMap();
            map.put(PLUGIN.bind(IWorkbenchPreferenceConstants.TOOLBAR_TEXT_NONE), IWorkbenchPreferenceConstants.TOOLBAR_TEXT_NONE);
            map.put(PLUGIN.bind(IWorkbenchPreferenceConstants.TOOLBAR_TEXT_SELECTED_RIGHT), IWorkbenchPreferenceConstants.TOOLBAR_TEXT_SELECTED_RIGHT);
            map.put(PLUGIN.bind(IWorkbenchPreferenceConstants.TOOLBAR_TEXT_ALL_BOTTOM), IWorkbenchPreferenceConstants.TOOLBAR_TEXT_ALL_BOTTOM);
            DISPLAY_VALUES = map;
        }
        return DISPLAY_VALUES;
    }

    protected void doStore() {
        JComboBox combo = (JComboBox) getComponents()[0][1];
        String display = (String) combo.getSelectedItem();
        String value = (String) getDisplayValueMap().get(display);
        getDialogSettings().put(getDialogSettingName(), value);
    }
}
