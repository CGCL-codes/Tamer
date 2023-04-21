package com.turnengine.client.local.location.plugin.grid;

import java.util.List;
import com.turnengine.client.local.location.bean.ICoordinateGrid;

public class CoordinateGridPlugin implements ICoordinateGridPlugin {

    private final ICoordinateGridSet gridSet;

    public CoordinateGridPlugin(ICoordinateGridSet gridSet) {
        this.gridSet = gridSet;
    }

    @Override
    public List<ICoordinateGrid> getGridList() {
        return gridSet.toList();
    }
}
