package er.extensions;

import com.webobjects.foundation.*;
import com.webobjects.appserver.*;

public class ERXJSOpenWindowSubmitButton extends ERXJSOpenWindowHyperlink {

    public ERXJSOpenWindowSubmitButton(WOContext context) {
        super(context);
    }

    protected String _contextComponentActionURL;

    public String pushURL() {
        _contextComponentActionURL = context().componentActionURL();
        return null;
    }

    public String contextComponentActionURL() {
        return _contextComponentActionURL;
    }
}
