package com.patientis.model.patient;

import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;
import com.patientis.framework.logging.Log;
import com.patientis.model.common.*;

/**
 * VisitUser
 * 
 */
public class VisitUserSet extends HashSet<VisitUserModel> {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor
	 */
    public VisitUserSet() {
    }

    /**
	 * Fire base model property change for parent on add/clear
	 */
    @SuppressWarnings("unused")
    public VisitUserSet(IBaseModel parent) {
    }

    /**
	 * Default constructor
	 */
    @SuppressWarnings("unchecked")
    public VisitUserSet(Collection c) {
        super(c);
    }

    /**
	 * Default constructor
	 */
    public VisitUserSet(int initialCapacity) {
        super(initialCapacity);
    }

    /**
	 * Add to collection
	 */
    @Override
    public boolean add(VisitUserModel o) {
        return super.add(o);
    }

    /**
	 * Return the first VisitUser found otherwise return a new VisitUser after adding it to the list
	 */
    public VisitUserModel giveFirst() {
        Iterator iter = this.iterator();
        if (iter.hasNext()) {
            Object o = iter.next();
            if (o instanceof VisitUserModel) {
                return (VisitUserModel) o;
            } else {
                Log.errorTrace(o.getClass().getName() + "!=VisitUserModel");
                return null;
            }
        } else {
            VisitUserModel m = new VisitUserModel();
            add(m);
            return m;
        }
    }
}
