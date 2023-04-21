package edu.unibi.agbi.biodwh.entity.mint;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Jan_Fuessmann
 * @version 1.0_2010
 */
@Entity(name = "mint_biosource_compartment_attribute")
@Table(name = "mint_biosource_compartment_attribute")
public class MintBiosourceCompartmentAttribute implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8781666189781150147L;

    private Integer id;

    private MintBiosource mintBiosource;

    private String attribute;

    private String attributeAc;

    private String attributeName;

    public MintBiosourceCompartmentAttribute() {
    }

    public MintBiosourceCompartmentAttribute(Integer id) {
        this.id = id;
    }

    public MintBiosourceCompartmentAttribute(Integer id, String attributeName, MintBiosource mintBiosource, String attribute, String attributeAc) {
        this.id = id;
        this.mintBiosource = mintBiosource;
        this.attribute = attribute;
        this.attributeAc = attributeAc;
        this.attributeName = attributeName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "attribute_name", length = 255)
    public String getAttributeName() {
        return this.attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "biosource_id", nullable = false)
    public MintBiosource getMintBiosource() {
        return this.mintBiosource;
    }

    public void setMintBiosource(MintBiosource mintBiosource) {
        this.mintBiosource = mintBiosource;
    }

    @Column(name = "attribute", length = 255)
    public String getAttribute() {
        return this.attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @Column(name = "attribute_ac")
    public String getAttributeAc() {
        return this.attributeAc;
    }

    public void setAttributeAc(String attributeAc) {
        this.attributeAc = attributeAc;
    }
}
