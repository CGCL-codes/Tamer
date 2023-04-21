package net.java.slee.resource.diameter.sh.events.avp.userdata;

import java.util.List;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.THeader;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TSePoTriExtension;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TSessionDescription;
import org.w3c.dom.Element;

public interface SePoTri {

    /**
   * Gets the value of the conditionNegated property.
   * 
   * @return
   *     possible object is
   *     {@link Boolean }
   *     
   */
    public abstract Boolean isConditionNegated();

    /**
   * Sets the value of the conditionNegated property.
   * 
   * @param value
   *     allowed object is
   *     {@link Boolean }
   *     
   */
    public abstract void setConditionNegated(Boolean value);

    /**
   * Gets the value of the group property.
   * 
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the group property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getGroup().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link Integer }
   * 
   * 
   */
    public abstract List<Integer> getGroup();

    /**
   * Gets the value of the requestURI property.
   * 
   * @return
   *     possible object is
   *     {@link String }
   *     
   */
    public abstract String getRequestURI();

    /**
   * Sets the value of the requestURI property.
   * 
   * @param value
   *     allowed object is
   *     {@link String }
   *     
   */
    public abstract void setRequestURI(String value);

    /**
   * Gets the value of the method property.
   * 
   * @return
   *     possible object is
   *     {@link String }
   *     
   */
    public abstract String getMethod();

    /**
   * Sets the value of the method property.
   * 
   * @param value
   *     allowed object is
   *     {@link String }
   *     
   */
    public abstract void setMethod(String value);

    /**
   * Gets the value of the sipHeader property.
   * 
   * @return
   *     possible object is
   *     {@link THeader }
   *     
   */
    public abstract Header getSIPHeader();

    /**
   * Sets the value of the sipHeader property.
   * 
   * @param value
   *     allowed object is
   *     {@link THeader }
   *     
   */
    public abstract void setSIPHeader(Header value);

    /**
   * Gets the value of the sessionCase property.
   * 
   * @return
   *     possible object is
   *     {@link Short }
   *     
   */
    public abstract Short getSessionCase();

    /**
   * Sets the value of the sessionCase property.
   * 
   * @param value
   *     allowed object is
   *     {@link Short }
   *     
   */
    public abstract void setSessionCase(Short value);

    /**
   * Gets the value of the sessionDescription property.
   * 
   * @return
   *     possible object is
   *     {@link TSessionDescription }
   *     
   */
    public abstract SessionDescription getSessionDescription();

    /**
   * Sets the value of the sessionDescription property.
   * 
   * @param value
   *     allowed object is
   *     {@link TSessionDescription }
   *     
   */
    public abstract void setSessionDescription(SessionDescription value);

    /**
   * Gets the value of the extension property.
   * 
   * @return
   *     possible object is
   *     {@link TSePoTriExtension }
   *     
   */
    public abstract SePoTriExtension getExtension();

    /**
   * Sets the value of the extension property.
   * 
   * @param value
   *     allowed object is
   *     {@link TSePoTriExtension }
   *     
   */
    public abstract void setExtension(SePoTriExtension value);

    /**
   * Gets the value of the any property.
   * 
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the any property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getAny().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link Object }
   * {@link Element }
   * 
   * 
   */
    public abstract List<Object> getAny();
}
