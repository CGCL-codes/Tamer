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
public class Hills extends StdRoom {

    public String ID() {
        return "Hills";
    }

    public Hills() {
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

    public static final Integer[] resourceList = { Integer.valueOf(RawMaterial.RESOURCE_GRAPES), Integer.valueOf(RawMaterial.RESOURCE_BERRIES), Integer.valueOf(RawMaterial.RESOURCE_BLUEBERRIES), Integer.valueOf(RawMaterial.RESOURCE_BLACKBERRIES), Integer.valueOf(RawMaterial.RESOURCE_STRAWBERRIES), Integer.valueOf(RawMaterial.RESOURCE_RASPBERRIES), Integer.valueOf(RawMaterial.RESOURCE_BOYSENBERRIES), Integer.valueOf(RawMaterial.RESOURCE_GREENS), Integer.valueOf(RawMaterial.RESOURCE_OLIVES), Integer.valueOf(RawMaterial.RESOURCE_BEANS), Integer.valueOf(RawMaterial.RESOURCE_RICE), Integer.valueOf(RawMaterial.RESOURCE_LEATHER), Integer.valueOf(RawMaterial.RESOURCE_FEATHERS), Integer.valueOf(RawMaterial.RESOURCE_MESQUITE), Integer.valueOf(RawMaterial.RESOURCE_EGGS), Integer.valueOf(RawMaterial.RESOURCE_HERBS), Integer.valueOf(RawMaterial.RESOURCE_POTATOES) };

    public static final Vector roomResources = new Vector(Arrays.asList(resourceList));

    public Vector resourceChoices() {
        return Hills.roomResources;
    }
}
