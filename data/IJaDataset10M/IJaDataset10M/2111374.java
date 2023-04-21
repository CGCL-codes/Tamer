package dinamica;

import dinamica.xml.*;

/**
 * Generic Transaction to execute one or more simple
 * action queries like INSERT, UPDATE or DELETE.<br>
 * This module requires one or more extra elements in config.xml
 * called "query", containing the filename of the SQL file
 * to load and execute. Input parameters will be automatically
 * replaced if config.xml contains validator=true.<br>
 * Please remember that if you are going to execute more than one
 * statement you have to activate JDBC transactions in your config.xml
 * file. Also note that this class extends dinamica.GenericTransaction
 * and it will invoke it parent's service() method before proceeding to
 * execute the queries defined via "query" elements in config.xml, it means
 * that if you defined one or more recordsets in config.xml, these will be created
 * BEFORE the DML queries are executed.<br><br>
 * Last update: jan-03-2005
 * @author Martin Cordova (dinamica@martincordova.com)
 */
public class GenericTableManager extends GenericTransaction {

    public int service(Recordset inputParams) throws Throwable {
        super.service(inputParams);
        Db db = getDb();
        Element q[] = getConfig().getDocument().getElements("query");
        if (q != null) {
            for (int i = 0; i < q.length; i++) {
                String queryName = q[i].getString();
                String sql = getResource(queryName);
                String rsName = q[i].getAttribute("params");
                if (rsName != null && !rsName.equals("")) {
                    Recordset rs = getRecordset(rsName);
                    if (rs.getRecordCount() == 0) throw new Throwable("This recordset [" + rsName + "] is empty and cannot be used to replace values in SQL template."); else {
                        rs.first();
                        sql = this.getSQL(sql, rs);
                    }
                }
                sql = this.getSQL(sql, inputParams);
                db.exec(sql);
            }
        }
        return 0;
    }
}
