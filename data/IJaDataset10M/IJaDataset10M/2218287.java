package com.planet_ink.coffee_mud.core.database;

import com.planet_ink.coffee_mud.core.interfaces.*;
import com.planet_ink.coffee_mud.core.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.Areas.interfaces.*;
import com.planet_ink.coffee_mud.Behaviors.interfaces.*;
import com.planet_ink.coffee_mud.CharClasses.interfaces.*;
import com.planet_ink.coffee_mud.Commands.interfaces.*;
import com.planet_ink.coffee_mud.Common.interfaces.*;
import com.planet_ink.coffee_mud.Exits.interfaces.*;
import com.planet_ink.coffee_mud.Items.interfaces.*;
import com.planet_ink.coffee_mud.Locales.interfaces.*;
import com.planet_ink.coffee_mud.MOBS.interfaces.*;
import com.planet_ink.coffee_mud.Races.interfaces.*;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
public class GCClassLoader {

    protected DBConnector DB = null;

    public GCClassLoader(DBConnector newDB) {
        DB = newDB;
    }

    public Vector DBReadClasses() {
        DBConnection D = null;
        Vector rows = new Vector();
        try {
            D = DB.DBFetch();
            ResultSet R = D.query("SELECT * FROM CMCCAC");
            while (R.next()) {
                Vector V = new Vector();
                V.addElement(DBConnections.getRes(R, "CMCCID"));
                V.addElement(DBConnections.getRes(R, "CMCDAT"));
                rows.addElement(V);
            }
        } catch (Exception sqle) {
            Log.errOut("DataLoader", sqle);
        }
        if (D != null) DB.DBDone(D);
        return rows;
    }

    public void DBDeleteClass(String classID) {
        DB.update("DELETE FROM CMCCAC WHERE CMCCID='" + classID + "'");
    }

    public void DBCreateClass(String classID, String data) {
        DB.update("INSERT INTO CMCCAC (" + "CMCCID, " + "CMCDAT " + ") values (" + "'" + classID + "'," + "'" + data + " '" + ")");
    }
}
