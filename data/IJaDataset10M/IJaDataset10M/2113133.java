package com.cosmos.acacia.crm.gui.contacts;

import com.cosmos.acacia.crm.data.contacts.Address;
import com.cosmos.acacia.gui.entity.AbstractEntityListPanel;
import com.cosmos.acacia.gui.entity.EntityPanel;

/**
 *
 * @author Miro
 */
public class AddressPanel extends EntityPanel<Address> {

    public AddressPanel(AbstractEntityListPanel entityListPanel, Address entity) {
        super(entityListPanel, entity, null);
    }
}
