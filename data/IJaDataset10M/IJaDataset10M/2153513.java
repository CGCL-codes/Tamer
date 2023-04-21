package org.ikasan.console.security;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import org.apache.log4j.Logger;
import org.ikasan.console.pointtopointflow.PointToPointFlowProfile;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.AuthorizationServiceException;
import org.springframework.security.ConfigAttribute;
import org.springframework.security.ConfigAttributeDefinition;

/**
 * Class for determining access/configuration rights for collection of
 * configuration
 * 
 * @author Ikasan Development Team
 */
public class AfterInvocationPointToPointFlowProfileCollectionFilteringProvider extends AbstractPointToPointFlowProfileAfterInvocationProvider {

    /** AFTER_POINT_TO_POINT_FLOW_PROFILE_COLLECTION_READ */
    private static final String AFTER_POINT_TO_POINT_FLOW_PROFILE_COLLECTION_READ = "AFTER_POINT_TO_POINT_FLOW_PROFILE_COLLECTION_READ";

    /** Constructor */
    public AfterInvocationPointToPointFlowProfileCollectionFilteringProvider() {
        super(AFTER_POINT_TO_POINT_FLOW_PROFILE_COLLECTION_READ);
    }

    /** Logger for this class */
    Logger logger = Logger.getLogger(AfterInvocationModuleCollectionFilteringProvider.class);

    /**
     * Decide if the user has rights to invoke actions on a PointToPointFlowProfile
     * 
     * @param authentication - The authentication scheme
     * @param object - TODO Not used!
     * @param config - TODO The configuration attribute to check
     * @param returnedObject - TODO The return object to seed
     * @return TODO A list of authorised objects or
     * @throws AccessDeniedException - Access was denied
     */
    public Object decide(Authentication authentication, Object object, ConfigAttributeDefinition config, Object returnedObject) throws AccessDeniedException {
        Iterator<?> iter = config.getConfigAttributes().iterator();
        while (iter.hasNext()) {
            ConfigAttribute attr = (ConfigAttribute) iter.next();
            if (!this.supports(attr)) {
                continue;
            }
            if (!(returnedObject instanceof Collection<?>)) {
                throw new AuthorizationServiceException("A Collection was required as the " + "returnedObject, but the returnedObject was [" + returnedObject + "]");
            }
            Collection<PointToPointFlowProfile> authorisedPointToPointFlowProfiles = new LinkedHashSet<PointToPointFlowProfile>();
            Iterator<?> collectionIter = ((Collection<?>) returnedObject).iterator();
            while (collectionIter.hasNext()) {
                Object domainObject = collectionIter.next();
                if (domainObject == null || !PointToPointFlowProfile.class.isAssignableFrom(domainObject.getClass())) {
                    continue;
                }
                PointToPointFlowProfile thisPointToPointFlowProfile = (PointToPointFlowProfile) domainObject;
                if (mayReadPointToPointFlowProfile(authentication, thisPointToPointFlowProfile)) {
                    authorisedPointToPointFlowProfiles.add(thisPointToPointFlowProfile);
                }
            }
            return authorisedPointToPointFlowProfiles;
        }
        return returnedObject;
    }

    /**
     * Returns true if the configuration attribute is supported by this provider
     * 
     * @param configAttribute configuration attribute to test
     * @return true if the configuration attribute is supported by this provider
     */
    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return configAttribute.getAttribute().equalsIgnoreCase(AFTER_POINT_TO_POINT_FLOW_PROFILE_COLLECTION_READ);
    }
}
