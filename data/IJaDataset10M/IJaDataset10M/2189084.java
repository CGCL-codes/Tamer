package org.amhm.persistence;

import java.io.Serializable;
import java.lang.Object;
import java.lang.Class;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import org.amhm.core.constants.AMHMConst;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Season")
public class Season extends Post implements Serializable, Searchable {

    private static final long serialVersionUID = 705598181786241298L;

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(Season.class, true);

    /**
     * Get accessor for typeDesc
     * @return  value of typeDesc
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return Season.typeDesc;
    }

    @XmlAttribute(required = true)
    private int subscribableId;

    /**
     * Get accessor for seasonId
     * @return  value of seasonId
     */
    public int getSubscribableId() {
        return subscribableId;
    }

    /**
     * Set accessor for seasonId
     * @param value the value to set in seasonId
     */
    public void setSubscribableId(int subscribableId) {
        this.subscribableId = subscribableId;
    }

    @XmlAttribute(required = true)
    private String seasonName;

    /**
     * Get accessor for seasonName
     * @return  value of seasonName
     */
    public String getSeasonName() {
        return this.seasonName;
    }

    /**
     * Set accessor for seasonName
     * @param value the value to set in seasonName
     */
    public void setSeasonName(String value) {
        this.seasonName = value;
    }

    @XmlAttribute(required = true)
    private String seasonThema;

    /**
     * Get accessor for seasonThema
     * @return  value of seasonThema
     */
    public String getSeasonThema() {
        return this.seasonThema;
    }

    /**
     * Set accessor for seasonThema
     * @param value the value to set in seasonThema
     */
    public void setSeasonThema(String value) {
        this.seasonThema = value;
    }

    @XmlTransient
    private Tariff[] tariffs;

    public Tariff[] getTariffs() {
        return tariffs;
    }

    public void setTariffs(Tariff[] tariffs) {
        this.tariffs = tariffs;
    }

    public void addTariff(Tariff tariff) {
        this.tariffs[this.tariffs.length] = tariff;
    }

    @XmlTransient
    private Program[] programs;

    public void setPrograms(Program[] programs) {
        this.programs = programs;
    }

    public Program[] getPrograms() {
        return programs;
    }

    @XmlTransient
    private Object __equalsCalc = null;

    @XmlTransient
    private boolean __hashCodeCalc = false;

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName(AMHMConst.AMHM_URL, "Season"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subscribableId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "subscribableId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("seasonName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "seasonName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("seasonThema");
        elemField.setXmlName(new javax.xml.namespace.QName("", "seasonThema"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tariffs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tariffs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://asso.amhm.free.fr/", "Tariff"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("programs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "programs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://asso.amhm.free.fr/", "Program"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    public Season() {
    }

    public Season(int subscribableId, String seasonName, String seasonThema, Tariff[] tariffs, Program[] programs) {
        this.subscribableId = subscribableId;
        this.seasonName = seasonName;
        this.seasonThema = seasonThema;
        this.tariffs = tariffs;
        this.programs = programs;
    }

    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof Season)) return false;
        Season other = (Season) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.subscribableId == 0 && other.getSubscribableId() == 0) || (this.subscribableId != 0 && this.subscribableId == other.getSubscribableId())) && ((this.seasonName == null && other.getSeasonName() == null) || (this.seasonName != null && this.seasonName.equals(other.getSeasonName()))) && ((this.seasonThema == null && other.getSeasonThema() == null) || (this.seasonThema != null && this.seasonThema.equals(other.getSeasonThema())));
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
        if (getSeasonName() != null) {
            _hashCode += getSeasonName().hashCode();
        }
        if (getSeasonThema() != null) {
            _hashCode += getSeasonThema().hashCode();
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
        return this.seasonName;
    }

    @Override
    public Set<Subscribable> getSubscribable() {
        Set<Subscribable> set = new HashSet<Subscribable>();
        if (this.programs != null) {
            set.addAll(Arrays.asList(this.programs));
        }
        return set;
    }

    public boolean hasChild() {
        if (this.programs != null) {
            return this.programs.length > 0;
        }
        return false;
    }

    public void accept(SubscribableVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void recoverValue(Map<String, String> values) {
        for (Map.Entry<String, String> e : values.entrySet()) {
            try {
                Field subscribableId = this.getClass().getField("subscribableId");
                if (e.getKey().toUpperCase().equals(subscribableId.getName().toUpperCase())) {
                    this.subscribableId = Integer.valueOf(e.getValue());
                }
                Field seasonName = this.getClass().getField("seasonName");
                if (e.getKey().toUpperCase().equals(seasonName.getName().toUpperCase())) {
                    this.seasonName = e.getValue();
                }
                Field seasonThema = this.getClass().getField("seasonThema");
                if (e.getKey().toUpperCase().equals(seasonThema.getName().toUpperCase())) {
                    this.seasonThema = e.getValue();
                }
            } catch (SecurityException e1) {
                e1.printStackTrace();
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void accept(XmlSerializableVisitor visitor) {
        visitor.visit(this);
    }
}
