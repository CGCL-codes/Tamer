package com.planet_ink.coffee_mud.Abilities.Specializations;

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

public class Specialization_Axe extends Specialization_Weapon {

    public String ID() {
        return "Specialization_Axe";
    }

    public String name() {
        return "Axe Specialization";
    }

    public Specialization_Axe() {
        super();
        weaponType = Weapon.CLASS_AXE;
    }

    private static final String[] EXPERTISES = { "AXESTRIKE", "AXESLICE", "AXEPIERCE" };

    private static final String[] EXPERTISE_NAMES = { "Axe Striking", "Axe Slicing", "Axe Piercing" };

    private static final String[] EXPERTISE_STATS = { "DEX", "STR", "STR" };

    private static final int[] EXPERTISE_LEVELS = { 24, 27, 27 };

    private final int[] EXPERTISE_DAMAGE_TYPE = { 0, Weapon.TYPE_SLASHING, Weapon.TYPE_PIERCING };

    protected String[] EXPERTISES() {
        return EXPERTISES;
    }

    protected String[] EXPERTISES_NAMES() {
        return EXPERTISE_NAMES;
    }

    protected String[] EXPERTISE_STATS() {
        return EXPERTISE_STATS;
    }

    protected int[] EXPERTISE_LEVELS() {
        return EXPERTISE_LEVELS;
    }

    protected int[] EXPERTISE_DAMAGE_TYPE() {
        return EXPERTISE_DAMAGE_TYPE;
    }
}
