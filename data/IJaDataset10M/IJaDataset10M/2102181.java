package org.aplikator.client.widgets;

import java.io.Serializable;
import java.util.List;
import org.aplikator.client.Aplikator;
import org.aplikator.client.command.MainPanelTitleCallback;
import org.aplikator.client.data.Operation;
import org.aplikator.client.data.PrimaryKey;
import org.aplikator.client.data.RecordContainer;
import org.aplikator.client.data.Record;
import org.aplikator.client.descriptor.ViewDTO;
import org.aplikator.client.descriptor.PropertyDTO;
import org.aplikator.client.panels.HorizontalFlowPanel;
import org.aplikator.client.panels.SlidingPanel;
import org.aplikator.client.rpc.Callback;
import org.aplikator.client.rpc.impl.GetPage;
import org.aplikator.client.rpc.impl.GetPageResponse;
import org.aplikator.client.rpc.impl.GetRecordCount;
import org.aplikator.client.rpc.impl.GetRecordCountResponse;
import org.aplikator.client.rpc.impl.ProcessRecords;
import org.aplikator.client.rpc.impl.ProcessRecordsResponse;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

public class TableCellWidget extends Composite implements TableInterface {

    public interface TableWidgetResources extends ClientBundle {

        public static final TableWidgetResources INSTANCE = GWT.create(TableWidgetResources.class);

        @Source("images/icons/first24.png")
        ImageResource first();

        @Source("images/icons/prev24.png")
        ImageResource prev();

        @Source("images/icons/next24.png")
        ImageResource next();

        @Source("images/icons/last24.png")
        ImageResource last();

        @Source("images/icons/create24.png")
        ImageResource create();

        @Source("images/icons/open24.png")
        ImageResource open();

        @Source("images/icons/delete24.png")
        ImageResource delete();

        @Source("images/icons/copy24.png")
        ImageResource copy();

        @Source("images/icons/reload24.png")
        ImageResource reload();

        @Source("images/icons/search24.png")
        ImageResource search();

        @Source("images/icons/close24.png")
        ImageResource close();

        @Source("images/icons/save24.png")
        ImageResource save();

        @Source("images/icons/arrowUp16.png")
        ImageResource arrowUp();

        @Source("images/icons/arrowDown16.png")
        ImageResource arrowDown();

        @Source("images/icons/zoomin16.png")
        ImageResource zoomIn();

        @Source("images/icons/zoomout16.png")
        ImageResource zoomOut();

        @Source("TableWidget.css")
        public TableWidgetCss css();
    }

    /**
     * The resources applied to the table.
     */
    interface TableCellResources extends CellTable.Resources {

        public static final TableCellResources INSTANCE = GWT.create(TableCellResources.class);

        @Source({ CellTable.Style.DEFAULT_CSS, "TableCellWidget.css" })
        public TableStyle cellTableStyle();
    }

    /**
     * The styles applied to the table.
     */
    interface TableStyle extends CellTable.Style {

        String spacerColumn();
    }

    public interface TableWidgetCss extends CssResource {

        public String tableBody();

        public String tableColumn();

        public String tableHeader();

        public String tableOddRow();

        public String tableEvenRow();

        public String tableSelectedRow();

        public String tableNavigationPanel();
    }

    static {
        TableWidgetResources.INSTANCE.css().ensureInjected();
        TableCellResources.INSTANCE.cellTableStyle().ensureInjected();
    }

    private final SlidingPanel dataPanel;

    private final HorizontalFlowPanel navigationPanel;

    private final InlineHTML statusLabel;

    private final Label searchID;

    private final TextBox searchField;

    private final Image buttonSearch;

    private Image buttonCreate;

    private Image buttonOpen;

    private Image buttonDelete;

    private final Image buttonCopy;

    private final Image buttonReload;

    private final Image buttonClose;

    private final Image buttonSave;

    private final CellTable<Record> grid;

    private SimplePager pager;

    private FormWidget form;

    private FlowPanel gridWrapper;

    private FlowPanel formWrapper;

    private boolean gridMode;

    private MainPanelTitleCallback titleCallback;

    private ViewDTO view;

    private List<Record> pageData;

    private int pageOffset = 0;

    private int pageSize = 0;

    private Record selectedRecord = null;

    private int recordCount = 0;

    private PrimaryKey ownerPrimaryKey;

    private PropertyDTO<Integer> ownerProperty;

    private FormWidget ownerForm;

    RecordContainer recordContainer;

