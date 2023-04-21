package er.directtoweb.components.strings;

import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.D2WDisplayString;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSPropertyListSerialization;
import java.util.regex.Pattern;

/**
 * Displays string representation of object inside of "pre" tags.
 * keyWhenRelationship is respected if the object is an EOEnterpriseObject.
 * NSDictionary and NSArray objects are displayed using NSPropertyListSerialization.
 */
public class ERD2WDisplayPreformattedString extends D2WDisplayString {

    public ERD2WDisplayPreformattedString(WOContext context) {
        super(context);
    }

    public String stringContents() {
        Object object = objectPropertyValue();
        String result = null;
        String keyWhenRelationship = null;
        if ((object instanceof EOEnterpriseObject) && (keyWhenRelationship = keyWhenRelationship()) != null) {
            object = ((EOEnterpriseObject) object).valueForKeyPath(keyWhenRelationship);
        }
        if ((object instanceof NSDictionary) || (object instanceof NSArray)) {
            result = NSPropertyListSerialization.stringFromPropertyList(object);
            if (result != null) {
                result = Pattern.compile("\t").matcher(result).replaceAll("    ");
            }
        } else if (object != null) {
            result = object.toString();
        }
        return result;
    }
}
