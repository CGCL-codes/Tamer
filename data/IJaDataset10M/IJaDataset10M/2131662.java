package net.sourceforge.mazix.persistence.dao.impl.xml.jaxb.levels.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice minOccurs="0">
 *         &lt;element ref="{}lantern"/>
 *         &lt;element ref="{}key"/>
 *         &lt;element ref="{}ball"/>
 *         &lt;element ref="{}movable_wall"/>
 *       &lt;/choice>
 *       &lt;attribute ref="{}x use="required""/>
 *       &lt;attribute ref="{}y use="required""/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "lantern", "key", "ball", "movableWall" })
@XmlRootElement(name = "empty")
public class Empty {

    protected Lantern lantern;

    protected Key key;

    protected Ball ball;

    @XmlElement(name = "movable_wall")
    protected MovableWall movableWall;

    @XmlAttribute(required = true)
    protected int x;

    @XmlAttribute(required = true)
    protected int y;

    /**
     * Gets the value of the lantern property.
     * 
     * @return
     *     possible object is
     *     {@link Lantern }
     *     
     */
    public Lantern getLantern() {
        return lantern;
    }

    /**
     * Sets the value of the lantern property.
     * 
     * @param value
     *     allowed object is
     *     {@link Lantern }
     *     
     */
    public void setLantern(Lantern value) {
        this.lantern = value;
    }

    /**
     * Gets the value of the key property.
     * 
     * @return
     *     possible object is
     *     {@link Key }
     *     
     */
    public Key getKey() {
        return key;
    }

    /**
     * Sets the value of the key property.
     * 
     * @param value
     *     allowed object is
     *     {@link Key }
     *     
     */
    public void setKey(Key value) {
        this.key = value;
    }

    /**
     * Gets the value of the ball property.
     * 
     * @return
     *     possible object is
     *     {@link Ball }
     *     
     */
    public Ball getBall() {
        return ball;
    }

    /**
     * Sets the value of the ball property.
     * 
     * @param value
     *     allowed object is
     *     {@link Ball }
     *     
     */
    public void setBall(Ball value) {
        this.ball = value;
    }

    /**
     * Gets the value of the movableWall property.
     * 
     * @return
     *     possible object is
     *     {@link MovableWall }
     *     
     */
    public MovableWall getMovableWall() {
        return movableWall;
    }

    /**
     * Sets the value of the movableWall property.
     * 
     * @param value
     *     allowed object is
     *     {@link MovableWall }
     *     
     */
    public void setMovableWall(MovableWall value) {
        this.movableWall = value;
    }

    /**
     * Gets the value of the x property.
     * 
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the value of the x property.
     * 
     */
    public void setX(int value) {
        this.x = value;
    }

    /**
     * Gets the value of the y property.
     * 
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the value of the y property.
     * 
     */
    public void setY(int value) {
        this.y = value;
    }
}
