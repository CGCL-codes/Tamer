package com.patientis.model.billing;

import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;
import com.patientis.framework.logging.Log;
import com.patientis.model.common.*;

/**
 * Invoice
 * 
 */
public class InvoiceSet extends HashSet<InvoiceModel> {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor
	 */
    public InvoiceSet() {
    }

    /**
	 * Fire base model property change for parent on add/clear
	 */
    @SuppressWarnings("unused")
    public InvoiceSet(IBaseModel parent) {
    }

    /**
	 * Default constructor
	 */
    @SuppressWarnings("unchecked")
    public InvoiceSet(Collection c) {
        super(c);
    }

    /**
	 * Default constructor
	 */
    public InvoiceSet(int initialCapacity) {
        super(initialCapacity);
    }

    /**
	 * Add to collection
	 */
    @Override
    public boolean add(InvoiceModel o) {
        return super.add(o);
    }

    /**
	 * Return the first Invoice found otherwise return a new Invoice after adding it to the list
	 */
    public InvoiceModel giveFirst() {
        Iterator iter = this.iterator();
        if (iter.hasNext()) {
            Object o = iter.next();
            if (o instanceof InvoiceModel) {
                return (InvoiceModel) o;
            } else {
                Log.errorTrace(o.getClass().getName() + "!=InvoiceModel");
                return null;
            }
        } else {
            InvoiceModel m = new InvoiceModel();
            add(m);
            return m;
        }
    }
}
