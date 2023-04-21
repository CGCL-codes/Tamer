package eu.fbk.hlt.ubinav;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for AMM complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;AMM&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;attribute name=&quot;DMAX&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot; /&gt;
 *       &lt;attribute name=&quot;PMAX&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;X&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *       &lt;attribute name=&quot;Y&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AMM")
public class AMM {

    @XmlAttribute(name = "DMAX")
    protected Integer dmax;

    @XmlAttribute(name = "PMAX")
    protected String pmax;

    @XmlAttribute(name = "X")
    protected String x;

    @XmlAttribute(name = "Y")
    protected String y;

    /**
	 * Gets the value of the dmax property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
    public Integer getDMAX() {
        return dmax;
    }

    /**
	 * Gets the value of the pmax property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
    public String getPMAX() {
        return pmax;
    }

    /**
	 * Gets the value of the x property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
    public String getX() {
        return x;
    }

    /**
	 * Gets the value of the y property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
    public String getY() {
        return y;
    }

    /**
	 * Sets the value of the dmax property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
    public void setDMAX(Integer value) {
        this.dmax = value;
    }

    /**
	 * Sets the value of the pmax property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
    public void setPMAX(String value) {
        this.pmax = value;
    }

    /**
	 * Sets the value of the x property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
    public void setX(String value) {
        this.x = value;
    }

    /**
	 * Sets the value of the y property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
    public void setY(String value) {
        this.y = value;
    }
}
