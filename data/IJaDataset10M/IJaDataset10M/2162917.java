package com.h9labs.swbem.win32;

import org.jinterop.dcom.impls.automation.IJIDispatch;
import com.h9labs.swbem.SWbemObject;
import com.h9labs.swbem.SWbemServices;

/**
 * The Win32Processor WMI class represents a device that can interpret a
 * sequence of instructions on a computer running on a Windows operating system.
 * On a multiprocessor computer, one instance of the Win32Processor class exists
 * for each processor.
 * 
 * @author akutz
 * 
 */
public class Win32Processor extends SWbemObject {

    /**
     * Initializes a new instance of the Win32Processor class.
     * 
     * @param objectDispatcher The underlying dispatch object used to
     *        communicate with the server.
     * @param service The service connection.
     */
    public Win32Processor(IJIDispatch objectDispatcher, SWbemServices service) {
        super(objectDispatcher, service);
    }

    /**
     * Gets the current speed of the processor, in MHz.
     * 
     * @return The current speed of the processor, in MHz.
     */
    public long getCurrentClockSpeed() {
        return super.getProperty("CurrentClockSpeed", Long.class);
    }

    /**
     * Gets the load capacity of each processor, averaged to the last second.
     * Processor loading refers to the total computing burden for each processor
     * at one time.
     * 
     * @return The load capacity of each processor, averaged to the last second.
     *         Processor loading refers to the total computing burden for each
     *         processor at one time.
     */
    public int getLoadPercentage() {
        return super.getProperty("LoadPercentage", Integer.class);
    }
}
