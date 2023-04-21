package net.sf.cyberrails.Engine;

import java.util.*;

public class MilepostContainer extends GameContainer<Milepost> {

    private final Map<String, Integer> _commentBuildCost, _nameBuildCost;

    private final Map<String, Integer> _commentMoveCost, _nameMoveCost;

    private final Collection<String> _types, _typeView;

    private final Map<String, Integer> _maxBuildsFromPerTurn, _maxBuildsToPerTurn;

    private final Map<String, Integer> _maxBuilders, _minBuilders;

    private final Set<String> _reversable, _starter;

    private static final String DEFAULT_STRING = "default";

    public MilepostContainer() {
        _commentMoveCost = new LinkedHashMap<String, Integer>();
        _commentMoveCost.put(DEFAULT_STRING, 0);
        _commentBuildCost = new LinkedHashMap<String, Integer>();
        _commentBuildCost.put(DEFAULT_STRING, 0);
        _nameMoveCost = new LinkedHashMap<String, Integer>();
        _nameMoveCost.put(DEFAULT_STRING, 1);
        _nameBuildCost = new LinkedHashMap<String, Integer>();
        _nameBuildCost.put(DEFAULT_STRING, 1);
        _types = new LinkedHashSet<String>();
        _typeView = Collections.unmodifiableCollection(_types);
        _maxBuildsFromPerTurn = new LinkedHashMap<String, Integer>();
        _maxBuildsFromPerTurn.put(DEFAULT_STRING, -1);
        _maxBuildsToPerTurn = new LinkedHashMap<String, Integer>();
        _maxBuildsToPerTurn.put(DEFAULT_STRING, -1);
        _maxBuilders = new LinkedHashMap<String, Integer>();
        _maxBuilders.put(DEFAULT_STRING, -1);
        _minBuilders = new LinkedHashMap<String, Integer>();
        _minBuilders.put(DEFAULT_STRING, -1);
        _reversable = new HashSet<String>();
        _starter = new HashSet<String>();
    }

    public MilepostContainer(MilepostContainer mc) {
        this();
        if (mc == null) return;
        _commentMoveCost.putAll(mc._commentMoveCost);
        _commentBuildCost.putAll(mc._commentBuildCost);
        _nameMoveCost.putAll(mc._nameMoveCost);
        _nameBuildCost.putAll(mc._nameBuildCost);
        _types.addAll(mc._types);
        _maxBuildsFromPerTurn.putAll(mc._maxBuildsFromPerTurn);
        _maxBuildsToPerTurn.putAll(mc._maxBuildsToPerTurn);
        _maxBuilders.putAll(mc._maxBuilders);
        _minBuilders.putAll(mc._minBuilders);
        _reversable.addAll(mc._reversable);
        _starter.addAll(mc._starter);
    }

    private void addType(String name) {
        name = fixString(name);
        if (!_types.contains(name) && !name.equals(DEFAULT_STRING)) {
            _types.add(name);
        }
    }

    public void add(Milepost val) {
        put(val.getName(), val);
        addType(val.getName());
    }

    public int getBuildCost(Milepost mp) {
        int result = 0;
        result += getCommentBuildCost(mp.getComment());
        result += getNameBuildCost(mp.getName());
        return result;
    }

    public int getMoveCost(Milepost mp) {
        int result = 0;
        result += getCommentMoveCost(mp.getComment());
        result += getNameMoveCost(mp.getName());
        return result;
    }

    private static final int getInt(String name, Map<String, Integer> map) {
        String fixed = fixString(name);
        if (map.containsKey(fixed)) {
            return map.get(fixed);
        } else {
            return map.get(DEFAULT_STRING);
        }
    }

    public int getCommentBuildCost(String comment) {
        return getInt(comment, _commentBuildCost);
    }

    public void setCommentBuildCost(String comment, int cost) {
        _commentBuildCost.put(fixString(comment), cost);
    }

    public int getNameBuildCost(String name) {
        return getInt(name, _nameBuildCost);
    }

    public void setNameBuildCost(String name, int cost) {
        name = fixString(name);
        addType(name);
        _nameBuildCost.put(name, cost);
    }

    public int getCommentMoveCost(String comment) {
        return getInt(comment, _commentMoveCost);
    }

    public void setCommentMoveCost(String comment, int cost) {
        _commentMoveCost.put(fixString(comment), cost);
    }

    public int getNameMoveCost(String name) {
        return getInt(name, _nameMoveCost);
    }

    public void setNameMoveCost(String name, int cost) {
        name = fixString(name);
        addType(name);
        _nameMoveCost.put(name, cost);
    }

    public int getMaximumBuildsFromPerTurn(String name) {
        return getInt(name, _maxBuildsFromPerTurn);
    }

    public void setMaximumBuildsFromPerTurn(String name, int max) {
        name = fixString(name);
        addType(name);
        _maxBuildsFromPerTurn.put(name, max);
    }

    public int getMaximumBuildsToPerTurn(String name) {
        return getInt(name, _maxBuildsToPerTurn);
    }

    public void setMaximumBuildsToPerTurn(String name, int max) {
        name = fixString(name);
        addType(name);
        _maxBuildsToPerTurn.put(name, max);
    }

    public int getMaximumBuilders(String name) {
        return getInt(name, _maxBuilders);
    }

    public void setMaximumBuilders(String name, int max) {
        name = fixString(name);
        addType(name);
        _maxBuilders.put(name, max);
    }

    public int getMinimumBuilders(String name) {
        return getInt(name, _minBuilders);
    }

    public void setMinimumBuilders(String name, int min) {
        name = fixString(name);
        addType(name);
        _minBuilders.put(name, min);
    }

    public boolean isTrackStarter(String name) {
        return _starter.contains(fixString(name));
    }

    public void setStarter(String name, boolean enable) {
        name = fixString(name);
        addType(name);
        if (enable) {
            _starter.add(name);
        } else {
            _starter.remove(name);
        }
    }

    public boolean canReverseAt(String name) {
        return _reversable.contains(fixString(name));
    }

    public void setReversible(String name, boolean enable) {
        name = fixString(name);
        addType(name);
        if (enable) {
            _reversable.add(name);
        } else {
            _reversable.remove(name);
        }
    }

    public Collection<String> types() {
        return _typeView;
    }

    public void removeType(String key) {
        _types.remove(key);
    }

    private static final String fixString(String string) {
        if (string == null) string = DEFAULT_STRING;
        return String.valueOf(string).toLowerCase();
    }
}
