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
public class GiantSpider extends StdRace {

    public String ID() {
        return "GiantSpider";
    }

    public String name() {
        return "Giant Spider";
    }

    public int shortestMale() {
        return 42;
    }

    public int shortestFemale() {
        return 42;
    }

    public int heightVariance() {
        return 8;
    }

    public int lightestWeight() {
        return 180;
    }

    public int weightVariance() {
        return 40;
    }

    public long forbiddenWornBits() {
        return Integer.MAX_VALUE;
    }

    public String racialCategory() {
        return "Arachnid";
    }

    private static final int[] parts = { 2, 99, 0, 1, 0, 0, 0, 1, 8, 8, 0, 0, 1, 0, 1, 0 };

    public int[] bodyMask() {
        return parts;
    }

    private int[] agingChart = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };

    public int[] getAgingChart() {
        return agingChart;
    }

    protected static Vector resources = new Vector();

    public int availabilityCode() {
        return Area.THEME_FANTASY | Area.THEME_SKILLONLYMASK;
    }

    public void affectEnvStats(Environmental affected, EnvStats affectableStats) {
        super.affectEnvStats(affected, affectableStats);
        affectableStats.setDisposition(affectableStats.disposition() | EnvStats.IS_SNEAKING);
    }

    public void affectCharStats(MOB affectedMOB, CharStats affectableStats) {
        super.affectCharStats(affectedMOB, affectableStats);
        affectableStats.setStat(CharStats.STAT_SAVE_POISON, affectableStats.getStat(CharStats.STAT_SAVE_POISON) + 100);
    }

    public String arriveStr() {
        return "creeps in";
    }

    public String leaveStr() {
        return "creeps";
    }

    public Weapon myNaturalWeapon() {
        if (naturalWeapon == null) {
            naturalWeapon = CMClass.getWeapon("StdWeapon");
            naturalWeapon.setName("a nasty maw");
            naturalWeapon.setWeaponType(Weapon.TYPE_NATURAL);
        }
        return naturalWeapon;
    }

    public Vector myResources() {
        synchronized (resources) {
            if (resources.size() == 0) {
                resources.addElement(makeResource("some " + name().toLowerCase() + " legs", RawMaterial.RESOURCE_MEAT));
            }
        }
        return resources;
    }
}
