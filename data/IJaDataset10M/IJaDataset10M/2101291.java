package org.identifylife.key.engine.core.service.keys.remote;

import javax.ws.rs.core.MediaType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.identifylife.key.engine.core.model.keys.Key;
import org.identifylife.key.engine.core.service.keys.KeyService;
import org.springframework.stereotype.Service;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * @author dbarnier
 *
 */
@Service("remoteKeyService")
public class RemoteKeyService implements KeyService {

    private static final Log logger = LogFactory.getLog(RemoteKeyService.class);

    private static final String ENDPOINT = "http://idlife02.identifylife.org:8080/key-store/ws/key/";

    private static final MediaType XML_MEDIA_TYPE = new MediaType("application", "xml");

    private WebResource resource;

    public RemoteKeyService() throws Exception {
        this(ENDPOINT);
    }

    public RemoteKeyService(String endPoint) throws Exception {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        resource = client.resource(endPoint);
    }

    @Override
    public Key getByUuid(String uuid) {
        try {
            Key key = resource.path(uuid + "/").accept(XML_MEDIA_TYPE).get(Key.class);
            logger.info("key: " + key);
            return key;
        } catch (UniformInterfaceException e) {
            logger.error("exception: " + e.getMessage(), e);
        }
        return null;
    }
}
