package com.planet_ink.coffee_mud.Items.Basic;

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

public class Ration extends StdFood {

    public String ID() {
        return "Ration";
    }

    public Ration() {
        super();
        setName("a ration pack");
        baseEnvStats.setWeight(10);
        amountOfNourishment = 500;
        setDisplayText("a standard ration pack sits here.");
        setDescription("Bits of salt dried meat, dried fruit, and hard bread.");
        baseGoldValue = 15;
        setMaterial(RawMaterial.RESOURCE_MEAT);
        recoverEnvStats();
    }
}
