package br.com.arsmachina.tapestrysecurity.internal;

import static br.com.arsmachina.tapestrysecurity.TapestrySecurityConstants.NEEDS_LOGGED_IN_USER_METADATA_KEY;
import static br.com.arsmachina.tapestrysecurity.TapestrySecurityConstants.NEEDS_PERMISSION_METADATA_KEY;
import java.io.IOException;
import org.apache.tapestry5.internal.services.ComponentModelSource;
import org.apache.tapestry5.internal.services.RequestConstants;
import org.apache.tapestry5.model.ComponentModel;
import org.apache.tapestry5.services.ComponentEventLinkEncoder;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.Dispatcher;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import br.com.arsmachina.authorization.Authorizer;
import br.com.arsmachina.authorization.annotation.NeedsLoggedInUser;
import br.com.arsmachina.authorization.annotation.NeedsPermission;

/**
 * Dispatcher that handles the {@link NeedsLoggedInUser} and {@link NeedsPermission} annotations in
 * page classes.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class TapestrySecurityDispatcher implements Dispatcher {

    private final ComponentEventLinkEncoder linkEncoder;

    private final Authorizer authorizer;

    private final ComponentModelSource componentModelSource;

    /**
	 * Single constructor of this class.
	 * 
	 * @param authorizer an {@link Authorizer}. It cannot be null.
	 * @param linkEncoder a {@link ComponentEventLinkEncoder}. It cannot be null.
	 * @param a {@link ComponentModelSource}. It cannot be null.
	 */
    public TapestrySecurityDispatcher(Authorizer authorizer, ComponentEventLinkEncoder linkEncoder, ComponentModelSource componentModelSource) {
        if (authorizer == null) {
            throw new IllegalArgumentException("Parameter authorizer cannot be null");
        }
        if (linkEncoder == null) {
            throw new IllegalArgumentException("Parameter linkEncoder cannot be null");
        }
        if (componentModelSource == null) {
            throw new IllegalArgumentException("Parameter componentModelSource cannot be null");
        }
        this.authorizer = authorizer;
        this.linkEncoder = linkEncoder;
        this.componentModelSource = componentModelSource;
    }

    public boolean dispatch(Request request, Response response) throws IOException {
        boolean requestHandled = false;
        final String path = request.getPath();
        if (path.startsWith(RequestConstants.ASSET_PATH_PREFIX)) {
            return false;
        }
        String pageName = extractPageName(request);
        if (pageName != null) {
            final ComponentModel pageModel = componentModelSource.getPageModel(pageName);
            final String loggedInMetaValue = pageModel.getMeta(NEEDS_LOGGED_IN_USER_METADATA_KEY);
            if (loggedInMetaValue != null) {
                authorizer.checkLoggedIn();
            }
            final String permissionsMetaValue = pageModel.getMeta(NEEDS_PERMISSION_METADATA_KEY);
            if (permissionsMetaValue != null) {
                final String[] permissionNames = permissionsMetaValue.split(",");
                authorizer.checkPermissions(permissionNames);
            }
        }
        return requestHandled;
    }

    private String extractPageName(Request request) {
        String pageName = null;
        final ComponentEventRequestParameters componentEventParameters = linkEncoder.decodeComponentEventRequest(request);
        if (componentEventParameters != null) {
            pageName = componentEventParameters.getContainingPageName();
        }
        if (pageName == null) {
            final PageRenderRequestParameters pageRenderParameters = linkEncoder.decodePageRenderRequest(request);
            if (pageRenderParameters != null) {
                pageName = pageRenderParameters.getLogicalPageName();
            }
        }
        return pageName;
    }
}
