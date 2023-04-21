package org.makumba.db.makumba.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.makumba.DBError;
import org.makumba.InvalidValueException;
import org.makumba.MakumbaError;
import org.makumba.OQLParseError;
import org.makumba.db.makumba.Update;
import org.makumba.providers.QueryAnalysis;
import org.makumba.providers.QueryProvider;
import org.makumba.providers.query.oql.QueryAST;

public class SQLUpdate implements Update {

    ParameterAssigner assigner;

    String debugString;

    String updateCommand;

    QueryProvider qP = QueryProvider.makeQueryAnalzyer("oql");

    SQLUpdate(org.makumba.db.makumba.Database db, String from, String setWhere, String DELIM) {
        int whereMark = setWhere.indexOf(DELIM);
        String set = setWhere.substring(0, whereMark);
        String where = setWhere.substring(whereMark + DELIM.length());
        debugString = (set == null ? "delete" : "update") + " on type: <" + from + ">" + (set == null ? " " : " setting: <" + set + ">") + " where: <" + where + ">";
        if (set.trim().length() == 0) {
            set = null;
        }
        if (where.trim().length() == 0) {
            where = null;
        }
        if (from != null && from.indexOf(',') >= 0) {
            throw new org.makumba.OQLParseError("Only 1 table can be involved in " + debugString);
        }
        from = from.replace('\t', ' ');
        String label;
        try {
            label = from.substring(from.trim().indexOf(' ') + 1).trim();
        } catch (StringIndexOutOfBoundsException e) {
            throw new org.makumba.OQLParseError("Invalid delete/update 'type' section: " + from);
        }
        String OQLQuery = "SELECT " + (set == null ? label : set) + " FROM " + from;
        if (where != null) {
            OQLQuery += " WHERE " + where;
        }
        QueryAnalysis qA = qP.getQueryAnalysis(OQLQuery);
        try {
            assigner = new ParameterAssigner(db, qA);
        } catch (OQLParseError e) {
            throw new org.makumba.OQLParseError(e.getMessage() + "\r\nin " + debugString + "\n" + OQLQuery, e);
        }
        String fakeCommand;
        try {
            fakeCommand = ((QueryAST) qA).writeInSQLQuery(new NameResolverHook(db));
        } catch (RuntimeException e) {
            throw new MakumbaError(e, debugString + "\n" + OQLQuery);
        }
        StringBuffer replaceLabel = new StringBuffer();
        int n = 0;
        int lastN;
        int maxN = fakeCommand.indexOf(" FROM ");
        while (true) {
            lastN = n;
            n = fakeCommand.indexOf(label + ".", lastN);
            if (n == -1 || n > maxN) {
                replaceLabel.append(fakeCommand.substring(lastN, maxN));
                break;
            }
            replaceLabel.append(fakeCommand.substring(lastN, n));
            n += label.length() + 1;
        }
        lastN = fakeCommand.indexOf(" WHERE ");
        if (lastN < 0) {
            lastN = fakeCommand.length();
        }
        n = fakeCommand.lastIndexOf(" " + label, lastN);
        replaceLabel.append(fakeCommand.substring(maxN, n));
        n = lastN;
        while (true) {
            lastN = n;
            n = fakeCommand.indexOf(label + ".", lastN);
            if (n == -1) {
                replaceLabel.append(fakeCommand.substring(lastN));
                break;
            }
            replaceLabel.append(fakeCommand.substring(lastN, n));
            n += label.length() + 1;
        }
        fakeCommand = replaceLabel.toString();
        StringBuffer command = new StringBuffer();
        command.append(set == null ? "DELETE FROM" : "UPDATE");
        command.append(fakeCommand.substring(fakeCommand.indexOf(" FROM ") + 5, fakeCommand.indexOf(" WHERE ")));
        if (set != null) {
            String setString = fakeCommand.substring(fakeCommand.indexOf("SELECT ") + 7, fakeCommand.indexOf(" FROM "));
            n = 0;
            while (true) {
                n = setString.indexOf("is null", n);
                if (n == -1) {
                    n = setString.indexOf("is  null", n);
                    if (n == -1) {
                        break;
                    }
                    setString = setString.substring(0, n) + " = null" + setString.substring(n + 8);
                    continue;
                }
                setString = setString.substring(0, n) + " = null" + setString.substring(n + 7);
            }
            command.append(" SET ").append(setString);
        }
        if (where != null) {
            command.append(fakeCommand.substring(fakeCommand.indexOf(" WHERE ")));
        }
        debugString += "\n generated SQL: " + command;
        updateCommand = command.toString();
    }

    public int execute(org.makumba.db.makumba.DBConnection dbc, Object[] args) {
        PreparedStatement ps = ((SQLDBConnection) dbc).getPreparedStatement(updateCommand);
        try {
            String s = assigner.assignParameters(ps, args);
            if (s != null) {
                throw new InvalidValueException("Errors while trying to assign arguments to update:\n" + debugString + "\n" + s);
            }
            java.util.logging.Logger.getLogger("org.makumba." + "db.update.execution").fine("" + ps);
            java.util.Date d = new java.util.Date();
            int rez;
            try {
                rez = ps.executeUpdate();
            } catch (SQLException se) {
                if (((Database) dbc.getHostDatabase()).isDuplicateException(se)) {
                    throw new org.makumba.NotUniqueError(se);
                }
                org.makumba.db.makumba.sql.Database.logException(se);
                throw new DBError(se, debugString);
            }
            long diff = new java.util.Date().getTime() - d.getTime();
            java.util.logging.Logger.getLogger("org.makumba." + "db.update.performance").fine("" + diff + " ms " + debugString);
            return rez;
        } catch (SQLException e) {
            throw new org.makumba.DBError(e);
        }
    }
}
