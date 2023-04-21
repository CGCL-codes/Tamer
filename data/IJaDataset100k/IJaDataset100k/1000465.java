package org.encuestame.core.service;

import java.util.List;
import org.encuestame.persistence.exception.EnMeNoResultsFoundException;
import org.encuestame.utils.json.SocialAccountBean;
import org.encuestame.utils.social.SocialProvider;

/**
 * Social Factory.
 * @author Picado, Juan juanATencuestame.org
 * @since Mar 8, 2011
 */
@Deprecated
public interface SocialOperations {

    /**
      * Get User Logged Verified Twitter Accounts.
      * @param username username
      * @param socialProvider
      * @return
      * @throws EnMeNoResultsFoundException
      */
    List<SocialAccountBean> getUserLoggedVerifiedTwitterAccount(final String username, final SocialProvider socialProvider) throws EnMeNoResultsFoundException;
}
