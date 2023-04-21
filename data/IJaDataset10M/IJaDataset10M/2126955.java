package org.formaria.oracle.forms.xml.node;

import org.formaria.oracle.forms.xml.visitor.FormsNodeVisitor;
import org.formaria.oracle.forms.xml.node.FormsNode;
import org.formaria.oracle.forms.xml.node.FormsNode;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ObjectLibraryTab complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ObjectLibraryTab">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;group ref="{http://xmlns.oracle.com/Forms}JdapiElements" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/choice>
 *       &lt;attribute name="DirtyInfo" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="ObjectCount" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="PersistentClientInfoLength" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="Comment" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Label" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Name" use="required" type="{http://xmlns.oracle.com/Forms}nameType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObjectLibraryTab", propOrder = { "alertOrAttachedLibraryOrBlock" })
public class ObjectLibraryTab implements FormsNode {

    @XmlElements({ @XmlElement(name = "Relation", type = Relation.class), @XmlElement(name = "ModuleParameter", type = ModuleParameter.class), @XmlElement(name = "TextSegment", type = TextSegment.class), @XmlElement(name = "CompoundText", type = CompoundText.class), @XmlElement(name = "TabPage", type = TabPage.class), @XmlElement(name = "AttachedLibrary", type = AttachedLibrary.class), @XmlElement(name = "Trigger", type = Trigger.class), @XmlElement(name = "Item", type = Item.class), @XmlElement(name = "Canvas", type = Canvas.class), @XmlElement(name = "VisualState", type = VisualState.class), @XmlElement(name = "Coordinate", type = Coordinate.class), @XmlElement(name = "VisualAttribute", type = VisualAttribute.class), @XmlElement(name = "ProgramUnit", type = ProgramUnit.class), @XmlElement(name = "Window", type = Window.class), @XmlElement(name = "Block", type = Block.class), @XmlElement(name = "Menu", type = Menu.class), @XmlElement(name = "LOVColumnMapping", type = LOVColumnMapping.class), @XmlElement(name = "Point", type = Point.class), @XmlElement(name = "ObjectGroup", type = ObjectGroup.class), @XmlElement(name = "Report", type = Report.class), @XmlElement(name = "Alert", type = Alert.class), @XmlElement(name = "Editor", type = Editor.class), @XmlElement(name = "RecordGroupColumn", type = RecordGroupColumn.class), @XmlElement(name = "LOV", type = LOV.class), @XmlElement(name = "Graphics", type = Graphics.class), @XmlElement(name = "PropertyClass", type = PropertyClass.class), @XmlElement(name = "RadioButton", type = RadioButton.class), @XmlElement(name = "RecordGroup", type = RecordGroup.class), @XmlElement(name = "DataSourceColumn", type = DataSourceColumn.class), @XmlElement(name = "MenuItem", type = MenuItem.class), @XmlElement(name = "Font", type = Font.class), @XmlElement(name = "DataSourceArgument", type = DataSourceArgument.class) })
    protected List<Object> alertOrAttachedLibraryOrBlock;

    @XmlAttribute(name = "DirtyInfo")
    protected Boolean dirtyInfo;

    @XmlAttribute(name = "ObjectCount")
    protected BigInteger objectCount;

    @XmlAttribute(name = "PersistentClientInfoLength")
    protected BigInteger persistentClientInfoLength;

    @XmlAttribute(name = "Comment")
    protected String comment;

    @XmlAttribute(name = "Label")
    protected String label;

    @XmlAttribute(name = "Name", required = true)
    protected String name;

    /**
   * Gets the value of the alertOrAttachedLibraryOrBlock property.
   *
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the alertOrAttachedLibraryOrBlock property.
   *
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getAlertOrAttachedLibraryOrBlock().add(newItem);
   * </pre>
   *
   *
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link Relation }
   * {@link ModuleParameter }
   * {@link TextSegment }
   * {@link CompoundText }
   * {@link TabPage }
   * {@link AttachedLibrary }
   * {@link Trigger }
   * {@link Item }
   * {@link Canvas }
   * {@link VisualState }
   * {@link Coordinate }
   * {@link VisualAttribute }
   * {@link ProgramUnit }
   * {@link Window }
   * {@link Block }
   * {@link Menu }
   * {@link LOVColumnMapping }
   * {@link Point }
   * {@link ObjectGroup }
   * {@link Report }
   * {@link Alert }
   * {@link Editor }
   * {@link RecordGroupColumn }
   * {@link LOV }
   * {@link Graphics }
   * {@link PropertyClass }
   * {@link RadioButton }
   * {@link RecordGroup }
   * {@link DataSourceColumn }
   * {@link MenuItem }
   * {@link Font }
   * {@link DataSourceArgument }
   *
   *
   */
    public List<Object> getAlertOrAttachedLibraryOrBlock() {
        if (alertOrAttachedLibraryOrBlock == null) {
            alertOrAttachedLibraryOrBlock = new ArrayList<Object>();
        }
        return this.alertOrAttachedLibraryOrBlock;
    }

    /**
   * Gets the value of the dirtyInfo property.
   *
   * @return
   *     possible object is
   *     {@link Boolean }
   *
   */
    public Boolean isDirtyInfo() {
        return dirtyInfo;
    }

    /**
   * Sets the value of the dirtyInfo property.
   *
   * @param value
   *     allowed object is
   *     {@link Boolean }
   *
   */
    public void setDirtyInfo(Boolean value) {
        this.dirtyInfo = value;
    }

    /**
   * Gets the value of the objectCount property.
   *
   * @return
   *     possible object is
   *     {@link BigInteger }
   *
   */
    public BigInteger getObjectCount() {
        return objectCount;
    }

    /**
   * Sets the value of the objectCount property.
   *
   * @param value
   *     allowed object is
   *     {@link BigInteger }
   *
   */
    public void setObjectCount(BigInteger value) {
        this.objectCount = value;
    }

    /**
   * Gets the value of the persistentClientInfoLength property.
   *
   * @return
   *     possible object is
   *     {@link BigInteger }
   *
   */
    public BigInteger getPersistentClientInfoLength() {
        return persistentClientInfoLength;
    }

    /**
   * Sets the value of the persistentClientInfoLength property.
   *
   * @param value
   *     allowed object is
   *     {@link BigInteger }
   *
   */
    public void setPersistentClientInfoLength(BigInteger value) {
        this.persistentClientInfoLength = value;
    }

    /**
   * Gets the value of the comment property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
    public String getComment() {
        return comment;
    }

    /**
   * Sets the value of the comment property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
   * Gets the value of the label property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
    public String getLabel() {
        return label;
    }

    /**
   * Sets the value of the label property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
    public void setLabel(String value) {
        this.label = value;
    }

    /**
   * Gets the value of the name property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
    public String getName() {
        return name;
    }

    /**
   * Sets the value of the name property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
    public void setName(String value) {
        this.name = value;
    }

    @Override
    public Object accept(FormsNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override
    public Object childrenAccept(FormsNodeVisitor visitor, Object data) {
        List<Object> children = getAlertOrAttachedLibraryOrBlock();
        for (Object child : children) {
            ((FormsNode) child).accept(visitor, data);
        }
        return data;
    }
}
