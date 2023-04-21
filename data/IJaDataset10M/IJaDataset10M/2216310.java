package org.kalypso.nofdpidss.profiles.wizard.waterbody;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.kalypso.nofdpidss.core.view.widgets.MyWizardPage;
import org.kalypso.nofdpidss.profiles.i18n.Messages;

/**
 * @author Dirk Kuch
 */
public class PageWaterBodyDetails extends MyWizardPage {

    private Text m_name;

    private Text m_description;

    public PageWaterBodyDetails() {
        super("createWaterBody");
        setTitle(Messages.PageWaterBodyDetails_0);
        setDescription(Messages.PageWaterBodyDetails_1);
    }

    @Override
    protected void checkPage() {
        final String name = m_name.getText();
        if (name == null || "".equals(name)) {
            isCustom(Messages.PageWaterBodyDetails_2);
            return;
        }
        setMessage(null);
        setErrorMessage(null);
        setPageComplete(true);
    }

    /**
   * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
   */
    public void createControl(final Composite parent) {
        setPageComplete(false);
        final Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout(2, false));
        container.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        setControl(container);
        final Label lName = new Label(container, SWT.NONE);
        lName.setText(Messages.PageWaterBodyDetails_3);
        m_name = new Text(container, SWT.BORDER);
        m_name.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        m_name.addModifyListener(new ModifyListener() {

            public void modifyText(final ModifyEvent e) {
                checkPage();
            }
        });
        final Label lDescription = new Label(container, SWT.NONE);
        lDescription.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));
        lDescription.setText(Messages.PageWaterBodyDetails_4);
        m_description = new Text(container, SWT.BORDER);
        m_description.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        checkPage();
    }

    public String getWaterBodyDescription() {
        return m_description.getText();
    }

    public String getWaterBodyName() {
        return m_name.getText();
    }
}
