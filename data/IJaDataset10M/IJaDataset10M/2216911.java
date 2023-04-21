package com.planet_ink.coffee_mud.Races;

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
import java.util.Vector;

@SuppressWarnings("unchecked")
public class DireRat extends GiantRat {

    public String ID() {
        return "DireRat";
    }

    public String name() {
        return "Dire Rat";
    }

    protected static Vector resources = new Vector();

    public String racialCategory() {
        return "Rodent";
    }

    private static final int[] parts = { 0, 2, 2, 1, 1, 0, 0, 1, 4, 4, 1, 0, 1, 1, 1, 0 };

    public int[] bodyMask() {
        return parts;
    }

    public Vector myResources() {
        synchronized (resources) {
            if (resources.size() == 0) {
                for (int i = 0; i < 5; i++) resources.addElement(makeResource("a strip of " + name().toLowerCase() + " hide", RawMaterial.RESOURCE_FUR));
                for (int i = 0; i < 2; i++) resources.addElement(makeResource("a pound of " + name().toLowerCase() + " meat", RawMaterial.RESOURCE_MEAT));
                resources.addElement(makeResource("a pair of " + name().toLowerCase() + " teeth", RawMaterial.RESOURCE_BONE));
                resources.addElement(makeResource("some " + name().toLowerCase() + " blood", RawMaterial.RESOURCE_BLOOD));
            }
        }
        return resources;
    }
}
