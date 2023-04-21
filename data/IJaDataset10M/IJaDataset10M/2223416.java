package com.byterefinery.rmbench.util.xml;

/**
 * @author cse
 */
public interface XMLConstants {

    String NAMESPACE_OLD = "http://byterefinery.com/xml/ns/rmbench";

    String NAMESPACE = "http://rmbench.com/xml/ns/rmbench";

    String NAMESPACELOC_1_0 = "http://rmbench.com/xml/ns/rmbench/rmb-1.0.xsd";

    String XSI_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";

    String ATT_NAMESPACE = null;

    public interface Elements {

        String MODEL = "model";

        String SCHEMAS = "schemas";

        String DIAGRAMS = "diagrams";

        String DIAGRAM = "diagram";

        String DESCRIPTION = "description";

        String SCHEMA = "schema";

        String TABLE = "table";

        String LOCATION = "location";

        String TABLE_REF = "tableref";

        String PRIMARY_KEY = "primarykey";

        String COLUMN_REF = "columnref";

        String INDEX = "index";

        String COLUMN = "column";

        String DEFAULT = "default";

        String COMMENT = "comment";

        String FOREIGN_KEY = "foreignkey";

        String FOREIGN_KEY_REF = "foreignkeyref";

        String TARGET = "target";

        String SOURCE = "source";

        String UNIQUE = "unique";

        String CHECK = "check";

        String EXPRESSION = "expression";
    }

    public interface Attributes {

        String NAME = "name";

        String SCHEMA = "schema";

        String COLLAPSED = "collpased";

        String Y = "y";

        String X = "x";

        String CATALOG = "catalog";

        String DBINFO = "dbinfo";

        String GENERATOR = "generator";

        String NULLABLE = "nullable";

        String TYPE = "type";

        String SCALE = "scale";

        String EXTRA = "extra";

        String SIZE = "size";

        String UNIQUE = "unique";

        String VERSION = "version";

        String SCHEMALOC = "schemaLocation";

        String TABLE = "table";

        String DELETE_RULE = "onDelete";

        String UPDATE_RULE = "onUpdate";

        String SLOTNUMBER = "slotNumber";

        String EDGE = "edge";
    }
}
