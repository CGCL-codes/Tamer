package com.planet_ink.coffee_mud.Behaviors;

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
import java.util.*;

@SuppressWarnings("unchecked")
public class GoodExecutioner extends StdBehavior {

    public String ID() {
        return "GoodExecutioner";
    }

    public long flags() {
        return Behavior.FLAG_POTENTIALLYAGGRESSIVE;
    }

    private boolean doPlayers = false;

    private boolean norecurse = false;

    protected long deepBreath = System.currentTimeMillis();

    private DVector protectedOnes = new DVector(2);

    public void setParms(String newParms) {
        super.setParms(newParms);
        newParms = newParms.toUpperCase();
        Vector V = CMParms.parse(newParms);
        doPlayers = V.contains("PLAYERS") || V.contains("PLAYER");
    }

    public boolean grantsAggressivenessTo(MOB M) {
        if (norecurse) return false;
        norecurse = true;
        try {
            if (M == null) return false;
            if (CMLib.flags().isBoundOrHeld(M)) return false;
            if (((!M.isMonster()) && (!doPlayers))) return false;
            if (CMLib.flags().isPossiblyAggressive(M)) return true;
            return ((CMLib.flags().isEvil(M)) || (M.baseCharStats().getCurrentClass().baseClass().equalsIgnoreCase("Thief")));
        } finally {
            norecurse = false;
        }
    }

    public void executeMsg(Environmental affecting, CMMsg msg) {
        super.executeMsg(affecting, msg);
        MOB source = msg.source();
        if (!canFreelyBehaveNormal(affecting)) {
            deepBreath = System.currentTimeMillis();
            return;
        }
        if (msg.sourceMinor() == CMMsg.TYP_LIFE) {
            MOB observer = (MOB) affecting;
            if ((observer.getVictim() == msg.source()) || (msg.source().getVictim() == observer)) observer.makePeace();
            synchronized (protectedOnes) {
                int x = protectedOnes.indexOf(msg.source().Name());
                if (x >= 0) protectedOnes.setElementAt(x, 2, Long.valueOf(System.currentTimeMillis())); else protectedOnes.addElement(msg.source().Name(), Long.valueOf(System.currentTimeMillis()));
            }
        }
        if ((deepBreath == 0) || (System.currentTimeMillis() - deepBreath) > 6000) {
            synchronized (protectedOnes) {
                for (int p = protectedOnes.size() - 1; p >= 0; p--) {
                    if ((System.currentTimeMillis() - ((Long) protectedOnes.elementAt(p, 2)).longValue()) > (30 * 1000)) protectedOnes.removeElementAt(p);
                }
                if (protectedOnes.contains(msg.source().Name())) return;
            }
            deepBreath = 0;
            MOB observer = (MOB) affecting;
            if ((source.isMonster() || doPlayers) && (source != observer) && (grantsAggressivenessTo(source))) {
                String reason = "EVIL";
                if (source.baseCharStats().getCurrentClass().baseClass().equalsIgnoreCase("Thief")) reason = "A THIEF";
                MOB oldFollowing = source.amFollowing();
                source.setFollowing(null);
                boolean yep = Aggressive.startFight(observer, source, true, false, source.name().toUpperCase() + " IS " + reason + ", AND MUST BE DESTROYED!");
                if (!yep) if (oldFollowing != null) source.setFollowing(oldFollowing);
            }
        }
    }
}
