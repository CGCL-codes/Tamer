package org.nicocube.airain.domain.server.data;

import org.nicocube.airain.domain.client.data.OrderedStorable;
import org.nicocube.airain.domain.client.data.StorageException;
import com.db4o.ObjectContainer;

/**
 * Define a provider for DB4O Container
 * @author nicolas
 *
 */
public interface ContainerProvider {

    /**
	 * @return an ObjectContainer
	 */
    ObjectContainer getContainer();

    void registerEvent(Class<? extends OrderedStorable> clazz, EventType type, EventHandler handler) throws StorageException;

    public static enum EventType {

        creating, updated
    }

    public static interface EventHandler {

        void handle(Object t) throws CancelEventException;
    }

    public static class CancelEventException extends Exception {

        private static final long serialVersionUID = 1L;

        public CancelEventException() {
            super();
        }
    }
}
