package org.openscience.cdk.io.cml;

import org.openscience.cdk.interfaces.IChemFile;
import org.xml.sax.Attributes;

/**
 * This interface describes the procedures classes must implement to be plugable
 * into the CMLHandler. Most procedures reflect those in SAX2.
 *
 * @cdk.module io
 * @cdk.githash
 *
 * @author Egon Willighagen <egonw@sci.kun.nl>
 **/
public interface ICMLModule {

    void startDocument();

    void endDocument();

    void startElement(CMLStack xpath, String uri, String local, String raw, Attributes atts);

    void endElement(CMLStack xpath, String uri, String local, String raw);

    void characterData(CMLStack xpath, char ch[], int start, int length);

    IChemFile returnChemFile();

    void inherit(ICMLModule conv);
}