    public TableCellWidget(ViewDTO view, PropertyDTO<Integer> ownerProperty, FormWidget ownerForm) {
        super();
        this.view = view;
        this.pageSize = view.getPageSize();
        this.ownerProperty = ownerProperty;
        this.ownerForm = ownerForm;
        final DockLayoutPanel basePanel = new DockLayoutPanel(Unit.EM);
        initWidget(basePanel);
        navigationPanel = new HorizontalFlowPanel();
        basePanel.addNorth(navigationPanel, 2);
        grid = new CellTable<Record>(this.pageSize, TableCellResources.INSTANCE);
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(grid);
        pager.setWidth("200px");
        navigationPanel.add(pager);
        statusLabel = new InlineHTML();
        navigationPanel.add(statusLabel);
        statusLabel.setWordWrap(false);
        buttonCreate = new Image(TableWidgetResources.INSTANCE.create());
        navigationPanel.add(buttonCreate);
        buttonCreate.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                TableCellWidget.this.buttonCreateClicked();
            }
        });
        buttonOpen = new Image(TableWidgetResources.INSTANCE.open());
        navigationPanel.add(buttonOpen);
        buttonOpen.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                TableCellWidget.this.buttonOpenClicked();
            }
        });
        buttonDelete = new Image(TableWidgetResources.INSTANCE.delete());
        navigationPanel.add(buttonDelete);
        buttonDelete.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                TableCellWidget.this.buttonDeleteClicked();
            }
        });
        buttonCopy = new Image(TableWidgetResources.INSTANCE.copy());
        navigationPanel.add(buttonCopy);
        buttonCopy.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                TableCellWidget.this.buttonCopyClicked();
            }
        });
        buttonReload = new Image(TableWidgetResources.INSTANCE.reload());
        navigationPanel.add(buttonReload);
        buttonReload.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                TableCellWidget.this.buttonReloadClicked();
            }
        });
        InlineHTML strutS = new InlineHTML("&nbsp;");
        strutS.getElement().getStyle().setProperty("marginLeft", "20px");
        navigationPanel.add(strutS);
        searchID = new InlineHTML();
        searchID.setWordWrap(false);
        searchField = new TextBox();
        searchField.setWidth("200px");
        buttonSearch = new Image(TableWidgetResources.INSTANCE.search());
        buttonSearch.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                TableCellWidget.this.buttonSearchClicked();
            }
        });
        adjustSearchLabel();
        navigationPanel.add(searchID);
        navigationPanel.add(searchField);
        navigationPanel.add(buttonSearch);
        InlineHTML strut2 = new InlineHTML("&nbsp;");
        strut2.getElement().getStyle().setProperty("marginLeft", "20px");
        navigationPanel.add(strut2);
        buttonClose = new Image(TableWidgetResources.INSTANCE.close());
        buttonClose.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                TableCellWidget.this.buttonCloseClicked();
            }
        });
        navigationPanel.add(buttonClose);
        buttonSave = new Image(TableWidgetResources.INSTANCE.save());
        buttonSave.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                TableCellWidget.this.buttonSaveClicked();
            }
        });
        navigationPanel.add(buttonSave);
        navigationPanel.addRight(new Label(""));
        showStatus();
        dataPanel = new SlidingPanel();
        dataPanel.addStyleName("tabledatapanel");
        basePanel.add(dataPanel);
        basePanel.addStyleName("tablebasepanel");
        final SingleSelectionModel<Record> selectionModel = new SingleSelectionModel<Record>(Record.KEY_PROVIDER);
        grid.setSelectionModel(selectionModel);
        initTableColumns(selectionModel, this.view);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            public void onSelectionChange(SelectionChangeEvent event) {
                selectedRecord = selectionModel.getSelectedObject();
                buttonOpenClicked();
            }
        });
        grid.addRangeChangeHandler(new RangeChangeEvent.Handler() {

            public void onRangeChange(RangeChangeEvent event) {
                pageOffset = grid.getPageStart();
                loadData();
            }
        });
        form = new FormWidget(this.view, this, this.ownerForm);
        form.setStyleName(TableWidgetResources.INSTANCE.css().tableBody());
        gridWrapper = new FlowPanel();
        gridWrapper.add(grid);
        gridWrapper.addStyleName("gridwarpper");
        formWrapper = new FlowPanel();
        formWrapper.add(form);
        formWrapper.addStyleName("formWarpper");
        dataPanel.add(gridWrapper);
        dataPanel.add(formWrapper);
        gridMode = true;
        adjustNavigationPanel();
        drawHeader();
        navigationPanel.setStyleName(TableWidgetResources.INSTANCE.css().tableNavigationPanel());
    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns(final SelectionModel<Record> selectionModel, final ViewDTO view) {
        grid.addColumn(new SpacerColumn<Record>());
        Column<Record, Boolean> checkColumn = new Column<Record, Boolean>(new CheckboxCell(true, true)) {

            @Override
            public Boolean getValue(Record object) {
                return selectionModel.isSelected(object);
            }
        };
        checkColumn.setFieldUpdater(new FieldUpdater<Record, Boolean>() {

            public void update(int index, Record object, Boolean value) {
                selectionModel.setSelected(object, value);
            }
        });
        grid.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br>"));
        for (int j = 0; j < view.getProperties().size(); j++) {
            PropertyColumn column = new PropertyColumn(view.getProperties().get(j));
            grid.addColumn(column, view.getProperties().get(j).getLocalizedName());
        }
        grid.addColumn(new SpacerColumn<Record>());
        grid.addColumnStyleName(0, TableCellResources.INSTANCE.cellTableStyle().spacerColumn());
        grid.addColumnStyleName(view.getProperties().size() + 2, TableCellResources.INSTANCE.cellTableStyle().spacerColumn());
    }

    private class PropertyColumn extends Column<Record, String> {

        public PropertyColumn(PropertyDTO<? extends Serializable> property) {
            super(new TextCell());
            this.property = property;
        }

        PropertyDTO<? extends Serializable> property;

        @Override
        public String getValue(Record object) {
            return property.getValue(object).toString();
        }
    }

    private void switchToGridMode() {
        gridMode = true;
        adjustNavigationPanel();
        dataPanel.setWidget(gridWrapper);
    }

    private void switchToFormMode() {
        gridMode = false;
        adjustNavigationPanel();
        dataPanel.setWidget(formWrapper);
    }

    public void reload() {
        if (view.getQueryParameters() != null && view.getQueryParameters().length > 0) {
            final DialogBox dialogBox = createParamDialogBox();
            dialogBox.setAnimationEnabled(true);
            dialogBox.center();
        } else {
            loadData();
        }
    }

    private void adjustSearchLabel() {
        if (this.view.getSortProperty() != null) {
            searchID.setText("Search in " + this.view.getSortProperty().getLocalizedName() + ": ");
            searchID.setVisible(true);
            searchField.setVisible(true);
            buttonSearch.setVisible(true);
        } else {
            searchID.setText("Not sorted");
            searchID.setVisible(false);
            searchField.setVisible(false);
            buttonSearch.setVisible(false);
        }
    }

    private void drawHeader() {
        for (int i = 0; i < this.view.getProperties().size(); i++) {
            PropertyDTO<? extends Serializable> prop = this.view.getProperties().get(i);
            if (prop.equals(this.view.getSortProperty())) {
                if (this.view.isSortAscending()) {
                } else {
                }
            } else {
            }
        }
    }

    private void loadData() {
        Aplikator.aplikatorService.execute(new GetRecordCount(view, ownerProperty, ownerPrimaryKey, false), new Callback<GetRecordCountResponse>() {

            public void process(GetRecordCountResponse resp) {
                recordCount = resp.getRecordCount();
                showStatus();
            }
        });
        Aplikator.aplikatorService.execute(new GetPage(view, ownerProperty, ownerPrimaryKey, pageOffset, pageSize), new Callback<GetPageResponse>() {

            public void process(GetPageResponse resp) {
                TableCellWidget.this.pageData = resp.getRecords();
                redrawPage();
                showStatus();
            }
        });
    }

    private void redrawPage() {
        grid.setRowData(grid.getPageStart(), pageData);
    }

    private DialogBox createParamDialogBox() {
        VerticalPanel dialogContents = new VerticalPanel();
        final DialogBox dialogBox = new DialogBox();
        dialogBox.setText("Input query parameters");
        dialogBox.setWidget(dialogContents);
        dialogContents.setSpacing(4);
        dialogBox.setWidget(dialogContents);
        final int paramCount = view.getQueryParameters().length;
        final TextBox[] paramValues = new TextBox[paramCount];
        for (int i = 0; i < paramCount; i++) {
            HorizontalPanel paramPane = new HorizontalPanel();
            paramPane.add(new Label(view.getQueryParameters()[i].getName()));
            paramValues[i] = new TextBox();
            paramPane.add(paramValues[i]);
            dialogContents.add(paramPane);
        }
        Button closeButton = new Button("OK", new ClickHandler() {

            public void onClick(ClickEvent event) {
                dialogBox.hide();
                StringBuilder title = new StringBuilder();
                for (int i = 0; i < paramCount; i++) {
                    view.getQueryParameters()[i].setValue(paramValues[i].getText());
                    title.append(view.getQueryParameters()[i].getName());
                    title.append(": ");
                    title.append(view.getQueryParameters()[i].getValue());
                    if (i < paramCount - 1) {
                        title.append(", ");
                    }
                }
                if (titleCallback != null) {
                    titleCallback.setTitle(title.toString());
                }
                loadData();
            }
        });
        dialogContents.add(closeButton);
        if (LocaleInfo.getCurrentLocale().isRTL()) {
            dialogContents.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_LEFT);
        } else {
            dialogContents.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);
        }
        return dialogBox;
    }

    public void applySorting(PropertyDTO<? extends Serializable> sortProperty, boolean ascending) {
        this.view.setSortProperty(sortProperty);
        this.view.setSortAscending(ascending);
        adjustSearchLabel();
        drawHeader();
        loadData();
    }

    public PrimaryKey getSelectedPrimaryKey() {
        return getSelectedRecord().getPrimaryKey();
    }

    public Record getSelectedRecord() {
        return this.selectedRecord;
    }

    private void buttonSearchClicked() {
        view.setSearchString(searchField.getText());
        search();
    }

    public void search() {
        if (view.getSearchString() != null && !"".equals(view.getSearchString())) {
            Aplikator.aplikatorService.execute(new GetRecordCount(view, ownerProperty, ownerPrimaryKey, true), new Callback<GetRecordCountResponse>() {

                public void process(GetRecordCountResponse resp) {
                    pageOffset = resp.getRecordCount();
                    loadData();
                }
            });
        } else {
            loadData();
        }
    }

    private void showStatus() {
        String text = "";
        grid.setRowCount(recordCount, false);
        statusLabel.setText(text);
    }

    private void buttonCreateClicked() {
        switchToFormMode();
        if (ownerProperty != null) {
            form.addRecord(recordContainer, ownerProperty, ownerPrimaryKey, null);
        } else {
            form.addRecord(new RecordContainer(), null, null, null);
        }
    }

    private void buttonOpenClicked() {
        switchToFormMode();
        openForm();
    }

    private void buttonCloseClicked() {
        closeForm(new Command() {

            public void execute() {
                switchToGridMode();
            }
        });
    }

    private void closeForm(Command afterConfirmation) {
        form.onClose(afterConfirmation);
    }

    private void openForm() {
        if (ownerProperty != null) {
            form.displayRecord(getSelectedPrimaryKey(), recordContainer);
        } else {
            form.displayRecord(getSelectedPrimaryKey(), new RecordContainer());
        }
    }

    private void buttonSaveClicked() {
        form.save();
    }

    public void save() {
        if (form != null) {
            form.save();
        }
    }

    private void buttonDeleteClicked() {
        if (ownerProperty != null) {
            recordContainer.addRecord(view, getSelectedRecord(), null, Operation.DELETE);
            pageData.remove(selectedRecord);
            redrawPage();
        } else {
            RecordContainer newContainer = new RecordContainer();
            newContainer.addRecord(view, getSelectedRecord(), null, Operation.DELETE);
            Aplikator.aplikatorService.execute(new ProcessRecords(newContainer), new Callback<ProcessRecordsResponse>() {

                public void process(ProcessRecordsResponse records) {
                    pageData.remove(selectedRecord);
                    redrawPage();
                }
            });
        }
    }

    private void buttonCopyClicked() {
        switchToFormMode();
        if (ownerProperty != null) {
            form.addRecord(recordContainer, ownerProperty, ownerPrimaryKey, getSelectedRecord());
        } else {
            form.addRecord(new RecordContainer(), null, null, getSelectedRecord());
        }
    }

    private void buttonReloadClicked() {
        reload();
    }

    public void addRecord(Record record) {
        pageData.add(record);
        redrawPage();
    }

    public void updateRecord(Record record) {
        for (int i = 0; i < pageData.size(); i++) {
            Record rec = pageData.get(i);
            if (rec.getPrimaryKey().equals(record.getPrimaryKey())) {
                pageData.set(i, record);
                break;
            }
        }
        redrawPage();
    }

    public PrimaryKey getOwnerPrimaryKey() {
        return ownerPrimaryKey;
    }

    public void setOwnerPrimaryKey(PrimaryKey value) {
        ownerPrimaryKey = value;
        reload();
    }

    public void setRecordContainer(RecordContainer recordContainer) {
        this.recordContainer = recordContainer;
    }

    private void adjustNavigationPanel() {
        buttonOpen.setVisible(gridMode);
        buttonReload.setVisible(gridMode);
        buttonClose.setVisible(!gridMode);
        buttonSave.setVisible(!gridMode && (ownerForm == null));
        statusLabel.setVisible(gridMode);
    }

    public void setTitleCallback(MainPanelTitleCallback titleCallback) {
        this.titleCallback = titleCallback;
    }

    public void setDirty(boolean dirty) {
    }
}
