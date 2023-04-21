package org.testing.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 *
 * @author Praca
 */
@RemoteServiceRelativePath("GWT.rpc")
public interface SampleGWTService extends RemoteService {

    public String myMethod(String s) throws CustomSerializableException;
}
