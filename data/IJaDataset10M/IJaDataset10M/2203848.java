package ee.ioc.cs.vsle.editor;

import java.util.*;
import ee.ioc.cs.vsle.factoryStorage.*;

/**
 * @author pavelg
 *
 */
public final class SpecGenFactory extends FactoryStorage {

    public static final String s_prefix = "\\SPECGEN";

    private ISpecGenerator m_currentInstance = new SpecGenerator.Factory().getInstance();

    private static final SpecGenFactory s_instance = new SpecGenFactory();

    private SpecGenFactory() {
    }

    public static SpecGenFactory getInstance() {
        return s_instance;
    }

    public List<IFactory> getAllInstances() {
        return getAllInstances(s_prefix);
    }

    public void setCurrentSpecGen(ISpecGenerator curr) {
        m_currentInstance = curr;
    }

    public ISpecGenerator getCurrentSpecGen() {
        return m_currentInstance;
    }
}
