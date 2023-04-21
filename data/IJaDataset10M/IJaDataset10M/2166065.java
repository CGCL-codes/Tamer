package edu.upc.lsi.kemlg.aws.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class OWLHandler {

    private String rdf, xsl;

    public OWLHandler(String rdf, String xsl) {
        this.rdf = rdf;
        this.xsl = xsl;
    }

    public InputStream getOwlStream() {
        ByteArrayInputStream bais;
        bais = new ByteArrayInputStream(rdf.getBytes());
        return bais;
    }

    public InputStream getXSLStream() {
        ByteArrayInputStream bais;
        bais = new ByteArrayInputStream(xsl.getBytes());
        return bais;
    }

    public String getXSL() {
        return xsl;
    }
}
