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

public class Assassin extends GenMob {

    public String ID() {
        return "Assassin";
    }

    public Assassin() {
        super();
        Username = "an assassin";
        setDescription("He`s all dressed in black, and has eyes as cold as ice.");
        setDisplayText("An assassin stands here.");
        Race R = CMClass.getRace("Human");
        if (R != null) {
            baseCharStats().setMyRace(R);
            R.startRacing(this, false);
        }
        baseCharStats().setStat(CharStats.STAT_DEXTERITY, 18);
        baseCharStats().setStat(CharStats.STAT_GENDER, 'M');
        baseCharStats().setStat(CharStats.STAT_WISDOM, 18);
        baseEnvStats().setSensesMask(baseEnvStats().disposition() | EnvStats.CAN_SEE_DARK);
        Ability A = CMClass.getAbility("Thief_Hide");
        if (A != null) {
            A.setProficiency(100);
            addAbility(A);
        }
        A = CMClass.getAbility("Thief_Sneak");
        if (A != null) {
            A.setProficiency(100);
            addAbility(A);
        }
        A = CMClass.getAbility("Thief_BackStab");
        if (A != null) {
            A.setProficiency(100);
            addAbility(A);
        }
        A = CMClass.getAbility("Thief_Assassinate");
        if (A != null) {
            A.setProficiency(100);
            addAbility(A);
        }
        Item I = CMClass.getWeapon("Longsword");
        if (I != null) {
            addInventory(I);
            I.wearAt(Wearable.WORN_WIELD);
        }
        I = CMClass.getArmor("LeatherArmor");
        if (I != null) {
            addInventory(I);
            I.wearIfPossible(this);
        }
        Weapon d = CMClass.getWeapon("Dagger");
        if (d != null) {
            d.wearAt(Wearable.WORN_HELD);
            addInventory(d);
        }
        recoverMaxState();
        resetToMaxState();
        recoverEnvStats();
        recoverCharStats();
    }
}
