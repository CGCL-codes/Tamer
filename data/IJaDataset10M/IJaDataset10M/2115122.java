package com.planet_ink.coffee_mud.MOBS.interfaces;

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

public interface Deity extends MOB {

    public String getClericRequirements();

    public void setClericRequirements(String reqs);

    public String getClericRequirementsDesc();

    public String getWorshipRequirements();

    public void setWorshipRequirements(String reqs);

    public String getWorshipRequirementsDesc();

    /** Manipulation of blessing objects, which includes spells, traits, skills, etc.*/
    public void addBlessing(Ability to, boolean clericOnly);

    public void delBlessing(Ability to);

    public int numBlessings();

    public Ability fetchBlessing(int index);

    public Ability fetchBlessing(String ID);

    public boolean fetchBlessingCleric(int index);

    public boolean fetchBlessingCleric(String ID);

    public String getClericRitual();

    public void setClericRitual(String ritual);

    public String getClericTriggerDesc();

    public String getServiceRitual();

    public void setServiceRitual(String ritual);

    public String getServiceTriggerDesc();

    public String getWorshipRitual();

    public void setWorshipRitual(String ritual);

    public String getWorshipTriggerDesc();

    /** Manipulation of curse objects, which includes spells, traits, skills, etc.*/
    public void addCurse(Ability to, boolean clericOnly);

    public void delCurse(Ability to);

    public int numCurses();

    public Ability fetchCurse(int index);

    public Ability fetchCurse(String ID);

    public boolean fetchCurseCleric(int index);

    public boolean fetchCurseCleric(String ID);

    public String getClericSin();

    public void setClericSin(String ritual);

    public String getClericSinDesc();

    public String getWorshipSin();

    public void setWorshipSin(String ritual);

    public String getWorshipSinDesc();

    /** Make sure that none of these can really be qualified for by the cleric!*/
    public void addPower(Ability to);

    public void delPower(Ability to);

    public int numPowers();

    public Ability fetchPower(int index);

    public Ability fetchPower(String ID);

    public String getClericPowerup();

    public void setClericPowerup(String ritual);

    public String getClericPowerupDesc();
}
