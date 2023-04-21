package net.sf.cyberrails.Engine;

import java.util.*;

public class RegionContainer extends GameContainer<Region> {

    private Map<String, Integer> _typeBuildCost, _nameBuildCost;

    private Map<String, Integer> _typeMoveCost, _nameMoveCost;

    public void add(Region val) {
        put(val.getName(), val);
    }

    public int getBuildCost(Region r) {
        int result = 0;
        String type = r.getType().toUpperCase(), name = r.getName().toUpperCase();
        if (_typeBuildCost != null && _typeBuildCost.containsKey(type)) {
            result += _typeBuildCost.get(type);
        }
        if (_nameBuildCost != null && _nameBuildCost.containsKey(name)) {
            result += _nameBuildCost.get(name);
        }
        return result;
    }

    public int getMoveCost(Region r) {
        int result = 0;
        String type = r.getType().toUpperCase(), name = r.getName().toUpperCase();
        if (_typeMoveCost != null && _typeMoveCost.containsKey(type)) {
            result += _typeMoveCost.get(type);
        }
        if (_nameMoveCost != null && _nameMoveCost.containsKey(name)) {
            result += _nameMoveCost.get(name);
        }
        return result;
    }

    protected void setTypeBuildCost(String type, int cost) {
        if (_typeBuildCost == null) {
            _typeBuildCost = new LinkedHashMap<String, Integer>();
        }
        _typeBuildCost.put(type.toUpperCase(), cost);
    }

    protected void setNameBuildCost(String name, int cost) {
        if (_nameBuildCost == null) {
            _nameBuildCost = new LinkedHashMap<String, Integer>();
        }
        _nameBuildCost.put(name.toUpperCase(), cost);
    }

    protected void setTypeMoveCost(String type, int cost) {
        if (_typeMoveCost == null) {
            _typeMoveCost = new LinkedHashMap<String, Integer>();
        }
        _typeMoveCost.put(type.toUpperCase(), cost);
    }

    protected void setNameMoveCost(String name, int cost) {
        if (_nameMoveCost == null) {
            _nameMoveCost = new LinkedHashMap<String, Integer>();
        }
        _nameMoveCost.put(name.toUpperCase(), cost);
    }
}
