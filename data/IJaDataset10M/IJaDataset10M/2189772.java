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

public class Dance_Foxtrot extends Dance {

    public String ID() {
        return "Dance_Foxtrot";
    }

    public String name() {
        return "Foxtrot";
    }

    public int abstractQuality() {
        return Ability.QUALITY_BENEFICIAL_OTHERS;
    }

    protected int ticks = 1;

    protected int increment = 1;

    public int castingQuality(MOB mob, Environmental target) {
        if (mob != null) {
            if (target instanceof MOB) {
                if ((((MOB) target).curState().getMana() >= ((MOB) target).maxState().getMana() / 2) && (((MOB) target).curState().getMovement() >= ((MOB) target).maxState().getMovement() / 2)) return Ability.QUALITY_INDIFFERENT;
            }
        }
        return super.castingQuality(mob, target);
    }

    public boolean tick(Tickable ticking, int tickID) {
        if (!super.tick(ticking, tickID)) return false;
        MOB mob = (MOB) affected;
        if (mob == null) return false;
        mob.curState().adjMovement((invokerManaCost / 15) + increment, mob.maxState());
        mob.curState().adjMana(increment, mob.maxState());
        if (increment <= 1 + (int) Math.round(CMath.div(adjustedLevel(invoker(), 0), 3))) {
            if ((++ticks) > 2) {
                increment++;
                ticks = 1;
            }
        }
        return true;
    }
}
