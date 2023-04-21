package cw.studentmanagementmodul.pojo;

import com.jgoodies.binding.beans.Model;
import cw.boardingschoolmanagement.persistence.AnnotatedClass;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

/**
 * @author CreativeWorkers.at
 */
@Entity
public class OrganisationUnit extends Model implements AnnotatedClass {

    private Long id = null;

    private String name = "";

    private OrganisationUnit parent = null;

    private List<OrganisationUnit> children = new ArrayList<OrganisationUnit>();

    private List<StudentClass> studentClasses = new ArrayList<StudentClass>();

    public static final String PROPERTYNAME_ID = "id";

    public static final String PROPERTYNAME_NAME = "name";

    public static final String PROPERTYNAME_PARENT = "parent";

    public static final String PROPERTYNAME_CHILDREN = "children";

    public static final String PROPERTYNAME_STUDENTCLASSES = "studentClasses";

    public OrganisationUnit() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof OrganisationUnit)) {
            return false;
        }
        if (this.getId() != ((OrganisationUnit) obj).getId()) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (parent != null) {
            builder.append(parent.toString());
            builder.append(" / ");
        }
        builder.append(name);
        return builder.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String old = this.name;
        this.name = name;
        firePropertyChange(PROPERTYNAME_NAME, old, name);
    }

    @ManyToOne
    public OrganisationUnit getParent() {
        return parent;
    }

    public void setParent(OrganisationUnit parent) {
        OrganisationUnit old = this.parent;
        this.parent = parent;
        firePropertyChange(PROPERTYNAME_PARENT, old, parent);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        Long old = this.id;
        this.id = id;
        firePropertyChange(PROPERTYNAME_ID, old, id);
    }

    @OneToMany(mappedBy = "parent")
    @OrderBy("name")
    public List<OrganisationUnit> getChildren() {
        return children;
    }

    public void setChildren(List<OrganisationUnit> children) {
        List<OrganisationUnit> old = this.children;
        this.children = children;
        firePropertyChange(PROPERTYNAME_CHILDREN, old, children);
    }

    @OneToMany(mappedBy = "organisationUnit")
    public List<StudentClass> getStudentClasses() {
        return studentClasses;
    }

    public void setStudentClasses(List<StudentClass> studentClasses) {
        List<StudentClass> old = this.studentClasses;
        this.studentClasses = studentClasses;
        firePropertyChange(PROPERTYNAME_STUDENTCLASSES, old, studentClasses);
    }
}
