package er.directtoweb.qualifiers;

import com.webobjects.directtoweb.BooleanQualifier;
import com.webobjects.directtoweb.D2WContext;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eocontrol.EOKeyValueUnarchiver;

/**
 * Tests if an object is a kind of an entity.<br />
 * 
 */
public class ERDIsKindOfEntityQualifier extends BooleanQualifier {

    private String _entityName;

    private String _keyPath;

    public static Object decodeWithKeyValueUnarchiver(EOKeyValueUnarchiver u) {
        return new ERDIsKindOfEntityQualifier(u);
    }

    public ERDIsKindOfEntityQualifier(EOKeyValueUnarchiver u) {
        super(null);
        _entityName = (String) u.decodeObjectForKey("entityName");
        _keyPath = (String) u.decodeObjectForKey("keyPath");
    }

    public boolean evaluateWithObject(Object o) {
        D2WContext c = (D2WContext) o;
        EOEntity e = (EOEntity) c.valueForKeyPath(_keyPath);
        return isKindOfEntity(e);
    }

    public boolean isKindOfEntity(EOEntity e) {
        return e.name().equals(_entityName) ? true : (e.parentEntity() != null ? isKindOfEntity(e.parentEntity()) : false);
    }

    public String toString() {
        return _keyPath + " isKindOfEntity " + _entityName;
    }
}
