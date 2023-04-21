package com.planet_ink.coffee_mud.Locales;

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

public class SpacePort extends StdRoom {

    public String ID() {
        return "SpacePort";
    }

    public SpacePort() {
        super();
        name = "the space port";
        baseEnvStats.setWeight(1);
        recoverEnvStats();
    }

    public int domainType() {
        return Room.DOMAIN_OUTDOORS_SPACEPORT;
    }

    public int domainConditions() {
        return Room.CONDITION_NORMAL;
    }
}
