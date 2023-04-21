package com.hyper9.jwbem.win32;

import org.jinterop.dcom.impls.automation.IJIDispatch;
import com.hyper9.jwbem.SWbemObject;
import com.hyper9.jwbem.SWbemServices;

/**
 * The Win32PerfFormattedDataPerfOSMemory formatted data class provides
 * pre-calculated performance data from performance counters that monitor the
 * physical and virtual memory on the computer.
 * 
 * @author akutz
 * @remarks <p>
 *          Physical memory is the amount of random access memory (RAM) on the
 *          computer. Virtual memory consists of space in physical memory and on
 *          disk. Many of the memory counters monitor paging, which is the
 *          movement of pages of code and data between disk and physical memory.
 *          Excessive paging, a symptom of a memory shortage, can cause delays
 *          which interfere with all system processes.
 *          </p>
 *          <p>
 *          This class represents the Memory object in System Monitor and
 *          returns the same data you see in System Monitor. The original data
 *          source is the PerfOS performance library. The corresponding raw data
 *          class is Win32PerfRawDataPerfOSMemory. Data is dynamically provided
 *          for this class from the performance library object by the
 *          WmiPerfInst provider.
 *          </p>
 */
public class Win32PerfFormattedDataPerfOSMemory extends SWbemObject {

    /**
     * Initializes a new instance of the Win32PerfFormattedDataPerfOSMemory
     * class.
     * 
     * @param objectDispatcher The underlying dispatch object used to
     *        communicate with the server.
     * @param service The service connection.
     */
    public Win32PerfFormattedDataPerfOSMemory(IJIDispatch objectDispatcher, SWbemServices service) {
        super(objectDispatcher, service);
    }

    /**
     * Gets the amount of physical memory in bytes available to processes
     * running on the computer.
     * 
     * @return <p>
     *         This value is calculated by summing space on the Zeroed, Free,
     *         and Standby memory lists. Free memory is ready for use; Zeroed
     *         memory is pages of memory filled with zeros to prevent later
     *         processes from seeing data used by a previous process. Standby
     *         memory is memory removed from a process's working set (its
     *         physical memory) on route to disk, but is still available to be
     *         recalled. This property displays the last observed value only; it
     *         is not an average.
     *         </p>
     */
    public long getAvailableBytes() {
        return super.getProperty("AvailableBytes", Long.class);
    }

    /**
     * Gets the amount of physical memory available to processes running on the
     * computer, in kilobytes.
     * 
     * @return The amount of physical memory available to processes running on
     *         the computer, in kilobytes.
     * @remarks <p>
     *          <It is calculated by summing space on the Zeroed, Free, and
     *          Standby memory lists. Free memory is ready for use; Zeroed
     *          memory contains memory pages filled with zeros to prevent later
     *          processes from seeing data used by a previous Process. Standby
     *          memory is memory removed from a process' working set (its
     *          physical memory), but is still available to be recalled. This
     *          property displays the last observed value only; it is not an
     *          average.
     *          </p>
     */
    public long getAvailableKBytes() {
        return super.getProperty("AvailableKBytes", Long.class);
    }

    /**
     * Gets the amount of physical memory available to processes running on the
     * computer, in megabytes.
     * 
     * @return The amount of physical memory available to processes running on
     *         the computer, in megabytes.
     * @remarks <p>
     *          This value is calculated by summing space on the Zeroed, Free,
     *          and Standby memory lists. Free memory is ready for use; Zeroed
     *          memory contains memory pages filled with zeros to prevent later
     *          processes from seeing data used by a previous process. Standby
     *          memory is memory removed from a process' working set (its
     *          physical memory), but is still available to be recalled. This
     *          property displays the last observed value only; it is not an
     *          average.
     *          </p>
     */
    public long getAvailableMBytes() {
        return super.getProperty("AvailableMBytes", Long.class);
    }
}
