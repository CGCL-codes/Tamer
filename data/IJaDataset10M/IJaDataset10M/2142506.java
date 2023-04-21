package gov.usda.gdpc.xml;

import gov.usda.gdpc.*;
import java.util.*;

/**
 *
 * @author  terryc
 */
public class PropertyTag {

    private static final Map myCache = new HashMap();

    /** COMMON PROPERTIES */
    public static final PropertyTag ID = new PropertyTag("ID", Property.class);

    public static final PropertyTag ID_VALUE = new PropertyTag("VALUE", String.class);

    public static final PropertyTag ID_HINT = new PropertyTag("HINT", String.class);

    public static final PropertyTag DATA_SOURCE = new PropertyTag("DATA_SOURCE", Property.class);

    public static final PropertyTag COMMENTS = new PropertyTag("COMMENTS", Property.class);

    public static final PropertyTag NAME = new PropertyTag("NAME", Property.class);

    /** LOCUS PROPERTIES */
    public static final PropertyTag CHROMOSOME_NUMBER = new PropertyTag("CHROMOSOME_NUMBER", Property.class);

    public static final PropertyTag GENETIC_BIN = new PropertyTag("GENETIC_BIN", Property.class);

    public static final PropertyTag GENETIC_MAP = new PropertyTag("GENETIC_MAP", Property.class);

    public static final PropertyTag GENETIC_POSITION = new PropertyTag("GENETIC_POSITION", Property.class);

    public static final PropertyTag LABEL = new PropertyTag("LABEL", Property.class);

    public static final PropertyTag LOCUS_TYPE = new PropertyTag("LOCUS_TYPE", Property.class);

    public static final PropertyTag PHYSICAL_POSITION = new PropertyTag("PHYSICAL_POSITION", Property.class);

    /** GENOTYPE EXPERIMENT PROPERTIES */
    public static final PropertyTag ALIGN_PROGRAM = new PropertyTag("ALIGN_PROGRAM", Property.class);

    public static final PropertyTag END_POSITION = new PropertyTag("END_POSITION", Property.class);

    public static final PropertyTag HUMAN_VALIDATION = new PropertyTag("HUMAN_VALIDATION", Property.class);

    public static final PropertyTag SCORING_TECHNOLOGY = new PropertyTag("SCORING_TECHNOLOGY", Property.class);

    public static final PropertyTag START_POSITION = new PropertyTag("START_POSITION", Property.class);

    public static final PropertyTag PRIMER1 = new PropertyTag("PRIMER1", Property.class);

    public static final PropertyTag PRIMER2 = new PropertyTag("PRIMER2", Property.class);

    /** TAXON PROPERTIES */
    public static final PropertyTag ACCESSION = new PropertyTag("ACCESSION", Property.class);

    public static final PropertyTag COLLECTOR = new PropertyTag("COLLECTOR", Property.class);

    public static final PropertyTag GENUS = new PropertyTag("GENUS", Property.class);

    public static final PropertyTag GERMPLASM_TYPE = new PropertyTag("GERMPLASM_TYPE", Property.class);

    public static final PropertyTag PLANT = new PropertyTag("PLANT", Property.class);

    public static final PropertyTag POPULATION = new PropertyTag("POPULATION", Property.class);

    public static final PropertyTag REFERENCE = new PropertyTag("REFERENCE", Property.class);

    public static final PropertyTag SEED_LOT = new PropertyTag("SEED_LOT", Property.class);

    public static final PropertyTag SOURCE = new PropertyTag("SOURCE", Property.class);

    public static final PropertyTag SPECIES = new PropertyTag("SPECIES", Property.class);

    public static final PropertyTag SUBSPECIES = new PropertyTag("SUBSPECIES", Property.class);

    /** LOCALITY PROPERTIES */
    public static final PropertyTag ALTITUDE = new PropertyTag("ALTITUDE", Property.class);

    public static final PropertyTag COUNTRY = new PropertyTag("COUNTRY", Property.class);

    public static final PropertyTag LATITUDE = new PropertyTag("LATITUDE", Property.class);

    public static final PropertyTag LONGITUDE = new PropertyTag("LONGITUDE", Property.class);

    public static final PropertyTag STATE_PROVINCE = new PropertyTag("STATE_PROVINCE", Property.class);

    /** ENVIRONMENT EXPERIMENT PROPERTIES */
    public static final PropertyTag EVALUATIONSITE = new PropertyTag("EVALUATIONSITE", Property.class);

    public static final PropertyTag HARVEST_DATE = new PropertyTag("HARVEST_DATE", Property.class);

    public static final PropertyTag PLANT_DATE = new PropertyTag("PLANT_DATE", Property.class);

    public static final PropertyTag ALLELE_LIST = new PropertyTag(GenotypeProperty.ALLELE_LIST.getSubType(), Property.class);

    public static final PropertyTag ALLELE_LIST_ALLELE = new PropertyTag("ALLELE", String.class);

    /** PHENOTYPE PROPERTIES */
    public static final PropertyTag VALUE = new PropertyTag("VALUE", Property.class);

    /** PHENOTYPE ONTOLOGY PROPERTIES */
    private final String myTag;

    private final Class myJavaClass;

    /**
     * PropertyTag Constructor.
     */
    public PropertyTag(String tag, Class javaClass) {
        myTag = tag;
        myJavaClass = javaClass;
        myCache.put(tag, this);
    }

    /**
     * Returns PropertyTag instance matching given tag.
     *
     * @param tag tag to retrieve
     *
     * @return instance
     */
    public static PropertyTag getInstance(String tag) {
        return (PropertyTag) myCache.get(tag);
    }

    /**
     * Returns the XML tag.
     *
     * @return tag
     */
    public String getTag() {
        return myTag;
    }

    /**
     * Returns the java class associated with this tag.
     *
     * @return java class
     */
    public Class getJavaClass() {
        return myJavaClass;
    }

    /**
     * Returns the XML tag.  This the same as getTag().
     *
     * @return tag
     */
    public String toString() {
        return myTag;
    }
}
