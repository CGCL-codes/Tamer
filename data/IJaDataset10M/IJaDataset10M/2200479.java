package com.wrupple.muba.catalogs.client.module.services.presentation;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.event.shared.EventBus;
import com.wrupple.muba.catalogs.client.activity.process.task.CatalogInteractionState.Mode;
import com.wrupple.muba.common.shared.State.ProcessManager;
import com.wrupple.vegetate.domain.FieldDescriptor;

/**
 * @author japi
 * 
 */
public interface CatalogFormFieldProvider {

    /**EventBus bus,ProcessManager pm,
	 * @param bus TODO
	 * @param pm TODO
	 * @param d
	 * @param mode TODO
	 * @return a cell capable of editing a value directly extracted from the corresponding field in the JavaScriptObject
	 * @throws Exception TODO
	 */
    Cell<? extends Object> createCell(EventBus bus, ProcessManager pm, FieldDescriptor d, Mode mode);
}
