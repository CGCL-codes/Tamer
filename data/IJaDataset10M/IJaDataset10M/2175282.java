package org.opennms.secret.model;

/**
 * Represents a OGPMember 
 *
 * @author brozow
 * 
 * @hibernate.class table="member" dynamic-update="true dynamic-insert="true"
 */
public class OGPMember {

    Long m_id;

    String m_first;

    String m_last;

    /**
     * The id field
     * @hibernate.id generator-class="native"
     */
    public Long getId() {
        return m_id;
    }

    public void setId(Long id) {
        m_id = id;
    }

    /**
     * The first name
     * @hibernate.property column="primoNombre"
     */
    public String getFirstName() {
        return m_first;
    }

    public void setFirstName(String first) {
        m_first = first;
    }

    /**
     * The last name
     * @hibernate.property
     */
    public String getLastName() {
        return m_last;
    }

    public void setLastName(String last) {
        m_last = last;
    }

    /**
     * The members full name
     * 
     */
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public String toString() {
        return getFullName();
    }
}
