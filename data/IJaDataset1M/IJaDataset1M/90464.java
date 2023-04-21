package com.loribel.tools.web.bo.generated;

import com.loribel.commons.abstraction.GB_Prototype;
import com.loribel.commons.business.abstraction.GB_BOData;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.tools.web.abstraction.GBW_BOData;
import com.loribel.tools.web.bo.GBW_GoogleSearchResultShortBO;
import com.loribel.tools.web.bo.GBW_GoogleSearchResultShortData;

public abstract class GBW_GoogleSearchResultShortDataGen implements GBW_BOData, java.io.Serializable, GB_BOData {

    public static final String BO_NAME = "GoogleSearchResultShort";

    private String id;

    private java.util.Date date;

    private String q;

    private String countryCode;

    private String countryFilter;

    private String langue;

    private String options;

    private int max;

    private String url;

    private boolean deleted;

    public String boName() {
        return BO_NAME;
    }

    public static final GB_Prototype newPrototype() {
        GB_Prototype retour = new GB_Prototype() {

            public Object newInstance() {
                return new GBW_GoogleSearchResultShortData();
            }
        };
        return retour;
    }

    public String getId() {
        return id;
    }

    public void setId(String a_id) {
        id = a_id;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date a_date) {
        date = a_date;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String a_q) {
        q = a_q;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String a_countryCode) {
        countryCode = a_countryCode;
    }

    public String getCountryFilter() {
        return countryFilter;
    }

    public void setCountryFilter(String a_countryFilter) {
        countryFilter = a_countryFilter;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String a_langue) {
        langue = a_langue;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String a_options) {
        options = a_options;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int a_max) {
        max = a_max;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String a_url) {
        url = a_url;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean a_deleted) {
        deleted = a_deleted;
    }

    public void updateFromBO(GB_SimpleBusinessObject a_bo) {
        GBW_GoogleSearchResultShortBO l_bo = (GBW_GoogleSearchResultShortBO) a_bo;
        setId(l_bo.getId());
        setDate(l_bo.getDate());
        setQ(l_bo.getQ());
        setCountryCode(l_bo.getCountryCode());
        setCountryFilter(l_bo.getCountryFilter());
        setLangue(l_bo.getLangue());
        setOptions(l_bo.getOptions());
        setMax(l_bo.getMax());
        setUrl(l_bo.getUrl());
        setDeleted(l_bo.isDeleted());
    }

    public void updateBO(GB_SimpleBusinessObject a_bo) {
        GBW_GoogleSearchResultShortBO l_bo = (GBW_GoogleSearchResultShortBO) a_bo;
        l_bo.setId(this.getId());
        l_bo.setDate(this.getDate());
        l_bo.setQ(this.getQ());
        l_bo.setCountryCode(this.getCountryCode());
        l_bo.setCountryFilter(this.getCountryFilter());
        l_bo.setLangue(this.getLangue());
        l_bo.setOptions(this.getOptions());
        l_bo.setMax(this.getMax());
        l_bo.setUrl(this.getUrl());
        l_bo.setDeleted(this.isDeleted());
    }
}
