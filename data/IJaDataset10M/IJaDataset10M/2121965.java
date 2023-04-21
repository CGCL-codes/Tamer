package com.edbaskerville.gridsweeper;

import java.util.*;
import com.edbaskerville.gridsweeper.parameters.*;

public class ExperimentCase {

    ParameterMap map;

    List<Long> rngSeeds;

    public ExperimentCase(ParameterMap map, List<Long> rngSeeds) {
        this.map = map;
        this.rngSeeds = rngSeeds;
    }

    public ParameterMap getMap() {
        return map;
    }

    public List<Long> getRngSeeds() {
        return rngSeeds;
    }
}
