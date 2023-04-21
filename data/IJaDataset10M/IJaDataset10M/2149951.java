package org.broadleafcommerce.openadmin.client.view.dynamic;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import org.broadleafcommerce.openadmin.client.BLCMain;

/**
 * 
 * @author jfischer
 *
 */
public class DynamicEntityListView extends VLayout implements DynamicEntityListDisplay {

    protected ToolStripButton addButton;

    protected ToolStripButton removeButton;

    protected ComboBoxItem entityType = new ComboBoxItem();

    protected ListGrid grid;

    protected ToolStrip toolBar;

    public DynamicEntityListView(DataSource dataSource) {
        this("", dataSource, true, true);
    }

    public DynamicEntityListView(String title, DataSource dataSource) {
        this(title, dataSource, true, true);
    }

    public DynamicEntityListView(String title, DataSource dataSource, Boolean canReorder, Boolean canEdit) {
        super();
        toolBar = new ToolStrip();
        toolBar.setHeight(30);
        toolBar.setWidth100();
        toolBar.addSpacer(6);
        addButton = new ToolStripButton();
        addButton.setTitle(BLCMain.getMessageManager().getString("addTitle"));
        addButton.setIcon(GWT.getModuleBaseURL() + "sc/skins/Enterprise/images/actions/add.png");
        toolBar.addButton(addButton);
        toolBar.addSpacer(6);
        removeButton = new ToolStripButton();
        removeButton.setTitle(BLCMain.getMessageManager().getString("removeTitle"));
        removeButton.setIcon(GWT.getModuleBaseURL() + "sc/skins/Enterprise/images/actions/remove.png");
        removeButton.setDisabled(true);
        toolBar.addButton(removeButton);
        toolBar.addFill();
        addMember(toolBar);
        grid = new ListGrid();
        grid.setCanReorderRecords(canReorder);
        grid.setAlternateRecordStyles(true);
        grid.setSelectionType(SelectionStyle.SINGLE);
        grid.setCanEdit(true);
        grid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
        grid.setEditByCell(true);
        grid.setAutoSaveEdits(true);
        grid.setSaveByCell(true);
        grid.setDataSource(dataSource);
        grid.setAutoFetchData(false);
        grid.setCanSort(true);
        grid.setCanResizeFields(true);
        grid.setShowFilterEditor(true);
        grid.setCanGroupBy(false);
        grid.setEmptyMessage(BLCMain.getMessageManager().getString("emptyMessage"));
        if (!canEdit) {
            grid.setAlternateBodyStyleName("editRowDisabled");
        }
        addMember(grid);
    }

    public ToolStripButton getAddButton() {
        return addButton;
    }

    public ToolStripButton getRemoveButton() {
        return removeButton;
    }

    public ComboBoxItem getEntityType() {
        return entityType;
    }

    public ListGrid getGrid() {
        return grid;
    }

    public ToolStrip getToolBar() {
        return toolBar;
    }
}
