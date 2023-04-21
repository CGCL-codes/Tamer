package org.dllearner.utilities.owl;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.dllearner.core.owl.Description;
import org.dllearner.kb.sparql.SparqlQueryDescriptionConvertVisitor;
import org.dllearner.parser.ManchesterSyntaxParser;
import org.dllearner.parser.ParseException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * Parser for Manchester Syntax strings (interface to OWL API parser).
 * 
 * @author Jens Lehmann
 *
 */
public class ManchesterOWLSyntaxParser {

    public static OWLClassExpression getOWLAPIDescription(String manchesterSyntaxDescription) throws ParserException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(manager.getOWLDataFactory(), manchesterSyntaxDescription);
        return parser.parseClassExpression();
    }

    public static Description getDescription(String manchesterSyntaxDescription) throws ParseException {
        return ManchesterSyntaxParser.parseClassExpression(manchesterSyntaxDescription);
    }
}
