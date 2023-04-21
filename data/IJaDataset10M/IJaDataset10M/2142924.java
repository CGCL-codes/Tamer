package org.unicore.resources;

import java.util.Enumeration;
import org.unicore.ajo.Enum;

/**
 * The types of limits that can be applied.
 * 
 * @see org.unicore.resources.Limit
 * 
 * @version $Id: LimitTypeValue.java,v 1.2 2005/04/13 08:19:35 bschuller Exp $
 * 
 * @author Sven van den Berghe, Fujitsu Laboratories of Europe.
 *
 **/
public class LimitTypeValue extends Enum {

    static final long serialVersionUID = 451L;

    public static final LimitTypeValue WALL_TIME = new LimitTypeValue(0, "Wall time", "seconds");

    public static final LimitTypeValue FILE_SIZE = new LimitTypeValue(1, "File size", "megabytes");

    public static final LimitTypeValue CORE_DUMP = new LimitTypeValue(2, "Core Dump size", "megabytes");

    public static final LimitTypeValue DATA_SEGMENT = new LimitTypeValue(3, "Data Segment size", "kilobytes");

    public static final LimitTypeValue LOCKED_MEMORY = new LimitTypeValue(4, "Locked Memory size", "kilobytes");

    public static final LimitTypeValue MEMORY_RSS = new LimitTypeValue(5, "Memory Resident Set size", "kilobytes");

    public static final LimitTypeValue OPEN_DESCRIPTORS = new LimitTypeValue(6, "Open descriptors", "count");

    public static final LimitTypeValue PIPE_SIZE = new LimitTypeValue(7, "Pipe size", "kilobytes");

    public static final LimitTypeValue STACK_SIZE = new LimitTypeValue(8, "Stack size", "kilobytes");

    public static final LimitTypeValue CPU_TIME = new LimitTypeValue(9, "CPU time", "seconds");

    public static final LimitTypeValue PROCESS_COUNT = new LimitTypeValue(10, "Process count", "count");

    public static final LimitTypeValue VIRTUAL_MEMORY = new LimitTypeValue(11, "Virtual memory size", "kilobytes");

    public static final LimitTypeValue THREAD_COUNT = new LimitTypeValue(12, "Thread count", "count");

    private String units;

    private LimitTypeValue(int aValue, String aName, String units) {
        super(aValue, aName);
        this.units = units;
    }

    public static Enumeration elements() {
        return Enum.elements(WALL_TIME.getClass());
    }

    /**
	     * get for the units
	     * @return String the units
	     */
    public String getUnits() {
        return units;
    }
}
