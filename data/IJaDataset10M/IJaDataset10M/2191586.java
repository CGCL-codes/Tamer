package er.directtoweb;

import com.webobjects.foundation.*;
import com.webobjects.appserver.*;
import com.webobjects.eocontrol.*;
import com.webobjects.eoaccess.*;
import com.webobjects.directtoweb.*;

public class ERD2WEditToOneRelationship extends D2WEditToOneRelationship {

    public ERD2WEditToOneRelationship(WOContext context) {
        super(context);
    }

    public void validationFailedWithException(Throwable e, Object value, String keyPath) {
        parent().validationFailedWithException(e, value, keyPath);
    }

    public Object restrictedChoiceList() {
        String restrictedChoiceKey = (String) d2wContext().valueForKey("restrictedChoiceKey");
        return restrictedChoiceKey != null && restrictedChoiceKey.length() > 0 ? valueForKeyPath(restrictedChoiceKey) : null;
    }
}
