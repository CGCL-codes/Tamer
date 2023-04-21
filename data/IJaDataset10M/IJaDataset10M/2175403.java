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
public class Pachyderm extends StdRace {

    public String ID() {
        return "Pachyderm";
    }

    public String name() {
        return "Pachyderm";
    }

    public int shortestMale() {
        return 60;
    }

    public int shortestFemale() {
        return 60;
    }

    public int heightVariance() {
        return 12;
    }

    public int lightestWeight() {
        return 850;
    }

    public int weightVariance() {
        return 300;
    }

    public long forbiddenWornBits() {
        return Integer.MAX_VALUE - Wearable.WORN_HEAD - Wearable.WORN_EARS - Wearable.WORN_EYES;
    }

    public String racialCategory() {
        return "Pachyderm";
    }

    private static final int[] parts = { 0, 2, 2, 1, 1, 0, 0, 1, 4, 4, 1, 0, 1, 1, 1, 0 };

    public int[] bodyMask() {
        return parts;
    }

    private int[] agingChart = { 0, 4, 8, 16, 28, 60, 80, 82, 84 };

    public int[] getAgingChart() {
        return agingChart;
    }

    protected static Vector resources = new Vector();

    public int availabilityCode() {
        return Area.THEME_FANTASY | Area.THEME_SKILLONLYMASK;
    }

    public void affectCharStats(MOB affectedMOB, CharStats affectableStats) {
        super.affectCharStats(affectedMOB, affectableStats);
        affectableStats.setRacialStat(CharStats.STAT_STRENGTH, 18);
        affectableStats.setRacialStat(CharStats.STAT_DEXTERITY, 3);
        affectableStats.setRacialStat(CharStats.STAT_INTELLIGENCE, 1);
    }

    public Weapon myNaturalWeapon() {
        if (naturalWeapon == null) {
            naturalWeapon = CMClass.getWeapon("StdWeapon");
            naturalWeapon.setName("a pair of tusks");
            naturalWeapon.setWeaponType(Weapon.TYPE_PIERCING);
        }
        return naturalWeapon;
    }

    public String healthText(MOB viewer, MOB mob) {
        double pct = (CMath.div(mob.curState().getHitPoints(), mob.maxState().getHitPoints()));
        if (pct < .10) return "^r" + mob.displayName(viewer) + "^r is almost dead!^N"; else if (pct < .20) return "^r" + mob.displayName(viewer) + "^r is covered in blood.^N"; else if (pct < .30) return "^r" + mob.displayName(viewer) + "^r is bleeding badly from lots of wounds.^N"; else if (pct < .40) return "^y" + mob.displayName(viewer) + "^y has numerous bloody wounds and gashes.^N"; else if (pct < .50) return "^y" + mob.displayName(viewer) + "^y has some bloody wounds and gashes.^N"; else if (pct < .60) return "^p" + mob.displayName(viewer) + "^p has a few bloody wounds.^N"; else if (pct < .70) return "^p" + mob.displayName(viewer) + "^p is cut and bruised heavily.^N"; else if (pct < .80) return "^g" + mob.displayName(viewer) + "^g has some minor cuts and bruises.^N"; else if (pct < .90) return "^g" + mob.displayName(viewer) + "^g has a few bruises and scratched scales.^N"; else if (pct < .99) return "^g" + mob.displayName(viewer) + "^g has a few small bruises.^N"; else return "^c" + mob.displayName(viewer) + "^c is in perfect health.^N";
    }

    public Vector myResources() {
        synchronized (resources) {
            if (resources.size() == 0) {
                for (int i = 0; i < 12; i++) resources.addElement(makeResource("a strip of " + name().toLowerCase() + " hide", RawMaterial.RESOURCE_LEATHER));
                for (int i = 0; i < 52; i++) resources.addElement(makeResource("a pound of " + name().toLowerCase() + " meat", RawMaterial.RESOURCE_BEEF));
                resources.addElement(makeResource("some " + name().toLowerCase() + " blood", RawMaterial.RESOURCE_BLOOD));
                resources.addElement(makeResource("a " + name().toLowerCase() + " tusk", RawMaterial.RESOURCE_BONE));
            }
        }
        return resources;
    }
}
