package com.creawor.hz_market.t_group_aim;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class t_group_aim_Form extends ActionForm {

    public t_group_aim_Form() {
        super();
    }

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String strparm) {
        id = strparm;
    }

    private String group_code;

    public String getGroup_code() {
        return group_code;
    }

    public void setGroup_code(String strparm) {
        group_code = strparm;
    }

    private String group_name;

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String strparm) {
        group_name = strparm;
    }

    private String grouptype;

    public String getGrouptype() {
        return grouptype;
    }

    public void setGrouptype(String strparm) {
        grouptype = strparm;
    }

    private String x;

    public String getX() {
        return x;
    }

    public void setX(String strparm) {
        x = strparm;
    }

    private String y;

    public String getY() {
        return y;
    }

    public void setY(String strparm) {
        y = strparm;
    }

    private String county;

    public String getCounty() {
        return county;
    }

    public void setCounty(String strparm) {
        county = strparm;
    }

    private String town;

    public String getTown() {
        return town;
    }

    public void setTown(String strparm) {
        town = strparm;
    }
}
