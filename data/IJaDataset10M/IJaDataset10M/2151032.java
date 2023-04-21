package com.everis.paiche.preferences;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import com.everis.paiche.Activator;
import com.everis.paiche.util.Constant;

public class SunJavaWebServer70Preference extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private final String LABEL_SERVICES = "Services";

    private final String LABEL_DIRECTORY = "&WebServer 7.0 home directory:";

    public static final String ENABLE_SERVICE = Constant.PROPERTIES_PACKAGE + "WEBSERVER70.ENABLE_SERVICE";

    public static final String DIRECTORY_SERVICE = Constant.PROPERTIES_PACKAGE + "WEBSERVER70.DIRECTORY_SERVICE";

    private IWorkbench workbench;

    public SunJavaWebServer70Preference() {
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    public void init(IWorkbench workbench) {
        this.setWorkbench(workbench);
    }

    public void setWorkbench(IWorkbench workbench) {
        this.workbench = workbench;
    }

    public IWorkbench getWorkbench() {
        return workbench;
    }

    protected void createFieldEditors() {
        addField(new RadioGroupFieldEditor(ENABLE_SERVICE, LABEL_SERVICES, 1, new String[][] { { "Enable", Constant.SERVICE_SERVER_WEB_ENABLE }, { "Disable", Constant.SERVICE_SERVER_WEB_DISABLE } }, getFieldEditorParent()));
        addField(new DirectoryFieldEditor(DIRECTORY_SERVICE, LABEL_DIRECTORY, getFieldEditorParent()));
    }

    @Override
    public boolean performOk() {
        return super.performOk();
    }
}
