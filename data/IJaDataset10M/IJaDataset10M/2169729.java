package com.google.code.ftspc.lector.indexers.Sphinx;

import com.google.code.ftspc.lector.ini_and_vars.Vars;
import com.google.code.ftspc.lector.parsers.Archives.ZIP.UnZip;
import com.google.code.ftspc.lector.indexers.CommonFunctions;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.File;
import java.util.TimerTask;
import static com.google.code.ftspc.lector.ini_and_vars.Vars.*;

/**
 * Class for the check, unpack and insert unpacked text files to the DB
 * @author Arthur Khusnutdinov
 */
class SphinxTimerForFilesChecking extends TimerTask {

    private UnZip UnZip;

    @Override
    public void run() {
        Vars.server_thread_status = false;
        System.out.println("Transferring data via FTP suspended.");
        String driver = "com.mysql.jdbc.Driver";
        String connection = "jdbc:mysql://" + mysql_host + ":" + mysql_port + "/" + mysql_db;
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connection, mysql_user, mysql_password);
            Vars.stmtForMySQL = con.createStatement();
            CommonFunctions localCommonFunctions = new CommonFunctions(new File(Mater_Lector));
            localCommonFunctions.indexDocs_main();
            while (current_run_indexes > 0) {
                synchronized (this) {
                    wait(5000);
                }
            }
            Vars.stmtForMySQL.close();
            con.close();
            Vars.stmtForMySQL = null;
            con = null;
            UnZip = null;
            Vars.server_thread_status = true;
            System.out.println("Transferring data via FTP continues.");
            System.out.println("Indexing done.");
        } catch (Exception ex) {
            Vars.logger.fatal("Error: ", ex);
        }
    }
}
