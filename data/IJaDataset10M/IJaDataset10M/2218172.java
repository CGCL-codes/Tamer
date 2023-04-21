package com.intel.gpe.client2.admin.panels.tables;

import java.util.Calendar;
import com.intel.gpe.client2.admin.panels.tree.GenericTargetSystemNode;
import com.intel.gpe.client2.common.tables.wrappers.NodeEntryWrapper;
import com.intel.gpe.client2.common.tables.wrappers.TimeEntryWrapper;
import com.intel.gui.controls2.configurable.ITableEntry;

/**
 * The target system entry.
 * 
 * @version $Id: TargetSystemEntry.java,v 1.7 2006/10/19 13:40:49 dizhigul Exp $
 * @author Denis Zhigula
 * @author Alexander Lukichev
 */
public class TargetSystemEntry implements ITableEntry, AdminTableEntry {

    private GenericTargetSystemNode node;

    protected Calendar calendar;

    public TargetSystemEntry(GenericTargetSystemNode node) {
        this.node = node;
        try {
            calendar = node.getTargetSystem().getClient().getTerminationTime();
        } catch (Exception e) {
        }
    }

    public void set(int i, Object o) {
    }

    public Comparable get(int i) {
        switch(i) {
            case 0:
                return new NodeEntryWrapper(node);
            case 1:
                return "---";
            case 2:
                return new TimeEntryWrapper(calendar);
            default:
                return null;
        }
    }

    public String getIconPath() {
        return node.getIconPath();
    }
}
