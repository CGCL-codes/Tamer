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

public class Song_Flying extends Song {

    public String ID() {
        return "Song_Flying";
    }

    public String name() {
        return "Flying";
    }

    public int abstractQuality() {
        return Ability.QUALITY_BENEFICIAL_OTHERS;
    }

    public long flags() {
        return Ability.FLAG_MOVING;
    }

    protected boolean HAS_QUANTITATIVE_ASPECT() {
        return false;
    }

    public void affectEnvStats(Environmental affected, EnvStats affectableStats) {
        super.affectEnvStats(affected, affectableStats);
        if (invoker == null) return;
        affectableStats.setDisposition(affectableStats.disposition() | EnvStats.IS_FLYING);
    }
}
