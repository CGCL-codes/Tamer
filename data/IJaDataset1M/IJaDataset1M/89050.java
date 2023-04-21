package org.broadleafcommerce.cms.field.domain;

import org.broadleafcommerce.openadmin.client.presentation.SupportedFieldType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * Created by bpolster.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "BLC_FLD_DEF")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "blCMSElements")
public class FieldDefinitionImpl implements FieldDefinition {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "FieldDefinitionId", strategy = GenerationType.TABLE)
    @TableGenerator(name = "FieldDefinitionId", table = "SEQUENCE_GENERATOR", pkColumnName = "ID_NAME", valueColumnName = "ID_VAL", pkColumnValue = "FieldDefinitionImpl", allocationSize = 10)
    @Column(name = "FLD_DEF_ID")
    protected Long id;

    @Column(name = "NAME")
    protected String name;

    @Column(name = "FRIENDLY_NAME")
    protected String friendlyName;

    @Column(name = "FLD_TYPE")
    protected String fieldType;

    @Column(name = "SECURITY_LEVEL")
    protected String securityLevel;

    @Column(name = "HIDDEN_FLAG")
    protected Boolean hiddenFlag = false;

    @Column(name = "VLDTN_REGEX")
    protected String validationRegEx;

    @Column(name = "VLDTN_ERROR_MSSG_KEY")
    protected String validationErrorMesageKey;

    @Column(name = "MAX_LENGTH")
    protected Integer maxLength;

    @Column(name = "COLUMN_WIDTH")
    protected String columnWidth;

    @Column(name = "TEXT_AREA_FLAG")
    protected Boolean textAreaFlag = false;

    @ManyToOne(targetEntity = FieldEnumerationImpl.class)
    @JoinColumn(name = "FLD_ENUM_ID")
    protected FieldEnumeration fieldEnumeration;

    @Column(name = "ALLOW_MULTIPLES")
    protected Boolean allowMultiples = false;

    @ManyToOne(targetEntity = FieldGroupImpl.class)
    @JoinColumn(name = "FLD_GROUP_ID")
    protected FieldGroup fieldGroup;

    @Column(name = "FLD_ORDER")
    protected int fieldOrder;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public SupportedFieldType getFieldType() {
        return fieldType != null ? SupportedFieldType.valueOf(fieldType) : null;
    }

    @Override
    public void setFieldType(SupportedFieldType fieldType) {
        this.fieldType = fieldType != null ? fieldType.toString() : null;
    }

    @Override
    public String getSecurityLevel() {
        return securityLevel;
    }

    @Override
    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

    @Override
    public Boolean getHiddenFlag() {
        return hiddenFlag;
    }

    @Override
    public void setHiddenFlag(Boolean hiddenFlag) {
        this.hiddenFlag = hiddenFlag;
    }

    @Override
    public String getValidationRegEx() {
        return validationRegEx;
    }

    @Override
    public void setValidationRegEx(String validationRegEx) {
        this.validationRegEx = validationRegEx;
    }

    @Override
    public Integer getMaxLength() {
        return maxLength;
    }

    @Override
    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public String getColumnWidth() {
        return columnWidth;
    }

    @Override
    public void setColumnWidth(String columnWidth) {
        this.columnWidth = columnWidth;
    }

    @Override
    public Boolean getTextAreaFlag() {
        return textAreaFlag;
    }

    @Override
    public void setTextAreaFlag(Boolean textAreaFlag) {
        this.textAreaFlag = textAreaFlag;
    }

    @Override
    public Boolean getAllowMultiples() {
        return allowMultiples;
    }

    @Override
    public void setAllowMultiples(Boolean allowMultiples) {
        this.allowMultiples = allowMultiples;
    }

    @Override
    public String getFriendlyName() {
        return friendlyName;
    }

    @Override
    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String getValidationErrorMesageKey() {
        return validationErrorMesageKey;
    }

    @Override
    public void setValidationErrorMesageKey(String validationErrorMesageKey) {
        this.validationErrorMesageKey = validationErrorMesageKey;
    }

    @Override
    public FieldGroup getFieldGroup() {
        return fieldGroup;
    }

    @Override
    public void setFieldGroup(FieldGroup fieldGroup) {
        this.fieldGroup = fieldGroup;
    }

    @Override
    public int getFieldOrder() {
        return fieldOrder;
    }

    @Override
    public void setFieldOrder(int fieldOrder) {
        this.fieldOrder = fieldOrder;
    }

    @Override
    public FieldEnumeration getFieldEnumeration() {
        return fieldEnumeration;
    }

    @Override
    public void setFieldEnumeration(FieldEnumeration fieldEnumeration) {
        this.fieldEnumeration = fieldEnumeration;
    }
}
