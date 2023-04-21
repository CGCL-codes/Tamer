package com.cosmos.acacia.crm.gui.security;

import com.cosmos.acacia.crm.data.security.PrivilegeCategory;
import com.cosmos.acacia.gui.entity.EntityListPanel;
import com.cosmos.acacia.gui.entity.EntityPanel;

/**
 *
 * @author Miro
 */
public class PrivilegeCategoryListPanel extends EntityListPanel<PrivilegeCategory> {

    public PrivilegeCategoryListPanel() {
        super(PrivilegeCategory.class, null);
    }

    @Override
    protected EntityPanel getEntityPanel(PrivilegeCategory entity) {
        return new PrivilegeCategoryPanel(this, entity);
    }
}
