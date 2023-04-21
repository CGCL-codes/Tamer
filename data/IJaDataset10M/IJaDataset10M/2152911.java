package edu.udo.cs.ai.nemoz.model.database.operations.base;

import java.util.Set;
import edu.udo.cs.ai.nemoz.model.database.NemozDatabase;
import edu.udo.cs.ai.nemoz.model.database.operations.base.BaseOperationException.NoSuchEntityException;
import edu.udo.cs.ai.nemoz.model.entities.Category;
import edu.udo.cs.ai.trude.core.Attribute;
import edu.udo.cs.ai.trude.core.Database;
import edu.udo.cs.ai.trude.core.Relation;
import edu.udo.cs.ai.trude.core.Tuple;
import edu.udo.cs.ai.trude.predicates.MatchPredicate;

/**
 * Returns the set of direct sub categories of a given category.
 *
 * @author oflasch
 */
public final class GetSubCategories {

    public static Set<Category> _(final Database database, final Category category) throws NoSuchEntityException {
        return BaseOperationUtils.extractCategories(subCategories(database, category), NemozDatabase.SUB_CATEGORY);
    }

    public static Relation subCategories(final Database database, final Category superCategory) {
        return database.getRelationVariable("CC").restrict(new MatchPredicate(new Tuple(new Attribute<Category>(NemozDatabase.SUPER_CATEGORY, superCategory))));
    }
}
