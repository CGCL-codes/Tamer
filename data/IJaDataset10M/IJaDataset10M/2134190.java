package com.bluebrim.gui.client;

import com.bluebrim.base.shared.CoObjectIF;

/**
 	Abstrakt subklass till CoAspectAdaptor som implementeras d� man 
 	bara �r intresserad av att h�mta ett v�rde fr�n verksamhetsobjektet.
 	Dvs #set �r implementerad som en NOP.
 */
public abstract class CoReadOnlyAspectAdaptor extends CoAspectAdaptor {

    /**
 * CoReadOnlyAspectAdaptor constructor comment.
 * @param context com.bluebrim.base.client.CoValueable
 * @param name java.lang.String
 */
    public CoReadOnlyAspectAdaptor(CoValueable context, String name) {
        super(context, name);
    }

    /**
 * CoReadOnlyAspectAdaptor constructor comment.
 * @param context com.bluebrim.base.client.CoValueable
 * @param name java.lang.String
 * @param subjectFiresChange boolean
 */
    public CoReadOnlyAspectAdaptor(CoValueable context, String name, boolean subjectFiresChange) {
        super(context, name, subjectFiresChange);
    }

    /**
 */
    protected abstract Object get(CoObjectIF subject);

    public boolean isReadOnly() {
        return true;
    }

    /**
 * set method comment.
 */
    public void set(CoObjectIF subject, Object value) {
    }

    public void setValue(Object aValue) {
    }
}
