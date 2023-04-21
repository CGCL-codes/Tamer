package sql.serverTypes;

import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import org.gjt.sp.jedit.*;
import org.gjt.sp.jedit.io.*;
import org.gjt.sp.util.*;
import sql.*;
import sql.serverTypes.oracle.*;

/**
 *  Description of the Class
 *
 * @author     svu
 */
public class OracleVFS extends SqlSubVFS {

    protected static final Map oracleObjectTypes = new HashMap();

    /**
	 *Constructor for the OracleVFS object
	 */
    public OracleVFS() {
        super(oracleObjectTypes);
    }

    static {
        oracleObjectTypes.put("Functions", new CodeObjectType("FUNCTION"));
        oracleObjectTypes.put("Procedures", new CodeObjectType("PROCEDURE"));
        oracleObjectTypes.put("Packages", new CodeObjectType("PACKAGE"));
        oracleObjectTypes.put("Package Bodies", new CodeObjectType("PACKAGE BODY"));
        oracleObjectTypes.put("Java", new CodeObjectType("JAVA SOURCE"));
        oracleObjectTypes.put("Tables", new OracleTableObjectType());
        oracleObjectTypes.put("Views", new ViewObjectType("VIEW", null, "selectViewCode", "VIEW {0}.{1} AS "));
        oracleObjectTypes.put("Triggers", new TriggerObjectType());
    }
}
