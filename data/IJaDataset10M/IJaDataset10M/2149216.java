package com.patientis.model.patient;

import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;
import com.patientis.framework.logging.Log;
import com.patientis.model.common.*;

/**
 * PatientList
 * 
 */
public class PatientListSet extends HashSet<PatientListModel> {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor
	 */
    public PatientListSet() {
    }

    /**
	 * Fire base model property change for parent on add/clear
	 */
    @SuppressWarnings("unused")
    public PatientListSet(IBaseModel parent) {
    }

    /**
	 * Default constructor
	 */
    @SuppressWarnings("unchecked")
    public PatientListSet(Collection c) {
        super(c);
    }

    /**
	 * Default constructor
	 */
    public PatientListSet(int initialCapacity) {
        super(initialCapacity);
    }

    /**
	 * Add to collection
	 */
    @Override
    public boolean add(PatientListModel o) {
        return super.add(o);
    }

    /**
	 * Return the first PatientList found otherwise return a new PatientList after adding it to the list
	 */
    public PatientListModel giveFirst() {
        Iterator iter = this.iterator();
        if (iter.hasNext()) {
            Object o = iter.next();
            if (o instanceof PatientListModel) {
                return (PatientListModel) o;
            } else {
                Log.errorTrace(o.getClass().getName() + "!=PatientListModel");
                return null;
            }
        } else {
            PatientListModel m = new PatientListModel();
            add(m);
            return m;
        }
    }
}
