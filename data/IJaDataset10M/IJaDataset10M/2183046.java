package com.planet_ink.coffee_mud.Libraries.interfaces;

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
public interface AutoTitlesLibrary extends CMLibrary {

    /**
     * 
     * @return
     */
    public Enumeration autoTitles();

    /**
     * 
     * @param title
     * @return
     */
    public String getAutoTitleMask(String title);

    /**
     * 
     * @param title
     * @return
     */
    public boolean isExistingAutoTitle(String title);

    /**
     * 
     * @param row
     * @param addIfPossible
     * @return
     */
    public String evaluateAutoTitle(String row, boolean addIfPossible);

    /**
     * 
     * @param mob
     * @return
     */
    public boolean evaluateAutoTitles(MOB mob);

    /**
     * 
     */
    public void reloadAutoTitles();

    /**
     * 
     * @param title
     */
    public void dispossesTitle(String title);
}
