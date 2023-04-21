package com.planet_ink.coffee_mud.Abilities.Poisons;

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

public class Poison_Mindsap extends Poison {

    public String ID() {
        return "Poison_Mindsap";
    }

    public String name() {
        return "Mindsap";
    }

    private static final String[] triggerStrings = { "POISONSAP" };

    public String[] triggerStrings() {
        return triggerStrings;
    }

    protected int POISON_TICKS() {
        return 50;
    }

    protected int POISON_DELAY() {
        return 1;
    }

    protected String POISON_DONE() {
        return "Your thoughts clear up.";
    }

    protected String POISON_START() {
        return "^G<S-NAME> seem(s) confused!^?";
    }

    protected String POISON_AFFECT() {
        return "";
    }

    protected String POISON_CAST() {
        return "^F^<FIGHT^><S-NAME> poison(s) <T-NAMESELF>!^</FIGHT^>^?";
    }

    protected String POISON_FAIL() {
        return "<S-NAME> attempt(s) to poison <T-NAMESELF>, but fail(s).";
    }

    protected int POISON_DAMAGE() {
        return 0;
    }

    public void affectCharStats(MOB affected, CharStats affectableStats) {
        affectableStats.setStat(CharStats.STAT_CONSTITUTION, affectableStats.getStat(CharStats.STAT_CONSTITUTION) - 1);
        if (affectableStats.getStat(CharStats.STAT_CONSTITUTION) <= 0) affectableStats.setStat(CharStats.STAT_CONSTITUTION, 1);
        affectableStats.setStat(CharStats.STAT_INTELLIGENCE, affectableStats.getStat(CharStats.STAT_INTELLIGENCE) - 10);
        if (affectableStats.getStat(CharStats.STAT_INTELLIGENCE) <= 0) affectableStats.setStat(CharStats.STAT_INTELLIGENCE, 1);
    }
}
