package com.volantis.mcs.marlin.sax;

import org.xml.sax.ContentHandler;

/**
 * This class is responsible for processing the incoming SAX2 events and
 * invoking PAPI.
 * <p>
 * This class requires that the startElement and endElement methods are given
 * non-null values for the localName parameter. This can be ensured by using
 * a namespace aware parser and setting the feature identified by the URI
 * http://xml.org/sax/features/namespaces.
 * <p>
 * This class can be used anywhere that a ContentHandler can be used, e.g. with
 * an XMLReader, or XMLFilter.
 * <pre>
 *   MarlinContentHandler handler = MarlinSAXHelper.getContentHandler(marinerRequestContext);
 *   XMLReader reader = MarlinSAXHelper.getXMLReader ();
 *   reader.setContentHandler(handler);
 * </pre>
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface MarlinContentHandler extends ContentHandler {
}
