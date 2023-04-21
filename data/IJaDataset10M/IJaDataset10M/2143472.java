package com.ibm.celldt.managedbuilder.gnu.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import com.ibm.celldt.managedbuilder.gnu.core.preferences.GnuToolsSearcher;
import com.ibm.celldt.managedbuilder.gnu.core.preferences.GnuToolsProperties;
import com.ibm.celldt.managedbuilder.gnu.ui.GnuManagedBuilderUIPlugin;
import com.ibm.celldt.preferences.ui.AbstractBaseFieldEditorPreferencePage;
import com.ibm.celldt.preferences.ui.DirectoryFieldEditorWithSearch;

/**
 * @author laggarcia
 * @sice 3.0.0
 */
public class GnuToolsPreferencesPage extends AbstractBaseFieldEditorPreferencePage {

    protected static final String EMPTY_STRING = "";

    private DirectoryFieldEditorWithSearch gnuToolsDirectory;

    public GnuToolsPreferencesPage() {
        this(GRID);
    }

    /**
	 * @param style
	 */
    public GnuToolsPreferencesPage(int style) {
        this(EMPTY_STRING, style);
    }

    /**
	 * @param title
	 * @param style
	 */
    public GnuToolsPreferencesPage(String title, int style) {
        this(title, null, style);
    }

    public GnuToolsPreferencesPage(String title, ImageDescriptor image, int style) {
        super(title, image, style);
        IPreferenceStore store = GnuManagedBuilderUIPlugin.getDefault().getPreferenceStore();
        setPreferenceStore(store);
    }

    protected void createFieldEditors() {
        Composite fieldEditorParent = getFieldEditorParent();
        this.gnuToolsDirectory = new DirectoryFieldEditorWithSearch(GnuToolsProperties.gnuToolsPath, GnuToolsProperties.gnuToolsPathLabel, fieldEditorParent);
        this.gnuToolsDirectory.setEmptyStringAllowed(false);
        addField(this.gnuToolsDirectory);
    }

    /**
	 * Initializes all fields editors, searchers and property listeners.
	 */
    protected void initialize() {
        super.initialize();
        this.gnuToolsDirectory.addSearcher(new GnuToolsSearcher(this.gnuToolsDirectory, getFieldEditorParent()));
    }

    public void init(IWorkbench workbench) {
    }
}
