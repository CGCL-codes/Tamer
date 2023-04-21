package com.loribel.tools.web.bo.generated;

import com.loribel.commons.business.abstraction.GB_BOPrototype;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.tools.web.abstraction.GBW_BOSearch;
import com.loribel.tools.web.bo.GBW_BOSearchAbstract;
import com.loribel.tools.web.bo.GBW_CrowlSearchBO;

/**
 * Generated class for CrowlSearch.
 */
public abstract class GBW_CrowlSearchBOGen extends GBW_BOSearchAbstract implements GBW_BOSearch {

    public static final String BO_NAME = "CrowlSearch";

    private String request;

    protected GBW_CrowlSearchBOGen() {
        super(BO_NAME);
    }

    public static final GB_BOPrototype newBOPrototype() {
        GB_BOPrototype retour = new GB_BOPrototype() {

            public String getName() {
                return GBW_CrowlSearchBO.BO_NAME;
            }

            public GB_SimpleBusinessObject newBusinessObject() {
                return new GBW_CrowlSearchBO();
            }
        };
        return retour;
    }

    public final Object accept(GBW_BOVisitor a_visitor) {
        return a_visitor.visitCrowlSearch((GBW_CrowlSearchBO) this);
    }

    /**
     * Getter for idSelector.
     */
    public final com.loribel.commons.abstraction.GB_StringSelector getIdSelector() {
        com.loribel.commons.abstraction.GB_StringSelector retour = (com.loribel.commons.abstraction.GB_StringSelector) getPropertyValue(BO_PROPERTY.ID_SELECTOR);
        return retour;
    }

    /**
     * Setter for idSelector.
     */
    public final void setIdSelector(com.loribel.commons.abstraction.GB_StringSelector a_idSelector) {
        setPropertyValue(BO_PROPERTY.ID_SELECTOR, a_idSelector);
    }

    /**
     * Getter for urlSiteSelector.
     */
    public final com.loribel.commons.abstraction.GB_StringSelector getUrlSiteSelector() {
        com.loribel.commons.abstraction.GB_StringSelector retour = (com.loribel.commons.abstraction.GB_StringSelector) getPropertyValue(BO_PROPERTY.URL_SITE_SELECTOR);
        return retour;
    }

    /**
     * Setter for urlSiteSelector.
     */
    public final void setUrlSiteSelector(com.loribel.commons.abstraction.GB_StringSelector a_urlSiteSelector) {
        setPropertyValue(BO_PROPERTY.URL_SITE_SELECTOR, a_urlSiteSelector);
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String a_request) {
        request = a_request;
    }

    /**
     * Property names of this Business Object.
     */
    public static final class BO_PROPERTY {

        public static final String ID_SELECTOR = "idSelector";

        public static final String URL_SITE_SELECTOR = "urlSiteSelector";
    }
}
