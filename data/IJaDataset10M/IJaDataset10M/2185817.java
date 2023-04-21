package edu.upc.lsi.kemlg.aws.agentshell.components;

import java.net.URL;

public class Ontology {

    public static final String OWL = "owl";

    public static final String XSD = "xsd";

    private URL url;

    private String type;

    public void setOntologyReference(URL url) {
        this.url = url;
    }

    public void setOntologyType(String type) {
        this.type = type;
    }

    public URL getOntologyReference() {
        return url;
    }

    public String getOntologyType() {
        return type;
    }
}
