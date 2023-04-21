package com.loribel.tools.sa.bo.generated;

import com.loribel.commons.abstraction.GB_Prototype;
import com.loribel.commons.business.abstraction.GB_BOData;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.tools.sa.bo.GB_SATxt2HtmlBO;
import com.loribel.tools.sa.bo.GB_SATxt2HtmlData;

public abstract class GB_SATxt2HtmlDataGen implements GB_BOData, java.io.Serializable {

    public static final String BO_NAME = "SATxt2Html";

    private boolean useLi;

    private boolean useLink;

    private int indent;

    public String boName() {
        return BO_NAME;
    }

    public static final GB_Prototype newPrototype() {
        GB_Prototype retour = new GB_Prototype() {

            public Object newInstance() {
                return new GB_SATxt2HtmlData();
            }
        };
        return retour;
    }

    public boolean isUseLi() {
        return useLi;
    }

    public void setUseLi(boolean a_useLi) {
        useLi = a_useLi;
    }

    public boolean isUseLink() {
        return useLink;
    }

    public void setUseLink(boolean a_useLink) {
        useLink = a_useLink;
    }

    public int getIndent() {
        return indent;
    }

    public void setIndent(int a_indent) {
        indent = a_indent;
    }

    public void updateFromBO(GB_SimpleBusinessObject a_bo) {
        GB_SATxt2HtmlBO l_bo = (GB_SATxt2HtmlBO) a_bo;
        setUseLi(l_bo.isUseLi());
        setUseLink(l_bo.isUseLink());
        setIndent(l_bo.getIndent());
    }

    public void updateBO(GB_SimpleBusinessObject a_bo) {
        GB_SATxt2HtmlBO l_bo = (GB_SATxt2HtmlBO) a_bo;
        l_bo.setUseLi(this.isUseLi());
        l_bo.setUseLink(this.isUseLink());
        l_bo.setIndent(this.getIndent());
    }
}
