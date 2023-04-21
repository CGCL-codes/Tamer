package com.google.resting.method.delete;

import java.util.List;
import org.apache.http.Header;
import com.google.resting.component.EncodingTypes;
import com.google.resting.component.RequestParams;
import com.google.resting.component.ServiceContext;
import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.component.impl.URLContext;
import com.google.resting.serviceaccessor.impl.ServiceAccessor;

/**
 * Helper class for HTTP DELETE operation
 * 
 * @author sujata.de
 * @since resting 0.2
 *
 */
public class DeleteHelper {

    public static final ServiceResponse delete(String url, int port, RequestParams requestParams, EncodingTypes encoding) {
        return delete(url, port, requestParams, encoding, null);
    }

    public static final ServiceResponse delete(String url, int port, RequestParams requestParams, EncodingTypes encoding, List<Header> inputHeaders) {
        URLContext urlContext = new URLContext(url, port);
        ServiceContext serviceContext = new DeleteServiceContext(urlContext, requestParams, encoding, inputHeaders);
        return ServiceAccessor.access(serviceContext);
    }
}
