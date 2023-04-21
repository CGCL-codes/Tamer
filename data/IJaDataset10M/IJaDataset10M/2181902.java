package edu.unibi.agbi.biodwh.entity.transfac.site;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import edu.unibi.agbi.biodwh.entity.transfac.ExternalDatabaseLinks;

/**
 * @author Benjamin Kormeier
 * @version 1.0 29.04.2008
 */
@Entity(name = "tf_site2db_links")
@Table(name = "tf_site2db_links")
@IdClass(SiteExternalDatabaseLinksId.class)
public class SiteExternalDatabaseLinks {

    @Id
    private Site siteId = new Site();

    @Id
    private ExternalDatabaseLinks link = new ExternalDatabaseLinks();

    public SiteExternalDatabaseLinks() {
    }

    public SiteExternalDatabaseLinks(Site siteId, ExternalDatabaseLinks link) {
        this.siteId = siteId;
        this.link = link;
    }

    /**
	 * @return the siteId
	 */
    public Site getSiteId() {
        return siteId;
    }

    /**
	 * @param siteId the siteId to set
	 */
    public void setSiteId(Site siteId) {
        this.siteId = siteId;
    }

    /**
	 * @return the link
	 */
    public ExternalDatabaseLinks getLink() {
        return link;
    }

    /**
	 * @param link the link to set
	 */
    public void setLink(ExternalDatabaseLinks link) {
        this.link = link;
    }
}
