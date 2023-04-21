package net.sf.wmutils.eclipse.mws.prefs;

import net.sf.wmutils.eclipse.mws.MWSPlugin;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import static net.sf.wmutils.eclipse.mws.MWSPluginResources.*;
import static net.sf.wmutils.eclipse.mws.prefs.PreferenceKeys.*;

/**
 * The "Integration Server" preferences page.
 */
public class MWSPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    /**
	 * Creates a new instance.
	 */
    public MWSPage() {
        super(GRID);
    }

    @Override
    protected void createFieldEditors() {
        final Composite parent = getFieldEditorParent();
        addField(new DirectoryFieldEditor(MWS_PREF_MWS_HOME_KEY, PREF_PAGE_MWSHOME_LABEL, parent));
        addField(new StringFieldEditor(MWS_PREF_SERVER_KEY, PREF_PAGE_MWS_SERVER_LABEL, parent));
        addField(new StringFieldEditor(MWS_PREF_NODE_KEY, PREF_PAGE_MWS_NODE_LABEL, parent));
        addField(new StringFieldEditor(MWS_PREF_ENCODING_KEY, PREF_PAGE_MWS_ENCODING_LABEL, parent));
    }

    public void init(IWorkbench pWorkbench) {
        setPreferenceStore(MWSPlugin.getDefault().getPreferenceStore());
    }
}
