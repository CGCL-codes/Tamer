package gvs.repository.server.configuration.preferences;

import gvs.repository.server.configuration.Activator;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>,
 * we can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */
public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    /**
     * 
     */
    public PreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("A demonstration of a preference page implementation");
    }

    /**
     * Creates the field editors. Field editors are abstractions of the common
     * GUI blocks needed to manipulate various types of preferences. Each field
     * editor knows how to save and restore itself.
     */
    public void createFieldEditors() {
        int columnSpan = 3;
        Composite parent = getFieldEditorParent();
        addSpacer(parent, columnSpan);
        Composite jxtaComposite = createJXTAGroupComposite(parent, columnSpan);
        addField(new StringFieldEditor(PreferenceConstants.P_JXTA_SERVER_NAME, "Server Name:", jxtaComposite));
        addField(new PasswordFieldEditor(PreferenceConstants.P_JXTA_SERVER_PASSWORD, "Server password:", jxtaComposite));
    }

    public void init(IWorkbench workbench) {
    }

    /**
     * @param parent
     * @param columnSpan
     * @return
     */
    private Composite createJXTAGroupComposite(Composite parent, int columnSpan) {
        Group jxtaGroup = new Group(parent, SWT.NONE);
        jxtaGroup.setText("JXTA Settings");
        jxtaGroup.setLayout(new GridLayout(1, true));
        GridData jxtaGD = new GridData(GridData.FILL_HORIZONTAL);
        jxtaGD.horizontalSpan = columnSpan;
        jxtaGD.minimumWidth = 500;
        jxtaGroup.setLayoutData(jxtaGD);
        Composite jxtaComposite = new Composite(jxtaGroup, SWT.NONE);
        jxtaComposite.setLayout(new GridLayout(3, true));
        jxtaComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        return jxtaComposite;
    }

    /**
     * Add some horizontal space to the dialog.
     * 
     * @param parent
     * @param columnSpan
     */
    private void addSpacer(Composite parent, int columnSpan) {
        Label l = new Label(parent, SWT.NONE);
        l.setLayoutData(getColumnSpanGridData(columnSpan));
    }

    /**
     * Return a new GridData object with the specified column span.
     * 
     * @param columnSpan
     * @return
     */
    private GridData getColumnSpanGridData(int columnSpan) {
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = columnSpan;
        return gd;
    }
}
