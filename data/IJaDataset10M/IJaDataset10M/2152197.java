package testingapplication.treetable;

import com.softaspects.jsf.component.tree.TreeDataModel;
import com.softaspects.jsf.component.tree.TreeInterfaceManagerModel;
import com.softaspects.jsf.component.table.*;
import com.softaspects.jsf.component.treeTable.TreeTableDataModel;
import testingapplication.table.TableDataModelUtil;
import testingapplication.tree.TreeDataModelUtil;

public class TreeTableModelBean {

    private TableDataModel tableDataModel = null;

    private TableColumnModel columnModel = null;

    private TableInterfaceManagerModel tableInterfaceManagerModel = null;

    private TableInterfaceManagerModel tableKeysInterfaceManagerModel = null;

    private HeaderModel headerModel = null;

    private TreeDataModel treeDataModel = null;

    private TreeInterfaceManagerModel treeInterfaceManagerModel = null;

    private TreeTableDataModel treeTableDataModel = null;

    public TableDataModel getTableDataModel() {
        if (tableDataModel == null) {
            tableDataModel = TreeTableDataModelUtil.getDataModelnstance();
            for (int i = 0; i < tableDataModel.getRowCount(); i++) {
                tableDataModel.setCellEditable(i, 2, true);
            }
        }
        return tableDataModel;
    }

    public TableColumnModel getColumnModel() {
        if (columnModel == null) {
            columnModel = TreeTableDataModelUtil.getColumnModelInstance();
        }
        return columnModel;
    }

    public TableInterfaceManagerModel getTableInterfaceManagerModel() {
        if (tableInterfaceManagerModel == null) tableInterfaceManagerModel = TreeTableDataModelUtil.getIntefaceManagerInstance();
        return tableInterfaceManagerModel;
    }

    public TableInterfaceManagerModel getTableKeysInterfaceManagerModel() {
        if (tableKeysInterfaceManagerModel == null) {
            tableKeysInterfaceManagerModel = TreeTableDataModelUtil.getIntefaceManagerInstance();
            TreeTableDataModelUtil.applyKeys(tableKeysInterfaceManagerModel);
        }
        return tableKeysInterfaceManagerModel;
    }

    public HeaderModel getHeaderModel() {
        if (headerModel == null) headerModel = TableDataModelUtil.getHeaderInstance();
        return headerModel;
    }

    public TreeDataModel getTreeDataModel() {
        if (treeDataModel == null) {
            treeDataModel = new TreeDataModel();
            treeDataModel.addElement(TreeDataModelUtil.getTreeRootItemInstance());
        }
        return treeDataModel;
    }

    public TreeInterfaceManagerModel getTreeInterfaceManagerModel() {
        if (treeInterfaceManagerModel == null) {
            treeInterfaceManagerModel = TreeTableDataModelUtil.getTreeIntefaceManagerInstance();
        }
        return treeInterfaceManagerModel;
    }

    public TreeTableDataModel getTreeTableDataModel() {
        if (treeTableDataModel == null) {
            treeTableDataModel = new TreeTableDataModel();
            treeTableDataModel.setTableDataModel(getTableDataModel());
            treeTableDataModel.setTreeDataModel(getTreeDataModel());
        }
        return treeTableDataModel;
    }
}
