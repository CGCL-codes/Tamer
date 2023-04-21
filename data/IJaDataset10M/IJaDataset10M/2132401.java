package com.cosmos.acacia.crm.gui.users;

import com.cosmos.acacia.crm.data.users.BusinessUnitAddress;
import com.cosmos.acacia.gui.entity.AbstractEntityListPanel;
import com.cosmos.acacia.gui.entity.EntityPanel;

/**
 *
 * @author Miro
 */
public class BusinessUnitAddressPanel extends EntityPanel<BusinessUnitAddress> {

    public BusinessUnitAddressPanel(AbstractEntityListPanel entityListPanel, BusinessUnitAddress entity) {
        super(entityListPanel, entity, null);
    }
}
