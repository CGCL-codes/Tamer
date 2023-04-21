package com.objectcode.time4u.server.api.data;

import java.util.Date;

public class Person implements IChangeData {

    private static final long serialVersionUID = -4022407515199667835L;

    private long m_id;

    private String m_userId;

    private String m_name;

    private String m_email;

    private Date m_lastSynchronize;

    public long getId() {
        return m_id;
    }

    public void setId(long id) {
        m_id = id;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    public String getUserId() {
        return m_userId;
    }

    public void setUserId(String userId) {
        m_userId = userId;
    }

    public String getEmail() {
        return m_email;
    }

    public void setEmail(String email) {
        m_email = email;
    }

    public Date getLastSynchronize() {
        return m_lastSynchronize;
    }

    public void setLastSynchronize(Date lastSynchronize) {
        m_lastSynchronize = lastSynchronize;
    }
}
