package org.verus.ngl.sl.objectmodel.administration;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author root
 */
@Entity
@Table(name = "general_privileges")
@IdClass(GENERAL_PRIVILEGESPK.class)
@NamedQueries({ @NamedQuery(name = "GENERAL_PRIVILEGES.findByPat_catIdLibId", query = "SELECT g FROM GENERAL_PRIVILEGES g WHERE g.patron_category_id = :patron_category_id and g.library_id=:library_id"), @NamedQuery(name = "GENERAL_PRIVILEGES.findByWef", query = "SELECT p FROM GENERAL_PRIVILEGES p WHERE p.library_id = :library_id and p.patron_category_id = :patron_category_id and p.status = :status and   p.wef <= :wef"), @NamedQuery(name = "GENERAL_PRIVILEGES.findByPat_catIdLibIdstatus", query = "SELECT g FROM GENERAL_PRIVILEGES g WHERE g.patron_category_id = :patron_category_id and g.library_id=:library_id and g.status = :status ORDER BY wef DESC"), @NamedQuery(name = "GENERAL_PRIVILEGES.findByPat_catIdLibIdwefstatus", query = "SELECT g FROM GENERAL_PRIVILEGES g WHERE g.patron_category_id = :patron_category_id and g.library_id=:library_id and g.wef= :wef and g.status = :status"), @NamedQuery(name = "GENERAL_PRIVILEGES.findByPat_catIdLibIdwef", query = "SELECT g FROM GENERAL_PRIVILEGES g WHERE g.patron_category_id = :patron_category_id and g.library_id=:library_id and g.wef= :wef") })
public class GENERAL_PRIVILEGES {

    @Id
    private Integer patron_category_id;

    @Id
    private Integer library_id;

    private String log;

    @Id
    private Timestamp wef;

    private String user_privileges;

    private String status;

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

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
