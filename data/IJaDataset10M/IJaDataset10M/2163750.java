package org.pachyderm.apollo.app;

import com.webobjects.appserver.WOComponent;
import com.webobjects.directtoweb.NextPageDelegate;

/**
 * This interface is implemented by pages that act as inspect pages ('task' is inspect). 
 * Inspect pages allow users to view properties of an object.
 */
public interface InspectPageInterface {

    public void setNextPage(WOComponent nextPage);

    public void setNextPageDelegate(NextPageDelegate delegate);

    public void setObject(Object object);

    public Object getObject();
}
