package org.mavenit.dbit;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Vector;
import org.mavenit.dbit.sql.Transaction;

/**
 * A repackaging of the SQL plugin for Maven.
 * @goal dbit:sql
 * @description Executes SQL against a database
 */
public class SqlExecuteMojo extends DbUnitBaseMojo {

    /**
     * files to load
     * @parameter
     */
    private Fileset fileset;

    /**
     * SQL input file
     * @parameter
     */
    private File src = null;

    /**
     * @parameter
     */
    private String sql;

    /**
     * @parameter
     */
    private String delimiter = ";";

    /**
     * Keep the format of a sql block?
     */
    private boolean keepformat = false;

    /**
     * SQL transactions to perform
     */
    private Vector transactions = new Vector();

    /**
     * Argument to Statement.setEscapeProcessing
     */
    private boolean escapeProcessing = true;

    /**
     * Results Output file.
     */
    private File output = null;

    /**
     * Append to an existing file or overwrite it?
     */
    private boolean append = false;

    /**
     * specify whether auto commit
     */
    private boolean autocommit = true;

    /**
     * Action to perform if an error is found
     **/
    private String onError = "abort";

    /**
     * Encoding to use when reading SQL statements from a file
     */
    private String encoding = null;

    /**
     * Load the sql file and then execute it
     */
    public void execute() throws RuntimeException {
        int goodSql = 0;
        int totalSql = 0;
        Vector savedTransaction = (Vector) transactions.clone();
        String savedSqlCommand = sql;
        if (sql == null) sql = "";
        if (sql.length() > 0) {
            sql = sql.trim();
        }
        Connection conn = null;
        try {
            String[] includedFiles = new String[0];
            if (fileset != null) {
                fileset.scan();
                includedFiles = fileset.getIncludedFiles();
            }
            if (src == null && sql.length() == 0 && includedFiles.length == 0) {
                if (transactions.size() == 0) {
                    throw new RuntimeException("Source file or fileset, " + "transactions or sql statement must be set!");
                }
            }
            if (src != null && !src.exists()) {
                throw new RuntimeException("Source file does not exist! [" + src.getPath() + "]");
            }
            conn = getConnection();
            Statement statement = conn.createStatement();
            statement.setEscapeProcessing(escapeProcessing);
            for (int j = 0; j < includedFiles.length; j++) {
                Transaction t = createTransaction(conn, statement);
                t.setSrc(new File(fileset.getBasedir(), includedFiles[j]));
            }
            Transaction t = createTransaction(conn, statement);
            t.setSrc(src);
            t.addText(sql);
            try {
                PrintStream out = System.out;
                try {
                    if (output != null) {
                        getLog().debug("Opening PrintStream to output file " + output);
                        out = new PrintStream(new BufferedOutputStream(new FileOutputStream(output.getAbsolutePath(), append)));
                    }
                    for (Enumeration e = transactions.elements(); e.hasMoreElements(); ) {
                        Transaction tran = (Transaction) e.nextElement();
                        tran.runTransaction(out);
                        totalSql += tran.getTotalSql();
                        goodSql += tran.getGoodSql();
                        if (!autocommit) {
                            getLog().debug("Committing transaction");
                            conn.commit();
                        }
                    }
                } finally {
                    if (out != null && out != System.out) {
                        out.close();
                    }
                }
            } catch (IOException e) {
                if (!autocommit && conn != null && onError.equals("abort")) {
                    RollBackConnection(conn);
                }
                throw new RuntimeException(e);
            } catch (SQLException e) {
                if (!autocommit && conn != null && onError.equals("abort")) {
                    RollBackConnection(conn);
                }
                throw new RuntimeException(e);
            } finally {
                CloseStatement(statement);
                CloseConnection(conn);
            }
            getLog().info(goodSql + " of " + totalSql + " SQL statements executed successfully");
        } catch (SQLException e) {
            RollBackConnection(conn);
        } finally {
            CloseConnection(conn);
            transactions = savedTransaction;
            sql = savedSqlCommand;
        }
    }

    /**
     * Add a SQL transaction to execute
     */
    public Transaction createTransaction(Connection connection, Statement statement) {
        Transaction t = new Transaction(connection, statement);
        t.setDelimiter(delimiter);
        t.setOnError(onError);
        t.setEncoding(encoding);
        t.setKeepformat(keepformat);
        transactions.addElement(t);
        return t;
    }

    private void RollBackConnection(Connection conn) {
        try {
            if (conn != null) conn.rollback();
        } catch (SQLException ex) {
        }
    }

    private void CloseConnection(Connection conn) {
        try {
            if (conn != null) conn.close();
        } catch (SQLException ex) {
        }
    }

    private void CloseStatement(Statement statement) {
        try {
            if (statement != null) statement.close();
        } catch (SQLException ex) {
        }
    }

    public Fileset getFileset() {
        return fileset;
    }

    public void setFileset(Fileset fileset) {
        this.fileset = fileset;
    }

    public File getSrc() {
        return src;
    }

    public void setSrc(File src) {
        this.src = src;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public boolean isAppend() {
        return append;
    }

    public void setAppend(boolean append) {
        this.append = append;
    }

    public boolean isAutocommit() {
        return autocommit;
    }

    public void setAutocommit(boolean autocommit) {
        this.autocommit = autocommit;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public boolean isEscapeProcessing() {
        return escapeProcessing;
    }

    public void setEscapeProcessing(boolean escapeProcessing) {
        this.escapeProcessing = escapeProcessing;
    }

    public boolean isKeepformat() {
        return keepformat;
    }

    public void setKeepformat(boolean keepformat) {
        this.keepformat = keepformat;
    }

    public String getOnError() {
        return onError;
    }

    public void setOnError(String onError) {
        this.onError = onError;
    }

    public File getOutput() {
        return output;
    }

    public void setOutput(File output) {
        this.output = output;
    }

    public Vector getTransactions() {
        return transactions;
    }

    public void setTransactions(Vector transactions) {
        this.transactions = transactions;
    }
}
