package com.entelience.objects;

import java.io.Serializable;

/** BEAN - ExtendedAuthentication return data
 */
public class ExtendedAuthentication implements Serializable {

    private boolean forceChange;

    private Integer e_people_id;

    private Integer e_company_id;

    public ExtendedAuthentication() {
        forceChange = false;
        e_people_id = null;
        e_company_id = null;
    }

    public void setForceChange(boolean value) {
        forceChange = value;
    }

    public boolean isForceChange() {
        return forceChange;
    }

    public void setE_people_id(Integer value) {
        e_people_id = value;
    }

    public Integer getE_people_id() {
        return e_people_id;
    }

    public void setE_company_id(Integer value) {
        e_company_id = value;
    }

    public Integer getE_company_id() {
        return e_company_id;
    }
}
