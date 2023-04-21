package fw4ex_client.preferences;

import java.util.ResourceBundle;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import fw4ex_client.Activator;

public class ReportPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    protected ResourceBundle bundle;

    private StringFieldEditor txt_Request_Delay;

    private StringFieldEditor txt_Max_Tries;

    private StringFieldEditor txt_Min_Delay;

    private BooleanFieldEditor box_autoCheck;

    protected IPreferenceStore store;

    public ReportPage() {
        bundle = Activator.getDefault().getResourceBundle();
        store = Activator.getDefault().getPreferenceStore();
        setPreferenceStore(store);
    }

    @Override
    protected void createFieldEditors() {
        txt_Request_Delay = new StringFieldEditor("request_delay", bundle.getString("Request_Delay"), getFieldEditorParent());
        txt_Max_Tries = new StringFieldEditor("max_tries", bundle.getString("Max_Tries"), getFieldEditorParent());
        txt_Min_Delay = new StringFieldEditor("min_delay", bundle.getString("First_Delay"), getFieldEditorParent());
        box_autoCheck = new BooleanFieldEditor("autocheck", bundle.getString("Report_Auto_Check"), getFieldEditorParent());
        txt_Request_Delay.setPreferenceStore(store);
        txt_Max_Tries.setPreferenceStore(store);
        txt_Min_Delay.setPreferenceStore(store);
        box_autoCheck.setPreferenceStore(store);
        txt_Request_Delay.setPreferenceName("report_request_delay");
        txt_Max_Tries.setPreferenceName("report_max_tries");
        txt_Min_Delay.setPreferenceName("report_min_delay");
        box_autoCheck.setPreferenceName("report_auto_check");
        addField(txt_Request_Delay);
        addField(txt_Max_Tries);
        addField(txt_Min_Delay);
        addField(box_autoCheck);
    }

    @Override
    public void init(IWorkbench workbench) {
    }
}
