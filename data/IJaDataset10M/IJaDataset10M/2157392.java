package com.patientis.model.system;

import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;
import com.patientis.framework.logging.Log;
import com.patientis.model.common.*;

/**
 * SchedulerJobInstance
 * 
 */
public class SchedulerJobInstanceSet extends HashSet<SchedulerJobInstanceModel> {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor
	 */
    public SchedulerJobInstanceSet() {
    }

    /**
	 * Fire base model property change for parent on add/clear
	 */
    @SuppressWarnings("unused")
    public SchedulerJobInstanceSet(IBaseModel parent) {
    }

    /**
	 * Default constructor
	 */
    @SuppressWarnings("unchecked")
    public SchedulerJobInstanceSet(Collection c) {
        super(c);
    }

    /**
	 * Default constructor
	 */
    public SchedulerJobInstanceSet(int initialCapacity) {
        super(initialCapacity);
    }

    /**
	 * Add to collection
	 */
    @Override
    public boolean add(SchedulerJobInstanceModel o) {
        return super.add(o);
    }

    /**
	 * Return the first SchedulerJobInstance found otherwise return a new SchedulerJobInstance after adding it to the list
	 */
    public SchedulerJobInstanceModel giveFirst() {
        Iterator iter = this.iterator();
        if (iter.hasNext()) {
            Object o = iter.next();
            if (o instanceof SchedulerJobInstanceModel) {
                return (SchedulerJobInstanceModel) o;
            } else {
                Log.errorTrace(o.getClass().getName() + "!=SchedulerJobInstanceModel");
                return null;
            }
        } else {
            SchedulerJobInstanceModel m = new SchedulerJobInstanceModel();
            add(m);
            return m;
        }
    }
}
