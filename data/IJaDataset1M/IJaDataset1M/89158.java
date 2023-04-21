package info.mediaspaces.mapping;

import java.util.Iterator;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;

/**
 * A specialization of a mapping element that allows the definition of mapping
 * relationships between RDFS properties. A property mapping can be used to
 * model N:1 relationships between RDFS properties. The definition of 1:N
 * relationships requires multiple property mappings to be defined.
 * 
 * @author haslhofer
 * 
 */
public interface PropertyMapping extends MappingElement {

    /**
	 * Sets the property mapping's target property (must be defined in the
	 * target model)
	 * 
	 * @param targetProperty
	 *            the target property
	 */
    public void setTargetProperty(OntProperty targetProperty);

    /**
	 * Returns the property mapping's target property
	 * 
	 * @return the target property
	 */
    public OntProperty getTargetProperty();

    /**
	 * Adds a source property to a property mapping specification (must be
	 * defined in the source model)
	 * 
	 * @param sourceProperty
	 *            the source property
	 */
    public void addSourceProperty(OntProperty sourceProperty);

    /**
	 * Returns an iterator over all source properties that are defined as part
	 * of a property mapping
	 * 
	 * @return an iterator over all source properties
	 */
    public Iterator<OntProperty> listSourceProperties();

    /**
	 * Allows the definition of a property mapping in the context of a certain
	 * target class
	 * 
	 */
    public void addTargetClassContext(OntClass targetClassContext);

    /**
	 * Returns an iterator over a set of classes that define a property
	 * mapping's target context.
	 * 
	 * @return an iterator over classes
	 */
    public Iterator<OntClass> listTargetClassContexts();

    /**
	 * Allows the definition of a property mapping in the context of a certain
	 * source class
	 * 
	 */
    public void addSourceClassContext(OntClass sourceClassContext);

    /**
	 * Returns an iterator over a set of classes that define a property
	 * mapping's source context.
	 * 
	 * @return an iterator over classes
	 */
    public Iterator<OntClass> listSourceClassContexts();

    /**
	 * Allows the definition of a property mapping in the context of a certain,
	 * already defined class mapping
	 * 
	 */
    public void addClassMappingContext(ClassMapping classMapping);

    /**
	 * An iterator over a set of class mappings that define a property mapping's
	 * context.
	 * 
	 * @return an iterator over class mappings
	 */
    public Iterator<ClassMapping> listClassMappingContexts();
}
