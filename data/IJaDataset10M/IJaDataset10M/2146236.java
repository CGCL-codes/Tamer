package org.argouml.uml.reveng;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * Keeps an instance of each ImportInterface implementation module registered.
 * ImporterManager is a singleton.
 */
public final class ImporterManager {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(ImporterManager.class);

    /**
     * The instance.
     */
    private static final ImporterManager INSTANCE = new ImporterManager();

    /**
     * @return The singleton instance of the importer manager.
     */
    public static ImporterManager getInstance() {
        return INSTANCE;
    }

    private Set<ImportInterface> importers = new HashSet<ImportInterface>();

    /**
     * The constructor.
     */
    private ImporterManager() {
    }

    /**
     * Register a new source language importer.
     * 
     * @param importer The ImportInterface object to register.
     * @deprecated for 0.25.3 by tfmorris. Use
     *             {@link #addImporter(ImportInterface)} instead.
     */
    @Deprecated
    public void addimporter(ImportInterface importer) {
        addImporter(importer);
    }

    /**
     * Register a new source language importer. 
     *
     * @param importer The ImportInterface object to register.
     */
    public void addImporter(ImportInterface importer) {
        importers.add(importer);
        LOG.debug("Added importer " + importer);
    }

    /**
     * Removes a importer.
     * 
     * @param importer
     *            the importer to be removed
     * 
     * @return false if no matching importer had been registered
     */
    public boolean removeImporter(ImportInterface importer) {
        boolean status = importers.remove(importer);
        LOG.debug("Removed importer " + importer);
        return status;
    }

    /**
     * @return A copy of the set of importers.
     */
    public Set<ImportInterface> getImporters() {
        return Collections.unmodifiableSet(importers);
    }
}
