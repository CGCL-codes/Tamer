package com.patientis.model.patient;

import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;
import com.patientis.framework.logging.Log;
import com.patientis.model.common.*;

/**
 * PatientLog
 * 
 */
public class PatientLogSet extends HashSet<PatientLogModel> {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor
	 */
    public PatientLogSet() {
    }

    /**
	 * Fire base model property change for parent on add/clear
	 */
    @SuppressWarnings("unused")
    public PatientLogSet(IBaseModel parent) {
    }

    /**
	 * Default constructor
	 */
    @SuppressWarnings("unchecked")
    public PatientLogSet(Collection c) {
        super(c);
    }

    /**
	 * Default constructor
	 */
    public PatientLogSet(int initialCapacity) {
        super(initialCapacity);
    }

    /**
	 * Add to collection
	 */
    @Override
    public boolean add(PatientLogModel o) {
        return super.add(o);
    }

    /**
	 * Return the first PatientLog found otherwise return a new PatientLog after adding it to the list
	 */
    public PatientLogModel giveFirst() {
        Iterator iter = this.iterator();
        if (iter.hasNext()) {
            Object o = iter.next();
            if (o instanceof PatientLogModel) {
                return (PatientLogModel) o;
            } else {
                Log.errorTrace(o.getClass().getName() + "!=PatientLogModel");
                return null;
            }
        } else {
            PatientLogModel m = new PatientLogModel();
            add(m);
            return m;
        }
    }
}
