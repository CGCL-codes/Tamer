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

public class Clericness extends CombatAbilities {

    public String ID() {
        return "Clericness";
    }

    boolean confirmedSetup = false;

    public void startBehavior(Environmental forMe) {
        super.startBehavior(forMe);
        if (!(forMe instanceof MOB)) return;
        MOB mob = (MOB) forMe;
        combatMode = COMBAT_RANDOM;
        makeClass(mob, getParmsMinusCombatMode(), "Cleric");
        newCharacter(mob);
        if ((preCastSet == Integer.MAX_VALUE) || (preCastSet <= 0)) setCombatStats(mob, 0, 0, -25, -30, +25, -50);
    }
}
