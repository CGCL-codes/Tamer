package org.proteored.miapeapi.cv;

public class UnitOntology {

    private static final String UNIT_ONTOLOGY_ADDRESS = "http://obo.cvs.sourceforge.net/*checkout*/obo/obo/ontology/phenotype/unit.obo";

    private static final String UNIT_ONTOLOGY_FULLNAME = "UNIT-ONTOLOGY";

    private static final String UNIT_ONTOLOGY_CVLABEL = "UO";

    private static final String UNIT_ONTOLOGY_VERSION = "1.15";

    public static String getAddress() {
        return UNIT_ONTOLOGY_ADDRESS;
    }

    public static String getFullName() {
        return UNIT_ONTOLOGY_FULLNAME;
    }

    public static String getCVLabel() {
        return UNIT_ONTOLOGY_CVLABEL;
    }

    public static String getVersion() {
        return UNIT_ONTOLOGY_VERSION;
    }
}
