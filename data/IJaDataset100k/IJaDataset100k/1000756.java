package de.haumacher.timecollect;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import au.com.bytecode.opencsv.CSVWriter;
import de.haumacher.timecollect.UIConfig.Report;
import de.haumacher.timecollect.common.config.ValueFactory;
import de.haumacher.timecollect.common.report.ReportBuilder;
import de.haumacher.timecollect.report.ReportDef;
import de.haumacher.timecollect.report.ReportGenerator;

public class ReportDialog extends DialogBase {

    protected static final String[] EMPTY = {};

    List<Report> displayedReports;

    private final Main main;

    private Table resultTable;

    private Composite resultBar;

    private Text queryText;

    private Combo queryCombo;

    private Button expertMode;

    private Button storeQueryButton;

    private Button revertQueryButton;

    private Button deleteQueryButton;

    private SashForm sash;

    public ReportDialog(final Main main, DialogConfig dialogConfig) {
        super(main.display, dialogConfig);
        this.main = main;
        dialog.setText("Activity Report");
        dialog.setLayout(new GridLayout(1, false));
        Composite selectBar = new Composite(dialog, SWT.NONE);
        selectBar.setLayoutData(new GridData(SWT.TOP, SWT.LEFT, false, false, 1, 1));
        selectBar.setLayout(new RowLayout());
        queryCombo = new Combo(selectBar, SWT.None);
        queryCombo.setVisibleItemCount(20);
        displayedReports = ReportDef.getAllReports(main.dbConfig, main.ui);
        for (Report report : displayedReports) {
            queryCombo.add(report.getName());
        }
        queryCombo.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                updateQueryText();
            }
        });
        queryCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                setEditable(true);
            }
        });
        Button updateButton = new Button(selectBar, SWT.BORDER);
        updateButton.setText("Query");
        updateButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                update();
            }
        });
        Button saveButton = new Button(selectBar, SWT.BORDER);
        saveButton.setText("Export CSV");
        saveButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                saveAs();
            }
        });
        storeQueryButton = new Button(selectBar, SWT.BORDER);
        storeQueryButton.setText("Store Query");
        storeQueryButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (expertMode.getSelection()) {
                    String sql = queryText.getText();
                    int reportId = queryCombo.getSelectionIndex();
                    String reportName = queryCombo.getText().trim();
                    if (reportId < 0) {
                        for (int n = 0, cnt = queryCombo.getItemCount(); n < cnt; n++) {
                            if (reportName.equals(queryCombo.getItem(n))) {
                                reportId = n;
                                break;
                            }
                        }
                    }
                    if (reportId >= 0) {
                        Report currentReport = displayedReports.get(reportId);
                        currentReport.setSql(sql);
                        currentReport.setPredefined(false);
                    } else {
                        if (reportName.length() > 0) {
                            Report newReport = ValueFactory.newInstance(Report.class);
                            newReport.setName(reportName);
                            newReport.setSql(sql);
                            newReport.setPredefined(false);
                            queryCombo.add(reportName);
                            displayedReports.add(newReport);
                            int index = queryCombo.getItemCount();
                            queryCombo.select(index);
                        }
                    }
                    updateConfig(main);
                }
            }
        });
        revertQueryButton = new Button(selectBar, SWT.BORDER);
        revertQueryButton.setText("Revert Query");
        revertQueryButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (expertMode.getSelection()) {
                    int reportId = queryCombo.getSelectionIndex();
                    if (reportId >= 0) {
                        Report currentReport = displayedReports.get(reportId);
                        queryText.setText(currentReport.getSql());
                    } else {
                        queryText.setText("");
                    }
                }
            }
        });
        deleteQueryButton = new Button(selectBar, SWT.BORDER);
        deleteQueryButton.setText("DeleteQuery");
        deleteQueryButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (expertMode.getSelection()) {
                    int reportId = queryCombo.getSelectionIndex();
                    if (reportId >= 0) {
                        queryCombo.remove(reportId);
                        displayedReports.remove(reportId);
                        updateConfig(main);
                        int leftItemCount = queryCombo.getItemCount();
                        if (leftItemCount > 0) {
                            queryCombo.select(reportId < leftItemCount ? reportId : reportId - 1);
                            updateQueryText();
                        } else {
                            queryText.setText("");
                        }
                    }
                }
            }
        });
        expertMode = new Button(selectBar, SWT.CHECK);
        expertMode.setText("Expert Mode");
        expertMode.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                updateExpertMode();
            }
        });
        sash = new SashForm(dialog, SWT.VERTICAL);
        sash.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        sash.setLayout(new FillLayout());
        queryText = new Text(sash, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
        GridData textLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        textLayout.heightHint = 300;
        textLayout.widthHint = 800;
        queryText.setLayoutData(textLayout);
        boolean initialExpertMode = false;
        queryText.setVisible(initialExpertMode);
        resultBar = new Composite(sash, SWT.NONE);
        resultBar.setLayout(new GridLayout(1, false));
        createResultTable();
        new TableColumn(resultTable, SWT.NONE);
        new TableItem(resultTable, SWT.NONE);
        updateExpertMode();
        dialog.pack();
    }

    protected void update() {
        final String query = queryText.getText();
        ReportBuilder builder = new ReportBuilder() {

            boolean firstHeading;

            @Override
            public Object createReport() {
                if (resultTable != null) {
                    resultTable.dispose();
                }
                return null;
            }

            @Override
            public void createCell(Object row, int col, String value) {
                ((TableItem) row).setText(col, value == null ? "-" : value);
            }

            @Override
            public Object createRow(Object body) {
                TableItem item = new TableItem((Table) body, SWT.NONE);
                return item;
            }

            @Override
            public void createHeading(Object body, int level, String label) {
                if (firstHeading) {
                    firstHeading = false;
                } else {
                    new TableItem((Table) body, SWT.NONE);
                }
                TableItem item = new TableItem((Table) body, SWT.NONE);
                item.setGrayed(true);
                item.setText(label);
            }

            @Override
            public void finishRow(Object row) {
            }

            @Override
            public void finishTable(Object table) {
                for (TableColumn column : ((Table) table).getColumns()) {
                    column.pack();
                }
            }

            @Override
            public void finishReport(Object report) {
                resultBar.layout();
            }

            @Override
            public Object createTable(Object report) {
                return ReportDialog.this.createResultTable();
            }

            @Override
            public Object createHead(Object table) {
                return table;
            }

            @Override
            public void createColumn(Object head, int col, String label) {
                TableColumn column = new TableColumn((Table) head, SWT.LEFT);
                column.setText(label);
            }

            @Override
            public void finishHead(Object head) {
            }

            @Override
            public Object createBody(Object table) {
                firstHeading = true;
                return table;
            }

            @Override
            public void finishBody(Object body) {
            }
        };
        new ReportGenerator(new MainContext(this.main), builder).run(query);
    }

    protected void saveAs() {
        final String query = queryText.getText();
        ReportBuilder builder = new ReportBuilder() {

            private boolean firstHeading;

            private boolean firstRow;

            private CSVWriter writer;

            private List<String> labels = new ArrayList<String>();

            private List<String> buffer = new ArrayList<String>();

            @Override
            public Object createReport() {
                FileDialog chooseFile = new FileDialog(dialog, SWT.SAVE);
                chooseFile.setText("Choose file name for saved report.");
                chooseFile.setFilterExtensions(new String[] { "*.csv", "*.txt" });
                chooseFile.setOverwrite(true);
                String fileName = chooseFile.open();
                if (fileName == null) {
                    throw new AbortException();
                }
                File file = new File(fileName);
                try {
                    writer = new CSVWriter(new FileWriter(file), ';', '"', '\\', "\r\n");
                } catch (IOException ex) {
                    main.handleError("Save problem", "Saving file failed", ex);
                    throw new AbortException();
                }
                return null;
            }

            @Override
            public Object createTable(Object report) {
                return null;
            }

            @Override
            public Object createHead(Object table) {
                labels.clear();
                return null;
            }

            @Override
            public void createColumn(Object head, int col, String label) {
                while (labels.size() <= col) {
                    labels.add("");
                }
                labels.set(col, label);
            }

            @Override
            public void finishHead(Object head) {
            }

            @Override
            public Object createBody(Object table) {
                firstHeading = true;
                firstRow = true;
                return null;
            }

            @Override
            public void createHeading(Object body, int level, String label) {
                if (firstHeading) {
                    firstHeading = false;
                } else {
                    writer.writeNext(EMPTY);
                }
                writer.writeNext(new String[] { label });
                firstRow = true;
            }

            @Override
            public Object createRow(Object body) {
                if (firstRow) {
                    firstRow = false;
                    writeColumnLabels();
                }
                buffer.clear();
                return null;
            }

            private void writeColumnLabels() {
                writer.writeNext(labels.toArray(new String[labels.size()]));
            }

            @Override
            public void createCell(Object row, int col, String value) {
                while (buffer.size() <= col) {
                    buffer.add("");
                }
                buffer.set(col, value == null ? "" : value);
            }

            @Override
            public void finishRow(Object row) {
                writer.writeNext(buffer.toArray(new String[buffer.size()]));
            }

            @Override
            public void finishBody(Object body) {
                if (firstRow) {
                    writeColumnLabels();
                }
            }

            @Override
            public void finishTable(Object table) {
            }

            @Override
            public void finishReport(Object report) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    main.handleError("Save problem", "Flushing data failed", ex);
                    throw new AbortException();
                }
            }
        };
        try {
            new ReportGenerator(new MainContext(this.main), builder).run(query);
        } catch (AbortException ex) {
        }
    }

    private Table createResultTable() {
        resultTable = new Table(resultBar, SWT.MULTI | SWT.BORDER);
        GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
        layoutData.heightHint = 600;
        resultTable.setLayoutData(layoutData);
        resultTable.setHeaderVisible(true);
        resultTable.setLinesVisible(true);
        return resultTable;
    }

    private void setEditable(boolean enableEdit) {
    }

    private void updateExpertMode() {
        setExpertMode(expertMode.getSelection());
    }

    private void setExpertMode(boolean expertMode) {
        queryText.setVisible(expertMode);
        storeQueryButton.setVisible(expertMode);
        revertQueryButton.setVisible(expertMode);
        deleteQueryButton.setVisible(expertMode);
        sash.setWeights(expertMode ? new int[] { 50, 50 } : new int[] { 0, 100 });
    }

    private void updateConfig(Main main) {
        List<Report> predefinedReports = ReportDef.predefinedReports(main.dbConfig.getMysql());
        List<Report> customReports = main.ui.getReports();
        HashSet<String> deletedPredefinedNames = new HashSet<String>();
        for (Report report : predefinedReports) {
            deletedPredefinedNames.add(report.getName());
        }
        customReports.clear();
        for (int n = 0, cnt = displayedReports.size(); n < cnt; n++) {
            Report report = displayedReports.get(n);
            if (!report.isPredefined()) {
                customReports.add(report);
            }
            deletedPredefinedNames.remove(report.getName());
        }
        for (String name : deletedPredefinedNames) {
            Report stub = ValueFactory.newInstance(Report.class);
            stub.setName(name);
            customReports.add(stub);
        }
        main.saveConfig();
    }

    private void updateQueryText() {
        int reportId = queryCombo.getSelectionIndex();
        if (reportId >= 0) {
            Report report = displayedReports.get(reportId);
            queryText.setText(report.getSql());
            setEditable(!report.isPredefined());
        }
    }
}
