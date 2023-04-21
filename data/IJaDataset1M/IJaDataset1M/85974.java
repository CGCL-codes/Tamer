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

public class RingOfMagicResistance extends Ring_Protection implements MiscMagic {

    public String ID() {
        return "RingOfMagicResistance";
    }

    public RingOfMagicResistance() {
        super();
        this.baseEnvStats().setLevel(GOLD_RING_OPAL);
        this.recoverEnvStats();
        material = RawMaterial.RESOURCE_GOLD;
    }
}
