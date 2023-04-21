package hu.sztaki.lpds.information.data;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author krisztian
 */
@Entity
@Table(name = "groupproperties")
public class GroupPropertyBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Getting the unique internal ID
     * @return unique ID
     */
    public long getId() {
        return id;
    }

    /**
     * Setting the unique internal ID
     * @param id unique ID
     */
    public void setId(long id) {
        this.id = id;
    }

    private String name;

    /**
     * Getting property name (for example: wms)
     * @return property name
     */
    public String getName() {
        return name;
    }

    /**
     * Setting property name
     * @param name property name
     */
    public void setName(String name) {
        this.name = name;
    }

    private String value;

    /**
     * Getting property value
     * @return property value
     */
    public String getValue() {
        return value;
    }

    /**
     * Setting property value
     * @param value property value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        return hash;
    }

    /**
     * @see Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GroupPropertyBean)) {
            return false;
        }
        GroupPropertyBean other = (GroupPropertyBean) object;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    /**
     * @see Object#toString() 
     */
    @Override
    public String toString() {
        return "hu.sztaki.lpds.information.data.GroupPropertyBean[id=" + id + "]";
    }
}
