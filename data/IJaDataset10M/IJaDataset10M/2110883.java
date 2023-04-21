package ru.athena.runTool.Model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import ru.athena.runTool.blockTypes.CodeBlock;
import ru.athena.runTool.blockTypes.StoredObject;

/**
 * @author corc_usr
 *
 */
public class TreeVisitorSaver implements HierarchicalVisitor {

    private Connection conn_;

    private String codeTreeTableName = "scriptsTreeTable";

    private PreparedStatement saveCodeTreeStmt = null;

    public TreeVisitorSaver(Connection conn) {
        this.conn_ = conn;
    }

    /** 
	 * implements saving of leaf node 
	 */
    public boolean visit(LeafNode leaf) throws Exception {
        return true;
    }

    /** 
	 *  implements save of composite enter
	 */
    public boolean visitEnter(CompositeNode composite) throws Exception {
        return true;
    }

    /** 
	 * implements save of composite exit
	 */
    public boolean visitLeave(CompositeNode composite) throws Exception {
        return true;
    }

    private PreparedStatement getCodeTreeSaveStmt() throws Exception {
        if (saveCodeTreeStmt == null) {
            try {
                StoredObject tab = new StoredObject("create table " + codeTreeTableName + "(" + "	ID			NUMBER PRIMARY KEY," + "	TREE_ID		VARCHAR2(30) NOT NULL" + "	PARENT		NUMBER REFERENCES scriptsTreeTable(ID)," + "	NAME		VARCHAR2(254)," + "	TEXT		VARCHAR2(2000)," + "	TYPE		VARCHAR2(254)" + ");", codeTreeTableName);
                tab.execute(conn_);
                StoredObject seq = new StoredObject("create sequence scriptsTreeSequence" + " start with 1 end with 99999999999999999999", "scriptsTreeSequence");
                seq.execute(conn_);
            } catch (SQLException e) {
            }
            StoredObject proc = new StoredObject("create or replace function saveCodeTreeElement( " + "	treeID	varchar2,\n" + "	parent	number,\n" + "	name	varchar2,\n" + "	text	varchar2,\n" + "	type	varchar2) as\n" + "begin\n" + "	insert into " + codeTreeTableName + "( tree_id, parent, name,text, type)\n" + " values( treeID, parent,name,text,type);\n" + "end saveCodeTreeElement;", "saveCodeTreeElement");
            proc.execute(conn_);
            saveCodeTreeStmt = conn_.prepareStatement("begin " + "	? : = saveCodeTreeElement( ?, ?, ?, ?);" + "end;");
        }
        return saveCodeTreeStmt;
    }

    public BigDecimal saveCodeTreeToDB(DefaultTreeModel tree) throws Exception {
        PreparedStatement stmt = null;
        TreePath start = new TreePath(new DefaultMutableTreeNode(tree.getRoot()));
        try {
            BigDecimal primaryKey = null;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) start.getLastPathComponent();
            CodeBlock cb = (CodeBlock) node.getUserObject();
            stmt = getCodeTreeSaveStmt();
            stmt.setBigDecimal(1, primaryKey);
            stmt.setString(2, cb.toString());
            stmt.setString(3, cb.getText());
            stmt.setString(4, cb.getClass().getName());
            stmt.execute();
            return primaryKey;
        } catch (SQLException e) {
            if (stmt != null) {
                stmt.close();
            }
            throw e;
        }
    }
}
