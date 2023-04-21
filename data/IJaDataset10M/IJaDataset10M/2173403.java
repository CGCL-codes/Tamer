package wilos.model.misc.concretebreakdownelement;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import wilos.model.misc.concreteactivity.ConcreteActivity;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.model.misc.project.Project;
import wilos.model.spem2.breakdownelement.BreakdownElement;
import wilos.resources.LocaleBean;

/**
 * 
 * A ConcreteBreakdownElement is a specific {@link BreakdownElement} for a
 * {@link Project}.
 * 
 */
public class ConcreteBreakdownElement implements Cloneable, Comparable<ConcreteBreakdownElement> {

    private String id;

    private Project project;

    private String concreteName;

    private Set<ConcreteActivity> superConcreteActivities;

    private BreakdownElement breakdownElement;

    private String displayOrder;

    private int instanciationOrder;

    /**
	 * Class constructor.
	 */
    public ConcreteBreakdownElement() {
        this.id = "";
        this.concreteName = "";
        this.project = null;
        this.breakdownElement = null;
        this.superConcreteActivities = new HashSet<ConcreteActivity>();
        this.displayOrder = "";
    }

    /**
	 * Compares the specified ConcreteBreakdownElement with the current instance
	 * of the class.
	 * 
	 * @param _arg0
	 *            the ConcreteBreakdownElement to be compare with the current
	 *            instance
	 * @return
	 */
    public int compareTo(ConcreteBreakdownElement _arg0) {
        if (this.breakdownElement != null) {
            int compare = this.breakdownElement.compareTo(_arg0.getBreakdownElement());
            if (compare == 0) {
                return this.getInstanciationOrder() - _arg0.getInstanciationOrder();
            } else return compare;
        } else return this.getInstanciationOrder() - _arg0.getInstanciationOrder();
    }

    /**
	 * Returns a copy of the current instance of ConcreteBreakdownElement
	 * 
	 * @return a copy of the ConcreteBreakdownElement
	 * @throws CloneNotSupportedException
	 */
    public ConcreteBreakdownElement clone() throws CloneNotSupportedException {
        ConcreteBreakdownElement concreteBreakdownElement = new ConcreteBreakdownElement();
        concreteBreakdownElement.copy(this);
        return concreteBreakdownElement;
    }

    /**
	 * Copy the values of the specified ConcreteBreakdownElement into the
	 * current instance.
	 * 
	 * @param _concreteBreakdownElement
	 *            the ConcreteBreakdownElement to be copy
	 */
    protected void copy(final ConcreteBreakdownElement _concreteBreakdownElement) {
        this.concreteName = _concreteBreakdownElement.getConcreteName();
        this.breakdownElement = _concreteBreakdownElement.getBreakdownElement();
        this.superConcreteActivities.addAll(_concreteBreakdownElement.getSuperConcreteActivities());
        this.project = _concreteBreakdownElement.getProject();
        this.instanciationOrder = _concreteBreakdownElement.instanciationOrder;
        this.displayOrder = _concreteBreakdownElement.displayOrder;
    }

