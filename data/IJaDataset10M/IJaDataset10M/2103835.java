package com.cosmos.acacia.crm.data;

import com.cosmos.acacia.annotation.Property;
import com.cosmos.acacia.annotation.PropertyValidator;
import com.cosmos.acacia.annotation.ValidationType;
import com.cosmos.resource.TextResource;
import java.io.Serializable;
import java.util.UUID;
import java.util.Map;
import java.util.TreeMap;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

/**
 *
 * @author Miro
 */
@Entity
@Table(name = "classifier_groups")
@NamedQueries({ @NamedQuery(name = ClassifierGroup.NQ_FIND_ALL, query = "select t from ClassifierGroup t" + " where" + "  t.businessPartnerId = :businessPartnerId" + " order by t.classifierGroupCode"), @NamedQuery(name = ClassifierGroup.NQ_FIND_BY_CODE, query = "select t from ClassifierGroup t" + " where" + "  t.businessPartnerId = :businessPartnerId" + "  and lower(t.classifierGroupCode) = lower(:classifierGroupCode)") })
public class ClassifierGroup extends DataObjectBean implements Serializable, TextResource {

    private static final long serialVersionUID = 1L;

    private static final String CLASS_NAME = "ClassifierGroup";

    public static final String NQ_FIND_ALL = CLASS_NAME + ".findAll";

    public static final String NQ_FIND_BY_CODE = CLASS_NAME + ".findByCode";

    public static final ClassifierGroup System = new ClassifierGroup();

    public static final Map<String, ClassifierGroup> ConstantsMap = new TreeMap<String, ClassifierGroup>();

    static {
        System.setClassifierGroupCode("System");
        System.setClassifierGroupName("System Group");
        System.setDescription("The System Classifier Group");
        System.setIsSystemGroup(true);
        setClassifierGroup(System);
    }

    private static final void setClassifierGroup(ClassifierGroup classifierGroup) {
        ConstantsMap.put(classifierGroup.getClassifierGroupCode(), classifierGroup);
    }

    @Id
    @Column(name = "classifier_group_id", nullable = false)
    @Type(type = "uuid")
    @Property(title = "Classifier Group Id", editable = false, readOnly = true, visible = false, hidden = true)
    private UUID classifierGroupId;

    @Column(name = "business_partner_id", nullable = false)
    @Type(type = "uuid")
    @Property(title = "Parent Id", editable = false, readOnly = true, visible = false, hidden = true)
    private UUID businessPartnerId;

    @Column(name = "classifier_group_code", nullable = false)
    @Property(title = "Group Code", propertyValidator = @PropertyValidator(validationType = ValidationType.LENGTH, maxLength = 32))
    private String classifierGroupCode;

    @Column(name = "classifier_group_name", nullable = false)
    @Property(title = "Group Name", propertyValidator = @PropertyValidator(validationType = ValidationType.LENGTH, maxLength = 100))
    private String classifierGroupName;

    @Column(name = "is_system_group", nullable = false)
    @Property(title = "System")
    private boolean isSystemGroup;

    @Column(name = "description")
    @Property(title = "Description")
    private String description;

    @JoinColumn(name = "classifier_group_id", referencedColumnName = "data_object_id", insertable = false, updatable = false)
    @OneToOne
    private DataObject dataObject;

    public ClassifierGroup() {
    }

    public ClassifierGroup(UUID classifierGroupId) {
        this.classifierGroupId = classifierGroupId;
    }

    public ClassifierGroup(UUID classifierGroupId, boolean isSystemGroup, String classifierGroupCode, String classifierGroupName) {
        this.classifierGroupId = classifierGroupId;
        this.isSystemGroup = isSystemGroup;
        this.classifierGroupCode = classifierGroupCode;
        this.classifierGroupName = classifierGroupName;
    }

    public UUID getClassifierGroupId() {
        return classifierGroupId;
    }

    public void setClassifierGroupId(UUID classifierGroupId) {
        this.classifierGroupId = classifierGroupId;
    }

    public UUID getBusinessPartnerId() {
        return businessPartnerId;
    }

    public void setBusinessPartnerId(UUID businessPartnerId) {
        this.businessPartnerId = businessPartnerId;
    }

    @Override
    public UUID getParentId() {
        return getBusinessPartnerId();
    }

    @Override
    public void setParentId(UUID parentId) {
        setBusinessPartnerId(parentId);
    }

    public boolean getIsSystemGroup() {
        return isSystemGroup;
    }

    public void setIsSystemGroup(boolean isSystemGroup) {
        this.isSystemGroup = isSystemGroup;
    }

    public String getClassifierGroupCode() {
        return classifierGroupCode;
    }

    public void setClassifierGroupCode(String classifierGroupCode) {
        this.classifierGroupCode = classifierGroupCode;
    }

    public String getClassifierGroupName() {
        return classifierGroupName;
    }

    public void setClassifierGroupName(String classifierGroupName) {
        this.classifierGroupName = classifierGroupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public DataObject getDataObject() {
        return dataObject;
    }

    @Override
    public void setDataObject(DataObject dataObject) {
        this.dataObject = dataObject;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("; code=").append(classifierGroupCode);
        sb.append(", name=").append(classifierGroupName);
        return sb.toString();
    }

    @Override
    public UUID getId() {
        return classifierGroupId;
    }

    @Override
    public void setId(UUID id) {
        this.classifierGroupId = id;
    }

    @Override
    public String toShortText() {
        return null;
    }

    @Override
    public String toText() {
        return classifierGroupName + " (" + classifierGroupCode + ")";
    }

    @Override
    public String getInfo() {
        return getClassifierGroupName();
    }
}
