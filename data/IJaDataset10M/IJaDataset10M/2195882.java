package dk.itu.maisun.wamp.admin.ajax.client;

import java.util.List;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CellSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

/** The overview detail panel. */
public class OverviewDetailPanel extends ContentPanel {

    private DetailContainer detailContainer;

    private Grid<BeanModel> overviewGrid;

    /** Instantiate a new overview detail panel.
	 * @param objectClass the object type
	 * @param width the width
	 * @param overviewHeight the overview grid height
	 * @param detailHeight the detail container height
	 * @param heading the heading
	 * @param columns the columns
	 * @param autoExpandColumn the auto expand column
	 * @param groupByColumn the group by column
	 * @param grpRenderer the group renderer
	 * @param editable whether it is editable
	 * @param detailContainer the detail container */
    public OverviewDetailPanel(Class objectClass, Integer width, Integer overviewHeight, Integer detailHeight, String heading, List<ColumnConfig> columns, String autoExpandColumn, String groupByColumn, GridGroupRenderer grpRenderer, Boolean editable, DetailContainer detailContainer) {
        super();
        this.setHeading(heading);
        this.setCollapsible(true);
        if (editable) {
            this.setSize(width, overviewHeight + detailHeight + 90);
        } else {
            this.setSize(width, overviewHeight + detailHeight + 20);
        }
        this.setFrame(true);
        this.setLayout(new TableLayout(1));
        this.initOverviewGrid(objectClass, width, overviewHeight, columns, autoExpandColumn, groupByColumn, grpRenderer, editable);
        if (detailContainer != null) {
            this.detailContainer = detailContainer;
            this.detailContainer.setSize(width, detailHeight);
            this.add(this.detailContainer);
        }
        if (objectClass != null) {
            ClientCache.getInstance().registerListStoreFor(objectClass, this.overviewGrid.getStore());
            ClientCache.getInstance().reloadObjectsFor(objectClass);
        }
    }

    /** Get the detail container.
	 * @return the detail container */
    public DetailContainer getDetailContainer() {
        return detailContainer;
    }

    /** Get the overview grid.
	 * @return the overview grid */
    public Grid<BeanModel> getOverviewGrid() {
        return overviewGrid;
    }

    /** Start editing.
	 * @param row the row to be edited
	 * @param column the column to be edited */
    public void startEditing(Integer row, Integer column) {
        ((EditorGrid<BeanModel>) this.overviewGrid).startEditing(row, column);
    }

    /** Stop editing. */
    public void stopEditing() {
        ((EditorGrid<BeanModel>) this.overviewGrid).stopEditing();
    }

    /** Initialise the overview grid.
	 * @param objectClass the object type
	 * @param width the width
	 * @param overviewHeight the overview grid height
	 * @param columns the columns
	 * @param autoExpandColumn the auto expand column
	 * @param groupByColumn the group by column
	 * @param grpRenderer the group renderer
	 * @param editable whether it is editable */
    private void initOverviewGrid(Class objectClass, Integer width, Integer overviewHeight, List<ColumnConfig> columns, String autoExpandColumn, String groupByColumn, GridGroupRenderer grpRenderer, Boolean editable) {
        ListStore<BeanModel> store = null;
        GroupingView view = null;
        if (groupByColumn != null) {
            store = new GroupingStore<BeanModel>();
            ((GroupingStore<BeanModel>) store).groupBy(groupByColumn);
            view = new GroupingView();
            view.setShowGroupedColumn(false);
            view.setForceFit(true);
            view.setGroupRenderer(grpRenderer);
        } else {
            store = new ListStore<BeanModel>();
        }
        OverviewDetailListener listener = null;
        listener = new OverviewDetailListener(this, objectClass);
        if (editable) {
            for (ColumnConfig column : columns) {
                if (column.getEditor() == null) {
                    TextField<String> textEditor = new TextField<String>();
                    textEditor.setAllowBlank(false);
                    column.setEditor(new CellEditor(textEditor));
                }
            }
            overviewGrid = new EditorGrid<BeanModel>(store, new ColumnModel(columns));
            ToolBar toolBar = new ToolBar();
            Button insertButton = new Button("Insert");
            insertButton.addListener(Events.Select, listener);
            toolBar.add(insertButton);
            Button removeButton = new Button("Remove");
            removeButton.addListener(Events.Select, listener);
            toolBar.add(removeButton);
            this.setTopComponent(toolBar);
            this.setButtonAlign(HorizontalAlignment.RIGHT);
            Button resetButton = new Button("Reset");
            resetButton.addListener(Events.Select, listener);
            this.addButton(resetButton);
            Button saveButton = new Button("Save");
            saveButton.addListener(Events.Select, listener);
            this.addButton(saveButton);
        } else {
            overviewGrid = new Grid<BeanModel>(store, new ColumnModel(columns));
        }
        final CellSelectionModel<BeanModel> cellSelectionModel = new CellSelectionModel<BeanModel>();
        overviewGrid.setSelectionModel(cellSelectionModel);
        overviewGrid.addListener(Events.CellClick, listener);
        overviewGrid.addListener(Events.CellDoubleClick, listener);
        if (view != null) {
            overviewGrid.setView(view);
        }
        overviewGrid.setSize(width, overviewHeight);
        overviewGrid.setAutoExpandColumn(autoExpandColumn);
        overviewGrid.setBorders(true);
        this.add(overviewGrid);
    }
}
