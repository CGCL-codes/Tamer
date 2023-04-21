package org.amhm.persistence;

import java.util.Set;
import java.io.Serializable;
import java.lang.Object;
import java.lang.Class;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import org.amhm.core.constants.AMHMConst;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Event")
public class Event extends XmlSerializable implements Serializable, Subscribable {

    private static final long serialVersionUID = -8541946366384550961L;

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(Event.class, true);

    /**
     * Get accessor for typeDesc
     * @return  value of typeDesc
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return Event.typeDesc;
    }

    @XmlAttribute(required = true)
    private int subscribableId;

    /**
     * Get accessor for eventId
     * @return  value of eventId
     */
    public int getSubscribableId() {
        return this.subscribableId;
    }

    /**
     * Set accessor for eventId
     * @param value the value to set in eventId
     */
    public void setSubscribableId(int value) {
        this.subscribableId = value;
    }

    @XmlAttribute(required = true)
    private String speaker;

    /**
     * Get accessor for speaker
     * @return  value of speaker
     */
    public String getSpeaker() {
        return this.speaker;
    }

    /**
     * Set accessor for speaker
     * @param value the value to set in speaker
     */
    public void setSpeaker(String value) {
        this.speaker = value;
    }

    @XmlAttribute(required = true)
    private String description;

    /**
     * Get accessor for description
     * @return  value of description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Set accessor for description
     * @param value the value to set in description
     */
    public void setDescription(String value) {
        this.description = value;
    }

    @XmlAttribute(required = true)
    private String eventDate;

    /**
     * Get accessor for eventDate
     * @return  value of eventDate
     */
    public String getEventDate() {
        return this.eventDate;
    }

    /**
     * Set accessor for eventDate
     * @param value the value to set in eventDate
     */
    public void setEventDate(String value) {
        this.eventDate = value;
    }

    @XmlAttribute(required = true)
    private int parentId;

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @XmlTransient
    private Tariff[] tariffs;

    public void addTariff(Tariff tariff) {
        this.tariffs[this.tariffs.length] = tariff;
    }

    @Override
    public Tariff[] getTariffs() {
        return tariffs;
    }

    /**
     * Sets the tariffs value for this Event.
     * 
     * @param tariffs
     */
    public void setTariffs(Tariff[] tariffs) {
        this.tariffs = tariffs;
    }

    @XmlTransient
    private Object __equalsCalc = null;

    @XmlTransient
    private boolean __hashCodeCalc = false;

    public static class EventDeclare {

        public EventDeclare() {
            typeDesc.setXmlType(new javax.xml.namespace.QName(AMHMConst.AMHM_URL, "Event"));
            org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
            elemField.setFieldName("subscribableId");
            elemField.setXmlName(new javax.xml.namespace.QName("", "subscribableId"));
            elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
            elemField.setNillable(false);
            typeDesc.addFieldDesc(elemField);
            elemField = new org.apache.axis.description.ElementDesc();
            elemField.setFieldName("speaker");
            elemField.setXmlName(new javax.xml.namespace.QName("", "speaker"));
            elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
            elemField.setNillable(false);
            typeDesc.addFieldDesc(elemField);
            elemField = new org.apache.axis.description.ElementDesc();
            elemField.setFieldName("description");
            elemField.setXmlName(new javax.xml.namespace.QName("", "description"));
            elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
            elemField.setNillable(false);
            typeDesc.addFieldDesc(elemField);
            elemField = new org.apache.axis.description.ElementDesc();
            elemField.setFieldName("eventDate");
            elemField.setXmlName(new javax.xml.namespace.QName("", "eventDate"));
            elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
            elemField.setNillable(false);
            typeDesc.addFieldDesc(elemField);
            elemField = new org.apache.axis.description.ElementDesc();
            elemField.setFieldName("tariffs");
            elemField.setXmlName(new javax.xml.namespace.QName("", "tariffs"));
            elemField.setXmlType(new javax.xml.namespace.QName(AMHMConst.AMHM_URL, "Tariff"));
            elemField.setNillable(false);
            typeDesc.addFieldDesc(elemField);
            elemField = new org.apache.axis.description.ElementDesc();
            elemField.setFieldName("parentId");
            elemField.setXmlName(new javax.xml.namespace.QName("", "parentId"));
            elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
            elemField.setNillable(false);
            typeDesc.addFieldDesc(elemField);
        }
    }

    public Event() {
    }

    public Event(int subscribableId, String speaker, String description, String eventDate, Tariff[] tariffs, int parentId) {
        this.subscribableId = subscribableId;
        this.speaker = speaker;
        this.description = description;
        this.eventDate = eventDate;
        this.tariffs = tariffs;
        this.parentId = parentId;
    }

    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Event)) return false;
        Event other = (Event) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.subscribableId == 0 && other.getSubscribableId() == 0) || (this.subscribableId != 0 && this.subscribableId == other.getSubscribableId())) && ((this.speaker == null && other.getSpeaker() == null) || (this.speaker != null && this.speaker.equals(other.getSpeaker()))) && ((this.description == null && other.getDescription() == null) || (this.description != null && this.description.equals(other.getDescription()))) && ((this.eventDate == null && other.getEventDate() == null) || (this.eventDate != null && this.eventDate.equals(other.getEventDate())));
        __equalsCalc = null;
        return _equals;
    }

    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getSubscribableId() != 0) {
            _hashCode += Integer.valueOf(getSubscribableId()).hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getEventDate() != null) {
            _hashCode += getEventDate().hashCode();
        }
        if (getSpeaker() != null) {
            _hashCode += getSpeaker().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getParentId() != 0) {
            _hashCode += Integer.valueOf(getParentId()).hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    public static org.apache.axis.encoding.Serializer getSerializer(String mechType, Class<Object> _javaType, QName _xmlType) {
        return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
    }

    public static org.apache.axis.encoding.Deserializer getDeserializer(String mechType, Class<Object> _javaType, QName _xmlType) {
        return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
    }

    public String getName() {
        return this.eventDate + " (" + this.speaker + ")";
    }

    public boolean hasChild() {
        return false;
    }

    public void addSubscribable(Subscribable component) {
    }

    public boolean removeSubscribable(Subscribable component) {
        return false;
    }

    public Subscription getSubscription() {
        return null;
    }

    public void setSubscription(Subscription subscription) {
    }

    public Set<Subscribable> getSubscribable() {
        return null;
    }

    public void accept(SubscribableVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void accept(XmlSerializableVisitor visitor) {
        visitor.visit(this);
    }
}
