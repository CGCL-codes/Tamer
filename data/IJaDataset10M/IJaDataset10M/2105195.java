package com.sun.ebxml.registry.query.filter;

import com.sun.ebxml.registry.*;
import org.oasis.ebxml.registry.bindings.query.*;
import org.oasis.ebxml.registry.bindings.rs.*;

/**
 * Class Declaration for SlotBranchProcessor
 * @see
 * @author Nikola Stojanovic
 */
public class SlotBranchProcessor extends BranchProcessor {

    private SlotBranch slotBranch = null;

    protected String getName() {
        return "Slot";
    }

    protected void setNativeBranch(Object branch) {
        slotBranch = (SlotBranch) branch;
    }

    protected void buildFilterClauses() throws RegistryException {
        convertSlotFilter();
        convertSlotValueFilters();
    }

    protected void buildQueryClauses() throws RegistryException {
    }

    protected void buildBranchClauses() throws RegistryException {
    }

    private void convertSlotFilter() throws RegistryException {
        if (slotBranch.getSlotFilter() != null) {
            whereClause = filterProcessor.addNativeWhereClause(whereClause, slotBranch.getSlotFilter());
        }
    }

    private void convertSlotValueFilters() throws RegistryException {
        SlotValueFilter[] slotValueFilter = slotBranch.getSlotValueFilter();
        if (slotValueFilter.length > 0) {
            filterProcessor.setSelectColumn("parent");
            for (int i = 0; i < slotValueFilter.length; i++) {
                whereClause = filterProcessor.addNativeWhereClause(whereClause, slotValueFilter[i]);
            }
        }
    }
}
