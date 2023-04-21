package eu.larkc.iris.storage.rdf;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.factory.IBasicFactory;
import org.deri.iris.api.factory.ITermFactory;
import org.deri.iris.basics.BasicFactory;
import org.deri.iris.terms.TermFactory;
import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.impl.TriplePatternImpl;
import org.ontoware.rdf2go.model.node.NodeOrVariable;
import org.ontoware.rdf2go.model.node.ResourceOrVariable;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import eu.larkc.iris.storage.FactsStorage;

/**
 * @author valer
 *
 */
public class RdfStorage implements FactsStorage {

    private Model model;

    private String predicateFilter;

    private ClosableIterator<Statement> iterator;

    @Override
    public IAtom next() {
        if (iterator == null) {
            if (predicateFilter != null) {
                iterator = model.findStatements(new TriplePatternImpl((ResourceOrVariable) null, new URIImpl(predicateFilter), (NodeOrVariable) null));
            } else {
                iterator = model.iterator();
            }
        }
        if (!iterator.hasNext()) {
            return null;
        }
        Statement statement = iterator.next();
        ITermFactory termFactory = TermFactory.getInstance();
        IBasicFactory basicFactory = BasicFactory.getInstance();
        RdfAtom rdfAtom = new RdfAtom(basicFactory.createPredicate(statement.getPredicate().toString(), 2), termFactory.createString(statement.getSubject().toString()), termFactory.createString(statement.getObject().toString()));
        return rdfAtom;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String getPredicateFilter() {
        return predicateFilter;
    }

    public void setPredicateFilter(String predicatefilter) {
        this.predicateFilter = predicatefilter;
    }
}
