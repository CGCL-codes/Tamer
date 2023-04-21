package fr.cnes.sitools.service.storage.proxy;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Reference;
import fr.cnes.sitools.common.application.SitoolsApplication;

/**
 * Application for managing directories for data storage in Sitools.
 * 
 * @author jp.boignard
 */
public class StorageApplication extends SitoolsApplication {

    /**
   * Constructor
   * 
   * @param context
   *          the restlet context must contain a <code>DataStorageStore</code> attribute for APP_STORE key
   */
    public StorageApplication(Context context) {
        super(context);
    }

    @Override
    public void sitoolsDescribe() {
        setName("StorageApplicationProxy");
        setDescription("Administration of Storage service");
    }

    @Override
    public void handle(Request request, Response response) {
        super.handle(request, response);
        String path = request.getResourceRef().getRemainingPart();
        Reference proxyRef = new Reference("http://localhost:8183/sitools/datastorage" + path);
        Request proxyRequest = new Request(request.getMethod(), proxyRef, request.getEntity());
        proxyRequest.setAttributes(request.getAttributes());
        proxyRequest.setClientInfo(request.getClientInfo());
        Response proxyResp = getContext().getClientDispatcher().handle(proxyRequest);
        response.setStatus(proxyResp.getStatus());
        response.setEntity(proxyResp.getEntity());
    }
}
