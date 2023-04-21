package ch.ethz.mxquery.model.updatePrimitives;

import ch.ethz.mxquery.datamodel.Identifier;
import ch.ethz.mxquery.datamodel.Source;
import ch.ethz.mxquery.datamodel.XQName;
import ch.ethz.mxquery.exceptions.DynamicException;
import ch.ethz.mxquery.exceptions.ErrorCodes;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.exceptions.QueryLocation;

public class Rename implements UpdatePrimitive {

    private Identifier targetId;

    Identifier parent;

    XQName name;

    public Rename(Identifier targetId, Identifier parent, XQName name) {
        this.targetId = targetId;
        this.name = name;
        this.parent = parent;
    }

    public void applyUpdate() throws MXQueryException {
        Source src = targetId.getStore();
        if (src instanceof UpdateableStore) {
            UpdateableStore ds = (UpdateableStore) this.targetId.getStore();
            ds.rename(this.targetId, this.name);
            ds.setModified(true);
        } else throw new DynamicException(ErrorCodes.A0009_EC_EVALUATION_NOT_POSSIBLE, "Update performed on a non-updateable store", QueryLocation.OUTSIDE_QUERY_LOC);
    }

    public Identifier getTargetId() {
        return this.targetId;
    }

    public int getType() {
        return UpdatePrimitive.RENAME;
    }

    public UpdateableStore getStore() {
        return null;
    }
}
