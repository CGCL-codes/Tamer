package eu.vph.predict.vre.in_silico.business.application.chaste.chaste_parameters.jaxb.v2_2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Defines a 3D cuboid "slab" mesh ranging from (0, 0, 0) to (x, y, z). The
 *         internode spacing is used to define the typical mesh step size in the x,y,z-directions
 *         (diagonal edges will be slightly longer).
 * 
 * <p>Java class for slab_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="slab_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="x" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="y" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="z" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="inter_node_space" use="required" type="{http://www.w3.org/2001/XMLSchema}double" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "slab_type")
public class SlabType {

    @XmlAttribute(required = true)
    protected double x;

    @XmlAttribute(required = true)
    protected double y;

    @XmlAttribute(required = true)
    protected double z;

    @XmlAttribute(name = "inter_node_space", required = true)
    protected double interNodeSpace;

    /**
     * Gets the value of the x property.
     * 
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the value of the x property.
     * 
     */
    public void setX(double value) {
        this.x = value;
    }

    /**
     * Gets the value of the y property.
     * 
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the value of the y property.
     * 
     */
    public void setY(double value) {
        this.y = value;
    }

    /**
     * Gets the value of the z property.
     * 
     */
    public double getZ() {
        return z;
    }

    /**
     * Sets the value of the z property.
     * 
     */
    public void setZ(double value) {
        this.z = value;
    }

    /**
     * Gets the value of the interNodeSpace property.
     * 
     */
    public double getInterNodeSpace() {
        return interNodeSpace;
    }

    /**
     * Sets the value of the interNodeSpace property.
     * 
     */
    public void setInterNodeSpace(double value) {
        this.interNodeSpace = value;
    }
}
