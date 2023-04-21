package er.bugtracker;

import com.webobjects.foundation.*;
import com.webobjects.appserver.*;
import com.webobjects.eocontrol.*;

public class PriorityComponent extends WOComponent {

    public PriorityComponent(WOContext aContext) {
        super(aContext);
    }

    public EOEnterpriseObject object;

    public String key;

    public String filename() {
        Number priority = (Number) object.valueForKeyPath("priority.sortOrder");
        String result = null;
        if (priority != null) {
            int c = priority.intValue();
            if (c == 2) return "gyrophare.gif"; else if (c == 3) return "doctor.gif"; else if (c == 4) return "bandaid.gif"; else if (c == 1) return "fire.gif";
        }
        return result;
    }
}
