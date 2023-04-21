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
import java.util.*;

@SuppressWarnings("unchecked")
public class Chimp extends Monkey {

    public String ID() {
        return "Chimp";
    }

    public String name() {
        return "Chimp";
    }

    public int shortestMale() {
        return 36;
    }

    public int shortestFemale() {
        return 34;
    }

    public int heightVariance() {
        return 8;
    }

    public int lightestWeight() {
        return 80;
    }

    public int weightVariance() {
        return 50;
    }

    public String racialCategory() {
        return "Primate";
    }

    private static final int[] parts = { 0, 2, 2, 1, 1, 2, 2, 1, 2, 2, 1, 0, 1, 1, 1, 0 };

    public int[] bodyMask() {
        return parts;
    }

    protected static Vector resources = new Vector();

    public void affectCharStats(MOB affectedMOB, CharStats affectableStats) {
        super.affectCharStats(affectedMOB, affectableStats);
        affectableStats.setRacialStat(CharStats.STAT_STRENGTH, 15);
        affectableStats.setRacialStat(CharStats.STAT_DEXTERITY, 15);
        affectableStats.setRacialStat(CharStats.STAT_INTELLIGENCE, 1);
    }

    public Vector myResources() {
        synchronized (resources) {
            if (resources.size() == 0) {
                resources.addElement(makeResource("a " + name().toLowerCase() + " hide", RawMaterial.RESOURCE_FUR));
                resources.addElement(makeResource("some " + name().toLowerCase() + " toes", RawMaterial.RESOURCE_HIDE));
                resources.addElement(makeResource("a pound of " + name().toLowerCase() + " flesh", RawMaterial.RESOURCE_MEAT));
                resources.addElement(makeResource("some " + name().toLowerCase() + " blood", RawMaterial.RESOURCE_BLOOD));
                resources.addElement(makeResource("a pile of " + name().toLowerCase() + " bones", RawMaterial.RESOURCE_BONE));
            }
        }
        return resources;
    }
}
