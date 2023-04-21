package org.gwt.advanced.client.misc;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.gwt.advanced.client.datamodel.*;
import org.gwt.advanced.client.ui.EditCellListener;
import org.gwt.advanced.client.ui.GridListenerAdapter;
import org.gwt.advanced.client.ui.GridPanelFactory;
import org.gwt.advanced.client.ui.SelectRowListener;
import org.gwt.advanced.client.ui.widget.*;
import org.gwt.advanced.client.ui.widget.border.Border;
import org.gwt.advanced.client.ui.widget.border.BorderFactory;
import org.gwt.advanced.client.ui.widget.border.RoundCornerBorder;
import org.gwt.advanced.client.ui.widget.border.SingleBorder;
import org.gwt.advanced.client.ui.widget.cell.*;
import org.gwt.advanced.client.ui.widget.tab.TabPosition;
import org.gwt.advanced.client.ui.widget.tab.TopBandRenderer;
import org.gwt.advanced.client.util.ThemeHelper;
import java.util.Date;

public class Sample {

    public static void sample1() {
        EditableGrid<Editable> grid = new EditableGrid<Editable>(new String[] { "First Name", "Surname" }, new Class[] { LabelCell.class, LabelCell.class });
        Editable model = new EditableGridDataModel(new Object[][] { new String[] { "John", "Doe" }, new String[] { "Piter", "Walkman" }, new String[] { "Rupert", "Brown" } });
        grid.setModel(model);
        GridPanel panel = new GridPanel();
        panel.getMediator().addGridListener(new GridListenerAdapter() {

            public void onSave(GridDataModel dataModel) {
            }
        });
    }

