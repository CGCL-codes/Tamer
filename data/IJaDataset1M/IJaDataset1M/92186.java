package com.fh.auge.ui.properties;

import java.util.Properties;
import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import com.fh.auge.core.investment.ApplicationModel;
import com.fh.auge.core.provider.IDataProvider;
import com.fh.auge.core.provider.IHistoricalPricesProvider;
import com.fh.auge.core.provider.ILatestPricesProvider;
import com.fh.auge.core.security.Security;
import com.fh.auge.core.security.SecurityChangeListener;
import com.fh.auge.core.security.SecurityIdentifierType;
import com.fh.auge.core.security.StockItem;

public class DataProvidersComposite extends Composite implements SecurityChangeListener {

    private DataBindingContext dbc;

    private ApplicationModel model;

    private Combo latestProvider;

    private ComboViewer latestProviderViewer;

    private Combo histProvider;

    private ComboViewer histProviderViewer;

    private WritableValue latestProviderValue;

    private WritableValue histProviderValue;

    private TableViewer latestViewer;

    private Properties props;

    private Button editButton;

    private Button deleteButton;

    private StockItem stockItem;

    public DataProvidersComposite(Composite p, int style, ApplicationModel model, StockItem stockItem) {
        super(p, style);
        this.model = model;
        this.stockItem = stockItem;
        latestProviderValue = WritableValue.withValueType(IDataProvider.class);
        histProviderValue = WritableValue.withValueType(IDataProvider.class);
        loadValues();
        GridLayoutFactory.swtDefaults().numColumns(2).applyTo(this);
        Label l = new Label(this, SWT.NONE);
        l.setText("Data Providers:");
        GridDataFactory.swtDefaults().span(2, 1).applyTo(l);
        latestViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
        latestViewer.getTable().setHeaderVisible(true);
        latestViewer.getTable().setLinesVisible(true);
        latestViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                editButton.setEnabled(!event.getSelection().isEmpty());
                deleteButton.setEnabled(!event.getSelection().isEmpty());
            }
        });
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.TOP).span(1, 3).grab(true, false).applyTo(latestViewer.getTable());
        TableViewerColumn nameColumn = new TableViewerColumn(latestViewer, SWT.NONE);
        nameColumn.getColumn().setWidth(150);
        nameColumn.getColumn().setMoveable(false);
        nameColumn.getColumn().setText("Name");
        nameColumn.setLabelProvider(new ColumnLabelProvider() {

            public String getText(Object element) {
                IDataProvider dataProvider = (IDataProvider) (element);
                return "" + dataProvider.getName();
            }

            public Image getImage(Object element) {
                IDataProvider dataProvider = (IDataProvider) (element);
                String id = null;
                if (props != null && props.containsKey(dataProvider.getRequiredIdType().getId())) {
                    id = props.getProperty(dataProvider.getRequiredIdType().getId());
                    if (id != null && id.trim().length() > 0) return DataProvidersComposite.this.getImage("icons/tick.png");
                }
                return DataProvidersComposite.this.getImage("icons/error.gif");
            }
        });
        TableViewerColumn idTypeColumn = new TableViewerColumn(latestViewer, SWT.NONE);
        idTypeColumn.getColumn().setWidth(150);
        idTypeColumn.getColumn().setMoveable(false);
        idTypeColumn.getColumn().setText("Identifier Type");
        idTypeColumn.setLabelProvider(new ColumnLabelProvider() {

            public String getText(Object element) {
                IDataProvider dataProvider = (IDataProvider) (element);
                return "" + dataProvider.getRequiredIdType().getName();
            }
        });
        TableViewerColumn idValueColumn = new TableViewerColumn(latestViewer, SWT.NONE);
        idValueColumn.getColumn().setWidth(150);
        idValueColumn.getColumn().setMoveable(false);
        idValueColumn.getColumn().setText("Identifier Value");
        idValueColumn.setLabelProvider(new ColumnLabelProvider() {

            public String getText(Object element) {
                IDataProvider dataProvider = (IDataProvider) (element);
                String id = null;
                if (props != null && props.containsKey(dataProvider.getRequiredIdType().getId())) {
                    id = props.getProperty(dataProvider.getRequiredIdType().getId());
                }
                return (id != null) ? id : "";
            }
        });
        latestViewer.setContentProvider(new ArrayContentProvider());
        latestViewer.setInput(model.getPricesProviderService().getProviders());
        editButton = new Button(this, SWT.PUSH);
        editButton.setText("Edit");
        editButton.setEnabled(false);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(editButton);
        editButton.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                ISelection selection = latestViewer.getSelection();
                if (!selection.isEmpty()) {
                    IStructuredSelection ss = (IStructuredSelection) selection;
                    IDataProvider provider = (IDataProvider) ss.getFirstElement();
                    SecurityIdentifierType identifierType = provider.getRequiredIdType();
                    String id = props.getProperty(identifierType.getId());
                    InputDialog d = new InputDialog(latestViewer.getTable().getShell(), "Edit Identifier", identifierType.getName(), (id == null) ? "" : id, null);
                    if (InputDialog.OK == d.open()) {
                        props.setProperty(identifierType.getId(), "" + d.getValue());
                        latestViewer.refresh(true);
                    }
                }
            }
        });
        deleteButton = new Button(this, SWT.PUSH);
        deleteButton.setText("Delete");
        deleteButton.setEnabled(false);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(deleteButton);
        new Label(this, SWT.NONE);
        Label l1 = new Label(this, SWT.NONE);
        l1.setText("Latest Prices:");
        GridDataFactory.swtDefaults().span(2, 1).indent(0, 10).applyTo(l1);
        latestProvider = new Combo(this, SWT.BORDER | SWT.READ_ONLY);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).span(1, 1).applyTo(latestProvider);
        new Label(this, SWT.NONE);
        latestProviderViewer = new ComboViewer(latestProvider);
        latestProviderViewer.setContentProvider(new ArrayContentProvider());
        latestProviderViewer.setLabelProvider(dataProvider);
        latestProviderViewer.setInput(model.getPricesProviderService().getLatestPricesProviders());
        Label l2 = new Label(this, SWT.NONE);
        l2.setText("Historical Prices:");
        GridDataFactory.swtDefaults().span(2, 1).applyTo(l2);
        histProvider = new Combo(this, SWT.BORDER | SWT.READ_ONLY);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).span(1, 1).grab(true, false).applyTo(histProvider);
        histProviderViewer = new ComboViewer(histProvider);
        histProviderViewer.setContentProvider(new ArrayContentProvider());
        histProviderViewer.setLabelProvider(dataProvider);
        histProviderViewer.setInput(model.getPricesProviderService().getHistoricalPricesProviders());
        bind();
        model.getSecurityService().getListeners().add(this);
    }

    public void securityChanged(Security security) {
        if (stockItem.getStock().getSecurity().equals(security)) {
            loadValues();
            if (!latestViewer.getTable().isDisposed()) latestViewer.refresh(true);
            dbc.updateTargets();
        }
    }

    public void dispose() {
        model.getSecurityService().getListeners().remove(this);
        super.dispose();
    }

    private void loadValues() {
        model.getPricesProviderService().findLatestPricesProvider(stockItem.getStock().getLatestPricesProviderId());
        latestProviderValue.setValue(model.getPricesProviderService().findLatestPricesProvider(stockItem.getStock().getLatestPricesProviderId()));
        histProviderValue.setValue(model.getPricesProviderService().findHistoricalPricesProvider(stockItem.getStock().getHistoricalPricesProviderId()));
        props = (Properties) stockItem.getStock().getSecurity().getIdentifiers().clone();
    }

    private void bind() {
        dbc = new DataBindingContext();
        dbc.bindValue(ViewersObservables.observeSingleSelection(latestProviderViewer), latestProviderValue, null, null);
        dbc.bindValue(ViewersObservables.observeSingleSelection(histProviderViewer), histProviderValue, null, null);
        final AggregateValidationStatus validationStatus = new AggregateValidationStatus(dbc.getBindings(), AggregateValidationStatus.MAX_SEVERITY);
        validationStatus.addChangeListener(new IChangeListener() {

            public void handleChange(ChangeEvent event) {
                IStatus status = (IStatus) validationStatus.getValue();
                validationStatusChanged(status);
            }
        });
    }

    protected void validationStatusChanged(IStatus status) {
    }

    private LabelProvider dataProvider = new LabelProvider() {

        public String getText(Object element) {
            IDataProvider dataProvider = (IDataProvider) (element);
            return (dataProvider).getName();
        }
    };

    public static void main(String[] args) {
        final Display display = new Display();
        Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {

            public void run() {
                Shell shell = new Shell(display);
                shell.setLayout(new FillLayout());
                ApplicationModel model = ApplicationModel.create();
                shell.open();
                while (!shell.isDisposed()) {
                    if (!display.readAndDispatch()) display.sleep();
                }
            }
        });
        display.dispose();
    }

    public void updateView() {
        loadValues();
        dbc.updateTargets();
        latestViewer.refresh();
    }

    public void updateModel() {
        dbc.updateModels();
        ILatestPricesProvider latest = (ILatestPricesProvider) latestProviderValue.getValue();
        stockItem.getStock().setLatestPricesProviderId(latest != null ? latest.getId() : null);
        IHistoricalPricesProvider hist = (IHistoricalPricesProvider) histProviderValue.getValue();
        stockItem.getStock().setHistoricalPricesProviderId(hist != null ? hist.getId() : null);
        stockItem.getStock().getSecurity().setIdentifiers(props);
    }

    protected Image getImage(String path) {
        return null;
    }
}
