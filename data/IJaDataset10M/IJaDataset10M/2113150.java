package org.gwtoolbox.sample.widget.client.table;

import java.util.List;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import org.gwtoolbox.commons.types.client.Gender;
import org.gwtoolbox.ioc.core.client.annotation.Component;
import org.gwtoolbox.ioc.core.client.annotation.Order;
import org.gwtoolbox.sample.widget.client.SamplePanel;
import static org.gwtoolbox.widget.client.panel.LayoutUtils.addGap;
import org.gwtoolbox.widget.client.WidgetImages;
import org.gwtoolbox.widget.client.button.SimpleButton;
import org.gwtoolbox.widget.client.data.DataTypes;
import org.gwtoolbox.widget.client.data.Record;
import org.gwtoolbox.widget.client.notification.AbstractCallback;
import org.gwtoolbox.widget.client.panel.LayoutUtils;
import org.gwtoolbox.widget.client.panel.contentpanel.ContentPanel;
import org.gwtoolbox.widget.client.table.basic.BasicTable;
import org.gwtoolbox.widget.client.table.basic.selection.CellSelectionFilter;
import org.gwtoolbox.widget.client.table.basic.selection.SingleCellSelectionModel;
import org.gwtoolbox.widget.client.table.datagrid.OldDataGrid;
import org.gwtoolbox.widget.client.table.datagrid.FieldGroup;
import org.gwtoolbox.widget.client.table.datagrid.RecordRowDecorator;
import org.gwtoolbox.widget.client.table.datagrid.column.Column;
import org.gwtoolbox.widget.client.table.datagrid.column.ColumnSpec;
import org.gwtoolbox.widget.client.table.datagrid.column.FieldColumn;
import org.gwtoolbox.widget.client.table.datagrid.column.RowNumberValueExtractor;
import org.gwtoolbox.widget.client.table.datagrid.selection.SingleRecordSelectionModel;
import org.gwtoolbox.widget.client.table.datagrid.selection.MultiRecordSelectionModel;
import org.gwtoolbox.widget.client.table.datagrid.selection.MultiRecordSelection;

/**
 * @author Uri Boness
 */
@Component
@Order(2)
@TableSample
public class DataGridSamplePane extends Composite implements SamplePanel {

    private final OldDataGrid grid;

