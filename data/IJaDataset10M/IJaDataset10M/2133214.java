package com.planet_ink.coffee_mud.Abilities.Diseases;

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

public class Disease_Vampirism extends Disease {

    public String ID() {
        return "Disease_Vampirism";
    }

    public String name() {
        return "Vampirism";
    }

    public String displayText() {
        return "(Vampirism)";
    }

    protected int canAffectCode() {
        return CAN_MOBS;
    }

    protected int canTargetCode() {
        return CAN_MOBS;
    }

    public int abstractQuality() {
        return Ability.QUALITY_MALICIOUS;
    }

    public boolean putInCommandlist() {
        return false;
    }

    protected int DISEASE_TICKS() {
        return CMProps.getIntVar(CMProps.SYSTEMI_TICKSPERMUDDAY) * 6;
    }

    protected int DISEASE_DELAY() {
        return CMProps.getIntVar(CMProps.SYSTEMI_TICKSPERMUDDAY);
    }

    protected String DISEASE_DONE() {
        return "Your vampirism lifts.";
    }

    protected String DISEASE_START() {
        return "^G<S-NAME> seem(s) pale and cold.^?";
    }

    protected String DISEASE_AFFECT() {
        return "";
    }

    public int abilityCode() {
        return DiseaseAffect.SPREAD_CONSUMPTION;
    }

    public int difficultyLevel() {
        return 9;
    }

    public void affectEnvStats(Environmental affected, EnvStats affectableStats) {
        super.affectEnvStats(affected, affectableStats);
        if (!(affected instanceof MOB)) return;
        if (((MOB) affected).location() == null) return;
        if (CMLib.flags().isInDark(((MOB) affected).location())) affectableStats.setSensesMask(affectableStats.sensesMask() | EnvStats.CAN_SEE_DARK); else affectableStats.setSensesMask(affectableStats.sensesMask() | EnvStats.CAN_NOT_SEE);
    }

    public boolean okMessage(Environmental myHost, CMMsg msg) {
        if ((affected != null) && (affected instanceof MOB)) {
            MOB mob = (MOB) affected;
            if (msg.amISource(mob) && (msg.tool() != null) && (msg.tool().ID().equals("Skill_Swim"))) {
                mob.tell("You can't swim!");
                return false;
            }
        }
        return super.okMessage(myHost, msg);
    }

    public void affectCharStats(MOB affected, CharStats affectableStats) {
        super.affectCharStats(affected, affectableStats);
        if (affected == null) return;
        affectableStats.setStat(CharStats.STAT_CHARISMA, affectableStats.getStat(CharStats.STAT_CHARISMA) + 1);
    }
}
