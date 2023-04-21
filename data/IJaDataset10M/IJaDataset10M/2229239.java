package gate.creole.ontology;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.ontotext.gate.ontology.OntologyImpl;

public class ObjectPropertyImpl extends PropertyImpl implements ObjectProperty {

    protected Set inversePropertiesSet;

    /**
   * Convenience constructor for simple cases where the domain and range are
   * single classes.
   * 
   * @param aName
   *          the name of the property.
   * @param aDomainClass
   *          the class representing the domain.
   * @param aRange
   *          the class representing the range.
   * @param anOntology
   *          the ontology this property belongs to.
   */
    public ObjectPropertyImpl(String name, String comment, OClass aDomainClass, OClass aRange, Ontology anOntology) {
        super(name, comment, aDomainClass, aRange, anOntology);
        inversePropertiesSet = new HashSet();
    }

    /**
   * Constructor for this property.
   * 
   * @param aName
   *          the name of the property.
   * @param aDomain
   *          the set of domain restrictions for this property. A set of
   *          {@link OClass} values.
   * @param aRange
   *          the set of range restrictions for this property. A set of
   *          {@link OClass} values.
   * @param anOntology
   *          the ontology this property belongs to.
   */
    public ObjectPropertyImpl(String name, String comment, Set aDomain, Set aRange, Ontology anOntology) {
        super(name, comment, aDomain, aRange, anOntology);
        inversePropertiesSet = new HashSet();
    }

    public void addSuperProperty(Property property) {
        super.addSuperProperty(property);
        range.addAll(property.getRange());
        OntologyImpl.reduceToMostSpecificClasses(range);
        Iterator subPropIter = getSubProperties(TRANSITIVE_CLOSURE).iterator();
        while (subPropIter.hasNext()) {
            Property aSubProperty = (Property) subPropIter.next();
            if (aSubProperty instanceof ObjectPropertyImpl) {
                ((ObjectPropertyImpl) aSubProperty).recalculateRange();
            }
        }
    }

    /**
   * @param instance
   * @return true if this value is compatible with the range restrictions on the
   *         property. False otherwise.
   */
    public boolean isValidRange(OInstance instance) {
        return super.isValidRange(instance);
    }

    public Set getRange() {
        return range;
    }

    public Set getInverseProperties() {
        return this.inversePropertiesSet;
    }

    public void setInverseOf(Property theInverse) {
        this.inversePropertiesSet.add(theInverse);
    }
}
