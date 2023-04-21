package org.opencube.oms.meta;

import java.util.Date;
import org.opencube.oms.OMSNode;

/**
 * @author <a href="mailto:maciek@fingo.pl">FINGO - Maciej Mroczko</a>
 * TODO: comment
 */
public class ComplexAttribute extends OMSNode {

    /**
	 * The attribute type constant: association = 'a'
	 */
    public static char TYPE_ASSOCIATION = 'a';

    /**
	 * The attribute type constant: composition = 'c'
	 */
    public static char TYPE_COMPOSITION = 'c';

    private char type = TYPE_ASSOCIATION;

    private Scheme sourceScheme = null;

    private Scheme targetScheme = null;

    private int minMultiplicity = 1;

    private int maxMultiplicity = 1;

    /**
	 * @param sourceScheme
	 * @param targetScheme
	 * @param minMultiplicity
	 * @param maxMultiplicity
	 * @param type
	 * @param id
	 * @param name
	 * @param creatingDate
	 * @param creater
	 * @param modifyingDate
	 * @param modifier
	 * 
	 * @throws OMSMetaDataException
	 */
    ComplexAttribute(Scheme sourceScheme, Scheme targetScheme, int minMultiplicity, int maxMultiplicity, char type, String id, String name, Date creatingDate, String creater, Date modifyingDate, String modifier) throws OMSMetaDataException {
        super(id, name, creatingDate, creater, modifyingDate, modifier);
        this.type = type;
        this.sourceScheme = sourceScheme;
        this.targetScheme = targetScheme;
        this.minMultiplicity = minMultiplicity;
        this.maxMultiplicity = maxMultiplicity;
        sourceScheme.getNamespace().getMetaData().addComplexAttribute(this);
        if (sourceScheme == null) {
            throw new OMSMetaDataException("The source scheme for the complex attribute cannot be null: name = '" + name + "',  id = '" + id + "'");
        }
        if (targetScheme == null) {
            throw new OMSMetaDataException("The target scheme for the complex attribute cannot be null: name = '" + name + "',  id = '" + id + "'");
        }
        if (type != TYPE_ASSOCIATION && type != TYPE_COMPOSITION) {
            throw new OMSMetaDataException("Invalid type '" + type + "' for the complex attribute: name = '" + name + "',  id = '" + id + "'");
        }
    }

    /**
	 * Returns source scheme (attribute owner)
	 * 
	 * @return Scheme - source scheme (attribute owner)
	 */
    public Scheme getSourceScheme() {
        return sourceScheme;
    }

    /**
	 * Returns attribute type
	 * 
	 * @return chat - the type of this attribute
	 */
    public char getType() {
        return type;
    }

    public String toString() {
        return "<complex_attribute name=\"" + name + "\" id=\"" + id + "\" type=\"" + type + "\" source_scm=\"" + sourceScheme.getName() + "\" target_scm=\"" + targetScheme.getName() + "\"/>";
    }

    /**
	 * Test type of this attribute.
	 * 
	 * @return boolean - <code> true</code> if this attribute is composition
	 */
    public boolean isComposition() {
        return type == TYPE_COMPOSITION;
    }

    /**
	 * Test type of this attribute.
	 * 
	 * @return boolean - <code> true</code> if this attribute is association
	 */
    public boolean isAssociation() {
        return type == TYPE_ASSOCIATION;
    }

    /**
	 * Return the attribute minimum multiplicity.
	 * 
	 * @return int - the attribute minimum multiplicity
	 */
    public int getMinMultiplicity() {
        return minMultiplicity;
    }

    /**
	 * Return the attribute maximum multiplicity.
	 * 
	 * @return int - the attribute maximum multiplicity
	 */
    public int getMaxMultiplicity() {
        return maxMultiplicity;
    }

    /**
	 * Returns the attribute (target) scheme
	 * 
	 * @return Scheme - the target scheme
	 */
    public Scheme getTargetScheme() {
        return targetScheme;
    }

    /**
	 * Tests if the attribute is multiple
	 * 
	 * @return <code>true</code> if the attribute is multiple, <code>false</code> otherwise.
	 */
    public boolean isMultiple() {
        return maxMultiplicity != 1;
    }
}
