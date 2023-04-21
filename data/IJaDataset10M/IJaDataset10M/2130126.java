package com.ivis.xprocess.core;

import java.util.Set;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.framework.annotations.Property;
import com.ivis.xprocess.framework.properties.PropertyType;

/**
 * A Role is the association of a Person with a RoleType
 *
 */
@com.ivis.xprocess.framework.annotations.Element(designator = "RL")
public interface Role extends Xelement {

    /**
     * Property name for RoleType
     */
    public static final String ROLETYPE = "ROLETYPE";

    @Property(propertyType = PropertyType.REFERENCE)
    public Person getPerson();

    /**
     * Is the Role suitable to use for this Task (on this day)
     *
     * @param requiredResource
     * @return true - if the Role and SkillLevels are available on that day and
     *         that the Roles Roletype matches or is a subtype of the Tasks
     *         RoleType, otherwise it returns false.
     *
     */
    public boolean isSuitableFor(RequiredResource requiredResource);

    @Property(name = ROLETYPE, propertyType = PropertyType.REFERENCE)
    public RoleType getRoleType();

    public void setRoleType(RoleType type);

    public void delete();

    /**
     * @return the set of xelements that will be deleted as a result of deleting this Role
     */
    public Set<Xelement> getDeleteWarnings();
}
