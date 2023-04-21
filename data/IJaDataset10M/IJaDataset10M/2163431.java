package es.gva.cit.catalog.schemas;

import java.net.URI;
import es.gva.cit.catalog.metadataxml.XMLNode;

/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class UnknownRecord extends Record {

    public UnknownRecord(URI uri, XMLNode node) {
        super(uri, node);
        super.setTitle("Unknown record");
        super.setAbstract_("It doesn't exist a parser for this record");
    }

    public boolean accept(URI uri, XMLNode node) {
        return true;
    }
}
