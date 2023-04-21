package com.loribel.tools.sa.bo.generated;

import com.loribel.commons.abstraction.GB_Prototype;
import com.loribel.commons.business.abstraction.GB_BOData;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.tools.sa.bo.GB_SAHtmlSiteMap2CsvBO;
import com.loribel.tools.sa.bo.GB_SAHtmlSiteMap2CsvData;

public abstract class GB_SAHtmlSiteMap2CsvDataGen implements GB_BOData, java.io.Serializable {

    public static final String BO_NAME = "SAHtmlSiteMap2Csv";

    private boolean indentLabel;

    private int maxIndent;

    private boolean useAbsoluteLink;

    private String url;

    public String boName() {
        return BO_NAME;
    }

    public static final GB_Prototype newPrototype() {
        GB_Prototype retour = new GB_Prototype() {

            public Object newInstance() {
                return new GB_SAHtmlSiteMap2CsvData();
            }
        };
        return retour;
    }

    public boolean isIndentLabel() {
        return indentLabel;
    }

    public void setIndentLabel(boolean a_indentLabel) {
        indentLabel = a_indentLabel;
    }

    public int getMaxIndent() {
        return maxIndent;
    }

    public void setMaxIndent(int a_maxIndent) {
        maxIndent = a_maxIndent;
    }

    public boolean isUseAbsoluteLink() {
        return useAbsoluteLink;
    }

    public void setUseAbsoluteLink(boolean a_useAbsoluteLink) {
        useAbsoluteLink = a_useAbsoluteLink;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String a_url) {
        url = a_url;
    }

    public void updateFromBO(GB_SimpleBusinessObject a_bo) {
        GB_SAHtmlSiteMap2CsvBO l_bo = (GB_SAHtmlSiteMap2CsvBO) a_bo;
        setIndentLabel(l_bo.isIndentLabel());
        setMaxIndent(l_bo.getMaxIndent());
        setUseAbsoluteLink(l_bo.isUseAbsoluteLink());
        setUrl(l_bo.getUrl());
    }

    public void updateBO(GB_SimpleBusinessObject a_bo) {
        GB_SAHtmlSiteMap2CsvBO l_bo = (GB_SAHtmlSiteMap2CsvBO) a_bo;
        l_bo.setIndentLabel(this.isIndentLabel());
        l_bo.setMaxIndent(this.getMaxIndent());
        l_bo.setUseAbsoluteLink(this.isUseAbsoluteLink());
        l_bo.setUrl(this.getUrl());
    }
}
