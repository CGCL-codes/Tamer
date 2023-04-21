package staff.components.all.business;

import org.innoq.objectbrowser.framework.db.BusinessObject;
import java.util.Collection;

/**
 * Class DeveloperVO
 */
public class DeveloperVO extends BusinessObject implements Developer {

    /**
     * DeveloperVO is accessed via corresponding interface
     * Developer.
     *
     * @return class Developer
     */
    public Class getBOInterfaceClass() {
        return Developer.class;
    }

    /**
     * Creates an instance of Developer.
     */
    public DeveloperVO(Object oid) {
        super(oid);
        init();
    }

    /**
     * Creates an instance of Developer.
     */
    public DeveloperVO() {
        super();
        init();
    }

    private void init() {
        addAttribute("name", String.class, false);
        addAttribute("age", Integer.class, true);
        addAssociation("DeveloperNNSkill", this, staff.components.all.business.Skill.class, 1, ASSOCIATION_N);
        addAssociation("DeveloperNNProject", this, staff.components.all.business.Project.class, 0, ASSOCIATION_N);
    }

    public void setSkill(Collection pSkill) {
        getAssociation("DeveloperNNSkill").setDestinations(pSkill);
    }

    public Collection getSkill() {
        return getAssociation("DeveloperNNSkill").getDestinations();
    }

    /**
     * doesn't update the opposite end of the association
     */
    public void addToSkill(Skill pSkill) {
        getAssociation("DeveloperNNSkill").addDestination(pSkill);
    }

    /**
     * doesn't update the opposite end of the association
     */
    public void removeFromSkill(Skill pSkill) {
        getAssociation("DeveloperNNSkill").removeDestination(pSkill);
    }

    public void setProject(Collection pProject) {
        getAssociation("DeveloperNNProject").setDestinations(pProject);
    }

    public Collection getProject() {
        return getAssociation("DeveloperNNProject").getDestinations();
    }

    /**
     * doesn't update the opposite end of the association
     */
    public void addToProject(Project pProject) {
        getAssociation("DeveloperNNProject").addDestination(pProject);
    }

    /**
     * doesn't update the opposite end of the association
     */
    public void removeFromProject(Project pProject) {
        getAssociation("DeveloperNNProject").removeDestination(pProject);
    }

    /**
     * Returns the name of this class.
     */
    public String getTypeName() {
        return "Developer";
    }

    /**
     * Returns the attribute name
     *
     * @return Value of String
     */
    public String getName() {
        return (String) getAttributeValue("name");
    }

    /**
     * Sets the attribute name
     *
     * @param pName New value of attribute name
     */
    public void setName(String pName) {
        setAttributeValue("name", pName);
    }

    /**
     * Returns the attribute age
     *
     * @return Value of Integer
     */
    public Integer getAge() {
        return (Integer) getAttributeValue("age");
    }

    /**
     * Sets the attribute age
     *
     * @param pAge New value of attribute age
     */
    public void setAge(Integer pAge) {
        setAttributeValue("age", pAge);
    }

    /**
     * DeveloperVO's corresponding interface
     * Developer inherits from BusinessObject.
     *
     * @return null
     */
    public Class getBOSuperclass() {
        return null;
    }

    /**
     * probably not generated
     */
    protected String createToStringRepresentation() {
        StringBuffer sb = new StringBuffer();
        String tResult;
        if (getName() != null) {
            sb.append(getName());
            sb.append(",");
        }
        if (getAge() != null) {
            sb.append(getAge());
            sb.append(",");
        }
        if (sb.length() > 0) {
            tResult = sb.substring(0, sb.length() - 1);
        } else {
            tResult = "";
        }
        return tResult;
    }
}
