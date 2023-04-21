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
public class Chicken extends StdRace {

    public String ID() {
        return "Chicken";
    }

    public String name() {
        return "Chicken";
    }

    public int shortestMale() {
        return 13;
    }

    public int shortestFemale() {
        return 13;
    }

    public int heightVariance() {
        return 6;
    }

    public int lightestWeight() {
        return 20;
    }

    public int weightVariance() {
        return 5;
    }

    public long forbiddenWornBits() {
        return Integer.MAX_VALUE - Wearable.WORN_HEAD - Wearable.WORN_EYES;
    }

    public String racialCategory() {
        return "Avian";
    }

    private static final int[] parts = { 0, 2, 0, 1, 1, 0, 0, 1, 2, 2, 0, 0, 1, 1, 0, 2 };

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

    public void affectCharStats(MOB affectedMOB, CharStats affectableStats) {
        super.affectCharStats(affectedMOB, affectableStats);
        affectableStats.setRacialStat(CharStats.STAT_STRENGTH, 3);
        affectableStats.setRacialStat(CharStats.STAT_DEXTERITY, 4);
        affectableStats.setRacialStat(CharStats.STAT_INTELLIGENCE, 1);
    }

    public String arriveStr() {
        return "walks in";
    }

    public String leaveStr() {
        return "walks";
    }

    public Weapon myNaturalWeapon() {
        if (naturalWeapon == null) {
            naturalWeapon = CMClass.getWeapon("StdWeapon");
            naturalWeapon.setName("a menacing beak");
            naturalWeapon.setWeaponType(Weapon.TYPE_NATURAL);
        }
        return naturalWeapon;
    }

    public String healthText(MOB viewer, MOB mob) {
        double pct = (CMath.div(mob.curState().getHitPoints(), mob.maxState().getHitPoints()));
        if (pct < .10) return "^r" + mob.displayName(viewer) + "^r is hovering on deaths door!^N"; else if (pct < .20) return "^r" + mob.displayName(viewer) + "^r is covered in blood and matted feathers.^N"; else if (pct < .30) return "^r" + mob.displayName(viewer) + "^r is bleeding badly from lots of wounds.^N"; else if (pct < .40) return "^y" + mob.displayName(viewer) + "^y has numerous bloody matted feathers.^N"; else if (pct < .50) return "^y" + mob.displayName(viewer) + "^y has some bloody matted feathers.^N"; else if (pct < .60) return "^p" + mob.displayName(viewer) + "^p has a lot of missing feathers.^N"; else if (pct < .70) return "^p" + mob.displayName(viewer) + "^p has a few missing feathers.^N"; else if (pct < .80) return "^g" + mob.displayName(viewer) + "^g has a missing feather.^N"; else if (pct < .90) return "^g" + mob.displayName(viewer) + "^g has a few feathers out of place.^N"; else if (pct < .99) return "^g" + mob.displayName(viewer) + "^g has a some ruffled features.^N"; else return "^c" + mob.displayName(viewer) + "^c is in perfect health.^N";
    }

    public boolean tick(Tickable ticking, int tickID) {
        if (!super.tick(ticking, tickID)) return false;
        if ((tickID == Tickable.TICKID_MOB) && (ticking instanceof MOB)) {
            if ((CMLib.dice().rollPercentage() > 99) && (((MOB) ticking).inventorySize() < 9)) {
                Item I = CMClass.getItem("GenFoodResource");
                I.setName("an egg");
                I.setDisplayText("an egg has been left here.");
                I.setMaterial(RawMaterial.RESOURCE_EGGS);
                I.setDescription("It looks like a chicken egg!");
                I.baseEnvStats().setWeight(1);
                CMLib.materials().addEffectsToResource(I);
                ((MOB) ticking).addInventory((Item) I.copyOf());
            }
            if ((((MOB) ticking).inventorySize() > 5) && (((MOB) ticking).location() != null) && (((MOB) ticking).location().fetchItem(null, "an egg") == null)) {
                Item I = ((MOB) ticking).fetchInventory("an egg");
                if (I != null) {
                    ((MOB) ticking).location().show(((MOB) ticking), null, CMMsg.MSG_NOISYMOVEMENT, "<S-NAME> lay(s) an egg.");
                    I.removeFromOwnerContainer();
                    I.executeMsg((MOB) ticking, CMClass.getMsg((MOB) ticking, I, null, CMMsg.TYP_ROOMRESET, null));
                    ((MOB) ticking).location().addItemRefuse(I, CMProps.getIntVar(CMProps.SYSTEMI_EXPIRE_RESOURCE));
                    ((MOB) ticking).location().recoverRoomStats();
                }
            }
        }
        return true;
    }

    public Vector myResources() {
        synchronized (resources) {
            if (resources.size() == 0) {
                resources.addElement(makeResource("some " + name().toLowerCase() + " lips", RawMaterial.RESOURCE_MEAT));
                resources.addElement(makeResource("some " + name().toLowerCase() + " feathers", RawMaterial.RESOURCE_FEATHERS));
                resources.addElement(makeResource("some " + name().toLowerCase() + " meat", RawMaterial.RESOURCE_POULTRY));
                resources.addElement(makeResource("some " + name().toLowerCase() + " blood", RawMaterial.RESOURCE_BLOOD));
                resources.addElement(makeResource("a pile of " + name().toLowerCase() + " bones", RawMaterial.RESOURCE_BONE));
            }
        }
        return resources;
    }
}
