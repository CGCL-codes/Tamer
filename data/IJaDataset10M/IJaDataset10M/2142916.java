package it.ancona.comune.ankonhippo.workflows.ankonhippo;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import nl.hippo.cms.Constants;
import nl.hippo.cms.workflows.shared.URIUtil;
import nl.hippo.cocoon.webdav.Property;
import nl.hippo.cocoon.webdav.WebDAVHelper;
import org.apache.commons.httpclient.HttpState;

public class StoreModifierFunction extends VersionCreatingComponent {

    public StoreModifierFunction() {
        super();
    }

    public void executeImpl(final Map transientVars, Map args, PropertySet ps) throws WorkflowException {
        String location = URIUtil.getHTTPURI((String) transientVars.get(AHConstants.LOCATION));
        location = PathUtil.makeAbsolute(transientVars, location);
        HttpState httpState = (HttpState) transientVars.get(AHConstants.HTTPSTATE);
        String user = "";
        try {
            user = WebDAVHelper.propfindAsString(location, Constants.CMS_1_0_NAMESPACE, Constants.LAST_WORKFLOW_USER_PROPERTY_NAME, httpState);
        } catch (IOException e) {
        }
        try {
            Set propertiesToSet = new HashSet();
            propertiesToSet.add(new Property(AHConstants.H, Constants.CMS_1_0_NAMESPACE, "lastModifiedBy", user));
            String lockToken = WebDAVHelper.getLockToken(location, httpState);
            WebDAVHelper.proppatch(location, propertiesToSet, null, lockToken, httpState);
        } catch (IOException e) {
            throw new WorkflowException(e);
        }
    }
}
