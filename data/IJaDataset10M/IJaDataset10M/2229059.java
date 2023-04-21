package org.gwm.splice.client.service;

import org.gwm.splice.client.service.data.Attributes;
import org.gwm.splice.client.service.data.RemoteObject;
import org.gwm.splice.client.service.query.Filter;

public class GenericDataService extends GenericRemoteService implements IRemoteDataService {

    public static final String ACTION_GET = "get";

    public static final String ACTION_SAVE = "save";

    public static final String ACTION_DELETE = "delete";

    public static final String ACTION_LIST = "list";

    private String objectType = null;

    private Filter filter = null;

    public GenericDataService() {
    }

    public GenericDataService(String objectType) {
        this.objectType = objectType;
    }

    public void get(Object id, IResponseHandler handler) {
        Attributes attrs = new Attributes();
        attrs.put("id", id);
        addObjectTypeParam(attrs);
        execute(ACTION_GET, attrs, handler);
    }

    public void save(RemoteObject dataObject, IResponseHandler handler) {
        execute(ACTION_SAVE, dataObject, handler);
    }

    public void delete(Object id, IResponseHandler handler) {
        Attributes attrs = new Attributes();
        attrs.put("id", id);
        addObjectTypeParam(attrs);
        execute(ACTION_DELETE, attrs, handler);
    }

    public void list(IResponseHandler handler) {
        list(null, handler);
    }

    public void list(Filter filter, IResponseHandler handler) {
        Attributes attrs;
        if (filter != null) {
            attrs = filter.toParameters();
        } else if (this.filter != null) {
            attrs = this.filter.toParameters();
        } else {
            attrs = new Attributes();
        }
        addObjectTypeParam(attrs);
        execute(ACTION_LIST, attrs, handler);
    }

    /**
	 * Helper only adds remoteObjectType to params if not null.
	 * @param attrs
	 */
    private void addObjectTypeParam(Attributes attrs) {
        if (objectType != null) {
            attrs.put("objectType", objectType);
        }
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}
