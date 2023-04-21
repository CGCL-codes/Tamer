package com.planet_ink.coffee_mud.Abilities.Traps;

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

public class Trap_Get extends Trap_Trap {

    public String ID() {
        return "Trap_Get";
    }

    public String name() {
        return "Get Trap";
    }

    protected int canAffectCode() {
        return Ability.CAN_ITEMS;
    }

    protected int canTargetCode() {
        return 0;
    }

    public void executeMsg(Environmental myHost, CMMsg msg) {
        if (sprung) {
            if (msg.source().isMine(affected)) unInvoke(); else super.executeMsg(myHost, msg);
            return;
        }
        super.executeMsg(myHost, msg);
        if (msg.amITarget(affected)) {
            if (msg.targetMinor() == CMMsg.TYP_GET) {
                spring(msg.source());
            }
        }
    }
}
