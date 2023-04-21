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

public class Song_Health extends Song {

    public String ID() {
        return "Song_Health";
    }

    public String name() {
        return "Health";
    }

    public int abstractQuality() {
        return Ability.QUALITY_BENEFICIAL_OTHERS;
    }

    public int castingQuality(MOB mob, Environmental target) {
        if (mob != null) {
            if (target instanceof MOB) {
                if (((MOB) target).curState().getHitPoints() >= ((MOB) target).maxState().getHitPoints() / 2) return Ability.QUALITY_INDIFFERENT;
            }
        }
        return super.castingQuality(mob, target);
    }

    public void affectCharState(MOB affectedMOB, CharState affectedState) {
        if (invoker != null) affectedState.setHitPoints(affectedState.getHitPoints() + (adjustedLevel(invoker(), 0) * 5));
    }

    public void affectCharStats(MOB affectedMOB, CharStats affectedStats) {
        super.affectCharStats(affectedMOB, affectedStats);
        affectedStats.setStat(CharStats.STAT_SAVE_POISON, affectedStats.getStat(CharStats.STAT_SAVE_POISON) + adjustedLevel(invoker(), 0));
    }
}
