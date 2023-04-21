package com.hack23.cia.model.impl.sweden.content;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.hack23.cia.model.api.common.ResourceType;
import com.hack23.cia.model.api.common.TypeContext;
import com.hack23.cia.model.api.sweden.common.ParliamentMetaData;
import com.hack23.cia.model.api.sweden.content.RegisterInformationData;
import com.hack23.cia.model.impl.common.BaseEntity;

/**
 * The Class RegisterInformation.
 */
@Entity
@Table
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RegisterInformation extends BaseEntity implements RegisterInformationData {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1551456381369005380L;

    /** The agreements. */
    private List<String> agreements = new ArrayList<String>();

    /** The allowances. */
    private List<String> allowances = new ArrayList<String>();

    /** The electoral area. */
    private String electoralArea = "";

    /** The houses. */
    private List<String> houses = new ArrayList<String>();

    /** The id. */
    private Long id;

    /** The imported date. */
    private Date importedDate;

    /** The import status. */
    private ImportStatus importStatus;

    /** The income activity. */
    private List<String> incomeActivity = new ArrayList<String>();

    /** The name. */
    private String name = "";

    /** The nothing declared. */
    private String nothingDeclared = "";

    /** The party. */
    private String party = "";

    /** The public roles. */
    private List<String> publicRoles = new ArrayList<String>();

    /** The resource type. */
    private ResourceType resourceType = ResourceType.SwedenModelData;

    /** The roles. */
    private List<String> roles = new ArrayList<String>();

    /** The stocks. */
    private List<String> stocks = new ArrayList<String>();

    /** The version. */
    private Long version = 1L;

    @Transient
    public boolean dataCanBeUpdated() {
        return true;
    }

    @Transient
    public boolean generatesParliamentMetaData() {
        return false;
    }

    @Transient
    public List<String> getAgreements() {
        return agreements;
    }

    @Transient
    public List<String> getAllowances() {
        return allowances;
    }

    @Override
    @Transient
    protected TypeContext getApplicationTypeContext() {
        return TypeContext.Content;
    }

    @Transient
    public String getElectoralArea() {
        return electoralArea;
    }

    @Transient
    public List<String> getHouses() {
        return houses;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    @Transient
    public Date getImportedDate() {
        return importedDate;
    }

    @Transient
    public ImportStatus getImportStatus() {
        return importStatus;
    }

    @Transient
    public List<String> getIncomeActivity() {
        return incomeActivity;
    }

    @Transient
    public String getName() {
        return name;
    }

    @Transient
    public String getNothingDeclared() {
        return nothingDeclared;
    }

    @Transient
    public ParliamentMetaData getParliamentMetaData() {
        return null;
    }

    @Transient
    public String getParty() {
        return party;
    }

    @Transient
    public List<String> getPublicRoles() {
        return publicRoles;
    }

    @Override
    @Transient
    public ResourceType getResourceType() {
        return resourceType;
    }

    @Transient
    public List<String> getRoles() {
        return roles;
    }

    @Transient
    public List<String> getStocks() {
        return stocks;
    }

    @Override
    @Version
    public Long getVersion() {
        return version;
    }

    /**
	 * Sets the agreements.
	 * 
	 * @param agreements the new agreements
	 */
    public void setAgreements(final List<String> agreements) {
        this.agreements = agreements;
    }

    /**
	 * Sets the allowances.
	 * 
	 * @param allowances the new allowances
	 */
    public void setAllowances(final List<String> allowances) {
        this.allowances = allowances;
    }

    public void setElectoralArea(final String electoralArea) {
        this.electoralArea = electoralArea;
    }

    /**
	 * Sets the houses.
	 * 
	 * @param houses the new houses
	 */
    public void setHouses(final List<String> houses) {
        this.houses = houses;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setImportedDate(final Date importedDate) {
        this.importedDate = importedDate;
    }

    public void setImportStatus(final ImportStatus importStatus) {
        this.importStatus = importStatus;
    }

    /**
	 * Sets the income activity.
	 * 
	 * @param incomeActivity the new income activity
	 */
    public void setIncomeActivity(final List<String> incomeActivity) {
        this.incomeActivity = incomeActivity;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setNothingDeclared(final String nothingDeclared) {
        this.nothingDeclared = nothingDeclared;
    }

    public void setParty(final String party) {
        this.party = party;
    }

    /**
	 * Sets the public roles.
	 * 
	 * @param publicRoles the new public roles
	 */
    public void setPublicRoles(final List<String> publicRoles) {
        this.publicRoles = publicRoles;
    }

    /**
	 * Sets the resource type.
	 * 
	 * @param resourceType the new resource type
	 */
    public void setResourceType(final ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    /**
	 * Sets the roles.
	 * 
	 * @param roles the new roles
	 */
    public void setRoles(final List<String> roles) {
        this.roles = roles;
    }

    /**
	 * Sets the stocks.
	 * 
	 * @param stocks the new stocks
	 */
    public void setStocks(final List<String> stocks) {
        this.stocks = stocks;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }
}
