package gleam.executive.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * This class is used to generate the Hibernate mapping file.
 * 
 * <p>
 * <a href="AnnotationServiceType.java.html"><i>View Source</i></a>
 * 
 * @author <a href="mailto:agaton@dcs.shef.ac.uk">Milan Agatonovic</a>
 * 
 * @struts.form include-all="true" extends="BaseForm"
 * @hibernate.class table="annotation_service_type"
 */
public class AnnotationServiceType extends BaseObject implements Serializable {

    protected Long id;

    protected String name;

    protected String data;

    /**
	 * @hibernate.id column="id" generator-class="native" unsaved-value="null"
	 * 
	 */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * 
	 * @hibernate.property column="name" length="100" not-null="true"
	 *                     unique="true"
	 */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @hibernate.property column="data" type = "text"                    
	 */
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnnotationServiceType)) return false;
        final AnnotationServiceType annotationServiceType = (AnnotationServiceType) o;
        if (name != null ? !name.equals(annotationServiceType.getName()) : annotationServiceType.getName() != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return (name != null ? name.hashCode() : 0);
    }

    @Override
    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("name", this.name);
        return sb.toString();
    }
}
