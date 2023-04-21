package com.google.solarchallenge.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;

/**
 * Async interface for {@link OAuthProviderService}.
 *
 * @author Arjun Satyapal
 */
public interface OAuthProviderServiceAsync {

    void getOAuthProviderList(AsyncCallback<ArrayList<String>> callback);
}
