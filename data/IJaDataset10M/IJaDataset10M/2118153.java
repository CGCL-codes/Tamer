package org.nexopenframework.ide.eclipse.memory;

import org.eclipse.osgi.util.NLS;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Message for <code>monitor</code> component</p>
 * 
 * @see org.eclipse.osgi.util.NLS
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.nexopenframework.ide.eclipse.memory.messages";

    public static String MemoryMonitorJob_dialog_memorylow_message;

    public static String MemoryMonitorJob_dialog_memorylow_title;

    public static String MemoryMonitorJob_taskname_memorymonitor;

    static {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
        super();
    }
}
