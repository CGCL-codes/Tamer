package uk.ac.manchester.mae.visitor;

import java.util.Set;
import org.coode.manchesterowlsyntax.ManchesterOWLSyntaxDescriptionParser;
import org.semanticweb.owl.expression.ParserException;
import org.semanticweb.owl.expression.ShortFormEntityChecker;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owl.util.OWLEntitySetProvider;
import org.semanticweb.owl.util.ReferencedEntitySetProvider;
import org.semanticweb.owl.util.SimpleShortFormProvider;
import uk.ac.manchester.mae.parser.MAEBinding;
import uk.ac.manchester.mae.parser.MAEConflictStrategy;
import uk.ac.manchester.mae.parser.MAEStoreTo;
import uk.ac.manchester.mae.parser.MAEmanSyntaxClassExpression;
import uk.ac.manchester.mae.parser.MAEpropertyChainExpression;
import uk.ac.manchester.mae.parser.SimpleNode;

/**
 * @author Luigi Iannone
 * 
 *         The University Of Manchester<br>
 *         Bio-Health Informatics Group<br>
 *         Mar 12, 2008
 */
public class ClassExtractor extends FormulaSetupVisitor {

    private OWLOntologyManager manager;

    private Set<OWLOntology> ontologies;

    private OWLDescription classDescription = null;

    /**
	 * @param ontologies
	 * @param shortFormProvider
	 * @param manager
	 */
    public ClassExtractor(Set<OWLOntology> ontologies, OWLOntologyManager manager) {
        this.ontologies = ontologies;
        this.manager = manager;
    }

    @Override
    public Object visit(SimpleNode node, Object data) {
        return null;
    }

    public Object visit(MAEmanSyntaxClassExpression node, Object data) {
        BidirectionalShortFormProviderAdapter adapter = new BidirectionalShortFormProviderAdapter(new SimpleShortFormProvider());
        OWLEntitySetProvider<OWLEntity> owlEntitySetProvider = new ReferencedEntitySetProvider(this.ontologies);
        adapter.rebuild(owlEntitySetProvider);
        ManchesterOWLSyntaxDescriptionParser parser = new ManchesterOWLSyntaxDescriptionParser(this.manager.getOWLDataFactory(), new ShortFormEntityChecker(adapter));
        try {
            this.classDescription = parser.parse(node.getContent());
            data = this.classDescription;
            return data;
        } catch (ParserException e) {
            return null;
        }
    }

    public Object visit(MAEBinding node, Object data) {
        Object toReturn = data;
        if (data == null) {
            data = this.manager.getOWLDataFactory().getOWLThing();
            toReturn = data;
        }
        return toReturn;
    }

    public Object visit(MAEpropertyChainExpression node, Object data) {
        Object toReturn = data;
        if (data == null) {
            data = this.manager.getOWLDataFactory().getOWLThing();
            toReturn = data;
        }
        return toReturn;
    }

    public Object visit(MAEConflictStrategy node, Object data) {
        Object toReturn = data;
        if (data == null) {
            data = this.manager.getOWLDataFactory().getOWLThing();
            toReturn = data;
        }
        return toReturn;
    }

    /**
	 * @see uk.ac.manchester.mae.parser.ArithmeticsParserVisitor#visit(uk.ac.manchester.mae.parser.MAEStoreTo,
	 *      java.lang.Object)
	 */
    public Object visit(MAEStoreTo node, Object data) {
        return null;
    }

    public OWLDescription getClassDescription() {
        return this.classDescription;
    }
}
