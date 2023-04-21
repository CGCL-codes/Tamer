package gov.usda.gdpc;

import gov.usda.gdpc.util.SortedList;
import java.io.ObjectStreamException;
import java.util.*;
import org.apache.log4j.Logger;

/**
 * This follows the enum pattern and represents a genotype experiment property.
 * This is used to define the specific properties of a genotype experiment.
 *
 * @author  terryc
 */
public class GenotypeExperimentProperty extends AbstractProperty {

    private static final long serialVersionUID = 813L;

    private static final Logger myLogger = Logger.getLogger(GenotypeExperimentProperty.class);

    /** The property type. */
    public static final String PROPERTY_TYPE = "GenotypeExperimentProperty";

    /** map of properties where key is the property sub-type */
    private static final Map myProperties = new HashMap();

    private static final List myPropList = new ArrayList();

    private static final List myReadOnlyList = Collections.unmodifiableList(myPropList);

    private static final List mySortedPropList = new SortedList();

    private static final List myReadOnlySortedList = Collections.unmodifiableList(mySortedPropList);

    private static final List myRequiredPropList = new ArrayList();

    private static final List myReadOnlyRequiredList = Collections.unmodifiableList(myRequiredPropList);

    /** keeps track of index */
    private static int myIndex = 0;

    private static int myRequiredIndex = 0;

    public static final String POLY_TYPE_LENGTH = "Length";

    public static final String POLY_TYPE_CATEGORICAL = "Categorical";

    public static final String POLY_TYPE_MOBILITY = "Mobility";

    public static final String POLY_TYPE_SEQUENCE = "Sequence";

    public static final String POLY_TYPE_SEQ_ALIGNMENT = "Sequence Alignment";

    public static final String POLY_TYPE_SNP = "SNP";

    public static final String POLY_TYPE_RAWDATA = "Raw Data";

    public static final String POLY_TYPE_VARIABLE = "Variable";

    /** Polymorphism Type associated with genotype experiment (required data type: java.lang.String). */
    public static final GenotypeExperimentProperty POLY_TYPE = new GenotypeExperimentProperty("Polymorphism Type", java.lang.String.class, PROPERTY_TYPE, "POLY_TYPE", true, myRequiredIndex++);

    /** Name of genotype experiment (required data type: java.lang.String). */
    public static final GenotypeExperimentProperty NAME = new GenotypeExperimentProperty("Name", java.lang.String.class, PROPERTY_TYPE, "NAME", true, myRequiredIndex++);

    /** Locus associated with genotype experiment (data type: gov.usda.gdpc.Locus). */
    public static final GenotypeExperimentProperty LOCUS = new GenotypeExperimentProperty("Locus", gov.usda.gdpc.Locus.class, PROPERTY_TYPE, "LOCUS");

    /**
     * String Representation of this environment experiment (data type: java.lang.String).
     *
     * @since version 2.0
     */
    public static final GenotypeExperimentProperty STR_REP = new GenotypeExperimentProperty("String Representation", java.lang.String.class, PROPERTY_TYPE, "STR_REP");

    /**
     * Source experiment of this genotype experiment (data type: gov.usda.gdpc.GenotypeExperiment)
     *
     * @since version 2.0
     */
    public static final GenotypeExperimentProperty SRC_EXP = new GenotypeExperimentProperty("Source Experiment", gov.usda.gdpc.GenotypeExperiment.class, PROPERTY_TYPE, "SRC_EXP");

    /** Producer's name of this genotype experiment (data type: java.lang.String) */
    public static final GenotypeExperimentProperty PRODUCER = new GenotypeExperimentProperty("Producer", java.lang.String.class, PROPERTY_TYPE, "PRODUCER");

    /** Align Program associated with genotype experiment (data type: java.lang.String). */
    public static final GenotypeExperimentProperty ALIGN_PROGRAM = new GenotypeExperimentProperty("Align Program", java.lang.String.class, PROPERTY_TYPE, "ALIGN_PROGRAM");

    /**
     * Primer list associated with genotype experiment (data type: gov.usda.gdpc.PrimerList).
     *
     * @since version 2.0
     */
    public static final GenotypeExperimentProperty PRIMER_LIST = new GenotypeExperimentProperty("Primer List", gov.usda.gdpc.PrimerList.class, PROPERTY_TYPE, "PRIMER_LIST");

    /**
     * Reference sequence associated with genotype experiment (data type: java.lang.String).
     *
     * @since version 2.0
     */
    public static final GenotypeExperimentProperty REF_SEQ = new GenotypeExperimentProperty("Reference Sequence", java.lang.String.class, PROPERTY_TYPE, "REF_SEQ");

    /**
     * Reference Taxon associated with genotype experiment (data type: gov.usda.gdpc.Taxon).
     *
     * @since version 2.0
     */
    public static final GenotypeExperimentProperty REF_TAXON = new GenotypeExperimentProperty("Reference Taxon", gov.usda.gdpc.Taxon.class, PROPERTY_TYPE, "REF_TAXON");

    /** Human Validation associated with genotype experiment (data type: java.lang.String). */
    public static final GenotypeExperimentProperty HUMAN_VALIDATION = new GenotypeExperimentProperty("Human Validation", java.lang.String.class, PROPERTY_TYPE, "HUMAN_VALIDATION");

    /** Start Position associated with genotype experiment (data type: java.lang.String). */
    public static final GenotypeExperimentProperty START_POSITION = new GenotypeExperimentProperty("Start Position", java.lang.String.class, PROPERTY_TYPE, "START_POSITION");

    /** End Position associated with genotype experiment (data type: java.lang.String). */
    public static final GenotypeExperimentProperty END_POSITION = new GenotypeExperimentProperty("End Position", java.lang.String.class, PROPERTY_TYPE, "END_POSITION");

