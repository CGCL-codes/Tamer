package softfab.preferences;

import java.io.IOException;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class SoftFabPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    PreferenceStore preferenceStore;

    Text use_file, input_file, input_extention, sf_root, sf_tag_req, delim, use_proxy, proxy_server, proxy_port;

    protected Control createContents(Composite parent) {
        GridLayout gl = new GridLayout(2, true);
        gl.horizontalSpacing = 0;
        parent.setLayout(gl);
        GridData gd;
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.verticalIndent = 20;
        gd.widthHint = 220;
        gd.horizontalAlignment = GridData.CENTER;
        gd.verticalAlignment = GridData.CENTER;
        Label l1 = new Label(parent, SWT.CENTER);
        l1.setText("Use file");
        l1.setLayoutData(gd);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.widthHint = 220;
        gd.horizontalAlignment = GridData.CENTER;
        gd.verticalAlignment = GridData.CENTER;
        use_file = new Text(parent, SWT.BORDER);
        use_file.setText(preferenceStore.getString("use_file"));
        use_file.setLayoutData(gd);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.verticalIndent = 20;
        gd.widthHint = 220;
        gd.horizontalAlignment = GridData.CENTER;
        gd.verticalAlignment = GridData.CENTER;
        Label l2 = new Label(parent, SWT.CENTER);
        l2.setText("Input file");
        l2.setLayoutData(gd);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.widthHint = 220;
        gd.horizontalAlignment = GridData.CENTER;
        gd.verticalAlignment = GridData.CENTER;
        input_file = new Text(parent, SWT.BORDER);
        input_file.setText(preferenceStore.getString("input_file"));
        input_file.setLayoutData(gd);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.verticalIndent = 20;
        gd.widthHint = 220;
        gd.horizontalAlignment = GridData.CENTER;
        gd.verticalAlignment = GridData.CENTER;
        Label l3 = new Label(parent, SWT.CENTER);
        l3.setText("Input extension");
        l3.setLayoutData(gd);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.widthHint = 220;
        gd.horizontalAlignment = GridData.CENTER;
        gd.verticalAlignment = GridData.CENTER;
        input_extention = new Text(parent, SWT.BORDER);
        input_extention.setText(preferenceStore.getString("input_extention"));
        input_extention.setLayoutData(gd);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.verticalIndent = 20;
        gd.widthHint = 220;
        gd.horizontalAlignment = GridData.CENTER;
        gd.verticalAlignment = GridData.CENTER;
        Label l4 = new Label(parent, SWT.CENTER);
        l4.setText("SoftFAB root");
        l4.setLayoutData(gd);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.widthHint = 220;
        gd.horizontalAlignment = GridData.CENTER;
        gd.verticalAlignment = GridData.CENTER;
        sf_root = new Text(parent, SWT.BORDER);
        sf_root.setText(preferenceStore.getString("sf_root"));
        sf_root.setLayoutData(gd);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.verticalIndent = 20;
        gd.widthHint = 220;
        gd.horizontalAlignment = GridData.CENTER;
        gd.verticalAlignment = GridData.CENTER;
        Label l5 = new Label(parent, SWT.CENTER);
        l5.setText("SoftFAB req tag");
        l5.setLayoutData(gd);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.widthHint = 220;
        gd.horizontalAlignment = GridData.CENTER;
        gd.verticalAlignment = GridData.CENTER;
        sf_tag_req = new Text(parent, SWT.BORDER);
        sf_tag_req.setText(preferenceStore.getString("sf_tag_req"));
        sf_tag_req.setLayoutData(gd);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.verticalIndent = 20;
        gd.widthHint = 220;
        gd.horizontalAlignment = GridData.CENTER;
        gd.verticalAlignment = GridData.CENTER;
        Label l6 = new Label(parent, SWT.CENTER);
        l6.setText("delim");
        l6.setLayoutData(gd);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.widthHint = 220;
        gd.horizontalAlignment = GridData.CENTER;
        gd.verticalAlignment = GridData.CENTER;
        delim = new Text(parent, SWT.BORDER);
        delim.setText(preferenceStore.getString("delim"));
        delim.setLayoutData(gd);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.verticalIndent = 20;
        gd.widthHint = 220;
        gd.horizontalAlignment = GridData.CENTER;
        gd.verticalAlignment = GridData.CENTER;
        Label l7 = new Label(parent, SWT.CENTER);
        l7.setText("Use proxy");
        l7.setLayoutData(gd);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.widthHint = 220;
        gd.horizontalAlignment = GridData.CENTER;
        gd.verticalAlignment = GridData.CENTER;
        use_proxy = new Text(parent, SWT.BORDER);
        use_proxy.setText(preferenceStore.getString("use_proxy"));
        use_proxy.setLayoutData(gd);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.verticalIndent = 20;
        gd.widthHint = 220;
        gd.horizontalAlignment = GridData.CENTER;
        gd.verticalAlignment = GridData.CENTER;
        Label l8 = new Label(parent, SWT.CENTER);
        l8.setText("Proxy server");
        l8.setLayoutData(gd);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.widthHint = 220;
        gd.horizontalAlignment = GridData.CENTER;
        gd.verticalAlignment = GridData.CENTER;
        proxy_server = new Text(parent, SWT.BORDER);
        proxy_server.setText(preferenceStore.getString("proxy_server"));
        proxy_server.setLayoutData(gd);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.verticalIndent = 20;
        gd.widthHint = 220;
        gd.horizontalAlignment = GridData.CENTER;
        gd.verticalAlignment = GridData.CENTER;
        Label l9 = new Label(parent, SWT.CENTER);
        l9.setText("Proxy port");
        l9.setLayoutData(gd);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        gd.widthHint = 220;
        gd.horizontalAlignment = GridData.CENTER;
        gd.verticalAlignment = GridData.CENTER;
        proxy_port = new Text(parent, SWT.BORDER);
        proxy_port.setText(preferenceStore.getString("proxy_port"));
        proxy_port.setLayoutData(gd);
        return parent;
    }

    public void init(IWorkbench workbench) {
        preferenceStore = merlin.softfab.SF_ViewPlugin.preferenceStore;
        try {
            preferenceStore.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Performs special processing when this page's Restore Defaults button has been pressed.
	 * Sets the contents of the nameEntry field to
	 * be the default 
	 */
    protected void performDefaults() {
    }

    /** 
	 * Method declared on IPreferencePage. Save the
	 * author name to the preference store.
	 */
    public boolean performOk() {
        try {
            preferenceStore.setValue("use_file", use_file.getText());
            preferenceStore.setValue("input_file", input_file.getText());
            preferenceStore.setValue("input_extention", input_extention.getText());
            preferenceStore.setValue("sf_root", sf_root.getText());
            preferenceStore.setValue("sf_tag_req", sf_tag_req.getText());
            preferenceStore.setValue("delim", delim.getText());
            preferenceStore.setValue("use_proxy", use_proxy.getText());
            preferenceStore.setValue("proxy_server", proxy_server.getText());
            preferenceStore.setValue("proxy_port", proxy_port.getText());
            preferenceStore.save();
        } catch (IOException ee) {
            ee.printStackTrace();
        }
        return super.performOk();
    }
}
