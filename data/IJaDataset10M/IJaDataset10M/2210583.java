package org.dmp.chillout.cid.form;

import org.apache.struts.action.ActionForm;

public class AddContentIdentifierForm extends ActionForm {

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String id) {
        contentid = id;
    }

    public String getHashvalue() {
        return hashvalue;
    }

    public void setHashvalue(String hash) {
        this.hashvalue = hash;
    }

    private String contentid;

    private String hashvalue;
}
