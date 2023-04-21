package com.planet_ink.coffee_mud.MOBS;

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

public class Sheep extends StdMOB {

    public String ID() {
        return "Sheep";
    }

    public Sheep() {
        super();
        Username = "a sheep";
        setDescription("She looks warm in that big fluffy coat of wool, but is nervous that you are so close.");
        setDisplayText("A sheep has wandered away from the herd.");
        CMLib.factions().setAlignment(this, Faction.ALIGN_NEUTRAL);
        setMoney(0);
        setWimpHitPoint(0);
        baseEnvStats().setDamage(1);
        baseEnvStats().setSpeed(1.0);
        baseEnvStats().setAbility(0);
        baseEnvStats().setLevel(1);
        baseEnvStats().setArmor(50);
        baseCharStats().setMyRace(CMClass.getRace("Sheep"));
        baseCharStats().getMyRace().startRacing(this, false);
        baseState.setHitPoints(CMLib.dice().roll(baseEnvStats().level(), 20, baseEnvStats().level()));
        recoverMaxState();
        resetToMaxState();
        recoverEnvStats();
        recoverCharStats();
    }
}
