package org.slf4j.profiler;

import java.util.HashMap;
import java.util.Map;

/**
 * A minimalist registry of profilers.
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public class ProfilerRegistry {

    private static final InheritableThreadLocal<ProfilerRegistry> inheritableThreadLocal = new InheritableThreadLocal<ProfilerRegistry>();

    Map<String, Profiler> profilerMap = new HashMap<String, Profiler>();

    public void put(Profiler profiler) {
        put(profiler.getName(), profiler);
    }

    public void put(String name, Profiler profiler) {
        profilerMap.put(name, profiler);
    }

    public static ProfilerRegistry getThreadContextInstance() {
        ProfilerRegistry pr = inheritableThreadLocal.get();
        if (pr == null) {
            pr = new ProfilerRegistry();
            inheritableThreadLocal.set(pr);
        }
        return pr;
    }

    public Profiler get(String name) {
        return profilerMap.get(name);
    }

    public void clear() {
        profilerMap.clear();
    }
}
