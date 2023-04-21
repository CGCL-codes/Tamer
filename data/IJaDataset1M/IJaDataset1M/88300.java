package com.hack23.cia.model.application.dto;

import gnu.trove.THashMap;
import java.util.Map;
import com.hack23.cia.model.application.impl.common.Agency;
import com.hack23.cia.model.application.impl.common.LanguageContent;
import com.hack23.cia.model.application.impl.common.Portal;
import com.hack23.cia.model.application.impl.common.UserSession;
import com.hack23.cia.model.sweden.impl.Vote.Position;

/**
 * The Class UserSessionDTO.
 */
public class UserSessionDTO {

    /** The agency language content values. */
    private final Map<Agency.LanguageContentKey, String> agencyLanguageContentValues = new THashMap<Agency.LanguageContentKey, String>();

    /** The portal language content values. */
    private final Map<Portal.LanguageContentKey, String> portalLanguageContentValues = new THashMap<Portal.LanguageContentKey, String>();

    /** The user session. */
    private final UserSession userSession;

    /**
	 * Instantiates a new user session dto.
	 * 
	 * @param userSession
	 *            the user session
	 */
    public UserSessionDTO(final UserSession userSession) {
        super();
        this.userSession = userSession;
        if (userSession != null) {
            if (userSession.getPortal() != null) {
                for (LanguageContent languageContent : userSession.getPortal().getLanguageContentByLanguage(userSession.getLanguage())) {
                    portalLanguageContentValues.put(Portal.LanguageContentKey.valueOf(languageContent.getContentPropertyName()), languageContent.getContent());
                }
                if (userSession.getPortal().getAgency() != null) {
                    for (LanguageContent languageContent : userSession.getPortal().getAgency().getLanguageContentByLanguage(userSession.getLanguage())) {
                        agencyLanguageContentValues.put(Agency.LanguageContentKey.valueOf(languageContent.getContentPropertyName()), languageContent.getContent());
                    }
                }
            }
        }
    }

    /**
	 * Gets the language resource.
	 * 
	 * @param key
	 *            the key
	 * 
	 * @return the language resource
	 */
    public final String getLanguageResource(final Agency.LanguageContentKey key) {
        String string = agencyLanguageContentValues.get(key);
        if (string == null) {
            string = "Missing resource=" + key;
        }
        return string;
    }

    /**
	 * Gets the language resource.
	 * 
	 * @param key
	 *            the key
	 * 
	 * @return the language resource
	 */
    public final String getLanguageResource(final Portal.LanguageContentKey key) {
        String string = portalLanguageContentValues.get(key);
        if (string == null) {
            string = "Missing resource=" + key;
        }
        return string;
    }

    /**
	 * Gets the language resource.
	 * 
	 * @param position
	 *            the position
	 * 
	 * @return the language resource
	 */
    public final String getLanguageResource(final Position position) {
        Agency.LanguageContentKey key = Agency.LanguageContentKey.valueOf(position.toString().toUpperCase());
        return getLanguageResource(key);
    }

    /**
	 * Gets the user session.
	 * 
	 * @return the user session
	 */
    public final UserSession getUserSession() {
        return userSession;
    }
}
