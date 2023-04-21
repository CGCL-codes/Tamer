package com.patientis.model.clinical;

import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;
import com.patientis.framework.logging.Log;
import com.patientis.model.common.*;

/**
 * Form
 * 
 */
public class FormSet extends HashSet<FormModel> {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor
	 */
    public FormSet() {
    }

    /**
	 * Fire base model property change for parent on add/clear
	 */
    @SuppressWarnings("unused")
    public FormSet(IBaseModel parent) {
    }

    /**
	 * Default constructor
	 */
    @SuppressWarnings("unchecked")
    public FormSet(Collection c) {
        super(c);
    }

    /**
	 * Default constructor
	 */
    public FormSet(int initialCapacity) {
        super(initialCapacity);
    }

    /**
	 * Add to collection
	 */
    @Override
    public boolean add(FormModel o) {
        return super.add(o);
    }

    /**
	 * Return the first Form found otherwise return a new Form after adding it to the list
	 */
    public FormModel giveFirst() {
        Iterator iter = this.iterator();
        if (iter.hasNext()) {
            Object o = iter.next();
            if (o instanceof FormModel) {
                return (FormModel) o;
            } else {
                Log.errorTrace(o.getClass().getName() + "!=FormModel");
                return null;
            }
        } else {
            FormModel m = new FormModel();
            add(m);
            return m;
        }
    }
}
