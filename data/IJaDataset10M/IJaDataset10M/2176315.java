package com.dfn.model;

import java.util.ArrayList;
import java.util.List;

public class Memory {

    private List<String> impressions;

    public List<String> getImpressions() {
        return this.impressions;
    }

    public void addImpression(String iid) {
        if (impressions == null) impressions = new ArrayList<String>();
        impressions.add(iid);
    }
}
