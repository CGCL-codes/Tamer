package it.infodea.tapestrydea.services.jcr.assets;

import java.util.Date;
import it.infodea.tapestrydea.services.jcr.JcrSessionProviderService;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.internal.services.RequestPathOptimizer;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.services.AssetFactory;
import org.apache.tapestry5.services.Request;

/**
 * Factory per la creazione degli assets di tipo repository
 * @author bobpuley
 *
 */
@Deprecated
public class RepositoryAssetFactory implements AssetFactory {

    private final JcrSessionProviderService jcrSessionProvider;

    private final Request request;

    private final RequestPathOptimizer optimizer;

    public RepositoryAssetFactory(JcrSessionProviderService jcrSessionProvider, Request request, RequestPathOptimizer optimizer) {
        this.jcrSessionProvider = jcrSessionProvider;
        this.request = request;
        this.optimizer = optimizer;
    }

    private String clientURL(Resource resource) {
        String clientURL = null;
        if (clientURL == null) {
            clientURL = buildClientURL(resource);
        }
        return clientURL;
    }

    private String buildClientURL(Resource resource) {
        String path = request.getContextPath() + "/" + resource.toString() + "?time=" + new Date();
        path = optimizer.optimizePath(path);
        return path;
    }

    public Asset createAsset(final Resource resource) {
        return new Asset() {

            public Resource getResource() {
                return resource;
            }

            public String toClientURL() {
                return clientURL(resource);
            }

            @Override
            public String toString() {
                return toClientURL();
            }
        };
    }

    public Resource getRootResource() {
        return new RepositoryResource(jcrSessionProvider, "/");
    }
}
