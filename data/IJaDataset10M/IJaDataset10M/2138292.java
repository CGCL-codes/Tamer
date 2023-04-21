package com.planet_ink.coffee_mud.Items.MiscMagic;

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

public class Wand_Nourishment extends StdWand {

    public String ID() {
        return "Wand_Nourishment";
    }

    public Wand_Nourishment() {
        super();
        setName("a wooden wand");
        setDisplayText("a small wooden wand is here.");
        setDescription("A wand made out of wood");
        secretIdentity = "The wand of nourishment.  Hold the wand say \\`shazam\\` to it.";
        baseGoldValue = 200;
        material = RawMaterial.RESOURCE_OAK;
        recoverEnvStats();
        secretWord = "SHAZAM";
    }

    public void setSpell(Ability theSpell) {
        super.setSpell(theSpell);
        secretWord = "SHAZAM";
    }

    public void setMiscText(String newText) {
        super.setMiscText(newText);
        secretWord = "SHAZAM";
    }

    public void executeMsg(Environmental myHost, CMMsg msg) {
        if (msg.amITarget(this)) {
            MOB mob = msg.source();
            switch(msg.targetMinor()) {
                case CMMsg.TYP_WAND_USE:
                    if ((mob.isMine(this)) && (!amWearingAt(Wearable.IN_INVENTORY))) if (msg.targetMessage().toUpperCase().indexOf("SHAZAM") >= 0) if (mob.curState().adjHunger(50, mob.maxState().maxHunger(mob.baseWeight()))) mob.tell("You are full."); else mob.tell("You feel nourished.");
                    return;
                default:
                    break;
            }
        }
        super.executeMsg(myHost, msg);
    }
}
