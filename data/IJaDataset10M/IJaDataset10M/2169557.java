package gate.creole.ontology;

import java.util.Set;

/**
 * @author Valentin Tablan
 * 
 */
public class TransitivePropertyImpl extends ObjectPropertyImpl implements TransitiveProperty {

    /**
   * @param name
   * @param comment
   * @param aDomainClass
   * @param aRange
   * @param anOntology
   */
    public TransitivePropertyImpl(String name, String comment, OClass aDomainClass, OClass aRange, Ontology anOntology) {
        super(name, comment, aDomainClass, aRange, anOntology);
    }

    /**
   * @param name
   * @param comment
   * @param aDomain
   * @param aRange
   * @param anOntology
   */
    public TransitivePropertyImpl(String name, String comment, Set aDomain, Set aRange, Ontology anOntology) {
        super(name, comment, aDomain, aRange, anOntology);
    }
}
