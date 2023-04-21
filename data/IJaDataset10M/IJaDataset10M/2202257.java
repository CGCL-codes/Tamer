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
public class Time extends StdCommand {

    public Time() {
    }

    private String[] access = { "TIME", "DATE" };

    public String[] getAccessWords() {
        return access;
    }

    public boolean execute(MOB mob, Vector commands, int metaFlags) throws java.io.IOException {
        Room room = mob.location();
        if (room == null) return false;
        mob.tell(room.getArea().getTimeObj().timeDescription(mob, room));
        if ((mob.playerStats() != null) && (mob.playerStats().getBirthday() != null)) {
            TimeClock C = CMLib.time().globalClock();
            int day = C.getDayOfMonth();
            int month = C.getMonth();
            int year = C.getYear();
            int bday = mob.playerStats().getBirthday()[0];
            int bmonth = mob.playerStats().getBirthday()[1];
            if ((month > bmonth) || ((month == bmonth) && (day > bday))) year++;
            StringBuffer timeDesc = new StringBuffer("");
            if (C.getDaysInWeek() > 0) {
                long x = ((long) year) * ((long) C.getMonthsInYear()) * C.getDaysInMonth();
                x = x + ((long) (bmonth - 1)) * ((long) C.getDaysInMonth());
                x = x + bmonth;
                timeDesc.append(C.getWeekNames()[(int) (x % C.getDaysInWeek())] + ", ");
            }
            timeDesc.append("the " + bday + CMath.numAppendage(bday));
            timeDesc.append(" day of " + C.getMonthNames()[bmonth - 1]);
            if (C.getYearNames().length > 0) timeDesc.append(", " + CMStrings.replaceAll(C.getYearNames()[year % C.getYearNames().length], "#", "" + year));
            mob.tell("Your next birthday is " + timeDesc.toString() + ".");
        }
        return false;
    }

    public boolean canBeOrdered() {
        return true;
    }
}
