package org.openxava.invoicing.actions;

import java.util.*;
import org.openxava.actions.*;
import org.openxava.model.*;
import org.openxava.model.meta.*;
import org.openxava.validators.*;

public class InvoicingDeleteSelectedAction extends TabBaseAction implements IChainAction {

    private String nextAction = null;

    private boolean restore;

    public void execute() throws Exception {
        if (!getMetaModel().containsMetaProperty("deleted")) {
            nextAction = "CRUD.deleteSelected";
            return;
        }
        markSelectedEntitiesAsDeleted();
    }

    private MetaModel getMetaModel() {
        return MetaModel.get(getTab().getModelName());
    }

    public String getNextAction() throws Exception {
        return nextAction;
    }

    private void markSelectedEntitiesAsDeleted() throws Exception {
        Map values = new HashMap();
        values.put("deleted", !isRestore());
        for (int row : getSelected()) {
            Map key = (Map) getTab().getTableModel().getObjectAt(row);
            try {
                MapFacade.setValues(getTab().getModelName(), key, values);
            } catch (ValidationException ex) {
                addError("no_delete_row", row + 1, key);
                addErrors(ex.getErrors());
            } catch (Exception ex) {
                addError("no_delete_row", row + 1, key);
            }
        }
        getTab().deselectAll();
        resetDescriptionsCache();
    }

    public boolean isRestore() {
        return restore;
    }

    public void setRestore(boolean restore) {
        this.restore = restore;
    }
}
