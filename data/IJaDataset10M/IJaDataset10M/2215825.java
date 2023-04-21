package de.campussource.cse.cdmm.domain.decorators.jpa;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import de.campussource.cse.cdmm.domain.Attribute;
import de.campussource.cse.cdmm.domain.Category;
import de.campussource.cse.cdmm.domain.Constants;
import de.campussource.cse.cdmm.domain.Course;
import de.campussource.cse.cdmm.domain.Role;
import de.campussource.cse.cdmm.domain.State;
import de.campussource.cse.cdmm.domain.visitors.Visitor;

@Entity(name = Constants.COURSE)
@Table(name = Constants.TABLENAME_COURSE)
@Inheritance(strategy = InheritanceType.JOINED)
public class CourseJPADecorator extends Course {

    @Transient
    private Course target;

    public CourseJPADecorator() {
        target = new Course();
    }

    public CourseJPADecorator(Course course) {
        this.target = course;
    }

    public boolean addToCategory(Category category) {
        return target.addToCategory(category);
    }

    public boolean removeFromCategory(Category category) {
        return target.removeFromCategory(category);
    }

    public boolean addRole(Role role) {
        return target.addRole(role);
    }

    public boolean removeRole(Role role) {
        return target.removeRole(role);
    }

    public void addToCagetories(List<Category> categories) {
        target.addToCagetories(categories);
    }

    public boolean addAsWorkgroupTo(Course course) {
        return target.addAsWorkgroupTo(course);
    }

    public boolean removeAsWorkgroupFrom(Course course) {
        return target.removeAsWorkgroupFrom(course);
    }

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinTable(name = Constants.COLUMNNAME_PARENT_ID)
    public Course getParent() {
        return target.getParent();
    }

    public void setParent(Course parent) {
        target.setParent(parent);
    }

    @OneToMany(mappedBy = Constants.PARENT, cascade = CascadeType.REMOVE)
    public List<Course> getWorkgroups() {
        return target.getWorkgroups();
    }

    public void setWorkgroups(List<Course> workgroups) {
        target.setWorkgroups(workgroups);
    }

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinTable(name = Constants.TABLENAME_COURSE2CATEGORY, joinColumns = { @JoinColumn(name = Constants.COLUMNNAME_CATEGORY_ID) }, inverseJoinColumns = { @JoinColumn(name = Constants.COLUMNNAME_COURSE_ID) })
    public List<Category> getCategories() {
        return target.getCategories();
    }

    public void setCategories(List<Category> categories) {
        target.setCategories(categories);
    }

    @OneToMany(mappedBy = Constants.COURSE, cascade = { CascadeType.REMOVE })
    public List<Role> getRoles() {
        return target.getRoles();
    }

    public void setRoles(List<Role> roles) {
        target.setRoles(roles);
    }

    public void accept(Visitor visitor) {
        target.accept(visitor);
    }

    public State getState() {
        return target.getState();
    }

    public void setState(State state) {
        target.setState(state);
    }

    public void setId(Long id) {
        target.setId(id);
    }

    public Long getId() {
        return target.getId();
    }

    public List<Attribute> getAttributes() {
        return target.getAttributes();
    }

    public void setAttributes(List<Attribute> attributes) {
        target.setAttributes(attributes);
    }

    public Date getDate() {
        return target.getDate();
    }

    public void setDate(Date date) {
        target.setDate(date);
    }

    public boolean isOlder(Date when) {
        return target.isOlder(when);
    }

    @Override
    public boolean equals(Object other) {
        return target.equals(other);
    }

    @Override
    public String toString() {
        return target.toString();
    }
}
