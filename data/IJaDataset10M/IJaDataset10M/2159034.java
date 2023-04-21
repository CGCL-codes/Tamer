package com.kescom.matrix.core.olap;

import java.util.Map;

public abstract class DynamicDimensionsCube extends CubeBase {

    public Map<String, IDimension> getDimensions(ICubeRuntime cubeRuntime) {
        Map<String, IDimension> dims = cubeRuntime.getCachedDimensions();
        if (dims == null) cubeRuntime.setCachedDimensions(dims = calcDimensions(cubeRuntime));
        return dims;
    }

    protected abstract Map<String, IDimension> calcDimensions(ICubeRuntime cubeRuntime);
}
