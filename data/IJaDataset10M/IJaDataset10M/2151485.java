package org.acegisecurity.portlet.jboss.context;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

public class SecurityContextPopulator {

    private final Log logger = LogFactory.getLog(getClass());

    private SubjectToAuthenticationTransformer subjectToAuthenticationTransformer;

    public void populateSecurityContext() {
        Authentication authentication = subjectToAuthenticationTransformer.getAuthentication();
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            if (logger.isDebugEnabled()) {
                logger.debug("ContextHolder updated with Authentication from container: '" + authentication + "'");
            }
        } else {
            logger.debug("ContextHolder cannot be updated with Authentication from container it is null.");
        }
    }

    @Required
    public void setSubjectToAuthenticationTransformer(SubjectToAuthenticationTransformer subjectToAuthenticationTransformer) {
        this.subjectToAuthenticationTransformer = subjectToAuthenticationTransformer;
    }
}
