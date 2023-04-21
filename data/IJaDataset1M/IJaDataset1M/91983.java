package com.planet_ink.coffee_mud.Items.MiscTech;

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

public class GenSSEngine extends GenShipComponent implements ShipComponent.ShipEngine {

    public String ID() {
        return "GenSSEngine";
    }

    public GenSSEngine() {
        super();
        setName("a generic ships engine");
        baseEnvStats.setWeight(500);
        setDisplayText("a generic ships engine sits here.");
        setDescription("");
        baseGoldValue = 500000;
        baseEnvStats().setLevel(1);
        recoverEnvStats();
        setMaterial(RawMaterial.RESOURCE_STEEL);
    }

    public boolean sameAs(Environmental E) {
        if (!(E instanceof GenSSEngine)) return false;
        return super.sameAs(E);
    }

    protected int maxThrust = 1000;

    public int getMaxThrust() {
        return maxThrust;
    }

    public void setMaxThrust(int max) {
        maxThrust = max;
    }

    protected int thrust = 1000;

    public int getThrust() {
        return thrust;
    }

    public void setThrust(int current) {
        thrust = current;
    }
}