    /** Date of this genotype experiment (data type: gov.usda.gdpc.util.Calendar) */
    public static final GenotypeExperimentProperty DATE = new GenotypeExperimentProperty("Date", gov.usda.gdpc.util.Calendar.class, PROPERTY_TYPE, "DATE");

    /** Comments associated with genotype experiment (data type: java.lang.String). */
    public static final GenotypeExperimentProperty COMMENTS = new GenotypeExperimentProperty("Comments", java.lang.String.class, PROPERTY_TYPE, "COMMENTS");

    /** ID of genotype experiment (required data type: gov.usda.gdpc.Identifier). */
    public static final GenotypeExperimentProperty ID = new GenotypeExperimentProperty("ID", gov.usda.gdpc.Identifier.class, PROPERTY_TYPE, "ID", true, myRequiredIndex++);

    /** Data source of genotype experiment (required data type: java.lang.String). */
    public static final GenotypeExperimentProperty DATA_SOURCE = new GenotypeExperimentProperty("Data Source", java.lang.String.class, PROPERTY_TYPE, "DATA_SOURCE", true, myRequiredIndex++);

    /**
     * GenotypeExperimentProperty constructor.
     *
     * @param name property name
     * @param dataType data type of this property (class)
     * @param propertyType property type
     * @param propertySubType property subtype
     * @param required indicates whether this property is required
     */
    private GenotypeExperimentProperty(String name, Class dataType, String propertyType, String propertySubType, boolean required, int requiredIndex) {
        super(name, dataType, propertyType, propertySubType, myIndex++, required, requiredIndex);
        put(propertySubType, this, required);
    }

    private GenotypeExperimentProperty(String name, Class dataType, String propertyType, String propertySubType) {
        super(name, dataType, propertyType, propertySubType, myIndex++);
        put(propertySubType, this, false);
    }

    /**
     * Stores new instance for later retrieval.
     */
    private static void put(String propertySubType, GenotypeExperimentProperty property, boolean required) {
        if (myProperties.containsKey(propertySubType)) {
            myLogger.error("put: can not create two properties with the same subtype: " + propertySubType);
            return;
        }
        myProperties.put(propertySubType, property);
        myPropList.add(property);
        mySortedPropList.add(property);
        if (required) {
            myRequiredPropList.add(property);
        }
    }

    /**
     * Return property matching given sub type.
     *
     * @param propertySubType sub type
     *
     * @return property instance
     */
    public static GenotypeExperimentProperty getInstance(String propertySubType) {
        GenotypeExperimentProperty result = (GenotypeExperimentProperty) myProperties.get(propertySubType);
        if (result == null) {
            result = new GenotypeExperimentProperty(propertySubType, java.lang.String.class, PROPERTY_TYPE, propertySubType);
        }
        return result;
    }

    /**
     * Returns number of created properties.
     *
     * @return number of properties
     */
    public static int getNumDefinedProperties() {
        return myIndex;
    }

    /**
     * Returns read-only list of defined properties sorted
     * by the name of the properties.
     *
     * @return read-only list of sorted properties
     */
    public static List getSortedPropList() {
        return myReadOnlySortedList;
    }

    /**
     * Returns read-only list of defined properties in order
     * of assigned indices.
     *
     * @return read-only list of properties
     */
    public static List getPropList() {
        return myReadOnlyList;
    }

    /**
     * Returns read-only list of properties that
     * are required.
     *
     * @return read-only list of required properties
     */
    public static List getRequiredPropList() {
        return myReadOnlyRequiredList;
    }

    public static int getNumRequiredProperties() {
        return myRequiredIndex;
    }

    /**
     * This replaces instances created from serialization with
     * the correct singleton instance.
     */
    private Object readResolve() throws ObjectStreamException {
        String subType = getSubType();
        return getInstance(subType);
    }

    /**
     * Returns a standard polymorphism type if one has
     * been defined (null otherwise) for the given string.
     *
     * @param str string to match
     *
     * @return standard defined polymorphism type; null if none
     */
    public static String getStandardPolyType(String str) {
        str = str.trim();
        if (str.equalsIgnoreCase(GenotypeExperimentProperty.POLY_TYPE_LENGTH)) {
            return GenotypeExperimentProperty.POLY_TYPE_LENGTH;
        } else if (str.equalsIgnoreCase(GenotypeExperimentProperty.POLY_TYPE_MOBILITY)) {
            return GenotypeExperimentProperty.POLY_TYPE_MOBILITY;
        } else if (str.equalsIgnoreCase(GenotypeExperimentProperty.POLY_TYPE_CATEGORICAL)) {
            return GenotypeExperimentProperty.POLY_TYPE_CATEGORICAL;
        } else if (str.equalsIgnoreCase(GenotypeExperimentProperty.POLY_TYPE_RAWDATA)) {
            return GenotypeExperimentProperty.POLY_TYPE_RAWDATA;
        } else if (str.equalsIgnoreCase(GenotypeExperimentProperty.POLY_TYPE_SEQUENCE)) {
            return GenotypeExperimentProperty.POLY_TYPE_SEQUENCE;
        } else if (str.equalsIgnoreCase(GenotypeExperimentProperty.POLY_TYPE_SEQ_ALIGNMENT)) {
            return GenotypeExperimentProperty.POLY_TYPE_SEQ_ALIGNMENT;
        } else if (str.equalsIgnoreCase(GenotypeExperimentProperty.POLY_TYPE_SNP)) {
            return GenotypeExperimentProperty.POLY_TYPE_SNP;
        } else {
            return null;
        }
    }
}
