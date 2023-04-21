package nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import nl.tranquilizedquality.adm.commons.business.domain.Repository;
import nl.tranquilizedquality.adm.commons.domain.DomainObject;
import nl.tranquilizedquality.adm.commons.hibernate.bean.AbstractUpdatableDomainObject;
import com.google.code.simplestuff.annotation.BusinessField;

/**
 * Hibernate implementation of a repository.
 * 
 * @author Salomo Petrus (salomo.petrus@gmail.com)
 * @since 3 jun. 2011
 */
@Entity(name = "REPOSITORIES")
public class HibernateRepository extends AbstractUpdatableDomainObject<Long> implements Repository {

    /**
     * 
     */
    private static final long serialVersionUID = 1365511299700579578L;

    /** The name of the repository. */
    @BusinessField
    private String name;

    /** The URL to the repository. */
    @BusinessField
    private String repositoryUrl;

    /** Determines if this repository is to be used or not. */
    @BusinessField
    private Boolean enabled;

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @SequenceGenerator(name = "REPOSITORIES_SEQ_GEN", initialValue = 1, allocationSize = 1, sequenceName = "REPOSITORIES_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "REPOSITORIES_SEQ_GEN")
    public Long getId() {
        return id;
    }

    /**
	 * @see nl.tranquilizedquality.adm.commons.business.domain.Repository#getName()
	 */
    @Override
    @Column(name = "NAME", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    /**
	 * @param name
	 *            the name to set
	 */
    public void setName(final String name) {
        this.name = name;
    }

    /**
	 * @see nl.tranquilizedquality.adm.commons.business.domain.Repository#getRepositoryUrl()
	 */
    @Override
    @Column(name = "REPOSITORY_URL", nullable = false, length = 255)
    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    /**
	 * @param repositoryUrl
	 *            the repositoryUrl to set
	 */
    public void setRepositoryUrl(final String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    @Column(name = "ENABLED", nullable = false)
    public Boolean isEnabled() {
        return enabled;
    }

    /**
	 * @return the enabled
	 */
    @Transient
    public Boolean getEnabled() {
        return enabled;
    }

    /**
	 * @param enabled
	 *            the enabled to set
	 */
    public void setEnabled(final Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void copy(final DomainObject<Long> object) {
        super.copy(object);
        if (object instanceof Repository) {
            final Repository repo = (Repository) object;
            this.enabled = repo.isEnabled();
            this.name = repo.getName();
            this.repositoryUrl = repo.getRepositoryUrl();
        }
    }
}
