package edu.udo.cs.ai.nemoz.model.database.operations.base;

import java.util.Set;
import edu.udo.cs.ai.nemoz.model.database.NemozDatabase;
import edu.udo.cs.ai.nemoz.model.entities.Category;
import edu.udo.cs.ai.trude.core.AttributeTemplate;
import edu.udo.cs.ai.trude.core.Database;
import edu.udo.cs.ai.trude.core.Header;
import edu.udo.cs.ai.trude.core.Relation;

/**
 * Returns the set of Categories of the given database.
 *
 * @author oflasch
 */
public final class GetCategories {

    public static Set<Category> _(final Database database) {
        final Relation itemCategories = database.getRelationVariable("IC").project(Header.with(AttributeTemplate.with(NemozDatabase.CATEGORY, Category.class)));
        final Relation superCategories = database.getRelationVariable("CC").project(Header.with(AttributeTemplate.with(NemozDatabase.SUPER_CATEGORY, Category.class)));
        final Set<Category> allCategories = BaseOperationUtils.extractCategories(itemCategories, NemozDatabase.CATEGORY);
        allCategories.addAll(BaseOperationUtils.extractCategories(superCategories, NemozDatabase.SUPER_CATEGORY));
        return allCategories;
    }
}
