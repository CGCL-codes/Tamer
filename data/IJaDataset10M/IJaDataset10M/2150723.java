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

public class Trap_OpenBlade extends Trap_Open {

    public String ID() {
        return "Trap_OpenBlade";
    }

    public String name() {
        return "Open Blade Trap";
    }

    protected int canAffectCode() {
        return Ability.CAN_EXITS | Ability.CAN_ITEMS;
    }

    protected int canTargetCode() {
        return 0;
    }

    protected int trapType() {
        return TRAP_PIT_BLADE;
    }
}
