package ch.ethz.mxquery.model.updatePrimitives;

import ch.ethz.mxquery.datamodel.Identifier;
import ch.ethz.mxquery.datamodel.Source;
import ch.ethz.mxquery.exceptions.DynamicException;
import ch.ethz.mxquery.exceptions.ErrorCodes;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.exceptions.QueryLocation;

public class InsertInto implements UpdatePrimitive {

    private Identifier targetId;

    private UpdateableStore store;

    public InsertInto(Identifier targetId, UpdateableStore sourceStore) {
        this.targetId = targetId;
        this.store = sourceStore;
    }

    public void applyUpdate() throws MXQueryException {
        Source sr = this.targetId.getStore();
        if (sr instanceof UpdateableStore) {
            UpdateableStore ds = (UpdateableStore) sr;
            ds.insertInto(this.targetId, this.store);
            ds.setModified(true);
        } else throw new DynamicException(ErrorCodes.A0009_EC_EVALUATION_NOT_POSSIBLE, "Update performed on a non-updateable store", QueryLocation.OUTSIDE_QUERY_LOC);
    }

    public Identifier getTargetId() {
        return this.targetId;
    }

    public int getType() {
        return UpdatePrimitive.INSERT_INTO;
    }

    public UpdateableStore getStore() {
        return store;
    }
}
