package com.cosmos.acacia.crm.data.security;

import com.cosmos.acacia.annotation.Component;
import com.cosmos.acacia.annotation.Form;
import com.cosmos.acacia.annotation.FormComponentPair;
import com.cosmos.acacia.annotation.FormContainer;
import com.cosmos.acacia.annotation.Property;
import com.cosmos.acacia.annotation.RelationshipType;
import com.cosmos.acacia.annotation.SelectableList;
import com.cosmos.acacia.crm.bl.security.SecurityServiceRemote;
import com.cosmos.acacia.crm.data.DataObject;
import com.cosmos.acacia.crm.data.DataObjectBean;
import com.cosmos.acacia.crm.data.DataObjectType;
import com.cosmos.acacia.crm.data.DbResource;
import com.cosmos.swingb.JBComboList;
import com.cosmos.swingb.JBDatePicker;
import com.cosmos.swingb.JBLabel;
import com.cosmos.swingb.JBPanel;
import com.cosmos.swingb.JBTextField;
import java.io.Serializable;
import java.util.UUID;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.Type;

/**
 *
 * @author Miro
 */
@Entity
@Table(name = "privileges", catalog = "acacia", schema = "public")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discriminator_id", discriminatorType = DiscriminatorType.STRING, length = 4)
@NamedQueries({ @NamedQuery(name = Privilege.NQ_FIND_ALL, query = "SELECT p FROM Privilege p" + " WHERE" + "  p.securityRole = :securityRole" + " ORDER BY p.privilegeCategory.categoryName, p.privilegeName"), @NamedQuery(name = Privilege.NQ_FIND_BY_PRIVILEGE_NAME, query = "SELECT p FROM Privilege p" + " WHERE" + "  p.securityRole = :securityRole" + "  and lower(p.privilegeName) = lower(:privilegeName)"), @NamedQuery(name = Privilege.NQ_FIND_ALL_NOT_EXPIRED, query = "SELECT p FROM Privilege p" + " WHERE" + "  p.securityRole = :securityRole" + "  and (p.expires is null" + "  or p.expires < :expires)" + " ORDER BY p.privilegeCategory.categoryName, p.privilegeName") })
@Form(formContainers = { @FormContainer(name = "roleList", title = "Roles", depends = { FormContainer.DEPENDS_ENTITY_FORM }, container = @Component(componentClass = JBPanel.class), relationshipType = RelationshipType.OneToMany, entityClass = PrivilegeRole.class) }, serviceClass = SecurityServiceRemote.class)
public abstract class Privilege extends DataObjectBean implements Serializable {

    private static final long serialVersionUID = 1L;

    protected static final String ENTITY_DATA_OBJECT_TYPE_ID = "EDOT";

    protected static final String ENTITY_DATA_OBJECT_ID = "EDO";

    protected static final String PERMISSION_CATEGORY_ID = "PC";

    protected static final String SPECIAL_PERMISSION_ID = "SP";

    private static final String CLASS_NAME = "Privilege";

    public static final String NQ_FIND_ALL = CLASS_NAME + ".findAll";

    public static final String NQ_FIND_ALL_NOT_EXPIRED = CLASS_NAME + ".findAllNotExpired";

    public static final String NQ_FIND_BY_PRIVILEGE_NAME = CLASS_NAME + ".findByPrivilegeName";

    @Id
    @Basic(optional = false)
    @Column(name = "privilege_id", nullable = false, precision = 19, scale = 0)
    @Type(type = "uuid")
    private UUID privilegeId;

    @Basic(optional = false)
    @Column(name = "discriminator_id", nullable = false, length = 4, insertable = false, updatable = false)
    private String discriminatorId;

    @JoinColumn(name = "security_role_id", referencedColumnName = "security_role_id", nullable = false)
    @ManyToOne(optional = false)
    @Property(title = "Security Role")
    private SecurityRole securityRole;

    @JoinColumn(name = "privilege_category_id", referencedColumnName = "privilege_category_id", nullable = false)
    @ManyToOne(optional = false)
    @Property(title = "Category", index = 10, selectableList = @SelectableList(className = "com.cosmos.acacia.crm.gui.security.PrivilegeCategoryListPanel"), formComponentPair = @FormComponentPair(parentContainerName = PRIMARY_INFO, firstComponent = @Component(componentClass = JBLabel.class, text = "Category:"), secondComponent = @Component(componentClass = JBComboList.class)))
    private PrivilegeCategory privilegeCategory;

    @Basic(optional = false)
    @Column(name = "privilege_name", nullable = false, length = 100)
    @Property(title = "Name", index = 20, formComponentPair = @FormComponentPair(parentContainerName = PRIMARY_INFO, firstComponent = @Component(componentClass = JBLabel.class, text = "Name:"), secondComponent = @Component(componentClass = JBTextField.class)))
    private String privilegeName;

    @Column(name = "expires")
    @Temporal(TemporalType.TIMESTAMP)
    @Property(title = "Expires", index = 30, formComponentPair = @FormComponentPair(parentContainerName = PRIMARY_INFO, firstComponent = @Component(componentClass = JBLabel.class, text = "Expires:"), secondComponent = @Component(componentClass = JBDatePicker.class)))
    private Date expires;

    @Transient
    @Property(title = "Entity")
    private DataObject entityDataObject;

    @Transient
    @Property(title = "Entity Type")
    private DataObjectType entityDataObjectType;

    @Transient
    @Property(title = "Permission Category")
    private DbResource permissionCategory;

    @Transient
    @Property(title = "Permission")
    private DbResource specialPermission;

    @JoinColumn(name = "privilege_id", referencedColumnName = "data_object_id", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false)
    private DataObject dataObject;

    public Privilege() {
        throw new UnsupportedOperationException("This constructor is not for use.");
    }

    public Privilege(String discriminatorId) {
        this.discriminatorId = discriminatorId;
    }

    public Privilege(String discriminatorId, UUID privilegeId) {
        this(discriminatorId);
        this.privilegeId = privilegeId;
    }

    public UUID getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(UUID privilegeId) {
        this.privilegeId = privilegeId;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public String getDiscriminatorId() {
        return discriminatorId;
    }

    public void setDiscriminatorId(String discriminatorId) {
        this.discriminatorId = discriminatorId;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    @Override
    public DataObject getDataObject() {
        return dataObject;
    }

    @Override
    public void setDataObject(DataObject dataObject) {
        this.dataObject = dataObject;
    }

    public PrivilegeCategory getPrivilegeCategory() {
        return privilegeCategory;
    }

    public void setPrivilegeCategory(PrivilegeCategory privilegeCategory) {
        this.privilegeCategory = privilegeCategory;
    }

    public SecurityRole getSecurityRole() {
        return securityRole;
    }

    public void setSecurityRole(SecurityRole securityRole) {
        this.securityRole = securityRole;
        if (securityRole != null) {
            setParentId(securityRole.getId());
        } else {
            setParentId(null);
        }
    }

    public DbResource getSpecialPermission() {
        return null;
    }

    public DataObjectType getEntityDataObjectType() {
        return null;
    }

    public DataObject getEntityDataObject() {
        return null;
    }

    public DbResource getPermissionCategory() {
        return null;
    }

    @Override
    public UUID getId() {
        return getPrivilegeId();
    }

    @Override
    public void setId(UUID id) {
        setPrivilegeId(id);
    }

    @Override
    public UUID getParentId() {
        if (securityRole != null) {
            return securityRole.getSecurityRoleId();
        }
        return null;
    }

    @Override
    public String getInfo() {
        return getPrivilegeName();
    }
}
