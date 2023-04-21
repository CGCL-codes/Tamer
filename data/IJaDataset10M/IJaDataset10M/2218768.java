package net.sourceforge.symba.webapp.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by IntelliJ IDEA. User: craig Date: 23-Sep-2009 Time: 21:21:20 To change this template use File | Settings |
 * File Templates.
 */
public interface IdentifiableServiceAsync extends DescribableServiceAsync {

    void getEndurantRef(String identifierOrEndurant, final AsyncCallback<String> async);

    void getName(String identifierOrEndurant, final AsyncCallback<String> async);

    void getIdentifier(String identifierOrEndurant, final AsyncCallback<String> async);
}
