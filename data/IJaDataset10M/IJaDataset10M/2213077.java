package net.sourceforge.ondex.statistics.directannotation;

/**
 * Class for constant strings.
 * 
 * @author hindlem
 * 
 */
public interface ArgumentNames {

    public static final String FCC_ARG = "FromConceptClass";

    public static final String FCC_ARG_DESC = "This option provides a concept class to map from ";

    public static final String TCC_ARG = "ToConceptClass";

    public static final String TCC_ARG_DESC = "This option provides a concept class to map to ";

    public static final String RT_ARG = "RelationType";

    public static final String RT_ARG_DESC = "The relation type to restrict to";

    public static final String RTS_ARG = "RelationType";

    public static final String RTS_ARG_DESC = "The relation type set to restrict to";

    public static final String OUTPUT_ARG = "Output";

    public static final String OUTPUT_ARG_DESC = "This option specifies the path of the output file.";

    public static final String FIELDS_ARG = "Fields";

    public static final String FIELDS_ARG_DESC = "Fields to output in the tab deliminated file";

    public static final String FROM_GDS_FIELDS_ARG = "FromConceptGDSFields";

    public static final String FROM_GDS_FIELDS_ARG_DESC = "AttributeName Fields to output in the tab deliminated file will be named FROM_GDS_AttributeNameID";

    public static final String TO_GDS_FIELDS_ARG = "ToConceptGDSFields";

    public static final String TO_GDS_FIELDS_ARG_DESC = "AttributeName Fields to output in the tab deliminated file will be named TO_GDS_AttributeNameID";

    public static final String RTS_GDS_FIELDS_ARG = "RelationGDSFields";

    public static final String RTS_GDS_FIELDS_ARG_DESC = "AttributeName Fields to output in the tab deliminated file will be named R_GDS_AttributeNameID";
}
