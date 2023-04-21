package er.extensions;

import com.webobjects.foundation.*;
import com.webobjects.appserver.*;
import com.webobjects.eocontrol.*;
import com.webobjects.eoaccess.*;
import com.webobjects.directtoweb.*;
import org.apache.log4j.Category;

public class ERXDebugMarker extends WOComponent {

    public static final Category cat = Category.getInstance(ERXDebugMarker.class);

    public ERXDebugMarker(WOContext aContext) {
        super(aContext);
    }

    public boolean isStateless() {
        return true;
    }

    public boolean synchronizesVariablesWithBindings() {
        return false;
    }

    private Object _object;

    public Object object() {
        if (_object == null) {
            _object = valueForBinding("object");
        }
        return _object;
    }

    public void reset() {
        super.reset();
        _object = null;
    }

    public boolean disabled() {
        return object() == null;
    }

    public WOComponent debug() {
        WOComponent result = null;
        if (cat.isDebugEnabled()) cat.debug("Object = " + object());
        if (object() instanceof EOEditingContext) {
            result = pageWithName("ERXEditingContextInspector");
            result.takeValueForKey(object(), "object");
        } else if (object() instanceof EOEnterpriseObject) {
            result = debugPageForObject((EOEnterpriseObject) object(), session());
            result.takeValueForKey(object(), "object");
        }
        return result;
    }

    public static WOComponent debugPageForObject(EOEnterpriseObject o, WOSession s) {
        return (WOComponent) D2W.factory().inspectPageForEntityNamed(o.entityName(), s);
    }
}
