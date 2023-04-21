package com.acv.webapp.action.account.admin.agencies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.acv.dao.profiles.model.UserProfile;
import com.acv.dao.security.model.TravelAgency;
import com.acv.service.common.exception.ObjectNotFoundException;
import com.acv.service.profile.UserProfileManager;
import com.acv.service.security.AudienceManager;
import com.acv.webapp.common.BaseAction;

/**
 * Action class to provide the detail info of agency
 * @author Wu Ge
 */
public class DetailAgencyAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    private String idAgency;

    private TravelAgency travelAgency;

    private transient AudienceManager audienceManager;

    private transient UserProfileManager userProfileManager;

    private Map<String, Object> searchParameters = new HashMap<String, Object>();

    private List<UserProfile> agents = new ArrayList<UserProfile>();

    private static final Logger log = Logger.getLogger(DetailAgencyAction.class);

    /**
	 *
	 * Begin the detail info agency Action.
	 *
	 * @return CANCEL to return to the page that call this action.
	 * @return SUCCESS to display the detail info of agency.
	 */
    public String start() {
        if (log.isDebugEnabled()) log.debug("DetailAgencyAction start() idAgency" + idAgency);
        try {
            travelAgency = (TravelAgency) audienceManager.getAudienceById(idAgency);
        } catch (ObjectNotFoundException e) {
            log.error("Unable to find travel agency id : " + idAgency, e);
            addFieldError("errorMessage", getText("errors.userNotFound", new String[] { getText("admin.agenices.createAgencyAdmin.section.agency.agency") }));
            return CANCEL;
        }
        searchParameters.put("audience", travelAgency);
        agents = userProfileManager.search(searchParameters, null);
        return SUCCESS;
    }

    public TravelAgency getTravelAgency() {
        return travelAgency;
    }

    public void setTravelAgency(TravelAgency travelAgency) {
        this.travelAgency = travelAgency;
    }

    public void setAudienceManager(AudienceManager audienceManager) {
        this.audienceManager = audienceManager;
    }

    public UserProfileManager getUserProfileManager() {
        return userProfileManager;
    }

    public void setUserProfileManager(UserProfileManager userProfileManager) {
        this.userProfileManager = userProfileManager;
    }

    public List<UserProfile> getAgents() {
        return agents;
    }

    public String getIdAgency() {
        return idAgency;
    }

    public void setIdAgency(String idAgency) {
        this.idAgency = idAgency;
    }
}
