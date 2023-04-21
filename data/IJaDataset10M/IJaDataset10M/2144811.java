package com.ail.coretest;

import java.util.HashMap;
import java.util.Map;
import com.ail.core.Type;
import com.ail.core.Version;

public class MapTestType extends Type {

    private Map<String, Version> myMap = new HashMap<String, Version>();

    public Map<String, Version> getMyMap() {
        return myMap;
    }

    public void setMyMap(Map<String, Version> myMap) {
        this.myMap = myMap;
    }
}
