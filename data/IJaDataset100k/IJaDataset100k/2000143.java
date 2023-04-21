package net.sourceforge.javo2.compiler.definition;

import static org.apache.log4j.Logger.getLogger;
import generated.ValueObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.javo2.api.IValueObject;
import net.sourceforge.javo2.compiler.exception.DefinitionNotFoundException;
import net.sourceforge.javo2.definition.DefinitionProcessor;

/**
 * @author Nicolás Di Benedetto<br>
 * {@link mailto:nikodb@users.sourceforge.net nikodb@users.sourceforge.net}.<br>
 * Created on 01/03/2008<br>
 * ValueObjectDefinitionResolver<br>
 * This class holds the value object definitions processed by the framework.<br>
 * This class is immutable.<br>
 */
public class ValueObjectDefinitionResolver {

    /**
	 * A mapping between value object definition
	 * id's and the definition itself.
	 */
    private Map<String, ValueObject> definitionByIdMap = null;

    /**
	 * A mapping between value object IF's name 
	 * and the definition id.
	 */
    private Map<Class<? extends IValueObject>, String> definitionByIFMap = null;

    /**
	 * Default constructor hidden.
	 * @see ValueObjectDefinitionResolver#getInstance()
	 */
    private ValueObjectDefinitionResolver() {
    }

    /**
	 * Private inner class for singleton creation.
	 * @author Nicolás Di Benedetto
	 * {@link mailto:nikodb@users.sourceforge.net nikodb@users.sourceforge.net}.<br>
	 * Created on 26/09/2007.<br>
	 */
    private static final class Singleton {

        private static final ValueObjectDefinitionResolver instance = new ValueObjectDefinitionResolver();
    }

    /**
	 * Factory method for singleton access.
	 * @return the singleton instance initialized.
	 */
    public static final ValueObjectDefinitionResolver getInstance(List<ValueObject> valueObjects) {
        Singleton.instance.populateDefinitionMaps(valueObjects);
        return Singleton.instance;
    }

    /**
	 * Populates the definition mapping by id.
	 * @param valueObjects
	 */
    private void populateDefinitionMaps(List<ValueObject> valueObjects) {
        for (ValueObject valueObject : valueObjects) {
            this.getDefinitionByIdMap().put(valueObject.getId(), valueObject);
            try {
                @SuppressWarnings("unchecked") Class<? extends IValueObject> ifClass = (Class<? extends IValueObject>) Class.forName(DefinitionProcessor.getTargetFQCN(valueObject));
                this.getDefinitionByIFMap().put(ifClass, valueObject.getId());
            } catch (ClassNotFoundException e) {
                getLogger(this.getClass()).error("Class not found while instantiating VO interface name <" + valueObject.getInterfaceName() + ">. This means that generated code is out of synch. Run JAVO2 compiler and try again.");
            }
        }
    }

    /**
	 * This method resolves a value object definition given its definition id.
	 * @param definitionId the id of the value object definition being retrieved.
	 * @return the value object for the given definition.
	 */
    public ValueObject getValueObjectById(String definitionId) {
        ValueObject valueObject = this.getDefinitionByIdMap().get(definitionId);
        if (null == valueObject) {
            getLogger(this.getClass()).error("Cannot find VO definition for the given definition id <" + definitionId + ">.");
            throw new DefinitionNotFoundException(definitionId);
        }
        return valueObject;
    }

    /**
	 * This method resolves a value object definition given its interface name.
	 * @param definitionIF the FQCN for the generated IF. 
	 * @return the value object definition for the given interface name.
	 */
    public ValueObject getValueObjectByIFName(Class<? extends IValueObject> definitionIF) {
        String definitionId = this.getDefinitionByIFMap().get(definitionIF);
        if (null == definitionId) {
            getLogger(this.getClass()).error("Cannot find VO definition for the given definition interface <" + definitionIF.getCanonicalName() + ">.");
            throw new DefinitionNotFoundException();
        }
        return this.getValueObjectById(definitionId);
    }

    /**
	 * Getter method for the definitionByIdMap attribute.
	 * @return the definitionByIdMap attribute.
	 */
    protected Map<String, ValueObject> getDefinitionByIdMap() {
        return (null == this.definitionByIdMap) ? this.definitionByIdMap = new HashMap<String, ValueObject>() : this.definitionByIdMap;
    }

    /**
	 * Setter method for the definitionByIdMap attribute.
	 * @param definitionByIdMap the definitionByIdMap attribute to set.
	 */
    protected void setDefinitionByIdMap(Map<String, ValueObject> definitionMap) {
        this.definitionByIdMap = definitionMap;
    }

    /**
	 * Getter method for the definitionByIFMap attribute.
	 * @return the definitionByIFMap attribute.
	 */
    protected Map<Class<? extends IValueObject>, String> getDefinitionByIFMap() {
        return (null == this.definitionByIFMap) ? this.definitionByIFMap = new HashMap<Class<? extends IValueObject>, String>() : this.definitionByIFMap;
    }

    /**
	 * Setter method for the definitionByIFMap attribute.
	 * @param definitionByIFMap the definitionByIFMap attribute to set.
	 */
    protected void setDefinitionByIFMap(Map<Class<? extends IValueObject>, String> definitionByIFMap) {
        this.definitionByIFMap = definitionByIFMap;
    }
}
