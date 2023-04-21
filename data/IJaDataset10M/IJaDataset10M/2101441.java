package net.sourceforge.openconferencer.client.ui.wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.openconferencer.client.Messages;
import net.sourceforge.openconferencer.client.contact.ContactManager;
import net.sourceforge.openconferencer.client.contact.ContactProviderInformation;
import net.sourceforge.openconferencer.client.contact.IContactProvider;
import net.sourceforge.openconferencer.client.contact.IContactProviderPreferencePage;
import net.sourceforge.openconferencer.client.util.LogHelper;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author aleksandar
 */
public class ChooseProviderPage extends WizardPage {

    private Table providerList;

    private List<ContactProviderInformation> providerInfos;

    private AddAccountWizard wizard;

    /**
	 * @param wizard
	 */
    public ChooseProviderPage(AddAccountWizard wizard) {
        super(Messages.wizard_addaccount_choose_provider, Messages.wizard_addaccount_choose_provider_title, null);
        this.wizard = wizard;
    }

    @Override
    public void createControl(Composite arg0) {
        Composite container = new Composite(arg0, NONE);
        Label info = new Label(container, NONE);
        providerList = new Table(container, SWT.BORDER);
        GridData listData = new GridData();
        listData.horizontalAlignment = GridData.FILL;
        listData.verticalAlignment = GridData.FILL;
        listData.grabExcessVerticalSpace = true;
        listData.grabExcessHorizontalSpace = true;
        container.setLayout(new GridLayout(1, true));
        providerList.setHeaderVisible(false);
        providerList.setLinesVisible(false);
        providerList.setLayoutData(listData);
        info.setText("Please select the service from which you would like to obtain contacts from.");
        setupData(providerList);
        setControl(container);
        setPageComplete(false);
        providerList.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                setPageComplete(true);
            }
        });
    }

    @Override
    public boolean canFlipToNextPage() {
        return providerList.getSelectionCount() > 0;
    }

    @Override
    public IWizardPage getNextPage() {
        try {
            int selection = providerList.getSelectionIndex();
            IContactProvider selectedProvider = getProviderInfos().get(selection).getContactProvider();
            IContactProviderPreferencePage page = selectedProvider.getContactDisplay().getAccountPreferencePage();
            wizard.getProviderPreferencesPage().setPreferencePage(page);
        } catch (Exception ex) {
            LogHelper.error("Failed to create provider preference page for Add Account wizard", ex);
        }
        return super.getNextPage();
    }

    /**
	 * @return
	 */
    public IContactProvider getSelectedContactProvider() {
        int selection = providerList.getSelectionIndex();
        return getProviderInfos().get(selection).getContactProvider();
    }

    /**
	 * @return
	 */
    public ContactProviderInformation getSelectedContactProviderInfo() {
        int selection = providerList.getSelectionIndex();
        return getProviderInfos().get(selection);
    }

    /**
	 * @param table
	 */
    protected void setupData(Table table) {
        ContactProviderInformation infos[] = ContactManager.getInstance().getContactProviders();
        ContactProviderInformation active[] = ContactManager.getInstance().getActiveContactProviders();
        List<String> activeIds = new ArrayList<String>();
        for (ContactProviderInformation activeProvider : active) activeIds.add(activeProvider.getId());
        TableColumn col1 = new TableColumn(table, SWT.NULL);
        TableColumn col2 = new TableColumn(table, SWT.NULL);
        col1.setText("C1");
        col2.setText("C2");
        providerInfos = new ArrayList<ContactProviderInformation>();
        for (ContactProviderInformation provider : infos) {
            if (activeIds.contains(provider.getId())) continue;
            TableItem item = new TableItem(table, SWT.NULL);
            if (provider.getIcon() != null) item.setImage(0, provider.getIcon());
            item.setText(1, provider.getName() + " - " + provider.getDescription());
            providerInfos.add(provider);
        }
        if (providerInfos.size() == 0) {
            TableItem item = new TableItem(table, SWT.NULL);
            item.setText("All available contact providers are currently configured for your system. \n" + "If you need more contact providers they can be downloaded using the Update Wizard.");
            table.setEnabled(false);
        }
        col1.pack();
        col2.pack();
    }

    /**
	 * @return the providerInfos
	 */
    public List<ContactProviderInformation> getProviderInfos() {
        if (providerInfos == null) providerInfos = Arrays.asList(ContactManager.getInstance().getContactProviders());
        return providerInfos;
    }
}
