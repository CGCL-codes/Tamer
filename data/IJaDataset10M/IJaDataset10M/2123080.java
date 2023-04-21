package com.voxdei.voxcontentSE.DAO.vdDomainType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.voxdei.voxcontentSE.DAO.basic.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * VdDomainType is a mapping of vd_domain_type Table.
 * @author Michael Salgado
 * @company VoxDei.
*/
@Entity
@Table(name = "vd_domain_type")
public class VdDomainType implements Serializable, Basic {

    private static final long serialVersionUID = 2874354886066287825L;

    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled;

    @Column(name = "DESCRIPTION")
    private String description;

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    /**
     * Constructor
     */
    public VdDomainType() {
    }

    /**
     * Getter method for enabled.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_domain_type.ENABLED</li>
     * <li>column size: 0</li>
     * <li>jdbc type returned by the driver: Types.BIT</li>
     * </ul>
     *
     * @return the value of enabled
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * Setter method for enabled.
     * <br>
     * Attention, there will be no comparison with current value which
     * means calling this method will mark the field as 'modified' in all cases.
     *
     * @param newVal the new value to be assigned to enabled
     */
    public void setEnabled(Boolean newVal) {
        if ((newVal != null && enabled != null && newVal.equals(enabled)) || (newVal == null && enabled == null)) {
            return;
        }
        enabled = newVal;
    }

    /**
     * Setter method for enabled.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to enabled
     */
    public void setEnabled(boolean newVal) {
        setEnabled(new Boolean(newVal));
    }

    /**
     * Getter method for description.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_domain_type.DESCRIPTION</li>
     * <li>column size: 16777215</li>
     * <li>jdbc type returned by the driver: Types.LONGVARCHAR</li>
     * </ul>
     *
     * @return the value of description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter method for description.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to description
     */
    public void setDescription(String newVal) {
        if ((newVal != null && description != null && (newVal.compareTo(description) == 0)) || (newVal == null && description == null)) {
            return;
        }
        description = newVal;
    }

    /**
     * Getter method for id.
     * <br>
     * PRIMARY KEY.<br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_domain_type.ID</li>
     * <li>column size: 10</li>
     * <li>jdbc type returned by the driver: Types.INTEGER</li>
     * </ul>
     *
     * @return the value of id
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter method for id.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to id
     */
    public void setId(Long newVal) {
        if ((newVal != null && id != null && (newVal.compareTo(id) == 0)) || (newVal == null && id == null)) {
            return;
        }
        id = newVal;
    }

    /**
     * Setter method for id.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to id
     */
    public void setId(long newVal) {
        setId(new Long(newVal));
    }

    /**
     * Copies the passed bean into the current bean.
     *
     * @param bean the bean to copy into the current bean
     */
    public void copy(VdDomainType bean) {
        setEnabled(bean.getEnabled());
        setDescription(bean.getDescription());
        setId(bean.getId());
    }

    /**
     * return a dictionnary of the object
     */
    @Override
    public Map<String, String> getDictionnary() {
        Map<String, String> dictionnary = new HashMap<String, String>();
        dictionnary.put("enabled", getEnabled() == null ? "" : getEnabled().toString());
        dictionnary.put("description", getDescription() == null ? "" : getDescription().toString());
        dictionnary.put("id", getId() == null ? "" : getId().toString());
        return dictionnary;
    }

    /**
     * return a dictionnary of the pk columns
     */
    @Override
    public Map<String, String> getPkDictionnary() {
        Map<String, String> dictionnary = new HashMap<String, String>();
        dictionnary.put("id", getId() == null ? "" : getId().toString());
        return dictionnary;
    }

    /**
     * return a the value string representation of the given field
     */
    @Override
    public String getValue(String column) {
        if (null == column || "".equals(column)) {
            return "";
        } else if ("ENABLED".equalsIgnoreCase(column) || "enabled".equalsIgnoreCase(column)) {
            return getEnabled() == null ? "" : getEnabled().toString();
        } else if ("DESCRIPTION".equalsIgnoreCase(column) || "description".equalsIgnoreCase(column)) {
            return getDescription() == null ? "" : getDescription().toString();
        } else if ("ID".equalsIgnoreCase(column) || "id".equalsIgnoreCase(column)) {
            return getId() == null ? "" : getId().toString();
        }
        return "";
    }

    /**
	 * @see java.lang.Object#equals(Object)
	 */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof VdDomainType)) {
            return false;
        }
        VdDomainType obj = (VdDomainType) object;
        return new EqualsBuilder().append(getEnabled(), obj.getEnabled()).append(getDescription(), obj.getDescription()).append(getId(), obj.getId()).isEquals();
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(-82280557, -700257973).append(getEnabled()).append(getDescription()).append(getId()).toHashCode();
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return toString(ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
	 * you can use the following styles:
	 * <li>ToStringStyle.DEFAULT_STYLE</li>
	 * <li>ToStringStyle.MULTI_LINE_STYLE</li>
     * <li>ToStringStyle.NO_FIELD_NAMES_STYLE</li>
     * <li>ToStringStyle.SHORT_PREFIX_STYLE</li>
     * <li>ToStringStyle.SIMPLE_STYLE</li>
	 */
    public String toString(ToStringStyle style) {
        return new ToStringBuilder(this, style).append("ENABLED", getEnabled()).append("DESCRIPTION", getDescription()).append("ID", getId()).toString();
    }

    public int compareTo(Object object) {
        VdDomainType obj = (VdDomainType) object;
        return new CompareToBuilder().append(getEnabled(), obj.getEnabled()).append(getDescription(), obj.getDescription()).append(getId(), obj.getId()).toComparison();
    }
}
