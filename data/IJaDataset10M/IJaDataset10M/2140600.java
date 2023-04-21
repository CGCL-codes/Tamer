package com.kenstevens.stratdom.ui;

import java.util.ArrayList;
import java.util.List;
import com.kenstevens.stratdom.model.Unit;

public class UnitSelection extends Selection {

    private final List<Unit> units = new ArrayList<Unit>();

    public UnitSelection(List<Unit> units, Selection.Source source) {
        super(source);
        this.getUnits().addAll(units);
    }

    public List<Unit> getUnits() {
        return units;
    }

    public SectorCoords getCoords() {
        if (units.isEmpty()) {
            throw new IllegalStateException("Selected empty list of units.");
        }
        return units.get(0).getCoords();
    }
}
