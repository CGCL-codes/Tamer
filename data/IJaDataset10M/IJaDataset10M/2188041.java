package com.loribel.tools.sa.bo.generated;

import com.loribel.commons.abstraction.GB_Prototype;
import com.loribel.commons.business.abstraction.GB_BOData;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.tools.sa.bo.GB_SAOrderBO;
import com.loribel.tools.sa.bo.GB_SAOrderData;

public abstract class GB_SAOrderDataGen implements GB_BOData, java.io.Serializable {

    public static final String BO_NAME = "SAOrder";

    private boolean ignoreCase;

    private boolean orderParagraph;

    private boolean reverseOrder;

    private String regexOrder;

    public String boName() {
        return BO_NAME;
    }

    public static final GB_Prototype newPrototype() {
        GB_Prototype retour = new GB_Prototype() {

            public Object newInstance() {
                return new GB_SAOrderData();
            }
        };
        return retour;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean a_ignoreCase) {
        ignoreCase = a_ignoreCase;
    }

    public boolean isOrderParagraph() {
        return orderParagraph;
    }

    public void setOrderParagraph(boolean a_orderParagraph) {
        orderParagraph = a_orderParagraph;
    }

    public boolean isReverseOrder() {
        return reverseOrder;
    }

    public void setReverseOrder(boolean a_reverseOrder) {
        reverseOrder = a_reverseOrder;
    }

    public String getRegexOrder() {
        return regexOrder;
    }

    public void setRegexOrder(String a_regexOrder) {
        regexOrder = a_regexOrder;
    }

    public void updateFromBO(GB_SimpleBusinessObject a_bo) {
        GB_SAOrderBO l_bo = (GB_SAOrderBO) a_bo;
        setIgnoreCase(l_bo.isIgnoreCase());
        setOrderParagraph(l_bo.isOrderParagraph());
        setReverseOrder(l_bo.isReverseOrder());
        setRegexOrder(l_bo.getRegexOrder());
    }

    public void updateBO(GB_SimpleBusinessObject a_bo) {
        GB_SAOrderBO l_bo = (GB_SAOrderBO) a_bo;
        l_bo.setIgnoreCase(this.isIgnoreCase());
        l_bo.setOrderParagraph(this.isOrderParagraph());
        l_bo.setReverseOrder(this.isReverseOrder());
        l_bo.setRegexOrder(this.getRegexOrder());
    }
}
