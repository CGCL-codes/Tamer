package org.stuarthardy.momentum.domain.market;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Index;
import org.stuarthardy.momentum.domain.AbstractBaseEntity;
import org.stuarthardy.momentum.domain.types.SectorType;

/**
 * momentum-domain
 * 
 * @author Stuart Hardy
 * 
 */
@Entity
public class Sector extends AbstractBaseEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = -8263512404889257393L;

    @Basic
    @Index(name = "SECTOR_CODE_INDEX")
    @Column(nullable = false)
    private String code;

    @Basic
    @Column(nullable = false)
    private String name;

    @Basic
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private SectorType sectorType;

    @ManyToOne(cascade = CascadeType.ALL)
    private SectorPrice latestSectorPrice;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<SectorPrice> sectorPrices;

    /**
     * @param code The shorthand code to describe this Sector
     * @param name The name of the Sector
     * @param description A brief description of the Sector
     * @param sectorType The {@link SectorType} of this Sector
     */
    public Sector(String code, String name, String description, SectorType sectorType) {
        super();
        this.code = code;
        this.name = name;
        this.description = description;
        this.sectorType = sectorType;
    }

    /**
     * @return the code property
     */
    public String getCode() {
        return this.code;
    }

    /**
     * @param code the code property
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the name property
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name the name property
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description property
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @param description the description property
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the sectorType property
     */
    public SectorType getSectorType() {
        return this.sectorType;
    }

    /**
     * @param sectorType the sectorType property
     */
    public void setSectorType(SectorType sectorType) {
        this.sectorType = sectorType;
    }

    /**
     * @return the latestSectorPrice property
     */
    public SectorPrice getLatestSectorPrice() {
        return this.latestSectorPrice;
    }

    /**
     * @param latestSectorPrice the latestSectorPrice property
     */
    public void setLatestSectorPrice(SectorPrice latestSectorPrice) {
        this.latestSectorPrice = latestSectorPrice;
    }

    /**
     * @return the sectorPrices property
     */
    public Set<SectorPrice> getSectorPrices() {
        return this.sectorPrices;
    }

    /**
     * @param sectorPrices the sectorPrices property
     */
    public void setSectorPrices(Set<SectorPrice> sectorPrices) {
        this.sectorPrices = sectorPrices;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (object.getClass() != getClass()) {
            return false;
        }
        Sector sector = (Sector) object;
        return new EqualsBuilder().appendSuper(super.equals(object)).append(code, sector.code).append(name, sector.name).append(description, sector.description).append(sectorType, sector.sectorType).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(57, 53).append(super.hashCode()).append(code).append(name).append(description).append(sectorType).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("Code", code).append("Name", name).append("Sector Type", sectorType != null ? sectorType.getCode() : "").append("Latest Price", latestSectorPrice != null ? latestSectorPrice.getPrice() : "").append("Base Entity", super.toString()).toString();
    }

    public Sector clone() {
        return new Sector(this.code, this.name, this.description, this.sectorType);
    }
}
