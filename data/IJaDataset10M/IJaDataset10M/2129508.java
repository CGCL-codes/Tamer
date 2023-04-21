package net.sourceforge.ondex.export.oxl2;

/**
 * Static String constants.
 * 
 */
public final class ArgumentNames {

    public static final String EXCLUDE_GDS_WITH_ATTRIBUTE_ARG = "ExcludeGDSWithAttribute";

    public static final String EXCLUDE_GDS_WITH_ATTRIBUTE_ARG_DESC = "This parameter can be used to exclude a number of attributes from being written in the ondex.xml file.\n" + "When **//ALL//** is used all attributes are excluded and thus no GDS values will be written.";

    public static final String EXCLUDE_C_WITH_CC_ARG = "ExcludeConceptsOfConceptClass";

    public static final String EXCLUDE_C_WITH_CC_ARG_DESC = "This parameter can be used to do some basic filtering on ConceptClass in the export method.\nThis is especially useful if graphs become to large.";

    public static final String EXCLUDE_R_WITH_RT_ARG = "ExcludeRelationsOfRelationType";

    public static final String EXCLUDE_R_WITH_RT_ARG_DESC = "This parameter can be used to do some basic filtering on RelationType in the export method.\nThis is especially useful if graphs become to large.";

    public static final String EXCLUSIVE_GDS_WITH_ATT_INCLUSION = "IncludeOnlyGDSAttribute";

    public static final String EXCLUSIVE_GDS_WITH_ATT_INCLUSION_DESC = "This parameter works by setting exclusive inclusions for a set of GDS Attributes. All other GDSs Attributes not specified will be excluded.";

    public static final String EXCLUSIVE_C_WITH_CC_INCLUSION = "IncludeOnlyConceptClass";

    public static final String EXCLUSIVE_C_WITH_CC_INCLUSION_DESC = "This parameter works by setting exclusive inclusions for a set of Concept Classes. All other Concept Classes not specified will be excluded.";

    public static final String EXCLUSIVE_R_WITH_RT_INCLUSION = "IncludeOnlyRelationType";

    public static final String EXCLUSIVE_R_WITH_RT_INCLUSION_DESC = "This parameter works by setting exclusive inclusions for a set of Relation Types. All other Relation Types not specified will be excluded.";

    public static final String EXPORT_FILE = "ExportFile";

    public static final String EXPORT_FILE_DESC = "Name of file to export to. " + "When this option ends with \".gz\" or \".zip\" the file wil be exported as a gzip/zip file, " + "this is the preffered option as it reduces disc space requirements by a lot." + "\nFor example a file of 1.4 GigaByte is often compressed to a file of 50 MegaByte or less!!";

    public static final String PRETTY_PRINTING = "pretty";

    public static final String PRETTY_PRINTING_DESC = "When this option is set the output XML is kind of pretty printed. This makes the output larger.";

    public static final String EXPORT_ISOLATED_CONCEPTS = "ExportIsolatedConcepts";

    public static final String EXPORT_ISOLATED_CONCEPTS_DESC = "When this is option is set, it will export also concepts without any relations.";
}
