package ru.athena.runTool.Model;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import ru.athena.runTool.LogWriter;
import ru.athena.runTool.blockTypes.CodeBlock;
import ru.athena.runTool.SQLEngine.*;

/**
 * @author corc_usr
 *
 */
public class OperationDBExecute implements Operation {

    private Connection conn_;

    public OperationDBExecute(Connection conn) {
        conn_ = conn;
    }

    /** 
	 * don't execute anything
	 * @throws Exception 
	 */
    public void visit(CompositeNode node) throws Exception {
    }

    /**
	 * execute current node from connection? passed to constructor
	 * @throws Exception 
	 */
    public void visit(LeafNode node) throws Exception {
        CodeBlock cb = (CodeBlock) node.getUserObject();
        if (cb.isExecutable()) {
            DbmsOutput dbms = new DbmsOutput(conn_);
            cb.execute(conn_);
            dbms.show(LogWriter.out());
        }
    }
}
