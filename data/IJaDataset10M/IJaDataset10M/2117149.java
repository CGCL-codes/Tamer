package er.directtoweb;

import com.webobjects.foundation.*;
import com.webobjects.eocontrol.*;
import com.webobjects.eoaccess.*;
import com.webobjects.directtoweb.*;
import com.webobjects.directtoweb.ERD2WUtilities;
import er.extensions.*;

public class ERDDefaultPropertyNameAssignment extends ERDAssignment {

    public static Object decodeWithKeyValueUnarchiver(EOKeyValueUnarchiver eokeyvalueunarchiver) {
        return new ERDDefaultPropertyNameAssignment(eokeyvalueunarchiver);
    }

    public ERDDefaultPropertyNameAssignment(EOKeyValueUnarchiver u) {
        super(u);
    }

    public ERDDefaultPropertyNameAssignment(String key, Object value) {
        super(key, value);
    }

    public static final NSArray _DEPENDENT_KEYS = new NSArray(new String[] { "propertyKey" });

    public NSArray dependentKeys(String keyPath) {
        return _DEPENDENT_KEYS;
    }

    public Object displayNameForProperty(D2WContext c) {
        return ERD2WUtilities.displayNameForKey((String) c.valueForKey("propertyKey"));
    }
}
