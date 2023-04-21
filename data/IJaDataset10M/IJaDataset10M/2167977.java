package er.selenium;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOCookie;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.NSTimestamp;

public class SeleniumDefaultSetupActions {

    public static void resetSession(WOResponse response, WOContext context) {
        WOCookie dummyCookie = new WOCookie(NSBundle.mainBundle().name() + "L", "dummy");
        dummyCookie.setPath("/");
        dummyCookie.setDomain(null);
        dummyCookie.setExpires(new NSTimestamp().timestampByAddingGregorianUnits(0, -2, 0, 0, 0, 0));
        response.addCookie(dummyCookie);
        context.session().terminate();
    }
}
