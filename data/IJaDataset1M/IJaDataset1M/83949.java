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
public class RoadThinGrid extends StdThinGrid {

    public String ID() {
        return "RoadThinGrid";
    }

    public RoadThinGrid() {
        super();
        name = "a road";
        baseEnvStats.setWeight(1);
        recoverEnvStats();
    }

    public int domainType() {
        return Room.DOMAIN_OUTDOORS_PLAINS;
    }

    public int domainConditions() {
        return Room.CONDITION_NORMAL;
    }

    public CMObject newInstance() {
        if (!CMSecurity.isDisabled("THINGRIDS")) return super.newInstance();
        return new RoadGrid().newInstance();
    }

    public String getGridChildLocaleID() {
        return "Road";
    }

    public Vector resourceChoices() {
        return Road.roomResources;
    }
}
