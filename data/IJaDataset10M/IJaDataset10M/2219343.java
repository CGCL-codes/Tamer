package er.directtoweb.components.dates._ajax;

import com.webobjects.appserver.*;
import er.directtoweb.components.dates.ERD2WQueryDateRange;

public class ERD2WAjaxQueryDateRange extends ERD2WQueryDateRange {

    public ERD2WAjaxQueryDateRange(WOContext context) {
        super(context);
    }

    public String minName() {
        return d2wContext().valueForKey("name") + "_min";
    }

    public String maxName() {
        return d2wContext().valueForKey("name") + "_max";
    }
}
