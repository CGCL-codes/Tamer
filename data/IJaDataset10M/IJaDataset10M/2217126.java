package com.planet_ink.coffee_mud.Abilities.Songs;

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
public class Dance_Cotillon extends Dance {

    public String ID() {
        return "Dance_Cotillon";
    }

    public String name() {
        return "Cotillon";
    }

    public int abstractQuality() {
        return Ability.QUALITY_BENEFICIAL_OTHERS;
    }

    protected String danceOf() {
        return name() + " Dance";
    }

    protected boolean HAS_QUANTITATIVE_ASPECT() {
        return false;
    }

    protected MOB whichLast = null;

    public int castingQuality(MOB mob, Environmental target) {
        if (mob != null) {
            if ((!mob.isInCombat()) || (mob.getGroupMembers(new HashSet()).size() < 2)) return Ability.QUALITY_INDIFFERENT;
        }
        return super.castingQuality(mob, target);
    }

    public boolean tick(Tickable ticking, int tickID) {
        if (!super.tick(ticking, tickID)) return false;
        if ((affected == invoker()) && ((invoker()).isInCombat())) {
            if (whichLast == null) whichLast = invoker(); else {
                MOB M = (MOB) affected;
                boolean pass = false;
                boolean found = false;
                for (int i = 0; i < M.location().numInhabitants(); i++) {
                    MOB M2 = M.location().fetchInhabitant(i);
                    if (M2 == whichLast) found = true; else if ((M2 != whichLast) && (found) && (M2.fetchEffect(ID()) != null) && (M2.isInCombat())) {
                        whichLast = M2;
                        break;
                    }
                    if (i == (M.location().numInhabitants() - 1)) {
                        if (pass) return true;
                        pass = true;
                        i = -1;
                    }
                }
                if ((whichLast != null) && (M.isInCombat()) && (M.getVictim().getVictim() != whichLast) && (whichLast.location().show(whichLast, null, M.getVictim(), CMMsg.MSG_NOISYMOVEMENT, "<S-NAME> dance(s) into <O-YOUPOSS> way."))) M.getVictim().setVictim(whichLast);
            }
        }
        return true;
    }
}
