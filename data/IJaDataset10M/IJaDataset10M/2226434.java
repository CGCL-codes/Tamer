// ========================================================================
// Copyright (c) 1996 Intelligent Switched Systems, Sydney
// $Id: CreateTables.java 291 1997-01-21 04:32:01Z mattw $
// ========================================================================

package ISS.Technology.JDBC;

import ISS.CodingStandards.*;
import ISS.Util.*;
import java.util.*;

/** Create Database tables
 * <p> Produces output suitable for piping to a sql cli to drop and recreate
 * db tables.
 * <p><h4>Notes</h4>
 * <p> The startable arg should be a Hashtable, where go maps to the string
 * to pass to the interpreter to execute a sql command and tables is a list of
 * classnames that extend Table to create.
 *
 * @version $Id: CreateTables.java 291 1997-01-21 04:32:01Z mattw $
 * @author Matthew Watson
*/
public class CreateTables implements Startable
{
    /* ------------------------------------------------------------ */
    public void startupInit(Object arg) throws Exception {
	Hashtable ht = (Hashtable)arg;
	Vector tables = (Vector)ht.get("tables");
	String go = (String)ht.get("go");
	for (Enumeration enum = tables.elements();
	     enum.hasMoreElements();){
	    String classN = (String)enum.nextElement();
	    Table table = (Table)Class.forName(classN).newInstance();
	    System.out.println("drop table " + table.tableName() + go);
	    System.out.println(table.create(go));
	}
    }
    /* ------------------------------------------------------------ */
};
