package org.escapek.client.ui.security.wizards;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.escapek.client.core.ui.EscapeKWizardPage;
import org.escapek.core.serviceManager.ServiceWrapper;
import org.escapek.i18n.LocaleService;
import org.escapek.i18n.MessageService;

public class NewGroupWizardPage extends EscapeKWizardPage {

    private boolean repositoryConnected;

    private Text nameText;

    private Text descText;

    private ModifyListener modifyListener = new ModifyListener() {

        public void modifyText(ModifyEvent e) {
            checkComplete();
        }
    };

    private MessageService message;

    protected NewGroupWizardPage(String pageName) {
        super(pageName);
        message = LocaleService.getMessageService(LocaleService.uiMessages);
        setTitle(message.getString("newGroup.mainPage.pageName"));
        if (ServiceWrapper.getInstance().isConnected()) {
            setMessage(message.getString("newGroup.mainPage.info.message"), IMessageProvider.INFORMATION);
            repositoryConnected = true;
        } else {
            setMessage(message.getString("common.notConnected.error"), IMessageProvider.ERROR);
            repositoryConnected = false;
        }
        setPageComplete(false);
    }

    @Override
    public void createControl(Composite parent) {
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        Composite composite = new Composite(parent, SWT.NULL);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        composite.setLayout(gridLayout);
        composite.setLayoutData(data);
        data = new GridData(SWT.FILL, SWT.CENTER, false, false);
        Label nameLabel = new Label(composite, SWT.NONE);
        nameLabel.setLayoutData(data);
        nameLabel.setText(message.getString("newGroup.mainPage.nameLabel.text"));
        data = new GridData(SWT.FILL, SWT.FILL, true, false);
        data.horizontalSpan = 3;
        nameText = new Text(composite, SWT.BORDER);
        nameText.setLayoutData(data);
        nameText.addModifyListener(modifyListener);
        data = new GridData(SWT.FILL, SWT.TOP, false, false);
        Label descLabel = new Label(composite, SWT.NONE);
        descLabel.setLayoutData(data);
        descLabel.setText(message.getString("common.descLabel.text"));
        descText = new Text(composite, SWT.BORDER | SWT.MULTI);
        data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.heightHint = 50;
        data.horizontalSpan = 3;
        descText.setLayoutData(data);
        if (!repositoryConnected) {
            nameText.setEnabled(false);
            descText.setEnabled(false);
        }
        nameText.setFocus();
        setControl(composite);
    }

    private void checkComplete() {
        setPageComplete(false);
        if (nameText.getText().equals("")) {
            setMessage(message.getString("newGroup.mainPage.emptyName.error.message"), IMessageProvider.ERROR);
            return;
        }
        setMessage(message.getString("common.info.pageComplete"), IMessageProvider.INFORMATION);
        getPageData().put("name", nameText.getText());
        getPageData().put("description", descText.getText());
        setPageComplete(true);
    }
}
