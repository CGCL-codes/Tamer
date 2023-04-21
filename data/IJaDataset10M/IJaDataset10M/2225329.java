package jena.util;

import jena.cmdline.*;
import com.hp.hpl.jena.db.*;
import java.util.*;

/** Framework for the database commands.
 * 
 * @author Andy Seaborne
 * @version $Id: DBcmd.java,v 1.2 2006/03/22 13:53:39 andy_seaborne Exp $
 */
public abstract class DBcmd {

    protected final ArgDecl argDeclDbURL = new ArgDecl(true, "db");

    protected final ArgDecl argDeclDbType = new ArgDecl(true, "dbType");

    protected final ArgDecl argDeclDbUser = new ArgDecl(true, "dbUser", "user");

    protected final ArgDecl argDeclDbPassword = new ArgDecl(true, "dbPassword", "password", "pw");

    protected final ArgDecl argDeclModelName = new ArgDecl(true, "model");

    protected final ArgDecl argDeclDbDriver = new ArgDecl(true, "driver");

    protected final ArgDecl argDeclVerbose = new ArgDecl(false, "v", "verbose");

    protected boolean verbose = false;

    protected final ArgDecl argDeclDebug = new ArgDecl(false, "debug");

    protected boolean debug = false;

    protected final ArgDecl argDeclHelp = new ArgDecl(false, "help", "h");

    protected String argDbURL = null;

    protected String argDbType = null;

    protected String argDriverName = null;

    protected String argDriverTypeName = null;

    protected String argDbUser = null;

    protected String argDbPassword = null;

    protected String argModelName = null;

    private static Map jdbcDrivers = new HashMap();

    static {
        jdbcDrivers.put("mysql", "com.mysql.jdbc.Driver");
        jdbcDrivers.put("mssql", "com.microsoft.jdbc.sqlserver.SQLServerDriver");
        jdbcDrivers.put("postgres", "org.postgresql.Driver");
        jdbcDrivers.put("postgresql", "org.postgresql.Driver");
    }

    private static Map jenaDriverName = new HashMap();

    static {
        jenaDriverName.put("mssql", "MsSQL");
        jenaDriverName.put("mysql", "MySQL");
        jenaDriverName.put("postgresql", "PostgreSQL");
        jenaDriverName.put("postgres", "PostgreSQL");
        jenaDriverName.put("oracle", "Oracle");
    }

    boolean takesPositionalArgs = false;

    String cmdName = "DB";

    CommandLine cmdLine = new CommandLine();

    private IDBConnection jdbcConnection = null;

    private ModelRDB dbModel = null;

    private String[] usage = new String[] { "Complain to jena-dev: someone forgot the usage string" };

    protected DBcmd(String n, boolean posArgs) {
        cmdName = n;
        takesPositionalArgs = posArgs;
        cmdLine.add(argDeclDbURL);
        cmdLine.add(argDeclDbType);
        cmdLine.add(argDeclDbUser);
        cmdLine.add(argDeclDbPassword);
        cmdLine.add(argDeclModelName);
        cmdLine.add(argDeclVerbose);
        cmdLine.add(argDeclDebug);
        cmdLine.add(argDeclHelp);
    }

    protected CommandLine getCommandLine() {
        return cmdLine;
    }

