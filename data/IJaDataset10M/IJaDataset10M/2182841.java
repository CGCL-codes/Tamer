package er.directtoweb;

import com.webobjects.foundation.*;
import com.webobjects.appserver.*;
import com.webobjects.eocontrol.*;
import com.webobjects.eoaccess.*;
import com.webobjects.directtoweb.*;

public class ERD2WQueryToOneRelationship extends D2WQueryToOneRelationship {

    public ERD2WQueryToOneRelationship(WOContext context) {
        super(context);
    }

    public Object restrictedChoiceList() {
        String restrictedChoiceKey = (String) d2wContext().valueForKey("restrictedChoiceKey");
        return restrictedChoiceKey != null && restrictedChoiceKey.length() > 0 ? valueForKeyPath(restrictedChoiceKey) : null;
    }
}
