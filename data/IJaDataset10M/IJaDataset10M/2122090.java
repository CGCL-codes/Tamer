package edu.udo.cs.ai.nemoz.model.database.operations.base;

import java.util.HashSet;
import java.util.Set;
import edu.udo.cs.ai.nemoz.model.database.NemozDatabase;
import edu.udo.cs.ai.nemoz.model.database.operations.base.BaseOperationException.NoSuchEntityException;
import edu.udo.cs.ai.nemoz.model.entities.Aspect;
import edu.udo.cs.ai.nemoz.model.entities.Category;
import edu.udo.cs.ai.trude.core.Attribute;
import edu.udo.cs.ai.trude.core.Database;
import edu.udo.cs.ai.trude.core.Relation;
import edu.udo.cs.ai.trude.core.Tuple;
import edu.udo.cs.ai.trude.predicates.MatchPredicate;

/**
 * Returns the set of all Aspects of the given Category.
 *
 * @author oflasch
 */
public final class GetAspectsOfCategory {

    public static Set<Aspect> _(final Database database, final Category category) throws NoSuchEntityException {
        return aspectsOfCategory(database, category);
    }

    protected static Set<Aspect> aspectsOfCategory(final Database database, final Category category) {
        final Set<Aspect> result = new HashSet<Aspect>();
        final Relation directAspects = database.getRelationVariable("CA").restrict(new MatchPredicate(new Tuple(new Attribute<Category>(NemozDatabase.CATEGORY, category))));
        result.addAll(BaseOperationUtils.extractAspects(directAspects, NemozDatabase.ASPECT));
        for (Category superCategory : GetSuperCategories._(database, category)) {
            result.addAll(aspectsOfCategory(database, superCategory));
        }
        return result;
    }
}
