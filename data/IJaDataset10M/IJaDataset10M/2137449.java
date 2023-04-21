package com.simpledata.bc.components.accessors;

import java.util.ArrayList;
import com.simpledata.bc.components.TarifTreeItem;
import com.simpledata.bc.components.bcoption.OptionCase;
import com.simpledata.bc.components.worksheet.dispatcher.DispatcherCase;
import com.simpledata.bc.datamodel.WorkSheet;

/**
 * This class provides accessors for the data contained in a DispatcherCase.
 * @author Simpledata SARL, 2004, all rights reserved.
 * @version $Id: WrapDispatcherCase.java,v 1.2 2007/04/02 17:04:28 perki Exp $
 */
public class WrapDispatcherCase {

    private final DispatcherCase m_dispatcher;

    /**
     * Creates a new accessor utility for the given DispatcherCase
     * @param dispatcher A DispatcherCase 
     */
    public WrapDispatcherCase(DispatcherCase dispatcher) {
        m_dispatcher = dispatcher;
    }

    /**
     * This method return the WorkSheet linked to the current state
     * of the dispatcher options.
     * @return One of the child WorkSheet.
     */
    public WorkSheet getAppliedWorkSheet() {
        ArrayList options = m_dispatcher.getOptions(OptionCase.class);
        assert options.size() > 0 : "Dispatcher must contains at least an OptionCase";
        OptionCase aCase = (OptionCase) options.get(0);
        String key = aCase.getSelectedKey();
        return m_dispatcher.getWorkSheetAt(key);
    }

    /**
     * Same as getAppliedWorkSheet, but it returns a TarifTreeItem
     * object. This method raise an assertion exception if the WorkSheet
     * cannot be casted.
     * @return One of the child TarifTreeItem.
     */
    public TarifTreeItem getAppliedTarifTreeItem() {
        WorkSheet ws = getAppliedWorkSheet();
        assert ws instanceof TarifTreeItem : "The selected WorkSheet must be a TarifTreeItem";
        return (TarifTreeItem) ws;
    }
}
