package teachin.domain.teachin.model.obj.teachin;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
 * Object mapping for hibernate-handled table: grading_type
 * author: auto-generated
 */
@Entity
@Table(name = "grading_type", catalog = "teachin")
public class GradingType implements Cloneable, Serializable {

    /** Serial Version UID */
    private static final long serialVersionUID = -559039806L;

    /** Field mapping */
    private Integer id;

    /** Field mapping */
    private String name;

    /**
	 * Default constructor, mainly for hibernate use
	 */
    public GradingType() {
    }

    /** Constructor taking a given ID
	 * @param id to set
	 */
    public GradingType(Integer id) {
        this.id = id;
    }

    /**
     * Return the value associated with the column: id
	 * @return A Integer object (this.id)
	 */
    @Id
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return this.id;
    }

    /**  
     * Set the value related to the column: id 
	 * @param id the id value you wish to set
	 */
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * Return the value associated with the column: name
	 * @return A String object (this.name)
	 */
    @Column(length = 100)
    public String getName() {
        return this.name;
    }

    /**  
     * Set the value related to the column: name 
	 * @param name the name value you wish to set
	 */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Deep copy
	 * @throws CloneNotSupportedException
     */
    @Override
    public GradingType clone() throws CloneNotSupportedException {
        super.clone();
        GradingType copy = new GradingType();
        copy.setId(this.getId());
        copy.setName(this.getName());
        return copy;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id: " + this.id + ", ");
        sb.append("name: " + this.name);
        return sb.toString();
    }

    @Override
    public boolean equals(Object aThat) {
        if (this == aThat) return true;
        if ((aThat == null) || (!(aThat.getClass().equals(GradingType.class)))) return false;
        GradingType that = (GradingType) aThat;
        return (((this.id == null) && (that.id == null)) || (this.id != null && this.id.equals(that.id))) && (((this.name == null) && (that.name == null)) || (this.name != null && this.name.equals(that.name)));
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 1000003 * result + (this.id == null ? 0 : this.id.hashCode());
        result = 1000003 * result + (this.name == null ? 0 : this.name.hashCode());
        return result;
    }
}
