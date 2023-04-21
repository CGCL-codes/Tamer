package com.sun.midp.jump.push.executive.persistence;

import com.sun.midp.push.persistence.Store;
import com.sun.midp.push.persistence.AbstractStoreUtils;
import com.sun.jump.module.contentstore.InMemoryContentStore;
import com.sun.jump.module.contentstore.JUMPStoreHandle;
import java.io.IOException;

/** Jump variant of <code>StoreUtils</code>. */
public final class StoreUtils extends AbstractStoreUtils {

    /** Hides a constructor. */
    protected StoreUtils() {
    }

    /** {@inheritDoc} */
    public static StoreOperationManager createInMemoryManager(final String[] dirs) throws IOException {
        final StoreOperationManager storeManager = new StoreOperationManager(new InMemoryContentStore());
        storeManager.doOperation(true, new StoreOperationManager.Operation() {

            public Object perform(final JUMPStoreHandle storeHandle) throws IOException {
                InMemoryContentStore.initStore(storeHandle, dirs);
                return null;
            }
        });
        return storeManager;
    }

    /** Jump-specific impl of <code>Refresher</code>. */
    private static class MyRefresher implements Refresher {

        /** Content-store dirs. */
        private static final String[] DIRS = { JUMPStoreImpl.CONNECTIONS_DIR, JUMPStoreImpl.ALARMS_DIR };

        /** <code>StoreOperationManager</code> to use. */
        private final StoreOperationManager som;

        /**
         * Default ctor.
         *
         * @throws IOException if creation fails
         */
        MyRefresher() throws IOException {
            som = createInMemoryManager(DIRS);
        }

        /** {@inheritDoc} */
        public Store getStore() throws IOException {
            return new JUMPStoreImpl(som);
        }
    }

    /** {@inheritDoc} */
    public Refresher getRefresher() throws IOException {
        return new MyRefresher();
    }
}
