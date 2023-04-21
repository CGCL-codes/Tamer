package com.wrupple.muba.catalogs.client.widgets.fields.providers;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.event.shared.EventBus;
import com.wrupple.muba.catalogs.client.activity.process.task.CatalogInteractionState.Mode;
import com.wrupple.muba.catalogs.client.module.services.presentation.CatalogFormFieldProvider;
import com.wrupple.muba.common.shared.State.ProcessManager;
import com.wrupple.vegetate.domain.FieldDescriptor;

public class CheckBoxFieldProvider implements CatalogFormFieldProvider {

    @Override
    public Cell<Boolean> createCell(EventBus bus, ProcessManager pm, FieldDescriptor d, Mode mode) {
        return new CheckboxCell();
    }
}
