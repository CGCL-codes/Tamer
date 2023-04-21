package net.sf.gilead.gwt.client;

import net.sf.gilead.pojo.gwt.IGwtSerializableParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Async interface for ProxyInformation remote service.
 * @author bruno.marchesson
 *
 */
public interface ProxyInformationServiceAsync {

    /**
	 * Indicates if a property was null or lazy loaded.
	 */
    void isInitialized(String className, IGwtSerializableParameter id, String propertyName, AsyncCallback<Boolean> callback);

    /**
	 * Update proxy information when a field (lazy loaded) is initialized from client side.
	 */
    void setInitialized(String className, IGwtSerializableParameter id, String propertyName, AsyncCallback<Void> callback);
}
