package org.plazmaforge.studio.reportdesigner.dialogs;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.plazmaforge.studio.core.dialogs.IDialogCommand;
import org.plazmaforge.studio.reportdesigner.ReportDesignerResources;
import org.plazmaforge.studio.reportdesigner.editor.ReportEditor;
import org.plazmaforge.studio.reportdesigner.model.PageSize;
import org.plazmaforge.studio.reportdesigner.model.Report;
import org.plazmaforge.studio.reportdesigner.model.ReportConstants;
import org.plazmaforge.studio.reportdesigner.model.Unit;
import org.plazmaforge.studio.reportdesigner.model.data.Entry;
import org.plazmaforge.studio.reportdesigner.util.ControlUtils;
import org.plazmaforge.studio.reportdesigner.util.ReportUtils;

/** 
 * @author Oleh Hapon
 * $Id: ReportEditDialog.java,v 1.18 2010/11/19 12:12:17 ohapon Exp $
 */
public class ReportEditDialog extends AbstractRDEditTitleDialog {

    private boolean modify;

    final int LABEL_WIDTH = 150;

    final int FIELD_WIDTH = 100;

    private Text nameField;

    private Text topMarginField;

    private Text bottomMarginField;

    private Text leftMarginField;

    private Text rightMarginField;

    private Combo pageFormatField;

    private Text pageWidthField;

    private Text pageHeightField;

    private Combo pageOrientationField;

    private Button titleOnNewPageField;

    private Button summaryOnNewPageField;

    private Button floatingColumnFooterField;

    private Button ignorePaginationField;

    private Combo printOrderField;

    private Combo whenNoDataField;

    private Text columnsField;

    private Text columnWidthField;

    private Text columnSpacingField;

    private Combo scriptletHandlingField;

    private Text scriptletClassField;

    private Combo languageField;

    private Text formatFactoryClassField;

    private Text resourceBundleField;

    private Combo whenResourceMissingTypeField;

    private Combo xmlEncodingField;

