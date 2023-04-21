package co.edu.unal.ungrid.registration.nonrigid.solver;

import java.util.HashMap;
import java.util.Map;

public abstract class SolverFactory {

    protected abstract Solver create();

    public static void register(String cls, SolverFactory factory) {
        m_factories.put(cls, factory);
    }

    public static final Solver getInstance(String cls) {
        assert cls != null;
        if (!m_factories.containsKey(cls)) {
            try {
                Class.forName(cls);
            } catch (ClassNotFoundException exc) {
                return null;
            }
            if (!m_factories.containsKey(cls)) {
                return null;
            }
        }
        SolverFactory factory = m_factories.get(cls);
        return factory.create();
    }

    private static Map<String, SolverFactory> m_factories = new HashMap<String, SolverFactory>();
}
