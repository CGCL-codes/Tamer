package com.kenstevens.jei.move;

import com.kenstevens.jei.model.Item;
import com.kenstevens.jei.model.Sector;
import com.kenstevens.jei.move.MobilityCalculatorImpl.MobilityType;

public interface MobilityCalculator {

    public void init();

    public double mobilityCost(Sector sector, MobilityType mobilityType);

    public MoveResult moveItem(Sector start, Sector end, Item item, int amount);

    public double distance(Sector sector, Sector consumerSector);
}
