package net.sf.webconsole.sql;

import java.sql.Connection;
import java.io.PrintStream;

/**
 * SQLExecutor defines the interface to execute or SQL statement.
 *
 * @version   1.0 2007-10-19
 * @author    <A HREF="mailto:chyxiang@yahoo.com">Chen Xiang (Sean)</A>
 */
public interface SQLExecutor {

    /**
     * set the jdbc connection.
     */
    public void setConnection(Connection con);

    /**
     * set the output print stream.
     */
    public void setOutput(PrintStream out);

    /**
     * execute.
     */
    public void execute(String[] sqls);

    /**
     * execute.
     */
    public void execute(String sql);

    /**
     * execute sqls from a input file.
     */
    public void executeFile(String fileName);
}
