package com.patientis.model.clinical;

import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;
import com.patientis.framework.logging.Log;
import com.patientis.model.common.*;

/**
 * Term
 * 
 */
public class TermSet extends HashSet<TermModel> {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor
	 */
    public TermSet() {
    }

    /**
	 * Fire base model property change for parent on add/clear
	 */
    @SuppressWarnings("unused")
    public TermSet(IBaseModel parent) {
    }

    /**
	 * Default constructor
	 */
    @SuppressWarnings("unchecked")
    public TermSet(Collection c) {
        super(c);
    }

    /**
	 * Default constructor
	 */
    public TermSet(int initialCapacity) {
        super(initialCapacity);
    }

    /**
	 * Add to collection
	 */
    @Override
    public boolean add(TermModel o) {
        return super.add(o);
    }

    /**
	 * Return the first Term found otherwise return a new Term after adding it to the list
	 */
    public TermModel giveFirst() {
        Iterator iter = this.iterator();
        if (iter.hasNext()) {
            Object o = iter.next();
            if (o instanceof TermModel) {
                return (TermModel) o;
            } else {
                Log.errorTrace(o.getClass().getName() + "!=TermModel");
                return null;
            }
        } else {
            TermModel m = new TermModel();
            add(m);
            return m;
        }
    }
}
