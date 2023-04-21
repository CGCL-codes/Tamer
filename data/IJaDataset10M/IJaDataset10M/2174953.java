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
public class BehaviorData extends StdWebMacro {

    public String name() {
        return this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.') + 1);
    }

    public String runMacro(ExternalHTTPRequests httpReq, String parm) {
        Hashtable parms = parseParms(parm);
        String last = httpReq.getRequestParameter("BEHAVIOR");
        if (last == null) return " @break@";
        if (last.length() > 0) {
            Behavior B = CMClass.getBehavior(last);
            if (B != null) {
                StringBuffer str = new StringBuffer("");
                if (parms.containsKey("HELP")) {
                    StringBuilder s = CMLib.help().getHelpText("BEHAVIOR_" + B.ID(), null, true);
                    if (s == null) s = CMLib.help().getHelpText(B.ID(), null, true);
                    int limit = 70;
                    if (parms.containsKey("LIMIT")) limit = CMath.s_int((String) parms.get("LIMIT"));
                    str.append(helpHelp(s, limit));
                }
                String strstr = str.toString();
                if (strstr.endsWith(", ")) strstr = strstr.substring(0, strstr.length() - 2);
                return clearWebMacros(strstr);
            }
        }
        return "";
    }
}
