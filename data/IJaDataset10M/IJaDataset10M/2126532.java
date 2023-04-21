package com.planet_ink.coffee_mud.Abilities.Thief;

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

public class Thief_ImprovedSwipe extends ThiefSkill {

    public String ID() {
        return "Thief_ImprovedSwipe";
    }

    public String name() {
        return "Improved Swipe";
    }

    public String displayText() {
        return "";
    }

    protected int canAffectCode() {
        return CAN_MOBS;
    }

    protected int canTargetCode() {
        return 0;
    }

    public int classificationCode() {
        return Ability.ACODE_THIEF_SKILL | Ability.DOMAIN_STEALING;
    }

    public int abstractQuality() {
        return Ability.QUALITY_OK_SELF;
    }

    public boolean isAutoInvoked() {
        return true;
    }

    public boolean canBeUninvoked() {
        return false;
    }

    public boolean okMessage(Environmental myHost, CMMsg msg) {
        if ((affected == null) || (!(affected instanceof MOB))) return super.okMessage(myHost, msg);
        MOB mob = (MOB) affected;
        if ((msg.amISource(mob)) && (msg.tool() != null) && (msg.tool().ID().equals("Thief_Swipe"))) {
            helpProficiency(mob);
            Ability A = mob.fetchAbility("Thief_Swipe");
            float f = (float) getXLEVELLevel(mob);
            int ableDiv = (int) Math.round(5.0 - (f * 0.2));
            A.setAbilityCode(proficiency() / ableDiv);
        }
        return super.okMessage(myHost, msg);
    }
}
