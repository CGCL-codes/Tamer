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
public class AbilityGainReport extends StdWebMacro {

    public String name() {
        return "AbilityGainReport";
    }

    public String runMacro(ExternalHTTPRequests httpReq, String parm) {
        String className = httpReq.getRequestParameter("CLASS");
        if (className == null) className = "";
        List<String> players = CMLib.database().getUserList();
        HashSet trainedFor = new HashSet();
        Hashtable profSpent = new Hashtable();
        for (String playerName : players) {
            MOB player = CMLib.players().getLoadPlayer(playerName);
            for (int a = 0; a < player.numLearnedAbilities(); a++) {
                Ability A = player.fetchAbility(a);
                if (A == null) continue;
                boolean autogains = false;
                boolean qualifiesFor = false;
                int bestProf = -1;
                for (int c = 0; c < player.baseCharStats().numClasses(); c++) {
                    CharClass C = player.baseCharStats().getMyClass(c);
                    if ((className.length() == 0) || (C.ID().equalsIgnoreCase(className))) {
                        int qlevel = CMLib.ableMapper().getQualifyingLevel(C.ID(), true, A.ID());
                        if (qlevel >= player.baseCharStats().getClassLevel(C.ID())) {
                            if (CMLib.ableMapper().getDefaultGain(C.ID(), true, A.ID())) autogains = true;
                            qualifiesFor = true;
                            if (CMLib.ableMapper().getDefaultProficiency(C.ID(), true, A.ID()) > bestProf) bestProf = CMLib.ableMapper().getDefaultProficiency(C.ID(), true, A.ID());
                        }
                    }
                }
                if ((!autogains) && (qualifiesFor) && (!trainedFor.contains(A.ID()))) trainedFor.add(A.ID());
                if ((qualifiesFor) && (A.proficiency() > bestProf)) {
                    long[] stats = (long[]) profSpent.get(A.ID());
                    if (stats == null) {
                        stats = new long[3];
                        profSpent.put(A.ID(), stats);
                    }
                    stats[0] += (A.proficiency() - bestProf);
                    stats[1]++;
                    stats[2] = stats[0] / stats[1];
                }
            }
        }
        DVector sorted = new DVector(2);
        while (profSpent.size() > 0) {
            Enumeration e = profSpent.keys();
            String bestKey = (String) e.nextElement();
            long[] bestStat = (long[]) profSpent.get(bestKey);
            for (; e.hasMoreElements(); ) {
                String key = (String) e.nextElement();
                long[] stat = (long[]) profSpent.get(key);
                if (stat[2] > bestStat[2]) {
                    bestKey = key;
                    bestStat = stat;
                }
            }
            profSpent.remove(bestKey);
            sorted.addElement(bestKey, bestStat);
        }
        StringBuffer buf = new StringBuffer("<TABLE WIDTH=100% BORDER=1>");
        buf.append("<TR><TD WIDTH=40%>Trained Skill</TD><TD WIDTH=30%>Avg. Prof. Gained</TD><TD WIDTH=30%>Instances</TD></TR>");
        buf.append("<TR><TD WIDTH=40%><BR></TD><TD WIDTH=30%><BR></TD><TD WIDTH=30%><BR></TD></TR>");
        for (int s = 0; s < sorted.size(); s++) {
            String able = (String) sorted.elementAt(s, 1);
            long[] stats = (long[]) sorted.elementAt(s, 2);
            if (trainedFor.contains(able)) buf.append("<TR><TD>" + able + "</TD><TD>" + stats[2] + "</TD><TD>" + stats[1] + "</TD></TR>");
        }
        for (Iterator i = trainedFor.iterator(); i.hasNext(); ) {
            String able = (String) i.next();
            if (!sorted.contains(able)) buf.append("<TR><TD>" + able + "</TD><TD>N/A</TD><TD>N/A</TD></TR>");
        }
        buf.append("</TABLE><P><BR><TABLE WIDTH=100% BORDER=1>");
        buf.append("<TR><TD WIDTH=40%>Possessed Skill</TD><TD WIDTH=30%>Avg. Prof. Gained</TD><TD WIDTH=30%>Instances</TD></TR>");
        buf.append("<TR><TD WIDTH=40%><BR></TD><TD WIDTH=30%><BR></TD><TD WIDTH=30%><BR></TD></TR>");
        for (int s = 0; s < sorted.size(); s++) {
            String able = (String) sorted.elementAt(s, 1);
            long[] stats = (long[]) sorted.elementAt(s, 2);
            buf.append("<TR><TD>" + able + "</TD><TD>" + stats[2] + "</TD><TD>" + stats[1] + "</TD></TR>");
        }
        buf.append("</TABLE>");
        return clearWebMacros(buf);
    }
}
