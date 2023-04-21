package org.springframework.rules.closure.support;

import org.springframework.core.enums.ShortCodedLabeledEnum;

/**
 * Enumeration of possible process statuses.
 *
 * @author Keith Donald
 */
public class ProcessStatus extends ShortCodedLabeledEnum {

    private static final long serialVersionUID = 1L;

    /** Process created. */
    public static final ProcessStatus CREATED = new ProcessStatus(0, "Created");

    /** Process is running. */
    public static final ProcessStatus RUNNING = new ProcessStatus(1, "Running");

    /** Process has stopped. */
    public static final ProcessStatus STOPPED = new ProcessStatus(2, "Stopped");

    /** Process has completed. */
    public static final ProcessStatus COMPLETED = new ProcessStatus(3, "Completed");

    /** Process has been reset. */
    public static final ProcessStatus RESET = new ProcessStatus(4, "Reset");

    /**
	 * Private constructor because this is a typesafe enum!
	 */
    private ProcessStatus(int code, String label) {
        super(code, label);
    }
}
