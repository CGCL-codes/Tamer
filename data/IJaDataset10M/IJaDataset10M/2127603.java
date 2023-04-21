package com.fh.auge.internal;

import java.util.List;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import com.fh.auge.core.InformationProvider;
import com.fh.auge.model.AppModel;

public class ProviderWizardPage extends WizardPage {

    private Combo provider;

    private ComboViewer providerViewer;

    private Text symbol;

    private WritableValue providerValue;

    private WritableValue idValue;

    List<InformationProvider> informationProviders;

    private LabelProvider marketLabelProvider = new LabelProvider() {

        @Override
        public String getText(Object element) {
            return ((InformationProvider) (element)).getName();
        }
    };

    protected ProviderWizardPage(String pageName, List<InformationProvider> informationProviders) {
        super(pageName);
        this.informationProviders = informationProviders;
        setTitle("Intraday Prices");
        setDescription("Select the provider for intraday prices.");
    }

    private void bind() {
        DataBindingContext dbc = new DataBindingContext();
        idValue = WritableValue.withValueType(String.class);
        providerValue = WritableValue.withValueType(InformationProvider.class);
        dbc.bindValue(SWTObservables.observeText(symbol, SWT.Modify), idValue, null, null);
        dbc.bindValue(ViewersObservables.observeSingleSelection(providerViewer), providerValue, null, null);
        WizardPageSupport.create(this, dbc);
    }

    public static void main(String[] args) {
        final Display display = Display.getDefault();
        Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {

            public void run() {
                Shell shell = new Shell(display);
                shell.setLayout(new FillLayout(SWT.VERTICAL));
                Wizard wizard = new Wizard() {

                    public void addPages() {
                        ProviderWizardPage securityQueryWizardPage = new ProviderWizardPage("a", AppModel.getInstance().getIntradayProviders());
                        addPage(securityQueryWizardPage);
                    }

                    @Override
                    public boolean performFinish() {
                        return false;
                    }
                };
                WizardDialog dialog = new WizardDialog(shell, wizard);
                dialog.create();
                if (dialog.open() == WizardDialog.OK) {
                }
                shell.pack();
                shell.open();
            }
        });
    }

    @Override
    public void createControl(Composite p) {
        Composite parent = new Composite(p, SWT.NONE);
        GridLayoutFactory.swtDefaults().numColumns(3).margins(20, 10).applyTo(parent);
        new Label(parent, SWT.NONE).setText("Provider:");
        provider = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).span(2, 1).grab(true, false).applyTo(provider);
        providerViewer = new ComboViewer(provider);
        providerViewer.setContentProvider(new ArrayContentProvider());
        providerViewer.setLabelProvider(marketLabelProvider);
        providerViewer.setInput(informationProviders);
        new Label(parent, SWT.NONE).setText("Identifier(Symbol,WKN,ISIN):");
        symbol = new Text(parent, SWT.BORDER);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).span(2, 1).grab(true, false).applyTo(symbol);
        bind();
        if (provider.getItemCount() > 0) {
            provider.select(0);
        }
        setControl(parent);
    }

    public InformationProvider getProvider() {
        return (InformationProvider) providerValue.getValue();
    }

    public String getSymbol() {
        return (String) idValue.getValue();
    }
}
