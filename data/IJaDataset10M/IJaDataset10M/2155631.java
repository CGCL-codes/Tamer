package com.kenstevens.stratinit.world.predicate;

import com.kenstevens.stratinit.model.Sector;
import com.kenstevens.stratinit.util.Predicate;

public class CityPredicate implements Predicate<Sector> {

    @Override
    public boolean apply(Sector sector) {
        return sector.getType() == Sector.Type.CITY;
    }
}
