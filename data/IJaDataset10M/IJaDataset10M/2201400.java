package gov.usda.gdpc.xml;

import gov.usda.gdpc.*;
import gov.usda.gdpc.util.GenericTable;
import java.util.*;

/**
 *
 * @author  terryc
 */
public class GDPCTag {

    private static final Map myCache = new HashMap();

    public static final GDPCTag HEAD_TAG = new GDPCTag("GDPC", null);

    public static final GDPCTag ENVIRONMENT_EXPERIMENT_GROUP = new GDPCTag("EnvironmentExperimentGroup", EnvironmentExperimentGroup.class);

    public static final GDPCTag GENOTYPE_EXPERIMENT_GROUP = new GDPCTag("GenotypeExperimentGroup", GenotypeExperimentGroup.class);

    public static final GDPCTag LOCALITY_GROUP = new GDPCTag("LocalityGroup", LocalityGroup.class);

    public static final GDPCTag LOCUS_GROUP = new GDPCTag("LocusGroup", LocusGroup.class);

    public static final GDPCTag TAXON_GROUP = new GDPCTag("TaxonGroup", TaxonGroup.class);

    public static final GDPCTag PHENOTYPE_GROUP = new GDPCTag("PhenotypeGroup", PhenotypeGroup.class);

    public static final GDPCTag GENOTYPE_GROUP = new GDPCTag("GenotypeGroup", GenotypeGroup.class);

    public static final GDPCTag PHENOTYPE_ONTOLOGY_GROUP = new GDPCTag("PhenotypeOntologyGroup", PhenotypeOntologyGroup.class);

    public static final GDPCTag TAXON_PARENT_GROUP = new GDPCTag("TaxonParentGroup", TaxonParentGroup.class);

    public static final GDPCTag DBELEMENT = new GDPCTag("DBElement", null);

    public static final GDPCTag ENVIRONMENT_EXPERIMENT = new GDPCTag("EnvironmentExperiment", EnvironmentExperiment.class);

    public static final GDPCTag GENOTYPE_EXPERIMENT = new GDPCTag("GenotypeExperiment", GenotypeExperiment.class);

    public static final GDPCTag LOCALITY = new GDPCTag("Locality", Locality.class);

    public static final GDPCTag LOCUS = new GDPCTag("Locus", Locus.class);

    public static final GDPCTag TAXON = new GDPCTag("Taxon", Taxon.class);

    public static final GDPCTag PHENOTYPE = new GDPCTag("Phenotype", Phenotype.class);

    public static final GDPCTag GENOTYPE = new GDPCTag("Genotype", Genotype.class);

    public static final GDPCTag PHENOTYPE_ONTOLOGY = new GDPCTag("PhenotypeOntology", PhenotypeOntology.class);

    public static final GDPCTag ALLELE = new GDPCTag("Allele", Allele.class);

    public static final GDPCTag TAXON_PARENT = new GDPCTag("TaxonParent", TaxonParent.class);

    public static final GDPCTag PROPERTIES = new GDPCTag("Properties", null);

    public static final GDPCTag FILTER = new GDPCTag("Filter", Filter.class);

    public static final GDPCTag DBELEMENTS = new GDPCTag("DBElements", null);

    public static final GDPCTag ENVIRONMENT_EXPERIMENT_FILTER = new GDPCTag("EnvironmentExperimentFilter", EnvironmentExperimentFilter.class);

    public static final GDPCTag TAXON_FILTER = new GDPCTag("TaxonFilter", TaxonFilter.class);

    public static final GDPCTag LOCUS_FILTER = new GDPCTag("LocusFilter", LocusFilter.class);

    public static final GDPCTag LOCALITY_FILTER = new GDPCTag("LocalityFilter", LocalityFilter.class);

    public static final GDPCTag GENOTYPE_EXPERIMENT_FILTER = new GDPCTag("GenotypeExperimentFilter", GenotypeExperimentFilter.class);

    public static final GDPCTag GENOTYPE_FILTER = new GDPCTag("GenotypeFilter", GenotypeFilter.class);

    public static final GDPCTag PHENOTYPE_FILTER = new GDPCTag("PhenotypeFilter", PhenotypeFilter.class);

    public static final GDPCTag PHENOTYPE_ONTOLOGY_FILTER = new GDPCTag("PhenotypeOntologyFilter", PhenotypeOntologyFilter.class);

    public static final GDPCTag TAXON_PARENT_FILTER = new GDPCTag("TaxonParentFilter", TaxonParentFilter.class);

    public static final GDPCTag SINGLE = new GDPCTag("Single", FilterSingleValue.class);

    public static final GDPCTag RANGE = new GDPCTag("Range", FilterRangeValue.class);

    public static final GDPCTag LIKE = new GDPCTag("Like", FilterLikeValue.class);

    public static final GDPCTag DISTINCT_PROPERTY_VALUES = new GDPCTag("DistinctPropertyValues", DistinctPropertyValues.class);

    public static final GDPCTag PROPERTIES_LIST = new GDPCTag("PropertiesList", null);

    public static final GDPCTag PROPERTY_DESC = new GDPCTag("PropertyDesc", null);

    public static final GDPCTag ENVIRONMENT_EXPERIMENT_PROPERTY = new GDPCTag("EnvironmentExperimentProperty", EnvironmentExperimentProperty.class);

    public static final GDPCTag GENOTYPE_EXPERIMENT_PROPERTY = new GDPCTag("GenotypeExperimentProperty", GenotypeExperimentProperty.class);

    public static final GDPCTag LOCALITY_PROPERTY = new GDPCTag("LocalityProperty", LocalityProperty.class);

    public static final GDPCTag LOCUS_PROPERTY = new GDPCTag("LocusProperty", LocusProperty.class);

    public static final GDPCTag TAXON_PROPERTY = new GDPCTag("TaxonProperty", TaxonProperty.class);

    public static final GDPCTag PHENOTYPE_PROPERTY = new GDPCTag("PhenotypeProperty", PhenotypeProperty.class);

    public static final GDPCTag GENOTYPE_PROPERTY = new GDPCTag("GenotypeProperty", GenotypeProperty.class);

    public static final GDPCTag TAXON_PARENT_PROPERTY = new GDPCTag("TaxonParentProperty", TaxonParentProperty.class);

    public static final GDPCTag GENERIC_TABLE = new GDPCTag("GenericTable", GenericTable.class);

    public static final GDPCTag GENERIC_TABLE_NUM_ROWS_ATTR = new GDPCTag("numRows", Integer.class);

    public static final GDPCTag GENERIC_TABLE_NUM_COLUMNS_ATTR = new GDPCTag("numColumns", Integer.class);

    public static final GDPCTag GENERIC_TABLE_ROW = new GDPCTag("row", String.class);

    public static final GDPCTag GENERIC_TABLE_ITEM = new GDPCTag("i", String.class);

    public static final GDPCTag GENERIC_TABLE_ITEM_ATTR = new GDPCTag("x", Integer.class);

    private final String myTag;

    private final Class myJavaClass;

    /**
     * GDPCTag constructor.
     */
    public GDPCTag(String tag, Class javaClass) {
        myTag = tag;
        myJavaClass = javaClass;
        myCache.put(tag, this);
    }

    public static GDPCTag getInstance(String tag) {
        return (GDPCTag) myCache.get(tag);
    }

    public String getTag() {
        return myTag;
    }

    public Class getJavaClass() {
        return myJavaClass;
    }

    public String toString() {
        return myTag;
    }
}
