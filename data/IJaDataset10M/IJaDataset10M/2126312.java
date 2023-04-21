package com.intel.gpe.server.tables;

import org.apache.axis.handlers.soap.SOAPService;
import com.intel.gui.controls2.configurable.ITableEntry;

/**
 * @version $Id: ServiceEntry.java,v 1.1 2006/08/18 10:40:47 dnpetrov Exp $
 * @author Denis Zhigula
 */
public class ServiceEntry implements ITableEntry {

    public static final String RUNNING = "RUNNING";

    public static final String STOPPED = "STOPPED";

    private SOAPService service;

    public ServiceEntry(SOAPService service) {
        this.service = service;
    }

    public void set(int i, Object o) {
        service = ((ServiceEntry) o).getUserObject();
    }

    public Comparable get(int i) {
        switch(i) {
            case 0:
                return service.getName();
            case 1:
                return service.isRunning() ? RUNNING : STOPPED;
            default:
                return null;
        }
    }

    public SOAPService getUserObject() {
        return service;
    }
}
