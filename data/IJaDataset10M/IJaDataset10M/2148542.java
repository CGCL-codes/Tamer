package com.voxdei.voxcontentSE.DAO.vdVideoContent;

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
 * VdVideoContent is a mapping of vd_video_content Table.
 * @author Michael Salgado
 * @company VoxDei.
*/
@Entity
@Table(name = "vd_video_content")
public class VdVideoContent implements Serializable, Basic {

    private static final long serialVersionUID = 2876007369282971953L;

    @Column(name = "SERVER", nullable = false)
    private String server;

    @Column(name = "ID_CONTENT", nullable = false)
    private Long idContent;

    @Column(name = "FOOTPAGE", nullable = false)
    private String footpage;

    @Column(name = "URL", nullable = false)
    private String url;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    /**
     * Constructor
     */
    public VdVideoContent() {
    }

    /**
     * Getter method for server.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_video_content.SERVER</li>
     * <li>column size: 255</li>
     * <li>jdbc type returned by the driver: Types.VARCHAR</li>
     * </ul>
     *
     * @return the value of server
     */
    public String getServer() {
        return server;
    }

    /**
     * Setter method for server.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to server
     */
    public void setServer(String newVal) {
        if ((newVal != null && server != null && (newVal.compareTo(server) == 0)) || (newVal == null && server == null)) {
            return;
        }
        server = newVal;
    }

    /**
     * Getter method for idContent.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_video_content.ID_CONTENT</li>
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
     * Getter method for footpage.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_video_content.FOOTPAGE</li>
     * <li>column size: 16777215</li>
     * <li>jdbc type returned by the driver: Types.LONGVARCHAR</li>
     * </ul>
     *
     * @return the value of footpage
     */
    public String getFootpage() {
        return footpage;
    }

    /**
     * Setter method for footpage.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to footpage
     */
    public void setFootpage(String newVal) {
        if ((newVal != null && footpage != null && (newVal.compareTo(footpage) == 0)) || (newVal == null && footpage == null)) {
            return;
        }
        footpage = newVal;
    }

    /**
     * Getter method for url.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_video_content.URL</li>
     * <li>column size: 255</li>
     * <li>jdbc type returned by the driver: Types.VARCHAR</li>
     * </ul>
     *
     * @return the value of url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter method for url.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to url
     */
    public void setUrl(String newVal) {
        if ((newVal != null && url != null && (newVal.compareTo(url) == 0)) || (newVal == null && url == null)) {
            return;
        }
        url = newVal;
    }

    /**
     * Getter method for title.
     * <br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_video_content.TITLE</li>
     * <li>column size: 255</li>
     * <li>jdbc type returned by the driver: Types.VARCHAR</li>
     * </ul>
     *
     * @return the value of title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter method for title.
     * <br>
     * The new value is set only if compareTo() says it is different,
     * or if one of either the new value or the current value is null.
     * In case the new value is different, it is set and the field is marked as 'modified'.
     *
     * @param newVal the new value to be assigned to title
     */
    public void setTitle(String newVal) {
        if ((newVal != null && title != null && (newVal.compareTo(title) == 0)) || (newVal == null && title == null)) {
            return;
        }
        title = newVal;
    }

    /**
     * Getter method for id.
     * <br>
     * PRIMARY KEY.<br>
     * Meta Data Information (in progress):
     * <ul>
     * <li>full name: vd_video_content.ID</li>
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
    public void copy(VdVideoContent bean) {
        setServer(bean.getServer());
        setIdContent(bean.getIdContent());
        setFootpage(bean.getFootpage());
        setUrl(bean.getUrl());
        setTitle(bean.getTitle());
        setId(bean.getId());
    }

    /**
     * return a dictionnary of the object
     */
    @Override
    public Map<String, String> getDictionnary() {
        Map<String, String> dictionnary = new HashMap<String, String>();
        dictionnary.put("server", getServer() == null ? "" : getServer().toString());
        dictionnary.put("id_content", getIdContent() == null ? "" : getIdContent().toString());
        dictionnary.put("footpage", getFootpage() == null ? "" : getFootpage().toString());
        dictionnary.put("url", getUrl() == null ? "" : getUrl().toString());
        dictionnary.put("title", getTitle() == null ? "" : getTitle().toString());
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
        } else if ("SERVER".equalsIgnoreCase(column) || "server".equalsIgnoreCase(column)) {
            return getServer() == null ? "" : getServer().toString();
        } else if ("ID_CONTENT".equalsIgnoreCase(column) || "idContent".equalsIgnoreCase(column)) {
            return getIdContent() == null ? "" : getIdContent().toString();
        } else if ("FOOTPAGE".equalsIgnoreCase(column) || "footpage".equalsIgnoreCase(column)) {
            return getFootpage() == null ? "" : getFootpage().toString();
        } else if ("URL".equalsIgnoreCase(column) || "url".equalsIgnoreCase(column)) {
            return getUrl() == null ? "" : getUrl().toString();
        } else if ("TITLE".equalsIgnoreCase(column) || "title".equalsIgnoreCase(column)) {
            return getTitle() == null ? "" : getTitle().toString();
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
        if (!(object instanceof VdVideoContent)) {
            return false;
        }
        VdVideoContent obj = (VdVideoContent) object;
        return new EqualsBuilder().append(getServer(), obj.getServer()).append(getIdContent(), obj.getIdContent()).append(getFootpage(), obj.getFootpage()).append(getUrl(), obj.getUrl()).append(getTitle(), obj.getTitle()).append(getId(), obj.getId()).isEquals();
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(-82280557, -700257973).append(getServer()).append(getIdContent()).append(getFootpage()).append(getUrl()).append(getTitle()).append(getId()).toHashCode();
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
        return new ToStringBuilder(this, style).append("SERVER", getServer()).append("ID_CONTENT", getIdContent()).append("FOOTPAGE", getFootpage()).append("URL", getUrl()).append("TITLE", getTitle()).append("ID", getId()).toString();
    }

    public int compareTo(Object object) {
        VdVideoContent obj = (VdVideoContent) object;
        return new CompareToBuilder().append(getServer(), obj.getServer()).append(getIdContent(), obj.getIdContent()).append(getFootpage(), obj.getFootpage()).append(getUrl(), obj.getUrl()).append(getTitle(), obj.getTitle()).append(getId(), obj.getId()).toComparison();
    }
}
