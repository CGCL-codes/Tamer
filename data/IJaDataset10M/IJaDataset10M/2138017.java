package com.funambol.toolbox.mime.xmlhash;

public class SifTToXmlConverter extends SifToXmlConverter {

    public SifTToXmlConverter() {
        this("SIF-TASKS", S4J_SIF_T);
    }

    public SifTToXmlConverter(String label) {
        this(label, S4J_SIF_T);
    }

    public SifTToXmlConverter(String label, String mimeType) {
        super(label, mimeType, "<task>", "</task>");
    }
}
