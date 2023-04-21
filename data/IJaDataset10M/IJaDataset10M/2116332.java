package com.capgemini.tridion.cde.view.model;

import java.util.HashMap;
import java.util.Map;

public class ComponentViews {

    private Map<String, ViewRegion> regions;

    public ComponentViews() {
        this.regions = new HashMap<String, ViewRegion>();
    }

    public Map<String, ViewRegion> getRegions() {
        return regions;
    }

    public void setRegions(Map<String, ViewRegion> regions) {
        this.regions = regions;
    }
}