    public static void sample2() {
        Editable model = new EditableGridDataModel(new Object[][] { new String[] { "John", "Doe" }, new String[] { "Piter", "Walkman" }, new String[] { "Rupert", "Brown" } });
        HierarchicalGridDataModel hierarchicalModel = new HierarchicalGridDataModel(new Object[][] { new Object[] { "Accountants", 3 }, new Object[] { "Management", 10 }, new Object[] { "Development", 100 } });
        hierarchicalModel.addSubgridModel(0, 0, model);
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    public static void sample3() {
        LazyLoadable model = new LazyGridDataModel(new DataModelCallbackHandler<Editable>() {

            public void synchronize(Editable model) {
                ((LazyLoadable) model).setTotalRowCount(1000);
            }
        });
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    public static void sample4() {
        LazyLoadable model = new LazyGridDataModel(new DataModelCallbackHandler<Editable>() {

            public void synchronize(Editable model) {
                Object[][] removedRows = model.getRemovedRows();
                model.clearRemovedRows();
            }
        });
    }

    public static void sample5() {
        AdvancedFlexTable table = new AdvancedFlexTable();
        table.setHeaderWidget(0, new Label("First Name"));
        table.setHeaderWidget(1, new Label("Surname"));
        table.enableVerticalScrolling(true);
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    public static void sample6() {
        Editable model = new EditableGridDataModel(null);
        GridPanel panel = new GridPanel();
        EditableGrid grid = panel.createEditableGrid(new String[] { "First Name", "Surname" }, new Class[] { LabelCell.class, LabelCell.class }, model);
        panel.display();
    }

    @SuppressWarnings({ "UnnecessaryLocalVariable" })
    public static void sample7() {
        Editable hierarchicalModel = new HierarchicalGridDataModel(null);
        GridPanel panel = new GridPanel();
        HierarchicalGrid grid = (HierarchicalGrid) panel.createEditableGrid(new String[] { "Department", "Number of Employees" }, new Class[] { LabelCell.class, IntegerCell.class }, null);
        grid.setModel(hierarchicalModel);
        grid.addGridPanelFactory(1, new GridPanelFactory() {

            public GridPanel create(GridDataModel model) {
                GridPanel panel = new GridPanel();
                return panel;
            }

            public GridDataModel create(int parentRow, GridDataModel parentModel) {
                return new EditableGridDataModel(new Object[0][0]);
            }
        });
        panel.display();
        RootPanel.get().add(panel);
    }

    public static void sample8() {
        GridPanel panel = new GridPanel();
        panel.setTopPagerVisible(false);
        panel.setBottomPagerVisible(true);
        panel.setTopToolbarVisible(false);
        panel.setBottomToolbarVisible(true);
        panel.setInvisibleColumn(0, true);
        panel.setSortableColumn(1, false);
        panel.setReadonlyColumn(2, true);
    }

    public static void sample9() {
        MasterDetailPanel panel = new MasterDetailPanel();
        GridPanel masterPanel = new GridPanel();
        panel.addGridPanel(masterPanel, null, "Departments");
        masterPanel.display();
        GridPanel detailPanel = new GridPanel();
        panel.addGridPanel(detailPanel, masterPanel, "Employees");
        detailPanel.display();
        panel.display();
    }

    public static void sample10() {
        DatePicker picker = new DatePicker(new Date());
        picker.setTimeVisible(false);
    }

    public static void sample11() {
        ThemeHelper helper = ThemeHelper.getInstance();
        Window.alert("Current theme is " + helper.getThemeName());
        helper.setThemeName("gray");
        Window.alert("Current theme is " + helper.getThemeName());
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    public static void sample12() {
        Editable model = new EditableGridDataModel(null);
        GridPanel panel = new GridPanel();
        EditableGrid grid = panel.createEditableGrid(new String[] { "First Name", "Surname" }, new Class[] { LabelCell.class, LabelCell.class }, model);
        panel.display();
        RootPanel.get().add(panel);
    }

    @SuppressWarnings({ "UnusedDeclaration", "unchecked" })
    public static void sample13() {
        TreeGridDataModel model = new TreeGridDataModel(new Object[][] { new String[] { "President" } });
        model.setAscending(false);
        model.setPageSize(3);
        TreeGridRow president = (TreeGridRow) model.getRow(0);
        president.setExpanded(true);
        president.setPageSize(3);
        president.setPagerEnabled(true);
        model.addRow(president, new String[] { "Financial Department Director" });
        model.addRow(president, new String[] { "Marketing Department Director" });
        model.addRow(president, new String[] { "Chief Security Officer" });
        model.addRow(president, new String[] { "Development Department Director" });
        TreeGridRow financialDirector = model.getRow(president, 0);
        model.addRow(financialDirector, new String[] { "Accountant" });
        model.addRow(financialDirector, new String[] { "Financial Manager" });
        TreeGridRow marketingDirector = model.getRow(president, 0);
        model.addRow(marketingDirector, new String[] { "Brand Manager" });
        model.addRow(marketingDirector, new String[] { "Sales manager" });
        model.addRow(marketingDirector, new String[] { "Promouter" });
        TreeGridRow developmentDirector = model.getRow(president, 0);
        president.setPageSize(3);
        president.setPagerEnabled(true);
        model.addRow(marketingDirector, new String[] { "Database Developer" });
        model.addRow(marketingDirector, new String[] { "UI Developer" });
        model.addRow(marketingDirector, new String[] { "Support Engeneer" });
        model.addRow(marketingDirector, new String[] { "Tester" });
        GridPanel panel = new GridPanel();
        panel.createEditableGrid(new String[] { "Posts" }, new Class[] { TextBoxCell.class }, null).setModel(model);
        RootPanel.get().add(panel);
    }

    public static void sample14() {
        SimpleGrid grid = new SimpleGrid();
        grid.setHeaderWidget(0, new Label("First Name"));
        grid.setHeaderWidget(1, new Label("Surname"));
        grid.enableVerticalScrolling(true);
        grid.setColumnWidth(0, 200);
        grid.setColumnWidth(0, 100);
    }

    public static void sample15() {
        SuggestionBoxDataModel model = new SuggestionBoxDataModel(new ListCallbackHandler() {

            public void fill(ListDataModel model) {
                if ("John".equals(((SuggestionBoxDataModel) model).getExpression())) {
                    model.clear();
                    model.add("john1", "John Doe");
                    model.add("john2", "John Parkinson");
                    model.add("john3", "John Todd");
                } else {
                    model.clear();
                    model.add("", "Nobody");
                }
            }
        });
        SuggestionBox box = new SuggestionBox();
        box.setModel(model);
        box.setMaxLength(3);
    }

    public static void sample16() {
        AdvancedTabPanel panel1 = new AdvancedTabPanel(TabPosition.LEFT);
        AdvancedTabPanel panel2 = new AdvancedTabPanel(TabPosition.BOTTOM);
        panel1.addTab(new Label("Nested Tabs"), panel2);
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    public static void sample17() {
        BorderFactory contentbBorderFactory = new BorderFactory() {

            public Border create() {
                return new SingleBorder();
            }
        };
        BorderFactory tabBorderFactory = new BorderFactory() {

            public Border create() {
                return new RoundCornerBorder();
            }
        };
        AdvancedTabPanel panel = new AdvancedTabPanel(TabPosition.TOP, tabBorderFactory, contentbBorderFactory);
    }

    @SuppressWarnings({ "unchecked" })
    public static void quickstart() {
        Editable model = new EditableGridDataModel(new Object[][] { new String[] { "John", "Doe" }, new String[] { "Piter", "Walkman" }, new String[] { "Rupert", "Brown" } });
        GridPanel panel = new GridPanel();
        panel.createEditableGrid(new String[] { "First Name", "Surname" }, new Class[] { LabelCell.class, LabelCell.class }, null).setModel(model);
        RootPanel.get().add(panel);
    }

    @SuppressWarnings({ "EmptyTryBlock" })
    public class MyHandler implements DataModelCallbackHandler {

        private GridPanel panel;

        public MyHandler(GridPanel panel) {
            this.panel = panel;
        }

        public void synchronize(GridDataModel model) {
            panel.lock();
            try {
            } finally {
                panel.unlock();
            }
        }
    }

    public class MyCell extends AbstractCell {

        private Button button = new Button("Sample");

        protected Widget createActive() {
            button.setEnabled(true);
            return button;
        }

        protected Widget createInactive() {
            button.setEnabled(false);
            return button;
        }

        public void setFocus(boolean focus) {
        }

        public Object getNewValue() {
            return button;
        }
    }

    public class MyValidator implements EditCellListener {

        public boolean onStartEdit(GridCell cell) {
            return true;
        }

        public boolean onFinishEdit(GridCell cell, Object newValue) {
            return cell.getColumn() != 0 || !"wrong".equals(newValue);
        }
    }

    public class MyGridPanelFactory implements GridPanelFactory {

        public GridPanel create(GridDataModel model) {
            GridPanel panel = new GridPanel();
            panel.createEditableGrid(new String[] { "First Name", "Surname" }, new Class[] { LabelCell.class, LabelCell.class }, (Editable) model);
            return panel;
        }

        public GridDataModel create(int parentRow, GridDataModel parentModel) {
            return new EditableGridDataModel(new Object[0][0]);
        }
    }

    public class MyRowSelectionListener implements SelectRowListener {

        public void onSelect(EditableGrid grid, int row) {
            Window.alert("Row number " + row);
        }
    }

    public class MyGridEventManager extends DefaultGridEventManager {

        public MyGridEventManager(GridPanel panel) {
            super(panel);
        }

        public boolean dispatch(GridPanel panel, char keyCode, int modifiers) {
            if (keyCode == KeyCodes.KEY_TAB) moveToNextCell(); else return super.dispatch(panel, keyCode, modifiers);
            return false;
        }
    }

    public class MyGridRenderer extends DefaultGridRenderer {

        public MyGridRenderer(EditableGrid grid) {
            super(grid);
        }

        public void drawHeaders(Object[] headers) {
            for (int i = 0; i < headers.length; i++) {
                Object header = headers[i];
                getGrid().setHeaderWidget(i, new Label(String.valueOf(header)));
            }
        }
    }

    public class ServerSideContentRenderer extends DefaultGridRenderer {

        private String html;

        public ServerSideContentRenderer(EditableGrid grid, String html) {
            super(grid);
            this.html = html;
        }

        public void drawContent(GridDataModel model) {
            DOM.setInnerHTML(getTBodyElement(), html);
        }
    }

    public static class MyTabPosition extends TabPosition {

        public static final TabPosition CUSTOM = new MyTabPosition("custom");

        protected MyTabPosition(String name) {
            super(name, new MyTabBandRenderer(), LayoutPosition.TOP);
        }
    }

    @SuppressWarnings({ "UnnecessaryLocalVariable" })
    public static class MyTabBandRenderer extends TopBandRenderer {

        public Widget render(AdvancedTabPanel panel) {
            Widget tab = super.render(panel);
            return tab;
        }
    }
}
