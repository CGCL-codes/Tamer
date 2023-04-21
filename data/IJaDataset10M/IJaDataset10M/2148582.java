package net.sourceforge.turtleweed.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.sourceforge.turtleweed.model.Environment;

public class MapManager implements IMapManager {

    private static IMapManager mm = new MapManager();

    private Map<String, Environment> environs = new HashMap<String, Environment>();

    public static IMapManager getManager() {
        return mm;
    }

    private MapManager() {
    }

    @Override
    public void addEnvironment(String string, boolean defaultEnv) throws ExistsException {
        if (environs.containsKey(string)) {
            throw new ExistsException();
        }
        Environment newEnv = new Environment(string);
        if (defaultEnv) {
            Set<String> envKeys = environs.keySet();
            for (String envKey : envKeys) {
                Environment currEnv = environs.get(envKey);
                if (currEnv.isDefaultEnv()) {
                    currEnv.setDefaultEnv(false);
                    break;
                }
            }
            newEnv.setDefaultEnv(true);
        }
        environs.put(string, newEnv);
    }

    @Override
    public Environment getEnvironment(String string) throws DoesNotExistException {
        if (!environs.containsKey(string)) {
            throw new DoesNotExistException();
        }
        return environs.get(string);
    }

    @Override
    public boolean environmentExists(String envName) {
        return environs.containsKey(envName);
    }

    @Override
    public void clear() {
        environs.clear();
    }
}
