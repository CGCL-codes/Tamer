package er.directtoweb.delegates;

import com.webobjects.appserver.WOComponent;
import com.webobjects.directtoweb.NextPageDelegate;

/**
 * NextPageDelegate that takes a given page name and when called creates and returns the given named page.<br />
 * 
 */
public class ERDPageNameDelegate implements NextPageDelegate {

    protected String _pageName;

    public ERDPageNameDelegate(String pageName) {
        _pageName = pageName;
    }

    public WOComponent nextPage(WOComponent sender) {
        return sender.pageWithName(_pageName);
    }
}
