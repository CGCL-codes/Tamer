package ch.ethz.mxquery.model.updatePrimitives;

import ch.ethz.mxquery.datamodel.Identifier;
import ch.ethz.mxquery.exceptions.MXQueryException;

public class Put implements UpdatePrimitive {

    private Identifier targetId;

    private String uri;

    public Put(Identifier targetId, String uri) {
        this.targetId = targetId;
        this.uri = uri;
    }

    public int getType() {
        return UpdatePrimitive.PUT;
    }

    public Identifier getTargetId() {
        return targetId;
    }

    public void applyUpdate() throws MXQueryException {
        UpdateableStore putStore = ((UpdateableStore) targetId.getStore()).getStoreSet().getNewStoreForItem(targetId, uri, true);
        putStore.setModified(true);
    }

    public String getURI() {
        return uri;
    }

    public UpdateableStore getStore() {
        return null;
    }
}
