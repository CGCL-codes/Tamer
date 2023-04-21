package com.planetachewood.gwt.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author <a href="mailto:mark.a.allen@gmail.com">Mark Allen</a>
 * @since 1.0
 * @since Mar 9, 2006
 * @version $Revision: 24 $ $Date: 2007-03-11 23:16:29 -0400 (Sun, 11 Mar 2007) $
 */
public interface GwtSearchServiceAsync {

    void searchStrips(String query, int startIndex, AsyncCallback asyncCallback);

    void reindex(AsyncCallback asyncCallback);
}
