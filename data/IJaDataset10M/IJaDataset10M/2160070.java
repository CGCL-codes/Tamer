package net.wgen.op.system;

import net.wgen.op.util.AssertionUtils;
import java.util.*;

/**
 * @author Paul Feuer, Wireless Generation, Inc.
 * @version $Id: OpModuleRegistry.java 22 2007-06-26 01:11:24Z paulfeuer $
 */
public class OpModuleRegistry {

    /** The singleton instance of the class. */
    private static OpModuleRegistry _self = null;

    /** Contains a mapping of name to OpModule object. */
    private Map<String, OpModule> _moduleRegistry = new HashMap<String, OpModule>();

    /**
     * The singleton accessor.
     * @return the singleton instance of this class
     */
    public static synchronized OpModuleRegistry getInstance() {
        if (_self == null) _self = new OpModuleRegistry();
        return _self;
    }

    /**
     * Private, no-arg constructor to limit creation to singleton accessor.
     */
    private OpModuleRegistry() {
    }

    /**
     * The application registered at the given name.
     *
     * @param name the name of the application
     *
     * @return the application registered at the given name
     */
    public final OpModule getModuleByName(String name) {
        return _moduleRegistry.get(name);
    }

    /**
     *
     * @param url
     * @return the application that claimed to the url
     */
    public final OpModule lookUpModule(String url) {
        Iterator<OpModule> it = _moduleRegistry.values().iterator();
        SortedMap<Integer, OpModule> claimingModules = new TreeMap<Integer, OpModule>();
        while (it.hasNext()) {
            OpModule module = it.next();
            int matchScore = module.getUrlMatchLength(url);
            if (matchScore >= 0) {
                claimingModules.put(matchScore, module);
            }
        }
        if (claimingModules.size() > 0) {
            OpModule winner = claimingModules.get(claimingModules.lastKey());
            return winner;
        } else {
            return UnknownModule.getInstance();
        }
    }

    /**
     *
     * @param clazz
     * @return the application that claimed to the class
     */
    public final OpModule lookUpModule(Class clazz) {
        Iterator<OpModule> it = _moduleRegistry.values().iterator();
        SortedMap<Integer, OpModule> claimingModules = new TreeMap<Integer, OpModule>();
        while (it.hasNext()) {
            OpModule module = it.next();
            int matchScore = module.getClassMatchLength(clazz);
            if (matchScore > 0) {
                claimingModules.put(matchScore, module);
            }
        }
        if (claimingModules.size() > 0) {
            return claimingModules.get(claimingModules.lastKey());
        } else {
            return UnknownModule.getInstance();
        }
    }

    /**
     *
     * @param module
     */
    protected final synchronized void registerModule(OpModule module) {
        AssertionUtils.assertNotNull(module, "module");
        if (_moduleRegistry.containsKey(module.getName())) {
            module.LOG.warn("Module registry already contains an module registered " + "at " + module.getName() + ": " + module.getClass().getName());
        } else {
            _moduleRegistry.put(module.getName(), module);
        }
    }

    /**
     *
     * @return a map of registered modules
     */
    public Map<String, OpModule> getModuleRegistry() {
        return Collections.unmodifiableMap(_moduleRegistry);
    }
}
