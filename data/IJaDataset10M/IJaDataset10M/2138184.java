package com.aionemu.gameserver.skillengine.properties;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Properties")
public class Properties {

    @XmlElements({ @XmlElement(name = "firsttarget", type = FirstTargetProperty.class), @XmlElement(name = "targetrange", type = TargetRangeProperty.class), @XmlElement(name = "addweaponrange", type = AddWeaponRangeProperty.class), @XmlElement(name = "targetrelation", type = TargetRelationProperty.class), @XmlElement(name = "firsttargetrange", type = FirstTargetRangeProperty.class) })
    protected List<Property> properties;

    /**
     * Gets the value of the getProperties property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the getProperties property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProperties().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FirstTargetProperty }
     * {@link TargetRangeProperty }
     * {@link AddWeaponRangeProperty }
     * {@link TargetRelationProperty }
     * {@link FirstTargetRangeProperty }
     * 
     * 
     */
    public List<Property> getProperties() {
        if (properties == null) {
            properties = new ArrayList<Property>();
        }
        return this.properties;
    }
}
