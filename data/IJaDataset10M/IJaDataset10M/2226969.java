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

@SuppressWarnings("unchecked")
public class HillsGrid extends StdGrid {

    public String ID() {
        return "HillsGrid";
    }

    public HillsGrid() {
        super();
        name = "the hills";
        baseEnvStats.setWeight(3);
        recoverEnvStats();
    }

    public int domainType() {
        return Room.DOMAIN_OUTDOORS_HILLS;
    }

    public int domainConditions() {
        return Room.CONDITION_NORMAL;
    }

    public String getGridChildLocaleID() {
        return "Hills";
    }

    public Vector resourceChoices() {
        return Hills.roomResources;
    }
}
