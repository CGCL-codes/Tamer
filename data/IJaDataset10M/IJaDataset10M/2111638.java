package com.loribel.tools.web.bo.generated;

import com.loribel.commons.abstraction.GB_Prototype;
import com.loribel.commons.business.abstraction.GB_BOData;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.tools.web.abstraction.GBW_BOData;
import com.loribel.tools.web.bo.GBW_SiteInfoShortBO;
import com.loribel.tools.web.bo.GBW_SiteInfoShortData;

public abstract class GBW_SiteInfoShortDataGen implements GBW_BOData, java.io.Serializable, GB_BOData {

    public static final String BO_NAME = "SiteInfoShort";

    private boolean deleted;

    private String id;

    private String urlSite;

    public String boName() {
        return BO_NAME;
    }

    public static final GB_Prototype newPrototype() {
        GB_Prototype retour = new GB_Prototype() {

            public Object newInstance() {
                return new GBW_SiteInfoShortData();
            }
        };
        return retour;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean a_deleted) {
        deleted = a_deleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String a_id) {
        id = a_id;
    }

    public String getUrlSite() {
        return urlSite;
    }

    public void setUrlSite(String a_urlSite) {
        urlSite = a_urlSite;
    }

    public void updateFromBO(GB_SimpleBusinessObject a_bo) {
        GBW_SiteInfoShortBO l_bo = (GBW_SiteInfoShortBO) a_bo;
        setDeleted(l_bo.isDeleted());
        setId(l_bo.getId());
        setUrlSite(l_bo.getUrlSite());
    }

    public void updateBO(GB_SimpleBusinessObject a_bo) {
        GBW_SiteInfoShortBO l_bo = (GBW_SiteInfoShortBO) a_bo;
        l_bo.setDeleted(this.isDeleted());
        l_bo.setId(this.getId());
        l_bo.setUrlSite(this.getUrlSite());
    }
}
