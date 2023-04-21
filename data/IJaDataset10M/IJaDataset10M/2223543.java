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
public class GreatBird extends StdRace {

    public String ID() {
        return "GreatBird";
    }

    public String name() {
        return "Great Bird";
    }

    public int shortestMale() {
        return 8;
    }

    public int shortestFemale() {
        return 8;
    }

    public int heightVariance() {
        return 10;
    }

    public int lightestWeight() {
        return 20;
    }

    public int weightVariance() {
        return 10;
    }

    public long forbiddenWornBits() {
        return Integer.MAX_VALUE - Wearable.WORN_HEAD - Wearable.WORN_EYES;
    }

    public String racialCategory() {
        return "Avian";
    }

    private String[] racialAbilityNames = { "WingFlying" };

    private int[] racialAbilityLevels = { 1 };

    private int[] racialAbilityProficiencies = { 100 };

    private boolean[] racialAbilityQuals = { false };

    protected String[] racialAbilityNames() {
        return racialAbilityNames;
    }

    protected int[] racialAbilityLevels() {
        return racialAbilityLevels;
    }

    protected int[] racialAbilityProficiencies() {
        return racialAbilityProficiencies;
    }

    protected boolean[] racialAbilityQuals() {
        return racialAbilityQuals;
    }

    private static final int[] parts = { 0, 2, 2, 1, 1, 0, 0, 1, 2, 2, 1, 0, 1, 1, 1, 2 };

    public int[] bodyMask() {
        return parts;
    }

    private int[] agingChart = { 0, 1, 2, 4, 7, 15, 20, 21, 22 };

    public int[] getAgingChart() {
        return agingChart;
    }

    protected static Vector resources = new Vector();

    public int availabilityCode() {
        return Area.THEME_FANTASY | Area.THEME_SKILLONLYMASK;
    }

    public void affectEnvStats(Environmental affected, EnvStats affectableStats) {
        super.affectEnvStats(affected, affectableStats);
        if (!CMLib.flags().isSleeping(affected)) affectableStats.setDisposition(affectableStats.disposition() | EnvStats.IS_FLYING);
        affectableStats.setSensesMask(affectableStats.sensesMask() | EnvStats.CAN_SEE_HIDDEN);
    }

    public void affectCharStats(MOB affectedMOB, CharStats affectableStats) {
        super.affectCharStats(affectedMOB, affectableStats);
        affectableStats.setRacialStat(CharStats.STAT_STRENGTH, 8);
        affectableStats.setRacialStat(CharStats.STAT_DEXTERITY, 10);
        affectableStats.setRacialStat(CharStats.STAT_INTELLIGENCE, 1);
        affectableStats.setStat(CharStats.STAT_SAVE_OVERLOOKING, affectableStats.getStat(CharStats.STAT_SAVE_OVERLOOKING) + 50);
    }

    public Weapon myNaturalWeapon() {
        if (naturalWeapon == null) {
            naturalWeapon = CMClass.getWeapon("StdWeapon");
            naturalWeapon.setName("some sharp talons");
            naturalWeapon.setWeaponType(Weapon.TYPE_PIERCING);
        }
        return naturalWeapon;
    }

    public String healthText(MOB viewer, MOB mob) {
        double pct = (CMath.div(mob.curState().getHitPoints(), mob.maxState().getHitPoints()));
        if (pct < .10) return "^r" + mob.displayName(viewer) + "^r is hovering on deaths door!^N"; else if (pct < .20) return "^r" + mob.displayName(viewer) + "^r is covered in blood and matted feathers.^N"; else if (pct < .30) return "^r" + mob.displayName(viewer) + "^r is bleeding badly from lots of wounds.^N"; else if (pct < .40) return "^y" + mob.displayName(viewer) + "^y has numerous bloody matted feathers.^N"; else if (pct < .50) return "^y" + mob.displayName(viewer) + "^y has some bloody matted feathers.^N"; else if (pct < .60) return "^p" + mob.displayName(viewer) + "^p has a lot of missing feathers.^N"; else if (pct < .70) return "^p" + mob.displayName(viewer) + "^p has a few missing feathers.^N"; else if (pct < .80) return "^g" + mob.displayName(viewer) + "^g has a missing feather.^N"; else if (pct < .90) return "^g" + mob.displayName(viewer) + "^g has a few feathers out of place.^N"; else if (pct < .99) return "^g" + mob.displayName(viewer) + "^g has a some ruffled features.^N"; else return "^c" + mob.displayName(viewer) + "^c is in perfect health.^N";
    }

    public Vector myResources() {
        synchronized (resources) {
            if (resources.size() == 0) {
                for (int i = 0; i < 2; i++) resources.addElement(makeResource("a pile of " + name().toLowerCase() + " feathers", RawMaterial.RESOURCE_FEATHERS));
                resources.addElement(makeResource("some " + name().toLowerCase() + " meat", RawMaterial.RESOURCE_POULTRY));
                resources.addElement(makeResource("some " + name().toLowerCase() + " blood", RawMaterial.RESOURCE_BLOOD));
                resources.addElement(makeResource("a pile of " + name().toLowerCase() + " bones", RawMaterial.RESOURCE_BONE));
            }
        }
        return resources;
    }
}
