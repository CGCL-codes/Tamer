package org.unitils.hibernate;

import org.unitils.core.Unitils;
import org.unitils.core.UnitilsException;

/**
 * Utility facade for handling Hibernate things such as asserting whether the mappings correspond to the actual
 * structure of the database and for invalidating a cached configuration.
 *
 * @author Filip Neven
 * @author Tim Ducheyne
 */
public class HibernateUnitils {

    /**
     * Checks if the mapping of the Hibernate managed objects with the database is still correct for the configurations
     * that are loaded for the current test. This method assumes that the {@link HibernateModule} is enabled and
     * correctly configured.
     */
    public static void assertMappingWithDatabaseConsistent() {
        Unitils unitils = Unitils.getInstance();
        Object testObject = unitils.getTestContext().getTestObject();
        if (testObject == null) {
            throw new UnitilsException("Unable to assert hibernate mapping for current test. No current test found.");
        }
        HibernateModule hibernateModule = unitils.getModulesRepository().getModuleOfType(HibernateModule.class);
        hibernateModule.assertMappingWithDatabaseConsistent(testObject);
    }

    /**
     * Forces the reloading of the hibernate configurations the next time that it is requested. If classes are given
     * only hibernate configurations that are linked to those classes will be reset. If no classes are given, all cached
     * hibernate configurations will be reset.
     *
     * @param classes The classes for which to reset the configs, null for all configs
     */
    public static void invalidateHibernateConfiguration(Class<?>... classes) {
        HibernateModule hibernateModule = Unitils.getInstance().getModulesRepository().getModuleOfType(HibernateModule.class);
        hibernateModule.invalidateConfiguration(classes);
    }
}
