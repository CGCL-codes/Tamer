package net.sourceforge.sqlexplorer.oracle.nodes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Comparator;
import net.sourceforge.sqlexplorer.dbstructure.nodes.AbstractNode;
import net.sourceforge.sqlexplorer.dbstructure.nodes.ColumnNode;
import net.sourceforge.sqlexplorer.dbstructure.nodes.INode;
import net.sourceforge.sqlexplorer.dbstructure.nodes.TableNode;
import net.sourceforge.sqlexplorer.plugin.SQLExplorerPlugin;
import net.sourceforge.sqlexplorer.sessiontree.model.SessionTreeNode;
import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

/**
 * @author Davy Vanherbergen
 * 
 */
public class TableIndexNode extends AbstractNode {

    private TableNode _parentTable;

    public TableIndexNode(INode node, String name, SessionTreeNode session, TableNode parentTable) throws Exception {
        _parentTable = parentTable;
        _parent = node;
        _sessionNode = session;
        _name = name;
        _imageKey = "Images.IndexIcon";
    }

    public Comparator getComparator() {
        return null;
    }

    public String getQualifiedName() {
        return getSchemaOrCatalogName() + "." + _name;
    }

    public String getType() {
        return "index";
    }

    public String getUniqueIdentifier() {
        return getSchemaOrCatalogName() + "." + _name;
    }

    public void loadChildren() {
        SQLConnection connection = getSession().getInteractiveConnection();
        ResultSet rs = null;
        PreparedStatement pStmt = null;
        try {
            pStmt = connection.prepareStatement("select column_name , descend from sys.all_ind_columns where index_name = ? and table_owner = ? and table_name = ? order by column_position");
            pStmt.setString(1, getName());
            pStmt.setString(2, getSchemaOrCatalogName());
            pStmt.setString(3, _parentTable.getName());
            rs = pStmt.executeQuery();
            while (rs.next()) {
                String columnName = rs.getString(1);
                String sort = rs.getString(2);
                ColumnNode col = new ColumnNode(this, columnName, _sessionNode, _parentTable, false);
                col.setLabelDecoration(sort);
                addChildNode(col);
            }
            rs.close();
        } catch (Exception e) {
            SQLExplorerPlugin.error("Couldn't load children for: " + getName(), e);
        } finally {
            if (pStmt != null) {
                try {
                    pStmt.close();
                } catch (Exception e) {
                    SQLExplorerPlugin.error("Error closing statement", e);
                }
            }
        }
    }
}
