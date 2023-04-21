package edu.unibi.agbi.biodwh.entity.intact;

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
@Entity(name = "intact_source_attribute")
@Table(name = "intact_source_attribute")
public class IntactSourceAttribute implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8045759508919677623L;

    private Integer id;

    private IntactSource intactSource;

    private String attribute;

    private String attributeAc;

    private String attributeName;

    public IntactSourceAttribute() {
    }

    public IntactSourceAttribute(Integer id) {
        this.id = id;
    }

    public IntactSourceAttribute(Integer id, String attributeName, IntactSource intactSource, String attribute, String attributeAc) {
        this.id = id;
        this.intactSource = intactSource;
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
    @JoinColumn(name = "entry_id", nullable = false)
    public IntactSource getIntactSource() {
        return this.intactSource;
    }

    public void setIntactSource(IntactSource intactSource) {
        this.intactSource = intactSource;
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
