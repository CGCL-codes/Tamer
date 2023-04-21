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
public class LevelNext extends StdWebMacro {

    public String name() {
        return this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.') + 1);
    }

    public String runMacro(ExternalHTTPRequests httpReq, String parm) {
        Hashtable parms = parseParms(parm);
        String last = httpReq.getRequestParameter("LEVEL");
        if (parms.containsKey("RESET")) {
            if (last != null) httpReq.removeRequestParameter("LEVEL");
            return "";
        }
        int lastLevel = CMProps.getIntVar(CMProps.SYSTEMI_LASTPLAYERLEVEL);
        String s = null;
        for (Enumeration e = parms.keys(); e.hasMoreElements(); ) {
            s = (String) e.nextElement();
            if (CMath.isInteger(s)) lastLevel = CMath.s_int(s);
        }
        if ((last == null) || (last.length() > 0)) {
            int level = 0;
            if (last != null) level = CMath.s_int(last);
            level++;
            if (level <= lastLevel) {
                httpReq.addRequestParameters("LEVEL", "" + level);
                return "";
            }
        }
        httpReq.addRequestParameters("LEVEL", "");
        if (parms.containsKey("EMPTYOK")) return "<!--EMPTY-->";
        return " @break@";
    }
}
