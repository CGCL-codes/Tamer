package com.planet_ink.coffee_mud.Abilities.Songs;

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

public class Play_Horns extends Play_Instrument {

    public String ID() {
        return "Play_Horns";
    }

    public String name() {
        return "Horns";
    }

    protected int requiredInstrumentType() {
        return MusicalInstrument.TYPE_HORNS;
    }

    public String mimicSpell() {
        return "Spell_FaerieFire";
    }

    private static Ability theSpell = null;

    protected Ability getSpell() {
        if (theSpell != null) return theSpell;
        if (mimicSpell().length() == 0) return null;
        theSpell = CMClass.getAbility(mimicSpell());
        return theSpell;
    }
}
