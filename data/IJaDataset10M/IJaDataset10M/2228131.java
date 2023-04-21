package com.synygy.bdviewer.dao;

import com.synygy.bdviewer.model.STIObject;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Detachable, read-only STIObject model. Ensures that memory used to load the
 * stiObject details is immediately freed rather than held in the session.
 * Typically used by <tt>List</tt>-type pages, where multiple elements are
 * loaded at a time.
 */
public class DetachableSTIObjectModel extends LoadableDetachableModel<STIObject> {

    /**
     * database identity of the stiObject
     */
    private final int id;

    /**
     * dao reference - must be a wicket-wrapped proxy, holding onto a reference
     * to the real dao will cause its serialization into session or a
     * not-serializable exception when the servlet container serializes the
     * session.
     */
    private final STIObjectDao stiObjectDao;

    /**
     * Constructor
     *
     * @param stiObject
     * @param stiObjectDao
     */
    public DetachableSTIObjectModel(STIObject stiObject, STIObjectDao stiObjectDao) {
        super(stiObject);
        this.id = stiObject.getId();
        this.stiObjectDao = stiObjectDao;
    }

    /**
     * Loads the stiObject from the database
     * @see org.apache.wicket.model.LoadableDetachableModel#load()
     */
    @Override
    protected STIObject load() {
        return stiObjectDao.load(id);
    }
}
