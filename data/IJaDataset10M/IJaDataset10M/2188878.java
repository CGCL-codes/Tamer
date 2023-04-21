package uk.ac.manchester.mae.visitor;

import org.semanticweb.owl.model.OWLDescription;
import uk.ac.manchester.mae.parser.ArithmeticsParserVisitor;

/**
 * @author Luigi Iannone
 * 
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Apr 25, 2008
 */
public abstract class FacetExtractor implements ArithmeticsParserVisitor {

    public abstract OWLDescription getExtractedDescription();
}
