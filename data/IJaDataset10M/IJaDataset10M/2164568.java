package com.planet_ink.coffee_mud.Items.Armor;

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

public class Shirt extends GenArmor {

    public String ID() {
        return "Shirt";
    }

    public Shirt() {
        super();
        setName("a nice tunic");
        setDisplayText("a plain tunic is folded neatly here.");
        setDescription("It is a plain buttoned tunic.");
        properWornBitmap = Wearable.WORN_TORSO;
        wornLogicalAnd = true;
        baseEnvStats().setArmor(2);
        baseEnvStats().setWeight(1);
        baseEnvStats().setAbility(0);
        baseGoldValue = 1;
        recoverEnvStats();
        material = RawMaterial.RESOURCE_COTTON;
    }
}
