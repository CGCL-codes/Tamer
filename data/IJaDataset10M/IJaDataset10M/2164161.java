package org.nakedobjects.persistence.nhibernate.property;

import java.lang.reflect.Field;
import NHibernate.Util.ReflectHelper;
import org.nakedobjects.application.collection.ListInternalCollection;
import org.nakedobjects.utility.NakedObjectRuntimeException;

public class PropertyHelper {

    public static final String MODIFIED_BY = "naked_modified_by";

    public static final String MODIFIED_ON = "naked_modified_on";

    public static final String NAKED_ACCESS = "naked_access";

    public static final String NAKED_PROPERTY = "naked_property";
}
