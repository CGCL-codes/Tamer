package edu.udo.cs.ai.nemoz.model.database.operations.base;

import java.util.Set;
import java.util.TreeSet;
import edu.udo.cs.ai.nemoz.model.database.NemozDatabase;
import edu.udo.cs.ai.nemoz.model.database.operations.base.BaseOperationException.NoSuchEntityException;
import edu.udo.cs.ai.nemoz.model.entities.Aspect;
import edu.udo.cs.ai.nemoz.model.entities.Category;
import edu.udo.cs.ai.trude.core.Attribute;
import edu.udo.cs.ai.trude.core.Database;
import edu.udo.cs.ai.trude.core.Tuple;

/**
 * Removes the given Aspect from the database.
 *
 * @author oflasch
 */
public final class RemoveAspect {

    public static void _(final Database database, final Aspect aspect) throws NoSuchEntityException {
        final Set<Category> categoriesOfThisAspect = new TreeSet<Category>();
        categoriesOfThisAspect.addAll(GetCategoriesOfAspect._(database, aspect));
        for (Category category : categoriesOfThisAspect) {
            database.getRelationVariable("CA").delete(new Tuple(new Attribute<Aspect>(NemozDatabase.ASPECT, aspect), new Attribute<Category>(NemozDatabase.CATEGORY, category)));
        }
    }
}
