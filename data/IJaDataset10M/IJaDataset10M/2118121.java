package org.swiftgantt.core.common;

/**
 * Logger for layout classes.
 * 
 * @author Yuxing Wang
 * 
 */
public class LayoutLogger extends GanttLogger {

    public LayoutLogger(Class<?> targetClass) {
        super(targetClass);
    }

    public LayoutLogger(LogAdapter logger) {
        super(logger);
    }

    @Override
    public String getCategory() {
        return "[LAYOUT]";
    }
}
