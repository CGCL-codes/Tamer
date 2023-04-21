package org.verus.ngl.web.administration;

import java.sql.Timestamp;

/**
 *
 * @author root
 */
public class General_privilege {

    private Integer patron_category_id;

    private Integer library_id;

    private String log;

    private Timestamp wef;

    private String user_privileges;

    public Integer getPatron_category_id() {
        return patron_category_id;
    }

    public void setPatron_category_id(Integer patron_category_id) {
        this.patron_category_id = patron_category_id;
    }

    public Integer getLibrary_id() {
        return library_id;
    }

    public void setLibrary_id(Integer library_id) {
        this.library_id = library_id;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Timestamp getWef() {
        return wef;
    }

    public void setWef(Timestamp wef) {
        this.wef = wef;
    }

    public String getUser_privileges() {
        return user_privileges;
    }

    public void setUser_privileges(String user_privileges) {
        this.user_privileges = user_privileges;
    }
}
