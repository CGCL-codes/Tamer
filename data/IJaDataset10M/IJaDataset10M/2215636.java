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
public class Eagle extends GreatBird {

    public String ID() {
        return "Eagle";
    }

    public String name() {
        return "Eagle";
    }

    public String racialCategory() {
        return "Avian";
    }

    private static final int[] parts = { 0, 2, 2, 1, 1, 0, 0, 1, 2, 2, 1, 0, 1, 1, 1, 2 };

    public int[] bodyMask() {
        return parts;
    }

    protected static Vector resources = new Vector();

    public void affectCharStats(MOB affectedMOB, CharStats affectableStats) {
        super.affectCharStats(affectedMOB, affectableStats);
        affectableStats.setRacialStat(CharStats.STAT_STRENGTH, 10);
        affectableStats.setRacialStat(CharStats.STAT_DEXTERITY, 11);
    }

    public Vector myResources() {
        synchronized (resources) {
            if (resources.size() == 0) {
                resources.addElement(makeResource("an " + name().toLowerCase() + " beak", RawMaterial.RESOURCE_BONE));
                resources.addElement(makeResource("some " + name().toLowerCase() + " feathers", RawMaterial.RESOURCE_FEATHERS));
                resources.addElement(makeResource("a pound of " + name().toLowerCase() + " meat", RawMaterial.RESOURCE_MEAT));
                resources.addElement(makeResource("some " + name().toLowerCase() + " blood", RawMaterial.RESOURCE_BLOOD));
                resources.addElement(makeResource("a pile of " + name().toLowerCase() + " bones", RawMaterial.RESOURCE_BONE));
            }
        }
        return resources;
    }
}
