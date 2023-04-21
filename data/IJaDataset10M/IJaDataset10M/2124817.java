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
public class Cub extends Bear {

    public String ID() {
        return "Cub";
    }

    public String name() {
        return "Cub";
    }

    public int shortestMale() {
        return 24;
    }

    public int shortestFemale() {
        return 24;
    }

    public int heightVariance() {
        return 6;
    }

    public int lightestWeight() {
        return 45;
    }

    public int weightVariance() {
        return 10;
    }

    public String racialCategory() {
        return "Ursine";
    }

    private static final int[] parts = { 0, 2, 2, 1, 1, 0, 0, 1, 4, 4, 1, 0, 1, 1, 0, 0 };

    public int[] bodyMask() {
        return parts;
    }

    protected static Vector resources = new Vector();

    public int availabilityCode() {
        return Area.THEME_FANTASY | Area.THEME_SKILLONLYMASK;
    }

    public void affectCharStats(MOB affectedMOB, CharStats affectableStats) {
        super.affectCharStats(affectedMOB, affectableStats);
        affectableStats.setRacialStat(CharStats.STAT_STRENGTH, 10);
        affectableStats.setRacialStat(CharStats.STAT_DEXTERITY, 10);
        affectableStats.setRacialStat(CharStats.STAT_INTELLIGENCE, 1);
    }

    public Weapon myNaturalWeapon() {
        if (naturalWeapon == null) {
            naturalWeapon = CMClass.getWeapon("StdWeapon");
            naturalWeapon.setName("a pair of claws");
            naturalWeapon.setWeaponType(Weapon.TYPE_SLASHING);
        }
        return naturalWeapon;
    }

    public Vector myResources() {
        synchronized (resources) {
            if (resources.size() == 0) {
                resources.addElement(makeResource("a " + name().toLowerCase() + " hide", RawMaterial.RESOURCE_FUR));
                resources.addElement(makeResource("some " + name().toLowerCase() + " paws", RawMaterial.RESOURCE_HIDE));
                resources.addElement(makeResource("a pound of " + name().toLowerCase() + " meat", RawMaterial.RESOURCE_MEAT));
                resources.addElement(makeResource("some " + name().toLowerCase() + " blood", RawMaterial.RESOURCE_BLOOD));
                resources.addElement(makeResource("a pile of " + name().toLowerCase() + " bones", RawMaterial.RESOURCE_BONE));
            }
        }
        return resources;
    }
}
