package de.chdev.artools.reporter.ui;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Button;
import com.bmc.arsys.studio.model.store.IStore;
import de.chdev.artools.reporter.interfaces.IReporter;
import de.chdev.artools.reporter.report.ExportRegistry;
import de.chdev.artools.reporter.report.ReportRegistry;
import de.chdev.artools.reporter.ui.provider.ObjectListContentProvider;
import de.chdev.artools.reporter.ui.provider.ObjectListLabelProvider;
import de.chdev.artools.reporter.utils.ARHelpTools;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;

/**
 * @author Christoph Heinig
 * 
 */
public class CreateReportDialogVE extends Composite {

    private Label introLabel = null;

    protected Combo reportTypeCombo = null;

    protected Combo exportCombo = null;

    private Label exportLabel = null;

    protected Table objectTable = null;

    protected TableViewer tableViewer = null;

    private Button loadObjectsButton = null;

    private Group exportGroup = null;

    protected Text fileText = null;

    private Button fileButton = null;

    private Group reportGroup = null;

    private Label reportLabel = null;

    private Label fileLabel = null;

    private Label serverLabel = null;

    protected Combo serverCombo = null;

    public CreateReportDialogVE(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    private void initialize() {
        GridData gridData31 = new GridData();
        gridData31.grabExcessHorizontalSpace = true;
        gridData31.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
        GridData gridData = new GridData();
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 5;
        introLabel = new Label(this, SWT.NONE);
        introLabel.setText(Messages.CreateReportDialogVE_introLabel);
        introLabel.setLayoutData(gridData);
        this.setLayout(gridLayout);
        serverLabel = new Label(this, SWT.NONE);
        serverLabel.setText(Messages.CreateReportDialogVE_serverLabel);
        serverLabel.setLayoutData(gridData31);
        createServerCombo();
        createReportGroup();
        this.setSize(new Point(450, 300));
        createFileGroup();
    }

    /**
     * This method initializes reportTypeCombo
     * 
     */
    private void createReportTypeCombo() {
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData3.grabExcessHorizontalSpace = true;
        reportTypeCombo = new Combo(reportGroup, SWT.READ_ONLY);
        reportTypeCombo.setLayoutData(gridData3);
        reportTypeCombo.setItems(ReportRegistry.getInstance().getReporterNames());
        reportTypeCombo.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                tableViewer.setInput(null);
                tableViewer.refresh();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
    }

    /**
     * This method initializes exportCombo
     * 
     */
    private void createExportCombo() {
        GridData gridData2 = new GridData();
        gridData2.grabExcessHorizontalSpace = true;
        gridData2.horizontalSpan = 2;
        gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        exportCombo = new Combo(exportGroup, SWT.READ_ONLY);
        exportCombo.setItems(ExportRegistry.getInstance().getExporterNames());
        exportCombo.setLayoutData(gridData2);
        if (exportCombo.getItemCount() > 0) {
            exportCombo.select(0);
        }
    }

    /**
     * This method initializes fileGroup
     * 
     */
    private void createFileGroup() {
        GridData gridData5 = new GridData();
        gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData5.horizontalSpan = 2;
        gridData5.grabExcessHorizontalSpace = true;
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 4;
        GridData gridData4 = new GridData();
        gridData4.horizontalSpan = 3;
        gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        exportGroup = new Group(this, SWT.NONE);
        exportGroup.setText(Messages.CreateReportDialogVE_exportGroupLabel);
        exportGroup.setLayoutData(gridData4);
        exportGroup.setLayout(gridLayout1);
        exportLabel = new Label(exportGroup, SWT.NONE);
        exportLabel.setText(Messages.CreateReportDialogVE_exportLabel);
        createExportCombo();
        Label filler = new Label(exportGroup, SWT.NONE);
        fileLabel = new Label(exportGroup, SWT.NONE);
        fileLabel.setText(Messages.CreateReportDialogVE_filenameLabel);
        fileText = new Text(exportGroup, SWT.BORDER);
        fileText.setLayoutData(gridData5);
        fileButton = new Button(exportGroup, SWT.NONE);
        fileButton.setText("...");
        fileButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                FileDialog dialog = new FileDialog(e.widget.getDisplay().getActiveShell(), SWT.SAVE);
                dialog.setOverwrite(true);
                dialog.setText(Messages.CreateReportDialogVE_fileDialogTitle);
                dialog.setFilterExtensions(new String[] { "*.*" });
                String filePath = dialog.open();
                fileText.setText(filePath);
            }
        });
    }

    /**
     * This method initializes reportGroup
     * 
     */
    private void createReportGroup() {
        GridData gridData1 = new GridData();
        gridData1.horizontalSpan = 3;
        gridData1.grabExcessVerticalSpace = true;
        gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData1.heightHint = 100;
        gridData1.grabExcessHorizontalSpace = true;
        GridLayout gridLayout2 = new GridLayout();
        gridLayout2.numColumns = 3;
        GridData gridData6 = new GridData();
        gridData6.horizontalSpan = 3;
        gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData6.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
        gridData6.grabExcessVerticalSpace = true;
        reportGroup = new Group(this, SWT.NONE);
        reportGroup.setText(Messages.CreateReportDialogVE_reportGroupLabel);
        reportGroup.setLayoutData(gridData6);
        reportGroup.setLayout(gridLayout2);
        reportLabel = new Label(reportGroup, SWT.NONE);
        reportLabel.setText(Messages.CreateReportDialogVE_reportLabel);
        createReportTypeCombo();
        loadObjectsButton = new Button(reportGroup, SWT.NONE);
        loadObjectsButton.setText(Messages.CreateReportDialogVE_loadObjectsLabel);
        loadObjectsButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                if (!reportTypeCombo.getText().isEmpty()) {
                    IReporter reporter = ReportRegistry.getInstance().getReporter(reportTypeCombo.getText());
                    String storeName = serverCombo.getText();
                    if (storeName.equals(Messages.CreateReportDialogVE_serverEntryAll)) {
                        List<IStore> stores = new ArrayList<IStore>();
                        stores.addAll(ARHelpTools.getInstance().getAllActiveStores());
                        reporter.setStores(stores);
                    } else {
                        List<IStore> storeList = new ArrayList<IStore>();
                        IStore store = ARHelpTools.getInstance().getStoreByName(storeName);
                        if (store != null) {
                            storeList.add(store);
                        }
                        reporter.setStores(storeList);
                    }
                    tableViewer.setInput(reporter);
                    tableViewer.refresh();
                }
            }
        });
        objectTable = new Table(reportGroup, SWT.FULL_SELECTION | SWT.BORDER | SWT.MULTI);
        objectTable.setHeaderVisible(true);
        objectTable.setLayoutData(gridData1);
        objectTable.setLinesVisible(true);
        tableViewer = new TableViewer(objectTable);
        tableViewer.setContentProvider(new ObjectListContentProvider());
        tableViewer.setLabelProvider(new ObjectListLabelProvider());
        TableColumn tableColumn = new TableColumn(objectTable, SWT.NONE);
        tableColumn.setWidth(400);
        tableColumn.setText(Messages.CreateReportDialogVE_objectTableHeaderName);
    }

    /**
     * This method initializes serverCombo
     * 
     */
    private void createServerCombo() {
        GridData gridData7 = new GridData();
        gridData7.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
        serverCombo = new Combo(this, SWT.READ_ONLY);
        serverCombo.setLayoutData(gridData7);
        serverCombo.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                tableViewer.setInput(null);
                tableViewer.refresh();
            }

            public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
            }
        });
    }
}
