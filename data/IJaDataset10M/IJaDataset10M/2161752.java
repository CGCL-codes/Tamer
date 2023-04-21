package concrete.predicate;

import gnu.jel.CompilationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import concrete.DomainConverter;
import concrete.relationconverter.AbstractRelationConverter;
import concrete.relationconverter.Relation;

public class PredicateToCompilablePredicate extends AbstractRelationConverter {

    private static final Logger logger = Logger.getLogger(PredicateToCompilablePredicate.class.getSimpleName());

    private final Map<cspom.predicate.Predicate, JELPredicate> compiled;

    public PredicateToCompilablePredicate(final DomainConverter domains) {
        super(domains);
        compiled = new HashMap<cspom.predicate.Predicate, JELPredicate>();
    }

    @Override
    public Relation convert(final cspom.Relation relation, final List<cspom.variable.Variable> signature) {
        if (!(relation instanceof cspom.predicate.Predicate)) {
            return null;
        }
        final cspom.predicate.Predicate predicate = (cspom.predicate.Predicate) relation;
        final JELPredicate jelPredicate = compiled.get(predicate);
        if (jelPredicate != null) {
            return new PredicateRelation(jelPredicate);
        }
        try {
            final JELPredicate compiledPredicate = new JELPredicate((cspom.predicate.Predicate) relation);
            compiled.put(predicate, compiledPredicate);
            return new PredicateRelation(compiledPredicate);
        } catch (CompilationException e) {
            logger.throwing(PredicateToCompilablePredicate.class.getSimpleName(), "convert", e);
            return null;
        }
    }

    @Override
    public boolean memoryConsuming() {
        return false;
    }

    @Override
    public boolean timeConsuming() {
        return false;
    }
}
