package org.fao.fenix.web.modules.re.client.view.text;

import java.util.ArrayList;
import java.util.List;
import org.fao.fenix.web.modules.core.client.Fenix;
import org.fao.fenix.web.modules.re.client.view.Catalogue;
import org.fao.fenix.web.modules.re.client.view.util.ResourceModel;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.binder.TreeTableBinder;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.data.TreeModelReader;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.treetable.TreeTable;
import com.extjs.gxt.ui.client.widget.treetable.TreeTableColumn;
import com.extjs.gxt.ui.client.widget.treetable.TreeTableColumnModel;

public class CatalogueText extends Catalogue {

    protected TreeStore<ResourceModel> treeStore;

    protected TreeTableBinder<ResourceModel> treeBinder;

    protected TreeLoader<ResourceModel> treeLoader;

    public CatalogueText() {
        System.out.println("CatalogueText HERE");
        treeTable = new TreeTable();
        columnHeaderIds = new String[] { "title", "type", "dateModified", "region", "category" };
    }

    public TreeTable buildTable() {
        List<TreeTableColumn> columns = new ArrayList<TreeTableColumn>();
        for (int i = 0; i < columnHeaderIds.length; i++) columns.add(buildTreeTableColumn(columnHeaderIds[i], Fenix.fenixLang.getString(columnHeaderIds[i])));
        TreeTableColumnModel cm = new TreeTableColumnModel(columns);
        treeTable = new TreeTable(cm);
        treeTable.setAnimate(false);
        treeTable.setItemIconStyle("pmText");
        treeLoader = new BaseTreeLoader<ResourceModel>(new TreeModelReader()) {

            public boolean hasChildren(BaseTreeModel parent) {
                return false;
            }
        };
        treeStore = new TreeStore<ResourceModel>(treeLoader);
        treeBinder = new TreeTableBinder<ResourceModel>(treeTable, treeStore);
        treeBinder.setDisplayProperty("name");
        treeBinder.init();
        treeTable.setContextMenu(new CatalogueContextMenuText().buildMenu(treeTable));
        fillTable(treeTable);
        return treeTable;
    }

    public TreeTableColumn buildTreeTableColumn(String columnHeaderId, String columnHeaderLabel) {
        TreeTableColumn column = new TreeTableColumn(columnHeaderId, columnHeaderLabel, 75);
        column.setSortable(true);
        column.setResizable(true);
        column.setMinWidth(75);
        column.setMaxWidth(150);
        column.setAlignment(Style.HorizontalAlignment.LEFT);
        return column;
    }

    public TreeTable fillTable(final TreeTable table) {
        table.removeAll();
        treeStore.removeAll();
        for (ResourceModel loParent : (List<ResourceModel>) getResourceChildren()) {
            treeStore.add(loParent, true);
        }
        return table;
    }

    public List<ResourceModel> getResourceChildren() {
        List<ResourceModel> models = new ArrayList<ResourceModel>();
        ResourceModel newModel = new ResourceModel("first");
        newModel.set("type", "text");
        newModel.set("dateModified", "date 0");
        newModel.set("region", "region 1");
        newModel.set("category", "category 1");
        ResourceModel newModel2 = new ResourceModel("child 1");
        newModel2.set("type", "text");
        newModel2.set("dateModified", "date 1");
        newModel2.set("region", "region2");
        newModel.add(newModel2);
        ResourceModel newModel3 = new ResourceModel("child 2");
        newModel3.set("type", "text");
        newModel3.set("dateModified", "date 2");
        newModel3.set("region", "region3");
        newModel.add(newModel3);
        models.add(newModel);
        ResourceModel newModelParent2 = new ResourceModel("second");
        newModelParent2.set("type", "text");
        newModelParent2.set("dateModified", "date x");
        newModelParent2.set("region", "regionx");
        newModelParent2.set("category", "category x");
        models.add(newModelParent2);
        models.add(new ResourceModel("third"));
        models.add(new ResourceModel("fourth"));
        return models;
    }
}