    @SuppressWarnings({ "unchecked" })
    public DataGridSamplePane() {
        Column indexColumn = new Column("#", new RowNumberValueExtractor(), DataTypes.INT, "30px", null);
        FieldColumn idColumn = new FieldColumn("ID", "id", DataTypes.INT, "50px", HasHorizontalAlignment.ALIGN_CENTER);
        FieldColumn genderColumn = new FieldColumn("Gender", "gender", DataTypes.GENDER, "100px", HasHorizontalAlignment.ALIGN_CENTER);
        genderColumn.setSortable(true);
        genderColumn.setGroupable(true);
        FieldColumn nameColumn = new FieldColumn("Name", "name", DataTypes.TEXT, "150px");
        nameColumn.setSortable(true);
        FieldColumn ageColumn = new FieldColumn("Age", "age", DataTypes.INT, null, HasHorizontalAlignment.ALIGN_CENTER);
        ageColumn.setSortable(true);
        ColumnSpec columns = new ColumnSpec().addColumn(indexColumn).addColumn(idColumn).addColumn(genderColumn).addColumn(nameColumn).addColumn(ageColumn);
        grid = new OldDataGrid(columns);
        grid.setVisible(false);
        grid.setWidth("100%");
        grid.setHeight("100%");
        grid.setDataSource(new PersonDataSource());
        grid.fetch(new AbstractCallback<Void>() {

            public void onSuccess(Void v) {
                grid.setVisible(true);
            }
        });
        final ContentPanel contentPanel = new ContentPanel("People", WidgetImages.Instance.get().icon_Table().createImage());
        contentPanel.setWidth("700px");
        contentPanel.setContent(grid);
        contentPanel.setCollapsable(true);
        HorizontalPanel top = new HorizontalPanel();
        top.add(contentPanel);
        addGap(top, "20px");
        final SimplePanel selectionPane = new SimplePanel();
        selectionPane.setWidth("500px");
        top.add(selectionPane);
        VerticalPanel main = new VerticalPanel();
        main.add(top);
        main.setCellWidth(top, "100%");
        addGap(main, "20px");
        SimpleButton highlightButton = new SimpleButton("Highlight All Females");
        highlightButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent clickEvent) {
                grid.setRowDecorator(new RecordRowDecorator<Record>() {

                    public void decorateRow(int row, Record record, BasicTable.BasicTableRowFormatter rowFormatter) {
                        if (record.getValue("gender") == Gender.FEMALE) {
                            rowFormatter.addStyleName(row, "row-highlight");
                        }
                    }
                });
            }
        });
        SimpleButton clearHighlightButton = new SimpleButton("Clear Hightlighting");
        clearHighlightButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent clickEvent) {
                grid.clearRowDecorator();
            }
        });
        main.add(LayoutUtils.hBuilder().add(highlightButton).addGap("20px").add(clearHighlightButton).getPanel());
        addGap(main, "20px");
        SimpleButton groupButton = new SimpleButton("Group By Gender");
        groupButton.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                FieldGroup group = new FieldGroup("Gender", "gender");
                group.setShowCounts(true);
                grid.setGroupBy(group);
            }
        });
        SimpleButton clearGroupingButton = new SimpleButton("Clear Grouping");
        clearGroupingButton.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                grid.setGroupBy(null);
            }
        });
        main.add(LayoutUtils.hBuilder().add(groupButton).addGap("20px").add(clearGroupingButton).getPanel());
        addGap(main, "20px");
        SimpleButton singleSelectionButton = new SimpleButton("Set Single Selection Model");
        singleSelectionButton.addClickListener(new ClickListener() {

            public void onClick(Widget widget) {
                applySingleSelection(grid, selectionPane);
                selectionPane.setVisible(true);
            }
        });
        SimpleButton multiSelectionButton = new SimpleButton("Set Multi Selection Model");
        multiSelectionButton.addClickListener(new ClickListener() {

            public void onClick(Widget widget) {
                applyMultiSelection(grid, selectionPane);
                selectionPane.setVisible(true);
            }
        });
        SimpleButton clearSelectionButton = new SimpleButton("Clear Selection Model");
        clearSelectionButton.addClickListener(new ClickListener() {

            public void onClick(Widget widget) {
                grid.clearSelectionModel();
                selectionPane.setVisible(false);
            }
        });
        main.add(LayoutUtils.hBuilder().add(singleSelectionButton).addGap("20px").add(multiSelectionButton).addGap("20px").add(clearSelectionButton).getPanel());
        addGap(main, "20px");
        RadioButton listRadio = new RadioButton("mode", "List");
        listRadio.addClickListener(new ClickListener() {

            public void onClick(Widget widget) {
                grid.setPageSize(null);
            }
        });
        listRadio.setChecked(true);
        RadioButton pageRadio = new RadioButton("mode", "Page");
        pageRadio.addClickListener(new ClickListener() {

            public void onClick(Widget widget) {
                grid.setPageSize(10);
            }
        });
        main.add(LayoutUtils.hBuilder().add(new Label("Mode:")).addGap("20px").add(listRadio).addGap("20px").add(pageRadio).getPanel());
        SimplePanel sp = new SimplePanel();
        sp.setWidget(main);
        initWidget(sp);
    }

    public String getName() {
        return "OldDataGrid";
    }

    public Widget getContentWidget() {
        return this;
    }

    public void reset() {
    }

    private void applySingleSelection(OldDataGrid grid, SimplePanel selectionPane) {
        SingleRecordSelectionModel selectionModel = new SingleRecordSelectionModel();
        grid.setSelectionModel(selectionModel);
        final RecordPanel recordPanel = new RecordPanel();
        recordPanel.setWidth("250px");
        selectionModel.addListener(new SingleRecordSelectionModel.Listener() {

            public void selectionCleared() {
                recordPanel.reset(null);
            }

            public void recordSelected(Record record) {
                recordPanel.reset(record);
            }
        });
        selectionPane.setWidget(recordPanel);
    }

    private void applyMultiSelection(OldDataGrid grid, SimplePanel selectionPane) {
        MultiRecordSelectionModel selectionModel = new MultiRecordSelectionModel();
        grid.setSelectionModel(selectionModel);
        final RecordsPanel recordsPanel = new RecordsPanel();
        selectionModel.addListener(new MultiRecordSelectionModel.Listener() {

            public void recordSelected(Record record, MultiRecordSelection model) {
                recordsPanel.reset(model.getSelectedRecords());
            }

            public void recordUnselected(Record record, MultiRecordSelection model) {
                recordsPanel.reset(model.getSelectedRecords());
            }

            public void selectionCleared() {
                recordsPanel.reset(null);
            }
        });
        selectionPane.setWidget(recordsPanel);
    }

    private class RecordPanel extends Composite {

        private BasicTable main;

        private RecordPanel() {
            main = new BasicTable();
            main.setWidth("100%");
            main.setHeaderText(0, "Field");
            main.setHeaderText(1, "Value");
            SingleCellSelectionModel selectionModel = new SingleCellSelectionModel();
            selectionModel.setSelectionFilter(new CellSelectionFilter() {

                public boolean isSelectable(int row, int column) {
                    return column != 0;
                }
            });
            main.setSelectionModel(selectionModel);
            CaptionPanel cp = new CaptionPanel("Selected Person", true);
            cp.setContentWidget(main);
            cp.setSize("100%", "200px");
            initWidget(cp);
        }

        public void reset(Record record) {
            main.removeAllRows();
            if (record == null) {
                return;
            }
            int row = 0;
            for (String field : record.getFields()) {
                main.setText(row, 0, field);
                main.setText(row, 1, String.valueOf(record.getValue(field)));
                main.getCellFormatter().setStyleName(row, 0, "FieldColumn");
                row++;
            }
        }
    }

    private class RecordsPanel extends Composite {

        private BasicTable main;

        private RecordsPanel() {
            main = new BasicTable();
            main.setWidth("100%");
            main.setHeaderText(0, "ID");
            main.setHeaderText(1, "Name");
            main.setHeaderText(2, "Age");
            main.setHeaderText(3, "Gender");
            CaptionPanel cp = new CaptionPanel("Selected People", true);
            cp.setContentWidget(main);
            cp.setWidth("100%");
            initWidget(cp);
        }

        public void reset(List<Record> records) {
            main.removeAllRows();
            if (records == null || records.isEmpty()) {
                return;
            }
            int row = 0;
            for (Record record : records) {
                main.setText(row, 0, String.valueOf(record.getLongValue("id")));
                main.setText(row, 1, record.getStringValue("name"));
                main.setText(row, 2, String.valueOf(record.getIntValue("age")));
                main.setText(row, 3, String.valueOf(record.getValue("gender")));
                row++;
            }
        }
    }
}
