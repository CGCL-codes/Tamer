package com.hp.hpl.jena.db.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import com.hp.hpl.jena.db.GraphRDB;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.db.RDFRDBException;
import com.hp.hpl.jena.graph.*;
import com.hp.hpl.jena.graph.query.BufferPipe;
import com.hp.hpl.jena.graph.query.Domain;
import com.hp.hpl.jena.graph.query.ExpressionSet;
import com.hp.hpl.jena.graph.query.Pipe;
import com.hp.hpl.jena.graph.query.Stage;
import com.hp.hpl.jena.shared.JenaException;

/**
    @author hedgehog
*/
public class DBQueryStage extends Stage {

    protected Graph graph;

    protected DBQuery compiled;

    public DBQueryStage(GraphRDB graph, SpecializedGraph sg, List varList, List dbPat, ExpressionSet constraints) {
        this.graph = graph;
        this.compiled = compile(sg, varList, dbPat, constraints);
    }

    protected DBQuery compile(SpecializedGraph sg, List varList, List dbPat, ExpressionSet constraints) {
        return compile(compiler, sg, varList, dbPat, constraints);
    }

    protected DBQuery compile(DBQueryStageCompiler compiler, SpecializedGraph sg, List varList, List dbPat, ExpressionSet constraints) {
        return DBQueryStageCompiler.compile(compiler, (DBQueryHandler) graph.queryHandler(), sg, varList, dbPat, constraints);
    }

    private static final DBQueryStageCompiler compiler = new DBQueryStageCompiler();

    protected void run(Pipe source, Pipe sink) {
        PreparedStatement ps = null;
        Domain current;
        Domain useme;
        IDBConnection conn;
        ResultSet rs = null;
        try {
            if (!compiled.isEmpty) try {
                conn = compiled.driver.getConnection();
                ps = conn.getConnection().prepareStatement(compiled.stmt);
            } catch (Exception e) {
                throw new JenaException("Query prepare failed: " + e);
            }
            if (ps != null) while (source.hasNext()) {
                current = source.get();
                setArgs(current, ps);
                ResultSetIterator it = null;
                try {
                    it = new ResultSetIterator();
                    ps.execute();
                    rs = ps.getResultSet();
                    it.reset(rs, ps);
                    while (it.hasNext()) {
                        useme = current.copy();
                        List row = (List) it.next();
                        for (int i = 0; i < compiled.resList.length; i++) {
                            int j = compiled.resList[i];
                            String o = (String) row.get(i);
                            Node n = compiled.driver.RDBStringToNode(o);
                            useme.setElement(j, n);
                        }
                        sink.put(useme);
                    }
                } catch (Exception e) {
                    throw new JenaException("Query execute failed: " + e);
                } finally {
                    if (it != null) it.close();
                }
            }
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException e1) {
                throw new RDFRDBException("Failed to get last inserted ID: " + e1);
            }
            if (ps != null) try {
                ps.close();
            } catch (Exception e) {
                throw new JenaException("Close on prepared stmt failed: " + e);
            }
            if (sink != null) {
                sink.close();
            }
        }
    }

    protected void setArgs(Domain args, PreparedStatement ps) {
        int i, ix;
        String val;
        Node arg;
        try {
            for (i = 0; i < compiled.argCnt; i++) {
                ix = ((Integer) compiled.argIndex.get(i)).intValue();
                arg = (Node) args.get(ix);
                if (arg == null) throw new JenaException("Null query argument");
                val = compiled.driver.nodeToRDBString(arg, false);
                ps.setString(i + 1, val);
            }
        } catch (SQLException e) {
            throw new JenaException("Bad query argument", e);
        }
    }

    public Pipe deliver(final Pipe result) {
        final Pipe stream = previous.deliver(new BufferPipe());
        new Thread() {

            public void run() {
                DBQueryStage.this.run(stream, result);
            }
        }.start();
        return result;
    }
}
