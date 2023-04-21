package com.cateshop.web.def;

import com.cateshop.def.ItemDefinition;
import com.cateshop.def.ItemDefinitionService;
import com.cateshop.web.SaveAction;

/**
 * @author notXX
 */
public class ItemDefinitionSaveAction extends SaveAction<ItemDefinition, ItemDefinitionForm> {

    @Override
    protected void save(ItemDefinition bean) {
        newInstance(ItemDefinitionService.class).save(bean);
    }
}
