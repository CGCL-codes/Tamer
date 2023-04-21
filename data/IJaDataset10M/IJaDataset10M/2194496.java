package er.directtoweb;

import com.webobjects.foundation.*;
import com.webobjects.eocontrol.*;
import com.webobjects.eoaccess.*;
import com.webobjects.appserver.*;
import com.webobjects.directtoweb.*;
import org.apache.log4j.Category;
import er.extensions.ERXUtilities;
import java.util.Enumeration;

public class ERDEntityAssignment extends Assignment implements ERDComputingAssignmentInterface {

    public static Object decodeWithKeyValueUnarchiver(EOKeyValueUnarchiver eokeyvalueunarchiver) {
        return new ERDEntityAssignment(eokeyvalueunarchiver);
    }

    public static final Category cat = Category.getInstance("er.directtoweb.rules.ERDefaultEntityAssignment");

    public ERDEntityAssignment(EOKeyValueUnarchiver u) {
        super(u);
    }

    public ERDEntityAssignment(String key, Object value) {
        super(key, value);
    }

    public static final NSArray _DEPENDENT_KEYS = new NSArray(new String[] { "pageConfiguration" });

    public NSArray dependentKeys(String keyPath) {
        return _DEPENDENT_KEYS;
    }

    protected NSArray entityNames = null;

    public Object fire(D2WContext c) {
        Object result = null;
        if (value(c) != null && value(c) instanceof String && ((String) value(c)).length() > 0) {
            result = ERXUtilities.caseInsensitiveEntityNamed(((String) value(c)).toLowerCase());
        }
        if (result == null && c.valueForKey("pageConfiguration") != null) {
            String lowerCasePageConfiguration = ((String) c.valueForKey("pageConfiguration")).toLowerCase();
            if (entityNames == null) {
                entityNames = (NSArray) ((NSArray) ERXUtilities.entitiesForModelGroup(EOModelGroup.defaultGroup()).valueForKey("name")).valueForKey("toLowerCase");
            }
            NSMutableArray possibleEntities = new NSMutableArray();
            for (Enumeration e = entityNames.objectEnumerator(); e.hasMoreElements(); ) {
                String lowercaseEntityName = (String) e.nextElement();
                if (lowerCasePageConfiguration.indexOf(lowercaseEntityName) != -1) possibleEntities.addObject(lowercaseEntityName);
            }
            if (possibleEntities.count() == 1) {
                result = ERXUtilities.caseInsensitiveEntityNamed((String) possibleEntities.lastObject());
            } else if (possibleEntities.count() > 1) {
                ERXUtilities.sortEOsUsingSingleKey(possibleEntities, "length");
                if (((String) possibleEntities.objectAtIndex(0)).length() == ((String) possibleEntities.objectAtIndex(1)).length()) cat.warn("Found multiple entities of the same length for pageConfiguration: " + c.valueForKey("pageConfiguration") + " possible entities: " + possibleEntities);
                result = ERXUtilities.caseInsensitiveEntityNamed((String) possibleEntities.objectAtIndex(0));
            }
            if (cat.isDebugEnabled()) cat.debug("Found possible entities: " + possibleEntities + " for pageConfiguration: " + c.valueForKey("pageConfiguration") + " result: " + result);
            return result;
        }
        return result;
    }
}
