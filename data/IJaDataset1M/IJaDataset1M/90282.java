package de.ios.framework.remote.auth.sv.impl;

import de.ios.framework.db2.*;
import de.ios.framework.remote.sv.impl.*;

public class MemberLinkDBO extends BasicDBO {

    public MemberLinkDBO() {
        super();
    }

    public MemberLinkDBO(Long _parentId, Long _childId, Long _companyId) {
        super();
        setAll(_parentId, _childId, _companyId, true);
    }

    public void setAll(Long _parentId, Long _childId, Long _companyId, boolean setNull) {
        if (setNull || (_parentId != null)) parentId.set(_parentId);
        if (setNull || (_childId != null)) childId.set(_childId);
        if (setNull || (_companyId != null)) companyId.set(_companyId);
    }

    /** The Parent of the Member. */
    public DBLongAttr parentId;

    /** The Child (Member) of the Parent. */
    public DBLongAttr childId;

    /** The Company, this Link belongs to. */
    public DBLongAttr companyId;
}