    public ReportEditDialog(Shell shell, Report report) {
        super(shell, IDialogCommand.EDIT);
        this.report = report;
    }

    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(ReportDesignerResources.Report_settings);
    }

    protected Control createContents(Composite parent) {
        Control contents = super.createContents(parent);
        setTitle(ReportDesignerResources.Report_settings);
        return contents;
    }

    protected Control createDialogArea(Composite parent) {
        Composite parentComposite = (Composite) super.createDialogArea(parent);
        Composite composite = new Composite(parentComposite, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
        layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
        layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
        layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite.setFont(parentComposite.getFont());
        Label nameLabel = new Label(composite, SWT.WRAP);
        nameLabel.setText(ReportDesignerResources.Report_Name);
        nameField = new Text(composite, SWT.BORDER);
        GridData data = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        data.widthHint = 100;
        nameField.setLayoutData(data);
        createPagePanel(composite);
        createTabPanel(composite);
        initControls();
        loadData();
        return parentComposite;
    }

    private Composite createPagePanel(Composite parent) {
        Group panel = new Group(parent, SWT.NONE);
        panel.setText(ReportDesignerResources.Page);
        GridData gridData = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
        panel.setLayoutData(gridData);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        panel.setLayout(gridLayout);
        Label pageFormatLabel = new Label(panel, SWT.NONE);
        pageFormatLabel.setText(ReportDesignerResources.Page_Format);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = LABEL_WIDTH;
        pageFormatLabel.setLayoutData(gridData);
        pageFormatField = new Combo(panel, SWT.READ_ONLY);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = FIELD_WIDTH;
        pageFormatField.setLayoutData(gridData);
        pageFormatField.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                if (!isLoad) {
                    return;
                }
                setPageSizeInControlByFormat();
            }
        });
        new Label(panel, SWT.NONE);
        Label pageWidthLabel = new Label(panel, SWT.NONE);
        pageWidthLabel.setText(ReportDesignerResources.Page_Width);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        pageWidthLabel.setLayoutData(gridData);
        pageWidthField = new Text(panel, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = FIELD_WIDTH;
        pageWidthField.setLayoutData(gridData);
        createUnitLabel(panel);
        Label pageHeightLabel = new Label(panel, SWT.NONE);
        pageHeightLabel.setText(ReportDesignerResources.Page_Height);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        pageHeightLabel.setLayoutData(gridData);
        pageHeightField = new Text(panel, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = FIELD_WIDTH;
        pageHeightField.setLayoutData(gridData);
        createUnitLabel(panel);
        Label pageOrientationLabel = new Label(panel, SWT.NONE);
        pageOrientationLabel.setText(ReportDesignerResources.Page_Orientation);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        pageOrientationLabel.setLayoutData(gridData);
        pageOrientationField = new Combo(panel, SWT.READ_ONLY);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = FIELD_WIDTH;
        pageOrientationField.setLayoutData(gridData);
        return panel;
    }

    private Composite createTabPanel(Composite parent) {
        TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
        GridData gridData = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
        tabFolder.setLayoutData(gridData);
        createPageMarginItem(tabFolder);
        createColumnsItem(tabFolder);
        createScriptletItem(tabFolder);
        createLocalizationItem(tabFolder);
        createOtherItem(tabFolder);
        return tabFolder;
    }

    protected void createPageMarginItem(TabFolder tabFolder) {
        TabItem tabitem = new TabItem(tabFolder, SWT.NULL);
        tabitem.setText(ReportDesignerResources.Page_Margin);
        Composite composite = new Composite(tabFolder, SWT.NULL);
        tabitem.setControl(composite);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        composite.setLayout(gridLayout);
        GridData gridData = null;
        Label topMarginLabel = new Label(composite, SWT.NONE);
        topMarginLabel.setText(ReportDesignerResources.Page_Margin_Top);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = LABEL_WIDTH;
        topMarginLabel.setLayoutData(gridData);
        topMarginField = new Text(composite, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = FIELD_WIDTH;
        topMarginField.setLayoutData(gridData);
        createUnitLabel(composite);
        Label bottomMarginLabel = new Label(composite, SWT.NONE);
        bottomMarginLabel.setText(ReportDesignerResources.Page_Margin_Bottom);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = LABEL_WIDTH;
        bottomMarginLabel.setLayoutData(gridData);
        bottomMarginField = new Text(composite, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = FIELD_WIDTH;
        bottomMarginField.setLayoutData(gridData);
        createUnitLabel(composite);
        Label leftMarginLabel = new Label(composite, SWT.NONE);
        leftMarginLabel.setText(ReportDesignerResources.Page_Margin_Left);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = LABEL_WIDTH;
        leftMarginLabel.setLayoutData(gridData);
        leftMarginField = new Text(composite, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = FIELD_WIDTH;
        leftMarginField.setLayoutData(gridData);
        createUnitLabel(composite);
        Label rightMarginLabel = new Label(composite, SWT.NONE);
        rightMarginLabel.setText(ReportDesignerResources.Page_Margin_Right);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = LABEL_WIDTH;
        rightMarginLabel.setLayoutData(gridData);
        rightMarginField = new Text(composite, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = FIELD_WIDTH;
        rightMarginField.setLayoutData(gridData);
        createUnitLabel(composite);
    }

    protected void createColumnsItem(TabFolder tabFolder) {
        TabItem tabitem = new TabItem(tabFolder, 0);
        tabitem.setText(ReportDesignerResources.Columns);
        Composite composite = new Composite(tabFolder, 0);
        tabitem.setControl(composite);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        composite.setLayout(gridLayout);
        composite.setLayoutData(new GridData(1808));
        GridData gridData = null;
        Label columnsLabel = new Label(composite, SWT.NONE);
        columnsLabel.setText(ReportDesignerResources.Column_Count);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gridData.widthHint = LABEL_WIDTH;
        columnsLabel.setLayoutData(gridData);
        columnsField = new Text(composite, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = FIELD_WIDTH;
        columnsField.setLayoutData(gridData);
        new Label(composite, SWT.NONE);
        Label columnWidthLabel = new Label(composite, SWT.NONE);
        columnWidthLabel.setText(ReportDesignerResources.Width);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = LABEL_WIDTH;
        columnWidthLabel.setLayoutData(gridData);
        columnWidthField = new Text(composite, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = FIELD_WIDTH;
        columnWidthField.setLayoutData(gridData);
        createUnitLabel(composite);
        Label columnSpacingLabel = new Label(composite, SWT.NONE);
        columnSpacingLabel.setText(ReportDesignerResources.Page_Spacing);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = LABEL_WIDTH;
        columnSpacingLabel.setLayoutData(gridData);
        columnSpacingField = new Text(composite, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = FIELD_WIDTH;
        columnSpacingField.setLayoutData(gridData);
        createUnitLabel(composite);
    }

    protected void createScriptletItem(TabFolder tabFolder) {
        TabItem tabitem = new TabItem(tabFolder, 0);
        tabitem.setText(ReportDesignerResources.Scriptlet);
        Composite composite = new Composite(tabFolder, 0);
        tabitem.setControl(composite);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        composite.setLayout(gridLayout);
        composite.setLayoutData(new GridData(1808));
        GridData gridData = null;
        Label scriptletClassLabel = new Label(composite, SWT.NONE);
        scriptletClassLabel.setText(ReportDesignerResources.Scriptlet_Class);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = LABEL_WIDTH;
        scriptletClassLabel.setLayoutData(gridData);
        scriptletHandlingField = new Combo(composite, SWT.READ_ONLY);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gridData.widthHint = FIELD_WIDTH;
        scriptletHandlingField.setLayoutData(gridData);
        scriptletHandlingField.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                if (!isLoad) {
                    return;
                }
                enableScriptletClassControl();
            }
        });
        scriptletClassField = new Text(composite, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gridData.widthHint = FIELD_WIDTH;
        scriptletClassField.setLayoutData(gridData);
        new Label(composite, SWT.NONE);
        Label languageLabel = new Label(composite, SWT.NONE);
        languageLabel.setText(ReportDesignerResources.Language);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        gridData.widthHint = LABEL_WIDTH;
        languageLabel.setLayoutData(gridData);
        languageField = new Combo(composite, SWT.READ_ONLY);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gridData.widthHint = FIELD_WIDTH;
        languageField.setLayoutData(gridData);
    }

    protected void createLocalizationItem(TabFolder tabFolder) {
        TabItem tabitem = new TabItem(tabFolder, 0);
        tabitem.setText(ReportDesignerResources.Localization);
        Composite composite = new Composite(tabFolder, 0);
        tabitem.setControl(composite);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        composite.setLayout(gridLayout);
        composite.setLayoutData(new GridData(1808));
        GridData gridData = null;
        Label formatFactoryClassLabel = new Label(composite, SWT.NONE);
        formatFactoryClassLabel.setText(ReportDesignerResources.Format_Factory_Class);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        formatFactoryClassLabel.setLayoutData(gridData);
        formatFactoryClassField = new Text(composite, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        formatFactoryClassField.setLayoutData(gridData);
        Label resourceBandleLabel = new Label(composite, SWT.NONE);
        resourceBandleLabel.setText(ReportDesignerResources.Resource_Bandle_Base_Name);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        resourceBandleLabel.setLayoutData(gridData);
        resourceBundleField = new Text(composite, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        resourceBundleField.setLayoutData(gridData);
        Label whenResourceMissingTypeLabel = new Label(composite, SWT.NONE);
        whenResourceMissingTypeLabel.setText(ReportDesignerResources.When_resource_missing_type);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        whenResourceMissingTypeLabel.setLayoutData(gridData);
        whenResourceMissingTypeField = new Combo(composite, SWT.READ_ONLY);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        whenResourceMissingTypeField.setLayoutData(gridData);
        Label xmlEncodingLabel = new Label(composite, SWT.NONE);
        xmlEncodingLabel.setText(ReportDesignerResources.XML_encoding);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
        xmlEncodingLabel.setLayoutData(gridData);
        xmlEncodingField = new Combo(composite, SWT.READ_ONLY);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        xmlEncodingField.setLayoutData(gridData);
    }

    protected void createOtherItem(TabFolder tabFolder) {
        TabItem tabitem = new TabItem(tabFolder, 0);
        tabitem.setText(ReportDesignerResources.Other);
        Composite composite = new Composite(tabFolder, 0);
        tabitem.setControl(composite);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        composite.setLayout(gridLayout);
        composite.setLayoutData(new GridData(1808));
        GridData gridData = null;
        titleOnNewPageField = new Button(composite, SWT.CHECK);
        titleOnNewPageField.setText(ReportDesignerResources.Title_on_new_page);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gridData.widthHint = LABEL_WIDTH;
        titleOnNewPageField.setLayoutData(gridData);
        Label printOrderLabel = new Label(composite, SWT.NONE);
        printOrderLabel.setText(ReportDesignerResources.Print_Order);
        summaryOnNewPageField = new Button(composite, SWT.CHECK);
        summaryOnNewPageField.setText(ReportDesignerResources.Summary_on_new_page);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
        summaryOnNewPageField.setLayoutData(gridData);
        printOrderField = new Combo(composite, SWT.READ_ONLY);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
        printOrderField.setLayoutData(gridData);
        floatingColumnFooterField = new Button(composite, SWT.CHECK);
        floatingColumnFooterField.setText(ReportDesignerResources.Floating_column_footer);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
        floatingColumnFooterField.setLayoutData(gridData);
        Label whenNoDataLabel = new Label(composite, SWT.NONE);
        whenNoDataLabel.setText(ReportDesignerResources.When_no_data);
        ignorePaginationField = new Button(composite, SWT.CHECK);
        ignorePaginationField.setText(ReportDesignerResources.Ignore_pagination);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
        ignorePaginationField.setLayoutData(gridData);
        whenNoDataField = new Combo(composite, SWT.READ_ONLY);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
        whenNoDataField.setLayoutData(gridData);
    }

    private void createUnitLabel(Composite parent) {
        Label label = new Label(parent, SWT.NONE);
        label.setText(report.getUnit().getName());
    }

    protected void okPressed() {
        storeData();
        modify = true;
        super.okPressed();
    }

    public boolean isModify() {
        return modify;
    }

    private boolean isLoad;

    protected void loadData() {
        isLoad = false;
        String name = report.getName();
        nameField.setText(name == null ? "" : name);
        String format = PageSize.deductPageFormat(report.getPageWidth(), report.getPageHeight());
        int index = pageFormats.indexOf(format);
        if (index < 0) {
            index = 0;
        }
        pageFormatField.select(index);
        if (index > 0) {
            enablePageSizeControls(false);
        }
        setPageSizeInControl(report.getPageWidth(), report.getPageHeight());
        ControlUtils.setEntryKey(pageOrientationField, pageOrientations, report.getOrientation());
        setUnitValue(topMarginField, report.getTopMargin());
        setUnitValue(bottomMarginField, report.getBottomMargin());
        setUnitValue(leftMarginField, report.getLeftMargin());
        setUnitValue(rightMarginField, report.getRightMargin());
        ControlUtils.setIntValue(columnsField, report.getColumnCount());
        setUnitValue(columnWidthField, report.getColumnWidth());
        setUnitValue(columnSpacingField, report.getColumnSpacing());
        int scriptletHandling = report.getScriptletHandling();
        ControlUtils.setEntryKey(scriptletHandlingField, scriptletHandlings, scriptletHandling);
        if (scriptletHandling == ReportConstants.SCRIPTLET_CLASSIC_HANDLING) {
            setText(scriptletClassField, report.getScriptletClass());
        }
        ControlUtils.setText(languageField, report.getLanguage());
        ControlUtils.setEntryKey(printOrderField, printOrders, report.getPrintOrder());
        ControlUtils.setEntryKey(whenNoDataField, whenNodataTypes, report.getWhenNoDataType());
        titleOnNewPageField.setSelection(report.isTitleNewPage());
        summaryOnNewPageField.setSelection(report.isSummaryNewPage());
        floatingColumnFooterField.setSelection(report.isFloatColumnFooter());
        ignorePaginationField.setSelection(report.isIgnorePagination());
        ControlUtils.setText(formatFactoryClassField, report.getFormatFactoryClass());
        ControlUtils.setText(resourceBundleField, report.getResourceBundle());
        ControlUtils.setEntryKey(whenResourceMissingTypeField, whenResourceMissingTypes, report.getWhenResourceMissingType());
        ControlUtils.setText(xmlEncodingField, report.getEncoding());
        enableScriptletClassControl();
        isLoad = true;
    }

    protected void storeData() {
        report.setName(nameField.getText());
        Point pageSize = null;
        int formatIndex = pageFormatField.getSelectionIndex();
        if (formatIndex > 0) {
            String format = pageFormats.get(formatIndex);
            pageSize = PageSize.pageFormats.get(format);
        }
        int pageWidth = 0;
        int pageHeight = 0;
        if (pageSize != null) {
            pageWidth = pageSize.x;
            pageHeight = pageSize.y;
        } else {
            pageWidth = getPixelValue(pageWidthField);
            pageHeight = getPixelValue(pageHeightField);
        }
        report.setPageSize(pageWidth, pageHeight);
        report.refreshPageSize();
        report.setOrientation(((byte) ControlUtils.getEntryKey(pageOrientationField, pageOrientations)));
        report.setTopMargin(getPixelValue(topMarginField));
        report.setBottomMargin(getPixelValue(bottomMarginField));
        report.setLeftMargin(getPixelValue(leftMarginField));
        report.setRightMargin(getPixelValue(rightMarginField));
        report.setColumnCount(ControlUtils.getIntValue(columnsField));
        report.setColumnWidth(getPixelValue(columnWidthField));
        report.setColumnSpacing(getPixelValue(columnSpacingField));
        int scriptletHandling = ControlUtils.getEntryKey(scriptletHandlingField, scriptletHandlings);
        report.setScriptletHandling(scriptletHandling);
        report.setScriptletClass(scriptletHandling == ReportConstants.SCRIPTLET_CLASSIC_HANDLING ? scriptletClassField.getText() : null);
        report.setLanguage(languageField.getText());
        report.setPrintOrder(((byte) ControlUtils.getEntryKey(printOrderField, printOrders)));
        report.setWhenNoDataType(((byte) ControlUtils.getEntryKey(whenNoDataField, whenNodataTypes)));
        report.setTitleNewPage(titleOnNewPageField.getSelection());
        report.setSummaryNewPage(summaryOnNewPageField.getSelection());
        report.setFloatColumnFooter(floatingColumnFooterField.getSelection());
        report.setIgnorePagination(ignorePaginationField.getSelection());
        report.setFormatFactoryClass(ControlUtils.getText(formatFactoryClassField));
        report.setResourceBundle(ControlUtils.getText(resourceBundleField));
        report.setWhenResourceMissingType((byte) ControlUtils.getEntryKey(whenResourceMissingTypeField, whenResourceMissingTypes));
        report.setEncoding(ControlUtils.getText(xmlEncodingField));
        if (!modify) {
            return;
        }
        updateEditor();
    }

    public void setReportEditor(ReportEditor reportEditor) {
        this.reportEditor = reportEditor;
    }

    protected void updateEditor() {
        if (reportEditor == null) {
            return;
        }
        reportEditor.makeDirty();
    }

    private List<String> pageFormats;

    private List<Entry> pageOrientations;

    private List<Entry> printOrders;

    private List<Entry> whenNodataTypes;

    private List<Entry> scriptletHandlings;

    private String[] languages;

    private List<Entry> whenResourceMissingTypes;

    private String[] xmlEncodings;

    private void initControls() {
        pageFormats = ReportUtils.getPageFormats();
        pageOrientations = ReportUtils.getPageOrientations();
        printOrders = ReportUtils.getPrintOrders();
        whenNodataTypes = ReportUtils.getWhenNoDataTypes();
        scriptletHandlings = ReportUtils.getScriptletHandlings();
        languages = ReportUtils.getLanguages();
        whenResourceMissingTypes = ReportUtils.getWhenResourceMissingTypes();
        xmlEncodings = ReportUtils.getXMLEncodings();
        ControlUtils.populate(pageFormatField, pageFormats);
        ControlUtils.populateEntries(pageOrientationField, pageOrientations);
        ControlUtils.populateEntries(printOrderField, printOrders);
        ControlUtils.populateEntries(whenNoDataField, whenNodataTypes);
        ControlUtils.populateEntries(scriptletHandlingField, scriptletHandlings);
        ControlUtils.populate(languageField, languages);
        ControlUtils.populateEntries(whenResourceMissingTypeField, whenResourceMissingTypes);
        ControlUtils.populate(xmlEncodingField, xmlEncodings);
    }

    private void enableScriptletClassControl() {
        int scriptletHandling = ControlUtils.getEntryKey(scriptletHandlingField, scriptletHandlings);
        scriptletClassField.setEnabled(scriptletHandling == ReportConstants.SCRIPTLET_CLASSIC_HANDLING);
    }

    private void setPageSizeInControlByFormat() {
        int index = pageFormatField.getSelectionIndex();
        if (index > 0) {
            String format = pageFormats.get(index);
            Point point = PageSize.pageFormats.get(format);
            if (point == null) {
                return;
            }
            setPageSizeInControl(point.x, point.y);
            enablePageSizeControls(false);
        } else {
            enablePageSizeControls(true);
        }
    }

    private void enablePageSizeControls(boolean enable) {
        pageWidthField.setEditable(enable);
        pageHeightField.setEditable(enable);
    }

    private void setPageSizeInControl(int width, int height) {
        setUnitValue(pageWidthField, width);
        setUnitValue(pageHeightField, height);
    }

    private float round(float value) {
        return Math.round(value * 100f) / 100f;
    }

    private void setUnitValue(Text text, int value) {
        Unit unit = report.getUnit();
        if (Unit.PIXELS.equals(unit)) {
            setIntValue(text, value);
            return;
        }
        float unitValue = report.getUnit().convertToUnit(value);
        unitValue = round(unitValue);
        setFormatValue(text, unitValue);
    }

    private int getPixelValue(Text text) {
        return report.getUnit().convertToPixel(getFloatValue(text, df));
    }

    private DecimalFormat df = new DecimalFormat("0.00");

    private void setFormatValue(Text text, float value) {
        text.setText("" + df.format(value));
    }

    protected float getFloatValue(Text text, DecimalFormat format) {
        if (text == null) {
            return 0f;
        }
        String str = text.getText();
        try {
            Number number = format.parse(str);
            return number.floatValue();
        } catch (ParseException e) {
        }
        return 0f;
    }
}
