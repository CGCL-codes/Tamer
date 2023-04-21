package com.planet_ink.coffee_mud.Items.Weapons;

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

public class Dirk extends Dagger {

    public String ID() {
        return "Dirk";
    }

    public Dirk() {
        super();
        setName("a dirk");
        setDisplayText("a pointy dirk is on the ground.");
        setDescription("The dirk is a single-edged, grooved weapon with a back edge near the point. ");
        baseEnvStats().setAbility(0);
        baseEnvStats().setLevel(0);
        baseEnvStats.setWeight(1);
        baseGoldValue = 2;
        baseEnvStats().setAttackAdjustment(0);
        baseEnvStats().setDamage(4);
        weaponType = TYPE_PIERCING;
        material = RawMaterial.RESOURCE_STEEL;
        recoverEnvStats();
    }
}
