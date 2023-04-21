package com.jade4spring.managers;

import jade.BootProfileImpl;
import jade.core.ProfileImpl;
import jade.util.BasicProperties;
import jade.wrapper.AgentContainer;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 * Standard implementation of ContainerManager. Used to manage a JADE
 * agent container by jade4spring.
 * 
 * @author Jaran Nilsen
 * @since 2.0
 */
@Component
public class ContainerManagerImpl implements ContainerManager {

    private final Log l = LogFactory.getLog(getClass());

    private String propertiesLocation = "classpath:/jade4spring.properties";

    private jade.core.Runtime runtime;

    private AgentContainer container;

    private long containerTimeout = 20000;

    /**
     * Executor service handling the execution of the contaner thread.
     */
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private boolean running = false;

    /**
     * @return the propertiesLocation
     */
    public final String getPropertiesLocation() {
        return propertiesLocation;
    }

    /**
     * @param propertiesLocation
     *            the propertiesLocation to set
     */
    public final void setPropertiesLocation(String propertiesLocation) {
        this.propertiesLocation = propertiesLocation;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.jade4spring.managers.ContainerManager#isRunning()
     */
    public final boolean isRunning() {
        return this.running;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.jade4spring.managers.ContainerManager#startContainer()
     */
    public final void startContainer() {
        l.info("Starting JADE container...");
        ProfileImpl containerProfile = createContainerProfile();
        runtime = jade.core.Runtime.instance();
        ContainerThread containerThread = new ContainerThread(runtime, containerProfile);
        executorService.execute(containerThread);
        long waitStart = System.currentTimeMillis();
        boolean timeout = false;
        while (!containerThread.isStarted() && !timeout) {
            timeout = (System.currentTimeMillis() - waitStart >= containerTimeout);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
        }
        if (timeout) {
            l.error("Container startup timed out.");
            running = false;
            return;
        }
        container = containerThread.getContainer();
        if (container == null) {
            l.error("An error prevented the agent container from starting.");
            running = false;
            return;
        }
        running = true;
        l.info("The JADE container is now active.");
    }

    /**
     * Create a container profile.
     * 
     * @return ProfileImpl
     */
    final ProfileImpl createContainerProfile() {
        BasicProperties properties = loadProfileProperties();
        return new BootProfileImpl(properties.toStringArray());
    }

    /**
     * Load profile properties.
     * 
     * @return BasicProperties
     */
    final BasicProperties loadProfileProperties() {
        BasicProperties properties = new BasicProperties();
        if (propertiesLocation.startsWith("classpath:")) {
            try {
                properties.load(getClass().getResourceAsStream(propertiesLocation.substring(10)));
            } catch (Exception e) {
                throw new IllegalArgumentException("The specified properties " + "file (" + propertiesLocation + ") cannot be " + "loaded.", e);
            }
        } else {
            FileInputStream is = null;
            try {
                is = new FileInputStream(propertiesLocation);
                properties.load(is);
            } catch (Exception e) {
                throw new IllegalArgumentException("The specified properties " + "file (" + propertiesLocation + ") cannot be " + "loaded.", e);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        l.warn("Failed to close profile properties file: " + e.getMessage());
                    }
                }
            }
        }
        return properties;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.jade4spring.managers.ContainerManager#stopContainer()
     */
    public final void stopContainer() {
        l.info("Initializing container shutdown...");
        if (container == null) {
            l.info("The container is not running.");
            return;
        }
        executorService.shutdownNow();
        l.info("Container shutdown commenced.");
    }

    /**
     * @return the containerTimeout
     */
    public final long getContainerTimeout() {
        return containerTimeout;
    }

    /**
     * @param containerTimeout
     *            the containerTimeout to set
     */
    public final void setContainerTimeout(long containerTimeout) {
        this.containerTimeout = containerTimeout;
    }
}
