package info.absu.snow;

import info.absu.snow.mapper.SimpleMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Contains default aliases for class names.
 * 
 * @author Denys Rtveliashvili
 *
 */
@SuppressWarnings("unchecked")
final class DefaultClassAliases implements ClassAliases {

    private static final Map<String, String> DEFAULT_ALIASES = createDefaultAliases();

    private static Map<String, String> createDefaultAliases() {
        Map<String, String> aliases = new HashMap<String, String>();
        aliases.put("Map", HashMap.class.getName());
        aliases.put("List", ArrayList.class.getName());
        aliases.put("Set", HashSet.class.getName());
        aliases.put("OrderedMap", LinkedHashMap.class.getName());
        aliases.put("OrderedSet", LinkedHashSet.class.getName());
        for (Class<? extends SimpleMapper> simpleMapper : SimpleMapper.BASIC_MAPPERS_FOR_SIMPLE_OBJECTS) {
            final Class klass = SimpleMapper.getTargetClassFor(simpleMapper);
            final String fullName = klass.getName();
            final String[] shortNames = SimpleMapper.getShortNames(simpleMapper);
            for (String shortName : shortNames) {
                aliases.put(shortName, fullName);
            }
        }
        return aliases;
    }

    public String getClassByAlias(String alias) {
        return DEFAULT_ALIASES.get(alias);
    }
}