    /**
	 * Defines if the specified Object is the same or has the same values as the
	 * current instance of the class.
	 * 
	 * @param obj
	 *            the Object to be compare to the ConcreteBreakdownElement
	 * @return true if the specified Object is the same, false otherwise
	 */
    public boolean equals(Object obj) {
        if (obj instanceof ConcreteBreakdownElement == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        ConcreteBreakdownElement concreteBreakdownElement = (ConcreteBreakdownElement) obj;
        return new EqualsBuilder().append(this.concreteName, concreteBreakdownElement.concreteName).append(this.breakdownElement, concreteBreakdownElement.breakdownElement).append(this.superConcreteActivities, concreteBreakdownElement.superConcreteActivities).append(this.project, concreteBreakdownElement.project).append(this.instanciationOrder, concreteBreakdownElement.instanciationOrder).append(this.displayOrder, concreteBreakdownElement.displayOrder).isEquals();
    }

    /**
	 * Returns a hash code value for the object. This method is supported for
	 * the benefit of hash tables.
	 * 
	 * @return the hash code of the current instance of ConcreteBreakdownElement
	 */
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.concreteName).append(this.breakdownElement).append(this.project).append(this.instanciationOrder).append(this.displayOrder).toHashCode();
    }

    /**
	 * Specifies the BreakdownElement related to the ConcreteBreakdownElement.
	 * Links the BreakdownElement to the current ConcreteBreakdownElement.
	 * 
	 * @param _breakdownElement
	 *            the BreakdownElement to be assigned to the
	 *            ConcreteBreakdownElement
	 */
    public void addBreakdownElement(BreakdownElement _breakdownElement) {
        this.breakdownElement = _breakdownElement;
        _breakdownElement.getConcreteBreakdownElements().add(this);
    }

    /**
	 * Remove the relation between the ConcreteBreakdownElement and the
	 * specified BreakdownElement.
	 * 
	 * @param _breakdownElement
	 *            the BreakdownElement to unlinked with the
	 *            ConcreteBreakdownElement
	 */
    public void removeBreakdownElement(BreakdownElement _breakdownElement) {
        _breakdownElement.getConcreteBreakdownElements().remove(this);
        this.breakdownElement = null;
    }

    /**
	 * Adds a specified ConcreteActivity in the Collection of super
	 * ConcreteActivities related to the current instance of the class.
	 * 
	 * @param _superConcreteActivity
	 *            the ConcreteActivity to be add to the Collection of super
	 *            ConcreteActivities
	 */
    public void addSuperConcreteActivity(ConcreteActivity _superConcreteActivity) {
        this.getSuperConcreteActivities().add(_superConcreteActivity);
        _superConcreteActivity.getConcreteBreakdownElements().add(this);
    }

    /**
	 * Adds the ConcreteBreakdownElement to the Collection of the specified
	 * ConcreteActivities related to .
	 * 
	 * @param _superConcreteActivities
	 *            the Set of concreteActivities to be add to the existing
	 *            Collection
	 */
    public void addAllSuperActivities(Set<ConcreteActivity> _superConcreteActivities) {
        for (ConcreteActivity concreteActivity : _superConcreteActivities) {
            concreteActivity.addConcreteBreakdownElement(this);
        }
    }

    /**
	 * Removes the relation between a specified ConcreteActivity and the
	 * ConcreteBreakdownElement.
	 * 
	 * @param _superConcreteActivity
	 *            the ConcreteActivity to be remove
	 */
    public void removeSuperConcreteActivity(ConcreteActivity _superConcreteActivity) {
        _superConcreteActivity.getConcreteBreakdownElements().remove(this);
        this.getSuperConcreteActivities().remove(_superConcreteActivity);
    }

    /**
	 * Removes the ConcreteBreakdownElement from all the Sets of the
	 * ConcreteActivities related.
	 */
    public void removeAllSuperConcreteActivities() {
        for (ConcreteActivity concreteActivity : this.getSuperConcreteActivities()) concreteActivity.getConcreteBreakdownElements().remove(this);
        this.getSuperConcreteActivities().clear();
    }

    /**
	 * Returns the name of the instance of ConcreteBreakdownElement.
	 * 
	 * @return the name of the ConcreteBreakdownElement
	 */
    public String getConcreteName() {
        return concreteName;
    }

    /**
	 * Initialize the name of the ConcreteBreakdownElement with the String
	 * specified in parameter.
	 * 
	 * @param concreteName
	 *            the name to be assigned to the ConcreteBreakdownElement
	 */
    public void setConcreteName(String concreteName) {
        this.concreteName = concreteName;
    }

    public String getMenuConcreteName() {
        return LocaleBean.getText("menu.project") + " " + this.concreteName;
    }

    /**
	 * Returns the String that identify uniquely an instance of
	 * ConcreteBreakdownElement.
	 * 
	 * @return the identifier of the current instance
	 */
    public String getId() {
        return this.id;
    }

    /**
	 * Initializes the value of the identifier with the specified one.
	 * 
	 * @param _id
	 *            the identifier to be assigned to the ConcreteBreakdownElement
	 */
    @SuppressWarnings("unused")
    protected void setId(String _id) {
        this.id = _id;
    }

    /**
	 * Returns the Set of ConcreteActivity that are related to the
	 * ConcreteBreakdownElement.
	 * 
	 * @return the Set of ConcreteActivity of the ConcreteBreakdownElement
	 */
    public Set<ConcreteActivity> getSuperConcreteActivities() {
        return superConcreteActivities;
    }

    /**
	 * Initializes the Set of the ConcreteActivity related to the
	 * ConcreteBreakdownElement with the values of the one passed in parameter.
	 * 
	 * @param superConcreteActivities
	 *            the Set that is assigned to the ConcreteBreakdownElement
	 */
    public void setSuperConcreteActivities(Set<ConcreteActivity> superConcreteActivities) {
        this.superConcreteActivities = superConcreteActivities;
    }

    /**
	 * Returns the BreakdownElement related to the ConcreteBreakdownElement.
	 * 
	 * @return the BreakdownElement assigned to the ConcreteBreakdownElement
	 */
    public BreakdownElement getBreakdownElement() {
        return breakdownElement;
    }

    /**
	 * Initializes the BreakdownElement of the ConcreteBreakdownElement with the
	 * one passed in parameter.
	 * 
	 * @param breakdownElement
	 *            the BreakdownElement to be assigned to the
	 *            ConcreteBreakdownElement
	 */
    public void setBreakdownElement(BreakdownElement breakdownElement) {
        this.breakdownElement = breakdownElement;
    }

    /**
	 * Returns the Project that the ConcreteBreakdownElement is assigned to.
	 * 
	 * @return the Project assigned to the ConcreteBreakdownElement
	 */
    public Project getProject() {
        return project;
    }

    /**
	 * Initializes the attribute that defines the Project related to the
	 * ConcreteBreakdownElement.
	 * 
	 * @param project
	 *            the project to set for the ConcreteBreakdownElement
	 */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
	 * Returns the order in which the ConcreteBreakdownElement has been
	 * instanciated.
	 * 
	 * @return the rank of instanciation of the ConcreteBreakdownElement
	 */
    public int getInstanciationOrder() {
        return instanciationOrder;
    }

    /**
	 * Defines the rank of instanciation of the ConcreteBreakdownElement.
	 * 
	 * @param instanciationOrder
	 *            the value of the ConcreteBreakdownElement's rank
	 */
    public void setInstanciationOrder(int instanciationOrder) {
        this.instanciationOrder = instanciationOrder;
    }

    /**
	 * Returns the order in which the ConcreteBreakdownElement is to be
	 * displayed.
	 * 
	 * @return the order of display
	 */
    public String getDisplayOrder() {
        return displayOrder;
    }

    /**
	 * Initialized the display order of the ConcreteBreakdownElement.
	 * 
	 * @param displayOrder
	 *            the display order to be assigned to the
	 *            ConcreteBreakdownElement
	 */
    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }
}
