package com.planet_ink.coffee_mud.WebMacros;

import com.planet_ink.coffee_mud.core.interfaces.*;
import com.planet_ink.coffee_mud.core.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.Areas.interfaces.*;
import com.planet_ink.coffee_mud.Behaviors.interfaces.*;
import com.planet_ink.coffee_mud.CharClasses.interfaces.*;
import com.planet_ink.coffee_mud.Libraries.interfaces.*;
import com.planet_ink.coffee_mud.Common.interfaces.*;
import com.planet_ink.coffee_mud.Exits.interfaces.*;
import com.planet_ink.coffee_mud.Items.interfaces.*;
import com.planet_ink.coffee_mud.Locales.interfaces.*;
import com.planet_ink.coffee_mud.MOBS.interfaces.*;
import com.planet_ink.coffee_mud.Races.interfaces.*;
import java.util.*;

@SuppressWarnings("unchecked")
public class HolidayNext extends StdWebMacro {

    public String name() {
        return this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.') + 1);
    }

    public boolean isAdminMacro() {
        return true;
    }

    public String runMacro(ExternalHTTPRequests httpReq, String parm) {
        Hashtable parms = parseParms(parm);
        String last = httpReq.getRequestParameter("HOLIDAY");
        if (parms.containsKey("RESET")) {
            if (last != null) httpReq.removeRequestParameter("HOLIDAY");
            return "";
        }
        Object resp = CMLib.quests().getHolidayFile();
        Vector steps = null;
        if (resp instanceof Vector) steps = (Vector) resp; else if (resp instanceof String) return (String) resp; else return "[Unknown error.]";
        Vector holidays = new Vector();
        Vector line = null;
        String var = null;
        Vector V = null;
        for (int s = 1; s < steps.size(); s++) {
            String step = (String) steps.elementAt(s);
            V = Resources.getFileLineVector(new StringBuffer(step));
            Vector cmds = CMLib.quests().parseQuestCommandLines(V, "SET", 0);
            Vector nameLine = null;
            for (int v = 0; v < cmds.size(); v++) {
                line = (Vector) cmds.elementAt(v);
                if (line.size() > 1) {
                    var = ((String) line.elementAt(1)).toUpperCase();
                    if (var.equals("NAME")) {
                        nameLine = line;
                    }
                }
            }
            if (nameLine != null) {
                String name = CMParms.combine(nameLine, 2);
                holidays.addElement(name);
            }
        }
        String lastID = "";
        for (Enumeration q = holidays.elements(); q.hasMoreElements(); ) {
            String holidayID = (String) q.nextElement();
            if ((last == null) || ((last.length() > 0) && (last.equals(lastID)) && (!holidayID.equalsIgnoreCase(lastID)))) {
                httpReq.addRequestParameters("HOLIDAY", holidayID);
                return "";
            }
            lastID = holidayID;
        }
        httpReq.addRequestParameters("HOLIDAY", "");
        if (parms.containsKey("EMPTYOK")) return "<!--EMPTY-->";
        return " @break@";
    }
}
