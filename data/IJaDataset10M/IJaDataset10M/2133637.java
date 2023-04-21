package org.jmonit;

import junit.framework.TestCase;
import org.jmonit.internal.DefaultPluginManager;
import org.jmonit.internal.DefaultRepository;

/**
 * @author <a href="mailto:nicolas.deloof@gmail.com">Nicolas De Loof</a>
 */
public abstract class AbstractRepositoryTestCase extends TestCase {

    protected Repository repository;

    /**
     * {@inheritDoc}
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        repository = new DefaultRepository();
        repository.setPluginManager(new DefaultPluginManager());
        Monitoring.setRepository(repository);
    }
}
