package net.sourceforge.javahexeditor.plugin.editors;

import net.sourceforge.javahexeditor.PreferencesManager;
import net.sourceforge.javahexeditor.plugin.BinaryEditorPlugin;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a preference page that is contributed to the Preferences dialog.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that belongs
 * to the main plug-in class. That way, preferences can be accessed directly via the preference store.
 */
public class PreferencesPage extends PreferencePage implements IWorkbenchPreferencePage {

    static final String preferenceFontData = "font.data";

    PreferencesManager preferences = null;

    /**
 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
 */
    protected Control createContents(Composite parent) {
        FontData fontData = BinaryEditorPlugin.getFontData();
        preferences = new PreferencesManager(fontData);
        return preferences.createPreferencesPart(parent);
    }

    public void init(IWorkbench workbench) {
    }

    /**
 * @see PreferencesPage#performDefaults()
 */
    protected void performDefaults() {
        super.performDefaults();
        preferences.setFontData(null);
    }

    /**
 * @see PreferencesPage#performOk()
 */
    public boolean performOk() {
        IPreferenceStore store = BinaryEditorPlugin.getDefault().getPreferenceStore();
        FontData fontData = preferences.getFontData();
        store.setValue(BinaryEditorPlugin.preferenceFontName, fontData.getName());
        store.setValue(BinaryEditorPlugin.preferenceFontStyle, fontData.getStyle());
        store.setValue(BinaryEditorPlugin.preferenceFontSize, fontData.getHeight());
        store.firePropertyChangeEvent(preferenceFontData, null, fontData);
        BinaryEditorPlugin.getDefault().savePluginPreferences();
        return true;
    }
}
