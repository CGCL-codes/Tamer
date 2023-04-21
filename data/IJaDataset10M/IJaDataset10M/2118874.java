package uk.ac.shef.wit.runes.runestone;

import uk.ac.shef.wit.runes.Runes;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionCannotHandle;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionNoSuchContent;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionNoSuchStructure;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

public class RunestoneImplDefault implements Runestone {

    private static final Logger log = Logger.getLogger(RunestoneImplDefault.class.getName());

    protected final Map<String, StoneProxy<? extends Serializable>> _stones;

    protected Set<String> _model = null;

    public RunestoneImplDefault() {
        _stones = new HashMap<String, StoneProxy<? extends Serializable>>(1 << 4);
    }

    public int size() {
        int size = 0;
        for (final Stone<? extends Serializable> stone : new HashSet<Stone<? extends Serializable>>(_stones.values())) size += stone.cardinality();
        return size;
    }

    public Set<String> getModel() {
        return Collections.unmodifiableSet(_model);
    }

    public void setModel(final Set<String> model) {
        _model = new HashSet<String>(model);
        for (Iterator<Map.Entry<String, StoneProxy<? extends Serializable>>> it = _stones.entrySet().iterator(); it.hasNext(); ) {
            final Map.Entry<String, StoneProxy<? extends Serializable>> e = it.next();
            final String type = e.getKey();
            if (canBeDropped(type)) {
                if (0 < e.getValue().dimensions().length) log.fine("dropping stone: " + type);
                it.remove();
            }
        }
    }

    public Set<String> getActualModel() {
        final Set<String> types = new HashSet<String>();
        for (final String type : _stones.keySet()) {
            types.add(type);
            for (final String alias : Runes.getAliases(type)) {
                final String[] components = Runes.componentsOf(alias);
                if (1 < components.length) for (int i = 0, size = components.length; i < size; ++i) types.add(components[i]);
            }
        }
        for (final Map.Entry<String, Set<String>> e : Runes.getAliases().entrySet()) {
            final String[] keyComponents = Runes.componentsOf(e.getKey());
            if (1 < keyComponents.length) {
                boolean allComponentsPresent = true;
                for (int i = 0, size = keyComponents.length; allComponentsPresent && i < size; ++i) if (!Runes.containsAlias(types, keyComponents[i])) allComponentsPresent = false;
                if (allComponentsPresent) for (final String base : e.getValue()) if (1 == Runes.componentsOf(base).length) types.add(base);
            }
        }
        return types;
    }

    public Structure getStructure(final String type, final int... mask) throws RunesExceptionCannotHandle, RunesExceptionNoSuchStructure {
        StoneProxy<? extends Serializable> stone = getStone(_stones, type);
        if (stone == null) {
            for (final Map.Entry<String, Set<String>> e : Runes.getAliases().entrySet()) {
                final String[] components = Runes.componentsOf(e.getKey());
                if (1 < components.length) for (int i = 0, size = components.length; i < size; ++i) if (type.equals(components[i])) {
                    stone = getStone(_stones, e.getValue().iterator().next());
                    if (stone != null) return new StructureView(stone, i, mask);
                }
            }
            _stones.put(type, stone = new StoneProxyImpl<Serializable>(type));
        }
        return new StructureImpl(stone, isModeled(type), mask);
    }

    public Structure getStructure(final String type, final String first, final String second, final String... rest) throws RunesExceptionCannotHandle, RunesExceptionNoSuchStructure {
        final String[] components = new String[2 + rest.length];
        components[0] = first;
        components[1] = second;
        for (int i = 0, size = rest.length; i < size; ++i) components[2 + i] = rest[i];
        final Structure base = getStructure(type);
        if (isModeled(type)) {
            Runes.registerAlias(type, Runes.typeFrom(components));
            return base;
        } else {
            final Structure[] derived = new Structure[components.length];
            for (int i = 0, size = components.length; i < size; ++i) if (isModeled(components[i])) derived[i] = getStructure(components[i]);
            return new CompositeImpl(base, derived);
        }
    }

    public <T extends Serializable> Content<T> getContent(final String type, final int... mask) throws RunesExceptionNoSuchContent {
        final StoneProxy<T> stone = getStone(_stones, type);
        if (stone == null) throw new RunesExceptionNoSuchContent(type); else return new ContentImpl<T>(stone, mask);
    }

    public <T extends Serializable> StructureAndContent<T> getStructureAndContent(final String type, final int... mask) throws RunesExceptionNoSuchStructure, RunesExceptionNoSuchContent, RunesExceptionCannotHandle {
        StoneProxy<T> stone = getStone(_stones, type);
        if (stone == null) _stones.put(type, stone = new StoneProxyImpl<T>(type));
        return new StructureAndContentImpl<T>(stone, isModeled(type), mask);
    }

    @SuppressWarnings("unchecked")
    private <T extends Serializable> StoneProxy<T> getStone(final Map<String, StoneProxy<? extends Serializable>> map, final String type) {
        StoneProxy<? extends Serializable> stone = map.get(type);
        if (stone == null) for (final String t : Runes.getAliases(type)) if ((stone = map.get(t)) != null) break;
        return (StoneProxy<T>) stone;
    }

    protected Object clone() throws CloneNotSupportedException {
        assert false : "to implement";
        throw new CloneNotSupportedException();
    }

    @Override
    public String toString() {
        return "default runestone implementation";
    }

    private boolean canBeDropped(final String type) {
        if (_model.contains(type)) return false;
        for (final String alias : Runes.getAliases(type)) for (final String component : Runes.componentsOf(alias)) if (Runes.containsAlias(_model, component)) return false;
        return true;
    }

    private boolean isModeled(final String type) {
        return _model != null && Runes.containsAlias(_model, type);
    }
}
