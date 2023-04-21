package com.safi.workshop.sqlexplorer.dbdetail.tab;

import java.sql.ResultSet;
import com.safi.workshop.sqlexplorer.Messages;
import com.safi.workshop.sqlexplorer.dataset.DataSet;
import com.safi.workshop.sqlexplorer.dbstructure.nodes.INode;
import com.safi.workshop.sqlexplorer.dbstructure.nodes.TableNode;

/**
 * @author Davy Vanherbergen
 * 
 */
public class ExportedKeysTab extends AbstractDataSetTab {

    @Override
    public String getLabelText() {
        return Messages.getString("DatabaseDetailView.Tab.ExportedKeys");
    }

    @Override
    public DataSet getDataSet() throws Exception {
        INode node = getNode();
        if (node == null) {
            return null;
        }
        if (node instanceof TableNode) {
            TableNode tableNode = (TableNode) node;
            ResultSet resultSet = node.getSession().getMetaData().getExportedKeys(tableNode.getTableInfo());
            DataSet dataSet = new DataSet(resultSet, new int[] { 4, 7, 8, 9, 10, 11, 12, 13, 14 });
            resultSet.close();
            return dataSet;
        }
        return null;
    }

    @Override
    public String getStatusMessage() {
        return Messages.getString("DatabaseDetailView.Tab.ExportedKeys.status") + " " + getNode().getQualifiedName();
    }
}
