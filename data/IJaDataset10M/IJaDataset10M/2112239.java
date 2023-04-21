package com.planet_ink.coffee_mud.Abilities.Fighter;

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

public class Fighter_RapidShot extends FighterSkill {

    public String ID() {
        return "Fighter_RapidShot";
    }

    public String name() {
        return "Rapid Shot";
    }

    public String displayText() {
        return "";
    }

    public int abstractQuality() {
        return Ability.QUALITY_BENEFICIAL_SELF;
    }

    protected int canAffectCode() {
        return Ability.CAN_MOBS;
    }

    protected int canTargetCode() {
        return 0;
    }

    public boolean isAutoInvoked() {
        return true;
    }

    public boolean canBeUninvoked() {
        return false;
    }

    public int classificationCode() {
        return Ability.ACODE_SKILL | Ability.DOMAIN_MARTIALLORE;
    }

    public boolean tick(Tickable ticking, int tickID) {
        if (!super.tick(ticking, tickID)) return false;
        if ((affected == null) || (!(affected instanceof MOB))) return true;
        MOB mob = (MOB) affected;
        if (mob.isInCombat()) {
            Item w = mob.fetchWieldedItem();
            if ((w != null) && (w instanceof Weapon) && (((Weapon) w).weaponClassification() == Weapon.CLASS_RANGED) && (((Weapon) w).ammunitionType().length() > 0) && ((mob.rangeToTarget() > 0) || ((w.envStats().sensesMask() & EnvStats.SENSE_ITEMNOMINRANGE) == EnvStats.SENSE_ITEMNOMINRANGE)) && ((mob.fetchAbility(ID()) == null) || proficiencyCheck(null, 0, false))) {
                helpProficiency(mob);
                for (int i = 0; i < (adjustedLevel(mob, 0) / 7); i++) CMLib.combat().postAttack(mob, mob.getVictim(), w);
            }
        }
        return true;
    }
}
