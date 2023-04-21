package com.icteam.fiji.security.jaas;

import java.util.Date;

/**
 * @author keys
 *         <p/>
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FIJICredentials implements java.io.Serializable {

    private int num;

    private String m_name;

    private String m_surname;

    private String m_company;

    private String m_language;

    private String m_role;

    private String m_username;

    private String m_id;

    private Date m_date = new Date();

    /**
    *
    */
    public FIJICredentials() {
        super();
    }

    /**
    * @return
    */
    public Date getDate() {
        return m_date;
    }

    /**
    * @return
    */
    public int getNum() {
        return num;
    }

    /**
    * @return
    */
    public String getName() {
        return m_name;
    }

    /**
    * @return
    */
    public String getSurname() {
        return m_surname;
    }

    /**
    * @param i
    */
    public void setNum(int i) {
        num = i;
    }

    /**
    * @param string
    */
    public void setName(String string) {
        m_name = string;
    }

    /**
    * @param string
    */
    public void setSurname(String string) {
        m_surname = string;
    }

    public String getLanguage() {
        return m_language;
    }

    public void setLanguage(String p_language) {
        m_language = p_language;
    }

    public String getRole() {
        return m_role;
    }

    public void setRole(String p_role) {
        m_role = p_role;
    }

    public String getUsername() {
        return m_username;
    }

    public void setUsername(String p_username) {
        m_username = p_username;
    }

    public String getId() {
        return m_id;
    }

    public void setId(String p_id) {
        m_id = p_id;
    }

    public String getCompany() {
        return m_company;
    }

    public void setCompany(String p_company) {
        m_company = p_company;
    }
}
