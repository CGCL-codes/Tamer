package at.campus02.datapit.core.algorithm.parameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * @author Gerhard Schlager
 */
@XmlType(name = "ParameterSpecification", propOrder = { "name", "description", "dataType", "defaultValue", "enumItems" })
@XmlAccessorType(XmlAccessType.FIELD)
public class ParameterSpecification {

    @XmlElement(name = "Name", nillable = false, required = true)
    @XmlSchemaType(name = "token")
    private String name;

    @XmlElement(name = "Description", nillable = false, required = true)
    private String description;

    @XmlElement(name = "DataType", nillable = false, required = true)
    private ParameterDataType dataType;

    @XmlElement(name = "DefaultValue", nillable = false, required = true)
    private Object defaultValue;

    @XmlElement(name = "EnumItems", nillable = false, required = false)
    private List<EnumListItem> enumItems = new ArrayList<EnumListItem>();

    public ParameterSpecification() {
    }

    public ParameterSpecification(String name, String description, ParameterDataType dataType, Object defaultValue) {
        this.name = name;
        this.description = description;
        this.dataType = dataType;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ParameterDataType getDataType() {
        return dataType;
    }

    public void setDataType(ParameterDataType dataType) {
        this.dataType = dataType;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public List<EnumListItem> getEnumItems() {
        return Collections.unmodifiableList(enumItems);
    }

    public void addEnumItem(EnumListItem item) {
        enumItems.add(item);
    }

    public void removeEnumItem(EnumListItem item) {
        enumItems.remove(item);
    }
}
