package org.eclipse.tptp.test.tools.web.ui.test.editor.jfaceviewer.dlg;

import org.eclipse.hyades.test.ui.internal.resources.UiPluginResourceBundle;
import org.eclipse.hyades.ui.internal.util.GridDataUtil;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tptp.test.tools.web.runner.util.WebHttpParameter;
import org.eclipse.tptp.test.tools.web.runner.util.WebRequestHelper;
import org.eclipse.tptp.test.tools.web.ui.internal.resources.WebToolsUiPluginResourceBundle;
import org.eclipse.tptp.test.tools.web.ui.test.editor.form.WebTestOverviewForm;

public class WebTestTableViewerDialog extends Dialog implements Listener {

    protected static class TableUI implements ModifyListener {

        private Text nameText;

        private Text valueText;

        private Listener listener;

        public TableUI(Shell shell) {
            super();
        }

        public Composite createControl(Composite parent) {
            Label label;
            Composite content = new Composite(parent, SWT.NULL);
            GridLayout layout = new GridLayout();
            layout.numColumns = 1;
            layout.verticalSpacing = 10;
            layout.horizontalSpacing = 5;
            content.setLayout(layout);
            GridData gd = GridDataUtil.createFill();
            gd.horizontalIndent = 5;
            content.setLayoutData(gd);
            label = new Label(content, SWT.NULL);
            label.setText(UiPluginResourceBundle.ENV_NAME);
            this.nameText = new Text(content, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
            this.nameText.setLayoutData(GridDataUtil.createFill());
            this.nameText.addModifyListener(this);
            label = new Label(content, SWT.NULL);
            label.setText(UiPluginResourceBundle.ENV_VALUE);
            this.valueText = new Text(content, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
            this.valueText.setLayoutData(GridDataUtil.createFill());
            this.valueText.addModifyListener(this);
            return content;
        }

        public String getValue() {
            return this.valueText.getText().trim();
        }

        public String getName() {
            return this.nameText.getText().trim();
        }

        /**
		 * Sent when the text is modified.
		 * @param e an event containing information about the modify
		 */
        public void modifyText(ModifyEvent e) {
            notifyListener();
        }

        /**
		 * @param text
		 */
        public void setValue(String text) {
            if (text != null) {
                this.valueText.setText(text);
            } else {
                this.valueText.setText("");
            }
        }

        /**
		 * @param text
		 */
        public void setName(String text) {
            if (text != null) {
                this.nameText.setText(text);
            } else {
                this.nameText.setText("");
            }
        }

        public void registerListener(Listener listener) {
            this.listener = listener;
        }

        public void notifyListener() {
            if (this.listener != null) {
                this.listener.handleEvent(null);
            }
        }
    }

    WebHttpParameter property;

    private TableUI propertyUI;

    private WebRequestHelper webRequestHelper;

    public WebTestTableViewerDialog(WebRequestHelper webRequestHelper, Shell shell, WebHttpParameter element) {
        super(shell);
        if (element != null) this.property = element; else new WebHttpParameter();
        this.webRequestHelper = webRequestHelper;
    }

    protected Control createDialogArea(Composite parent) {
        getShell().setText(WebToolsUiPluginResourceBundle.Parameters_Label);
        Composite result = new Composite(parent, SWT.NULL);
        GridData gridData = GridDataUtil.createFill();
        gridData.heightHint = 250;
        gridData.widthHint = 400;
        result.setLayoutData(gridData);
        result.setLayout(new GridLayout());
        this.propertyUI = new TableUI(getShell());
        Composite content = this.propertyUI.createControl(result);
        if (this.property != null) {
            this.propertyUI.setName(this.property.getName());
            this.propertyUI.setValue(this.property.getValue());
        }
        return content;
    }

    protected Control createButtonBar(Composite parent) {
        Control control = super.createButtonBar(parent);
        this.propertyUI.registerListener(this);
        if (this.property == null) {
            getButton(IDialogConstants.OK_ID).setEnabled(false);
        }
        return control;
    }

    protected void okPressed() {
        webRequestHelper.encodeAndSetParameter(this.propertyUI.getName(), this.propertyUI.getValue(), "GET");
        webRequestHelper.updateRequestParameter();
        super.okPressed();
    }

    public void handleEvent(Event e) {
        getButton(IDialogConstants.OK_ID).setEnabled(!this.propertyUI.getName().equals(""));
    }
}
