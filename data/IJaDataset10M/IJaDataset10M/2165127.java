package com.planet_ink.coffee_mud.MOBS;

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

public class Goblin extends StdMOB {

    public String ID() {
        return "Goblin";
    }

    Random randomizer = new Random(System.currentTimeMillis());

    int birthType = 0;

    public Goblin() {
        super();
        int goblinType = Math.abs(randomizer.nextInt() % 1000);
        setMOBSpecifics(goblinType);
        baseCharStats().setMyRace(CMClass.getRace("Goblin"));
        baseCharStats().getMyRace().startRacing(this, false);
        recoverMaxState();
        resetToMaxState();
        recoverEnvStats();
        recoverCharStats();
    }

    public boolean tick(Tickable ticking, int tickID) {
        if (tickID == Tickable.TICKID_MOB) {
            if (birthType != baseEnvStats().ability()) setMOBSpecifics(baseEnvStats().ability());
        }
        return super.tick(ticking, tickID);
    }

    public void setMOBSpecifics(int goblinType) {
        if (!CMProps.getBoolVar(CMProps.SYSTEMB_MUDSTARTED)) return;
        if (goblinType < 0) goblinType *= -1;
        while (this.inventorySize() > 0) {
            Item I = fetchInventory(0);
            if (I != null) this.delInventory(I);
        }
        birthType = goblinType;
        setMoney(randomizer.nextInt() % 15);
        setWimpHitPoint(0);
        baseEnvStats.setWeight(40 + Math.abs(randomizer.nextInt() % 30));
        CMLib.factions().setAlignment(this, Faction.ALIGN_EVIL);
        baseCharStats().setStat(CharStats.STAT_INTELLIGENCE, 5 + Math.abs(randomizer.nextInt() % 6));
        baseCharStats().setStat(CharStats.STAT_CHARISMA, 2 + Math.abs(randomizer.nextInt() % 3));
        baseEnvStats().setArmor(25 + Math.abs(randomizer.nextInt() % 20));
        baseEnvStats().setLevel(1 + Math.abs(randomizer.nextInt() % 3));
        baseEnvStats().setAbility(goblinType);
        baseState.setHitPoints(CMLib.dice().roll(baseEnvStats().level(), 20, baseEnvStats().level()));
        Weapon m = null;
        Armor c = null;
        if (goblinType > 0 && goblinType <= 99) {
            Username = "a nasty Goblin";
            setDescription("He\\`s dirty, cranky, and very smelly.");
            setDisplayText("A nasty goblin marches around.");
            m = CMClass.getWeapon("Mace");
        }
        if (goblinType > 100 && goblinType <= 199) {
            Username = "a Goblin";
            setDescription("He\\`s smelly and has red skin.");
            setDisplayText("A nasty goblin scuttles about.");
            m = CMClass.getWeapon("Mace");
        }
        if (goblinType > 200 && goblinType <= 299) {
            Username = "an ugly Goblin";
            setDescription("He\\`s dirty, cranky, and very smelly.");
            setDisplayText("A nasty goblin scurries nearby.");
            m = CMClass.getWeapon("Mace");
        }
        if (goblinType > 300 && goblinType <= 399) {
            Username = "a Goblin female";
            setDescription("She\\`s ugly and very smelly.");
            setDisplayText("A female goblin sits nearby.");
        }
        if (goblinType > 400 && goblinType <= 499) {
            Username = "a mean Goblin";
            setDescription("He appears to be bigger...and smellier than most goblins.");
            setDisplayText("A mean goblin glares at you.");
            m = CMClass.getWeapon("Shortsword");
            c = CMClass.getArmor("ChainMailArmor");
        }
        if (goblinType > 500 && goblinType <= 599) {
            Username = "a smelly Goblin";
            setDescription("He\\`s dirty, cranky, and very smelly.");
            setDisplayText("A nasty goblin sits nearby.");
            m = CMClass.getWeapon("Mace");
        }
        if (goblinType > 600 && goblinType <= 699) {
            Username = "a Goblin";
            setDescription("He\\`s dirty, cranky, and very smelly.");
            setDisplayText("A very smelly goblin stands near you.");
            m = CMClass.getWeapon("Mace");
        }
        if (goblinType > 700 && goblinType <= 799) {
            Username = "a Goblin";
            setDescription("He\\`s dirty, cranky, and very smelly.");
            setDisplayText("A nasty goblin glares are you with lemon colored eyes.");
            m = CMClass.getWeapon("Mace");
        }
        if (goblinType > 800 && goblinType <= 899) {
            Username = "a Goblin";
            setDescription("He\\`s dirty, cranky, and very smelly.");
            setDisplayText("A goblin stares are you with red eyes.");
            m = CMClass.getWeapon("Mace");
        }
        if (goblinType > 900 && goblinType <= 999) {
            Username = "an armed Goblin";
            setDescription("He\\`s wielding a sword.");
            setDisplayText("A nasty goblin marches around.");
            m = CMClass.getWeapon("Shortsword");
        }
        if (m != null) {
            m.wearAt(Wearable.WORN_WIELD);
            addInventory(m);
        }
        if (c != null) {
            c.wearAt(Wearable.WORN_TORSO);
            addInventory(c);
        }
    }
}
