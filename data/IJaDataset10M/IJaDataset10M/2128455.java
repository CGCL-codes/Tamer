package com.voxdei.voxcontentSE.DAO.vdFrontpage;

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
 * VdFrontpage is a mapping of vd_frontpage Table.
 * @author Michael Salgado
 * @company VoxDei.
*/
@Entity
@Table(name = "vd_frontpage")
public class VdFrontpage implements Serializable, Basic {

    private static final long serialVersionUID = 2872702402849603697L;

    @Column(name = "ORDERNUM", nullable = false)
    private Long ordernum;

    @Id
    @Column(name = "ID_CONTENT", nullable = false)
    private Long idContent;

    /**
     * Constructor
     */
    public VdFrontpage() {
    }

    /**
     * Getter method for ordernum.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_frontpage.ORDERNUM</li>
     * <li>column size: 10</li>
     * <li>jdbc type returned by the driver: Types.INTEGER</li>
     * </ul>
     *
     * @return the value of ordernum
     */
    public Long getOrdernum() {
        return ordernum;
    }

    /**
     * Setter method for ordernum.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to ordernum
     */
    public void setOrdernum(Long newVal) {
        if ((newVal != null && ordernum != null && (newVal.compareTo(ordernum) == 0)) || (newVal == null && ordernum == null)) {
            return;
        }
        ordernum = newVal;
    }

    /**
     * Setter method for ordernum.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to ordernum
     */
    public void setOrdernum(long newVal) {
        setOrdernum(new Long(newVal));
    }

    /**
     * Getter method for idContent.
     * <br>
     * PRIMARY KEY.<br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_frontpage.ID_CONTENT</li>
     * <li>column size: 10</li>
     * <li>jdbc type returned by the driver: Types.INTEGER</li>
     * </ul>
     *
     * @return the value of idContent
     */
    public Long getIdContent() {
        return idContent;
    }

    /**
     * Setter method for idContent.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to idContent
     */
    public void setIdContent(Long newVal) {
        if ((newVal != null && idContent != null && (newVal.compareTo(idContent) == 0)) || (newVal == null && idContent == null)) {
            return;
        }
        idContent = newVal;
    }

    /**
     * Setter method for idContent.
     * <br>
     * Convenient for those who do not want to deal with Objects for primary types.
     *
     * @param newVal the new value to be assigned to idContent
     */
    public void setIdContent(long newVal) {
        setIdContent(new Long(newVal));
    }

    /**
     * Copies the passed bean into the current bean.
     *
     * @param bean the bean to copy into the current bean
     */
    public void copy(VdFrontpage bean) {
        setOrdernum(bean.getOrdernum());
        setIdContent(bean.getIdContent());
    }

    /**
     * return a dictionnary of the object
     */
    @Override
    public Map<String, String> getDictionnary() {
        Map<String, String> dictionnary = new HashMap<String, String>();
        dictionnary.put("ordernum", getOrdernum() == null ? "" : getOrdernum().toString());
        dictionnary.put("id_content", getIdContent() == null ? "" : getIdContent().toString());
        return dictionnary;
    }

    /**
     * return a dictionnary of the pk columns
     */
    @Override
    public Map<String, String> getPkDictionnary() {
        Map<String, String> dictionnary = new HashMap<String, String>();
        dictionnary.put("id_content", getIdContent() == null ? "" : getIdContent().toString());
        return dictionnary;
    }

    /**
     * return a the value string representation of the given field
     */
    @Override
    public String getValue(String column) {
        if (null == column || "".equals(column)) {
            return "";
        } else if ("ORDERNUM".equalsIgnoreCase(column) || "ordernum".equalsIgnoreCase(column)) {
            return getOrdernum() == null ? "" : getOrdernum().toString();
        } else if ("ID_CONTENT".equalsIgnoreCase(column) || "idContent".equalsIgnoreCase(column)) {
            return getIdContent() == null ? "" : getIdContent().toString();
        }
        return "";
    }

    /**
	 * @see java.lang.Object#equals(Object)
	 */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof VdFrontpage)) {
            return false;
        }
        VdFrontpage obj = (VdFrontpage) object;
        return new EqualsBuilder().append(getOrdernum(), obj.getOrdernum()).append(getIdContent(), obj.getIdContent()).isEquals();
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(-82280557, -700257973).append(getOrdernum()).append(getIdContent()).toHashCode();
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
        return new ToStringBuilder(this, style).append("ORDERNUM", getOrdernum()).append("ID_CONTENT", getIdContent()).toString();
    }

    public int compareTo(Object object) {
        VdFrontpage obj = (VdFrontpage) object;
        return new CompareToBuilder().append(getOrdernum(), obj.getOrdernum()).append(getIdContent(), obj.getIdContent()).toComparison();
    }
}
