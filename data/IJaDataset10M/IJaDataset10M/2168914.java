package org.fao.fenix.domain.contentdefinition;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.fao.fenix.domain.metadataitems.Contact;
import org.fao.fenix.domain.metadataitems.Organization;
import org.fao.fenix.domain.util.ResourceType;
import org.hibernate.annotations.CollectionOfElements;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Resource {

    @Id
    @GeneratedValue
    private Long resourceId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String label;

    private String description;

    @ManyToOne
    @JoinColumn(nullable = true)
    private Organization provider;

    @ManyToOne
    @JoinColumn(nullable = true)
    private Organization source;

    @ManyToOne
    @JoinColumn(nullable = true)
    private Contact contact;

    @CollectionOfElements
    private List<String> keywords;

    @Column(nullable = true)
    private Date startDate;

    @Column(nullable = true)
    private Date lastUpdate;

    @Enumerated(EnumType.STRING)
    private ResourceType type;

    /**
     * 
     */
    public Resource(ResourceType type) {
        this.type = type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the contact
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * @param contact
     *            the contact to set
     */
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the lastUpdate
     */
    public Date getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @param lastUpdate
     *            the lastUpdate to set
     */
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @return the provider
     */
    public Organization getProvider() {
        return provider;
    }

    /**
     * @param provider
     *            the provider to set
     */
    public void setProvider(Organization provider) {
        this.provider = provider;
    }

    /**
     * @return the resourceId
     */
    public Long getResourceId() {
        return resourceId;
    }

    /**
     * @param resourceId
     *            the resourceId to set TODO change from public to private
     */
    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * @return the source
     */
    public Organization getSource() {
        return source;
    }

    /**
     * @param source
     *            the source to set
     */
    public void setSource(Organization source) {
        this.source = source;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate
     *            the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
