package net.persister.executor.action;

import net.persister.meta.EntityModel;
import net.persister.meta.EntryInstance;

/**
 * @author Park, chanwook
 *
 */
public class AssignedInsertAction extends AbstractEntityAction {

    public AssignedInsertAction(EntryInstance entryInstance, EntityModel entityModel) {
        super.entryInstance = entryInstance;
        super.entityModel = entityModel;
    }

    @Override
    public String getPreparedSQL() {
        return entityModel.getPreparedAssignedInsertQuery();
    }
}
