package com.ham.mud.races;

import com.ham.mud.characters.Stats;

/**
 * Created by hlucas on Jul 7, 2011 at 11:52:25 AM
 */
public class Ogre extends Race {

    @Override
    public Stats getStats() {
        return new Stats(16, 19, 15, 12, 14, 14);
    }

    @Override
    public String getName() {
        return "Ogre";
    }
}
