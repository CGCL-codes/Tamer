package com.ham.mud.races;

import com.ham.mud.characters.Stats;

/**
 * Created by hlucas on Jul 7, 2011 at 11:52:25 AM
 */
public class Fairy extends Race {

    @Override
    public Stats getStats() {
        return new Stats(13, 12, 16, 16, 16, 17);
    }

    @Override
    public String getName() {
        return "Fairy";
    }
}
