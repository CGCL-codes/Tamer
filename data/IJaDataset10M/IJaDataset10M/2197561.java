package jard.webshop.nbp;

import jard.webshop.util.Constants;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author CJP
 */
@Entity
@NamedQueries(@NamedQuery(name = Constants.GET_ALL_PREFS, query = "SELECT p FROM SitePref p"))
public class SitePref implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String webshopname;

    private String description;

    private String keywords;

    private String author;

    private Boolean showAlertMessage;

    private String alertMessage;

    private Boolean siteOffline;

    private String offlineMessage;

    public SitePref() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SitePref)) {
            return false;
        }
        SitePref other = (SitePref) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jard.webshop.nbp.SitePref[ id=" + id + " ]";
    }

    public String getWebshopname() {
        return webshopname;
    }

    public void setWebshopname(String webshopname) {
        this.webshopname = webshopname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boolean getShowAlertMessage() {
        return showAlertMessage;
    }

    public void setShowAlertMessage(Boolean showAlertMessage) {
        this.showAlertMessage = showAlertMessage;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public Boolean getSiteOffline() {
        return siteOffline;
    }

    public void setSiteOffline(Boolean siteOffline) {
        this.siteOffline = siteOffline;
    }

    public String getOfflineMessage() {
        return offlineMessage;
    }

    public void setOfflineMessage(String offlineMessage) {
        this.offlineMessage = offlineMessage;
    }
}
