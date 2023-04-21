package com.planet_ink.coffee_mud.Items.MiscMagic;

import com.planet_ink.coffee_mud.Items.Basic.StdItem;
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

public class PracticePoint extends StdItem implements MiscMagic {

    public String ID() {
        return "PracticePoint";
    }

    public PracticePoint() {
        super();
        setName("a practice point");
        setDisplayText("A shiny green coin has been left here.");
        myContainer = null;
        setDescription("A shiny green coin with magical script around the edges.");
        myUses = Integer.MAX_VALUE;
        myWornCode = 0;
        material = 0;
        baseEnvStats.setWeight(0);
        recoverEnvStats();
    }

    public void executeMsg(Environmental myHost, CMMsg msg) {
        if (msg.amITarget(this)) {
            MOB mob = msg.source();
            switch(msg.targetMinor()) {
                case CMMsg.TYP_GET:
                case CMMsg.TYP_REMOVE:
                    {
                        setContainer(null);
                        destroy();
                        if (!mob.isMine(this)) mob.setPractices(mob.getPractices() + 1);
                        unWear();
                        if (!CMath.bset(msg.targetCode(), CMMsg.MASK_OPTIMIZE)) mob.location().recoverRoomStats();
                        return;
                    }
                default:
                    break;
            }
        }
        super.executeMsg(myHost, msg);
    }
}
