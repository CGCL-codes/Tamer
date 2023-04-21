package web.banco.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Object approval that represents the table Approval in Derby database
 * 
 * @author Rodolfo Vasconcelos
 */
@Entity
public class Approval implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private String client;

    private String securityKey;

    private String status;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(String secutiryKey) {
        this.securityKey = secutiryKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
