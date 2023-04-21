package com.acv.webapp.action.newsgroup;

import java.util.Map.Entry;
import org.apache.log4j.Logger;
import com.acv.dao.icontact.impl.IContactSubscriber;
import com.acv.service.i18n.model.CacheKey;
import com.acv.service.i18n.model.CountryProvinceMap;
import com.acv.service.i18n.model.I18nCacheableList;
import com.acv.service.icontact.IContactClient;
import com.acv.service.icontact.IContactException;
import com.acv.webapp.common.BaseAction;
import com.opensymphony.xwork2.ValidationAware;

/**
 * Action class for registration of user news group
 * @author Wu Ge
 */
public class UserNewsGroupAction extends BaseAction implements ValidationAware {

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(UserNewsGroupAction.class);

    private IContactSubscriber userNewsGroup;

    private IContactClient iContactClient;

    private boolean serviceAvailabilityFlag = true;

    /**
     * create entries for countrys
     */
    private Entry<CacheKey, String> _countryKey = null;

    private Entry<CacheKey, String> getCountryKey() {
        if (_countryKey == null) {
            for (Entry<CacheKey, String> pair : getCountryProvinceMap().keySet()) {
                if (pair.getKey().getId().toString().equals(userNewsGroup.getCountry())) {
                    _countryKey = pair;
                    break;
                }
            }
        }
        return _countryKey;
    }

    /**
	 * Initailise the action
	 * @return SUCCESS
	 */
    public String start() throws SecurityException, IllegalArgumentException {
        if (userNewsGroup == null) {
            userNewsGroup = new IContactSubscriber();
        }
        getPrePopulationManager().prePopulationDataMapping(userNewsGroup);
        if (userNewsGroup.getLanguage() == null) {
            userNewsGroup.setLanguage(getLocale().getLanguage());
        }
        return SUCCESS;
    }

    /**
	 * Send informations to Intelicontact for newsletter registration
	 *
	 */
    public String executeSend() throws Exception {
        if (userNewsGroup != null) {
            if (userNewsGroup.getLanguage().equals("en")) {
                userNewsGroup.setLanguage("english");
            } else if (userNewsGroup.getLanguage().equals("fr")) {
                userNewsGroup.setLanguage("french");
            }
            if (userNewsGroup.getProvince() != null && !"".equals(userNewsGroup.getProvince())) {
                I18nCacheableList provinces = (I18nCacheableList) getCountryProvinceMap().get(getCountryKey());
                userNewsGroup.setProvince(provinces.get(new Long(userNewsGroup.getProvince())));
            }
            userNewsGroup.setCountry(getCountryKey().getValue());
            try {
                iContactClient.subscribe(userNewsGroup);
            } catch (IContactException e) {
                log.error(e.getIContactCause().toString(), e);
                serviceAvailabilityFlag = false;
            }
            return SUCCESS;
        }
        return INPUT;
    }

    /** Validate the Province in user group
	 *  @return outcome
	 */
    public boolean validateProvince() {
        if (getCountryKey() != null) {
            I18nCacheableList provinces = (I18nCacheableList) getCountryProvinceMap().get(getCountryKey());
            if (provinces != null && !provinces.isEmpty()) {
                Long id = null;
                try {
                    id = new Long(userNewsGroup.getProvince());
                } catch (Exception e) {
                    return false;
                }
                return provinces.get(id) != null;
            } else {
                return true;
            }
        } else {
            log.error("The request contains an unknown country id: " + userNewsGroup.getCountry());
            return false;
        }
    }

    public IContactSubscriber getUserNewsGroup() {
        return userNewsGroup;
    }

    public void setCandidate(IContactSubscriber userNewsGroup) {
        this.userNewsGroup = userNewsGroup;
    }

    public I18nCacheableList getTitleList() {
        return getI18nRetriever().getTitleList(getLocale().getLanguage());
    }

    public I18nCacheableList getLanguageList() {
        return getI18nRetriever().getLanguageList(getLocale().getLanguage());
    }

    private CountryProvinceMap countryProvinceMap = null;

    public CountryProvinceMap getCountryProvinceMap() {
        if (countryProvinceMap == null) {
            countryProvinceMap = getI18nRetriever().getCountryProvinceMap(getLocale().getLanguage());
        }
        return countryProvinceMap;
    }

    public boolean isServiceAvailabilityFlag() {
        return serviceAvailabilityFlag;
    }

    public void setServiceAvailabilityFlag(boolean serviceAvailabilityFlag) {
        this.serviceAvailabilityFlag = serviceAvailabilityFlag;
    }

    public void setUserNewsGroup(IContactSubscriber userNewsGroup) {
        this.userNewsGroup = userNewsGroup;
    }

    public void setIContactClient(IContactClient contactClient) {
        iContactClient = contactClient;
    }
}
