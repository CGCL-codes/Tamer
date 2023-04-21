package com.prolix.editor.systempreferences.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import com.prolix.editor.dialogs.BasicGLMDialog;
import com.prolix.editor.graph.templates.commands.dialogs.ProlixSWTUtils;
import com.prolix.editor.systempreferences.DefaultGLMSettings;

/**
 * <<class description>>
 * 
 * @author Susanne Neumann, Stefan Zander, Philipp Prenner
 */
public class GLMConfigDialog extends BasicGLMDialog {

    private Text urlText;

    private Text portText;

    private Text urlResourcesAPI;

    private Text oicsTmURL;

    private Text oicsUolURL;

    private Text oicsAtomSvcURL;

    private Text oicsLODAtomSvcURL;

    private Text oicsUserDataURL;

    /**
	 * @param parentShell
	 */
    public GLMConfigDialog(Shell parentShell) {
        super(parentShell);
    }

    protected boolean executeOK() {
        validate();
        if (isError()) {
            return false;
        }
        DefaultGLMSettings i = DefaultGLMSettings.getInstance();
        i.setOICSURL(urlText.getText());
        i.setOICSPort(Integer.parseInt(portText.getText()));
        i.setOICSURLResourcesAPI(urlResourcesAPI.getText());
        i.setOicsAtomSvcURL(oicsAtomSvcURL.getText());
        i.setOicsTmURL(oicsTmURL.getText());
        i.setOicsUolURL(oicsUolURL.getText());
        i.setOicsUserDataURL(oicsUserDataURL.getText());
        i.setOicsLODAtomSvcURL(oicsLODAtomSvcURL.getText());
        return true;
    }

    protected void finish() {
    }

    protected String getDialogHeadText() {
        return "Default OpenGLM Settings";
    }

    protected String getDialogMessageText() {
        return "Please don't mess with these unless you're 100% sure what you're doing.";
    }

    protected String getDialogTitleText() {
        return "Default OpenGLM Settings";
    }

    protected Point getSize() {
        return new Point(750, 600);
    }

    protected void setupView(Composite parent) {
        Label label = new Label(parent, SWT.None);
        label.setText("OICS Authentication Server (without http://): ");
        urlText = new Text(parent, SWT.BORDER);
        urlText.setText(DefaultGLMSettings.getInstance().getOICSURL());
        urlText.setLayoutData(ProlixSWTUtils.gridDataFillHorizonal());
        label = new Label(parent, SWT.None);
        label.setText("OICS Authentication Port: ");
        portText = new Text(parent, SWT.BORDER);
        portText.setText("" + DefaultGLMSettings.getInstance().getOICSPort());
        portText.setLayoutData(ProlixSWTUtils.gridDataFillHorizonal());
        label = new Label(parent, SWT.None);
        label.setText("OICS Resources Search API URL:");
        urlResourcesAPI = new Text(parent, SWT.BORDER);
        urlResourcesAPI.setText(DefaultGLMSettings.getInstance().getOICSURLResourcesAPI());
        urlResourcesAPI.setLayoutData(ProlixSWTUtils.gridDataFillHorizonal());
        label = new Label(parent, SWT.None);
        label.setText("OICS teaching method filter URL:");
        oicsTmURL = new Text(parent, SWT.BORDER);
        oicsTmURL.setText(DefaultGLMSettings.getInstance().getOicsTmURL());
        oicsTmURL.setLayoutData(ProlixSWTUtils.gridDataFillHorizonal());
        label = new Label(parent, SWT.None);
        label.setText("OICS learning design filter URL:");
        oicsUolURL = new Text(parent, SWT.BORDER);
        oicsUolURL.setText(DefaultGLMSettings.getInstance().getOicsUolURL());
        oicsUolURL.setLayoutData(ProlixSWTUtils.gridDataFillHorizonal());
        label = new Label(parent, SWT.None);
        label.setText("OICS Atom Service Document URL for all repositories:");
        oicsAtomSvcURL = new Text(parent, SWT.BORDER);
        oicsAtomSvcURL.setText(DefaultGLMSettings.getInstance().getOicsAtomSvcURL());
        oicsAtomSvcURL.setLayoutData(ProlixSWTUtils.gridDataFillHorizonal());
        label = new Label(parent, SWT.None);
        label.setText("OICS Atom Service Document URL for LOD repositories:");
        oicsLODAtomSvcURL = new Text(parent, SWT.BORDER);
        oicsLODAtomSvcURL.setText(DefaultGLMSettings.getInstance().getOicsLODAtomSvcURL());
        oicsLODAtomSvcURL.setLayoutData(ProlixSWTUtils.gridDataFillHorizonal());
        label = new Label(parent, SWT.None);
        label.setText("OICS User Data Document URL:");
        oicsUserDataURL = new Text(parent, SWT.BORDER);
        oicsUserDataURL.setText(DefaultGLMSettings.getInstance().getOicsUserDataURL());
        oicsUserDataURL.setLayoutData(ProlixSWTUtils.gridDataFillHorizonal());
    }

    protected void validate() {
        if (urlText.getText() == null || urlText.getText().isEmpty()) {
            showError("error base url");
            return;
        }
        if (portText.getText() == null || portText.getText().isEmpty()) {
            showError("error base port1");
        }
        int port = 0;
        try {
            port = Integer.parseInt(portText.getText());
        } catch (NumberFormatException e) {
            showError("error base port2");
            return;
        }
        if (port <= 0) {
            showError("error base port3");
            return;
        }
        if (urlResourcesAPI.getText() == null || urlResourcesAPI.getText().isEmpty()) {
            showError("error res url");
            return;
        }
        if (oicsAtomSvcURL.getText() == null || oicsAtomSvcURL.getText().isEmpty()) {
            showError("error res url");
            return;
        }
        if (oicsLODAtomSvcURL.getText() == null || oicsLODAtomSvcURL.getText().isEmpty()) {
            showError("error res url");
            return;
        }
        if (oicsTmURL.getText() == null || oicsTmURL.getText().isEmpty()) {
            showError("error res url");
            return;
        }
        if (oicsUolURL.getText() == null || oicsUolURL.getText().isEmpty()) {
            showError("error res url");
            return;
        }
        if (oicsUserDataURL.getText() == null || oicsUserDataURL.getText().isEmpty()) {
            showError("error res url");
            return;
        }
        showError(null);
    }
}
