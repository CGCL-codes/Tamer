package com.sun.sgs.impl.kernel;

import com.sun.sgs.app.ChannelManager;
import com.sun.sgs.app.DataManager;
import com.sun.sgs.app.TaskManager;
import com.sun.sgs.service.Service;

/**
 * This is an overridden implementation of {@code KernelContext} used only
 * during to startup to provide access to {@code Manager}s and
 * {@code Service}s as they become available.
 */
final class StartupKernelContext extends KernelContext {

    /**
     * Creates an instance of {@code StartupKernelContext}.
     *
     * @param applicationName the name of the application represented
     *                        by this context
     */
    public StartupKernelContext(String applicationName) {
        super(applicationName, new ComponentRegistryImpl(), new ComponentRegistryImpl());
    }

    /**
     * Adds a {@code Manager} to those avilable in this context.
     *
     * @param manager the {@code Manager} to add
     */
    void addManager(Object manager) {
        ((ComponentRegistryImpl) managerComponents).addComponent(manager);
    }

    /**
     * Adds a {@code Service} to those avilable in this context.
     *
     * @param service the {@code Service} to add
     */
    void addService(Service service) {
        ((ComponentRegistryImpl) serviceComponents).addComponent(service);
    }

    /**
     * {@inheritDoc}
     */
    ChannelManager getChannelManager() {
        return getManager(ChannelManager.class);
    }

    /**
     * {@inheritDoc}
     */
    DataManager getDataManager() {
        return getManager(DataManager.class);
    }

    /**
     * {@inheritDoc}
     */
    TaskManager getTaskManager() {
        return getManager(TaskManager.class);
    }

    /**
     * {@inheritDoc}
     */
    void notifyReady() {
        throw new AssertionError("A temporary startup context should never " + "be notified that an application is ready");
    }
}
