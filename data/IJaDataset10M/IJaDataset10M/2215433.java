package com.foursoft.fourever.objectmodel.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import com.foursoft.fourever.objectmodel.Instance;
import com.foursoft.fourever.objectmodel.InstanceObserver;
import com.foursoft.fourever.objectmodel.ObjectModel;
import com.foursoft.fourever.objectmodel.StringEnumerationType;

/**
 *
 */
public class StringEnumerationTypeImpl extends SimpleTypeImpl implements StringEnumerationType {

    private final List<String> values;

    /**
	 * @param typeName
	 * @param description
	 * @param model
	 * @param values
	 * 
	 */
    public StringEnumerationTypeImpl(String typeName, String description, ObjectModel model, Iterator<String> values) {
        super(typeName, description, model);
        List<String> tmpList = new ArrayList<String>();
        while (values.hasNext()) {
            tmpList.add(values.next());
        }
        this.values = Collections.unmodifiableList(tmpList);
    }

    /**
	 * @see com.foursoft.fourever.objectmodel.StringEnumerationType#getValues()
	 */
    public Iterator<String> getValues() {
        return values.iterator();
    }

    /**
	 * @see com.foursoft.fourever.objectmodel.impl.TypeImpl#createInstance(boolean)
	 */
    public Instance createInstance(boolean deep) {
        StringEnumerationInstanceImpl sei = new StringEnumerationInstanceImpl(this);
        sei.sendMemento();
        addInstance(sei);
        ((ObjectModelImpl) getObjectModel()).update(sei, InstanceObserver.INSTANCE_CREATED, null, null, -1);
        return sei;
    }
}
