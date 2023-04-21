package com.safi.workshop.sqlexplorer.dbstructure.nodes;

import java.util.LinkedList;
import net.sourceforge.squirrel_sql.fw.sql.ITableInfo;
import com.safi.workshop.sqlexplorer.Messages;
import com.safi.workshop.sqlexplorer.dbproduct.MetaDataSession;
import com.safi.workshop.sqlexplorer.plugin.SQLExplorerPlugin;

/**
 * TableTypeNode can represents a parent node for VIEW, TABLE, .. depending on what the
 * database supports.
 * 
 * @author Davy Vanherbergen
 * 
 */
public class TableFolderNode extends AbstractFolderNode {

    /** all catalog/schema tables */
    private ITableInfo[] _allTables;

    private String _origName;

    /**
   * Create new database table object type node (view, table, etc...)
   * 
   * @param parent
   *          node
   * @param name
   *          of this node
   * @param sessionNode
   *          session for this node
   */
    public TableFolderNode(INode parent, String name, MetaDataSession sessionNode, ITableInfo[] tables) {
        super(parent, tidiedName(name), sessionNode, name + "_FOLDER");
        LinkedList<ITableInfo> list = new LinkedList<ITableInfo>();
        for (ITableInfo info : tables) if (!info.getSimpleName().startsWith("BIN$")) list.add(info);
        _allTables = list.toArray(new ITableInfo[0]);
        _origName = name;
    }

    @Override
    public String getQualifiedName() {
        return _origName;
    }

    /**
   * Load all the children of this table type.
   * 
   * @see com.safi.workshop.sqlexplorer.dbstructure.nodes.AbstractNode#loadChildren()
   */
    @Override
    public void loadChildren() {
        try {
            ITableInfo[] tables = null;
            if (_allTables != null && _allTables.length != 0) {
                tables = _allTables.clone();
                _allTables = null;
            } else {
                String catalogName = null;
                String schemaName = null;
                if (_parent instanceof CatalogNode) {
                    catalogName = _parent.toString();
                    if (!_parent.hasChildNodes()) {
                        catalogName = null;
                    }
                }
                if (_parent instanceof SchemaNode) {
                    schemaName = _parent.toString();
                }
                tables = _session.getMetaData().getTables(catalogName, schemaName, "%", new String[] { _origName }, null);
            }
            for (int i = 0; i < tables.length; i++) {
                if (tables[i].getType().equalsIgnoreCase(_origName)) {
                    if (!isExcludedByFilter(tables[i].getSimpleName())) {
                        addChildNode(new TableNode(this, tables[i].getSimpleName(), _session, tables[i]));
                    }
                }
            }
        } catch (Throwable e) {
            SQLExplorerPlugin.error("Could not load child nodes for " + _name, e);
        }
    }

    private static String tidiedName(String name) {
        String[] words = name.split(" ");
        name = "";
        for (String word : words) {
            name = name + word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase() + " ";
        }
        name = name.trim();
        if (name.equals("View")) {
            name = Messages.getString("DatabaseStructureView.view");
        }
        if (name.equals("Table")) {
            name = Messages.getString("DatabaseStructureView.table");
        }
        return name;
    }
}
