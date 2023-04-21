package org.apache.zookeeper.inspector.logger;

/**
 * Provides a {@link Logger} for use across the entire application
 * 
 */
public class LoggerFactory {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("org.apache.zookeeper.inspector");

    /**
	 * @return {@link Logger} for ZooInspector
	 */
    public static org.slf4j.Logger getLogger() {
        return logger;
    }
}
