package org.nbnResolving.views;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.nbnResolving.converters.RdfXmlConverter;
import org.nbnResolving.pidef.PidefDocument;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.DCTerms;

/**
 * Renders all UrnRecord to RDF/XML.
 * 
 * @author "Thomas Haidlas"
 */
public final class RdfXmlView extends BaseView {

    private static final Map<String, String> PREFIXES = new HashMap<String, String>();

    private static final String RDF_FORMAT = "RDF/XML-ABBREV";

    private RdfXmlConverter rdfConverter;

    static {
        PREFIXES.put("dcterms", DCTerms.NS);
        PREFIXES.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        PREFIXES.put("owl", com.hp.hpl.jena.vocabulary.OWL.NS);
    }

    /** Constructor */
    public RdfXmlView() {
        setContentType("application/rdf+xml");
    }

    /**
	 * @return RDF/XML converter
	 */
    public RdfXmlConverter getRdfConverter() {
        return this.rdfConverter;
    }

    /**
	 * @param converter Converter to set
	 */
    public void setRdfConverter(final RdfXmlConverter converter) {
        this.rdfConverter = converter;
    }

    @Override
    protected void renderMergedOutputModel(final Map<String, Object> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Model rdfModel = ModelFactory.createDefaultModel();
        rdfModel.setNsPrefixes(PREFIXES);
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            if (entry.getValue() instanceof PidefDocument) {
                final PidefDocument record = (PidefDocument) entry.getValue();
                this.rdfConverter.addRecordToModel(rdfModel, record);
            }
        }
        rdfModel.write(response.getOutputStream(), RDF_FORMAT);
    }
}