    protected void init(String[] args) {
        try {
            cmdLine.process(args);
        } catch (IllegalArgumentException ex) {
            usage();
            System.exit(1);
        }
        if (cmdLine.contains(argDeclHelp)) {
            usage();
            System.exit(0);
        }
        verbose = cmdLine.contains(argDeclVerbose);
        debug = cmdLine.contains(argDeclDebug);
        if (debug) verbose = true;
        if (cmdLine.contains(argDeclDbURL)) argDbURL = cmdLine.getArg(argDeclDbURL).getValue();
        if (cmdLine.contains(argDeclDbType)) argDbType = cmdLine.getArg(argDeclDbType).getValue();
        if (cmdLine.contains(argDeclDbUser)) argDbUser = cmdLine.getArg(argDeclDbUser).getValue();
        if (cmdLine.contains(argDeclDbPassword)) argDbPassword = cmdLine.getArg(argDeclDbPassword).getValue();
        if (cmdLine.contains(argDeclModelName)) argModelName = cmdLine.getArg(argDeclModelName).getValue();
        if (verbose) {
            System.out.println("URL       = " + argDbURL);
            System.out.println("User      = " + argDbUser);
            System.out.println("Password  = " + argDbPassword);
            System.out.println("Type      = " + argDbType);
            System.out.println("Name      = " + argModelName);
        }
        if (argDbURL == null || argDbType == null || argDbUser == null || argDbPassword == null) {
            System.err.println("Missing a required argument (need JDBC URL, user, password and DB type)");
            System.exit(9);
        }
        if (!takesPositionalArgs && cmdLine.numItems() != 0) {
            System.err.println(cmdName + ": No positional arguments allowed");
            usage();
            System.exit(9);
        }
        if (takesPositionalArgs && cmdLine.numItems() == 0) {
            System.err.println(cmdName + ": Positional argument required");
            usage();
            System.exit(9);
        }
        argDbType = argDbType.toLowerCase();
        argDriverName = (String) jdbcDrivers.get(argDbType);
        argDriverTypeName = (String) jenaDriverName.get(argDbType);
        if (cmdLine.contains(argDeclDbDriver)) argDriverName = cmdLine.getArg(argDeclDbDriver).getValue();
        if (argDriverName == null) {
            System.err.println("No driver: please say which JDBC driver to use");
            System.exit(9);
        }
        try {
            Class.forName(argDriverName);
        } catch (Exception ex) {
            System.err.println("Couldn't load the driver class: " + argDriverName);
            System.err.println("" + ex);
            System.exit(9);
        }
    }

    protected ModelRDB getRDBModel() {
        if (dbModel == null) {
            try {
                if (argModelName == null) dbModel = ModelRDB.open(getConnection()); else try {
                    dbModel = ModelRDB.open(getConnection(), argModelName);
                } catch (com.hp.hpl.jena.shared.DoesNotExistException ex) {
                    System.out.println("No model '" + argModelName + "' in that database");
                    System.exit(9);
                }
            } catch (com.hp.hpl.jena.db.RDFRDBException dbEx) {
                Throwable t = dbEx.getCause();
                if (t == null) t = dbEx;
                System.out.println("Failed to connect to the database: " + t.getMessage());
                System.exit(9);
            }
        }
        return dbModel;
    }

    protected IDBConnection getConnection() {
        if (jdbcConnection == null) {
            try {
                jdbcConnection = new DBConnection(argDbURL, argDbUser, argDbPassword, argDriverTypeName);
            } catch (Exception ex) {
                System.out.println("Exception making connection: " + ex.getMessage());
                System.exit(9);
            }
        }
        return jdbcConnection;
    }

    protected void exec() {
        if (cmdLine.numItems() == 0) {
            exec0();
            return;
        }
        boolean inTransaction = false;
        try {
            for (int i = 0; i < cmdLine.numItems(); i++) {
                if (getRDBModel().supportsTransactions()) {
                    if (!inTransaction) {
                        inTransaction = true;
                        getRDBModel().begin();
                    }
                }
                String arg = cmdLine.getItem(i);
                boolean contTrans = false;
                try {
                    contTrans = exec1(arg);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace(System.out);
                    if (inTransaction) {
                        getRDBModel().abort();
                        inTransaction = false;
                    }
                    dbModel.close();
                    dbModel = null;
                    System.exit(9);
                }
                if (!contTrans && inTransaction) getRDBModel().commit();
            }
        } finally {
            if (inTransaction) getRDBModel().commit();
        }
        dbModel.close();
        dbModel = null;
    }

    /** Called if there are no psoitional arguments
     */
    protected abstract void exec0();

    /** Called for each postional argument, inside a transaction.
     *  Return true to continue this transaction, false to end it and start a new
     *  one if there are any more args 
     */
    protected abstract boolean exec1(String arg);

    protected void setUsage(String a) {
        String[] aa = new String[] { a };
        setUsage(aa);
    }

    /** Usage message: one line per entry*/
    protected void setUsage(String[] a) {
        usage = a;
    }

    protected void usage() {
        for (int i = 0; i < usage.length; i++) {
            System.err.println(usage[i]);
        }
    }
}
