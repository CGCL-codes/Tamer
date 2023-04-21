package net.persister.test.template;

import net.persister.template.test.AbstractPersisterTransactionalTest;

/**
 * @author Park, chanwook
 *
 */
public abstract class AbstractPersisterTransactionalTestForCommon extends AbstractPersisterTransactionalTest {

    @Override
    protected String initialPersistentContextPath() {
        return ConfigurationPath.COMMON;
    }
}
