package org.blueoxygen.komodo;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.blueoxygen.cimande.DefaultPersistence;

/**
 * @author harry
 * email :  harry@intercitra.com
 * 
 */
@Entity()
@Table(name = "creator")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Creator extends DefaultPersistence {

    private Article article;

    private String creatorName;

    private String creatorOrganization;

    private String creatorService;

    /**
	 * @hibernate.property
	 * @return
	 */
    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    /**
	 * @hibernate.property
	 * @return
	 */
    public String getCreatorOrganization() {
        return creatorOrganization;
    }

    public void setCreatorOrganization(String creatorOrganization) {
        this.creatorOrganization = creatorOrganization;
    }

    /**
	 * @hibernate.property
	 * @return
	 */
    public String getCreatorService() {
        return creatorService;
    }

    public void setCreatorService(String creatorService) {
        this.creatorService = creatorService;
    }

    /**
	 * @hibernate.many-to-one column="art_id"
	 * @return
	 */
    @ManyToOne
    @JoinColumn(name = "art_id")
    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
