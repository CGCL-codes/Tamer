package com.planet_ink.coffee_mud.Commands;

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

@SuppressWarnings("unchecked")
public class ClanMorgueSet extends StdCommand {

    public ClanMorgueSet() {
    }

    private String[] access = { "CLANMORGUESET" };

    public String[] getAccessWords() {
        return access;
    }

    public boolean execute(MOB mob, Vector commands, int metaFlags) throws java.io.IOException {
        boolean skipChecks = mob.Name().equals(mob.getClanID());
        Room R = mob.location();
        if (skipChecks) {
            commands.setElementAt(getAccessWords()[0], 0);
            R = CMLib.map().getRoom(CMParms.combine(commands, 1));
        } else {
            commands.clear();
            commands.addElement(getAccessWords()[0]);
            commands.addElement(CMLib.map().getExtendedRoomID(R));
        }
        if ((mob.getClanID() == null) || (R == null) || (mob.getClanID().equalsIgnoreCase(""))) {
            mob.tell("You aren't even a member of a clan.");
            return false;
        }
        Clan C = CMLib.clans().getClan(mob.getClanID());
        if (C == null) {
            mob.tell("There is no longer a clan called " + mob.getClanID() + ".");
            return false;
        }
        if (C.getStatus() > Clan.CLANSTATUS_ACTIVE) {
            mob.tell("You cannot set a morgue.  Your " + C.typeName() + " does not have enough members to be considered active.");
            return false;
        }
        if (skipChecks || CMLib.clans().goForward(mob, C, commands, Clan.FUNC_CLANHOMESET, false)) {
            if (!CMLib.law().doesOwnThisProperty(C.clanID(), R)) {
                mob.tell("Your " + C.typeName() + " does not own this room.");
                return false;
            }
            if (skipChecks || CMLib.clans().goForward(mob, C, commands, Clan.FUNC_CLANHOMESET, true)) {
                C.setMorgue(CMLib.map().getExtendedRoomID(R));
                C.update();
                mob.tell("Your " + C.typeName() + " morgue is now set to " + R.roomTitle(mob) + ".");
                CMLib.clans().clanAnnounce(mob, "The morgue of " + C.typeName() + " " + C.clanID() + " is now set to " + R.roomTitle(mob) + ".");
                return true;
            }
        } else {
            mob.tell("You aren't in the right position to set your " + C.typeName() + "'s morgue.");
            return false;
        }
        return false;
    }

    public boolean canBeOrdered() {
        return false;
    }
}
