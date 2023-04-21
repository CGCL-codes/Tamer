package de.beas.explicanto.jaxb.impl.runtime;

public interface PrefixCallback {

    void onPrefixMapping(String prefix, String uri) throws org.xml.sax.SAXException;
}
