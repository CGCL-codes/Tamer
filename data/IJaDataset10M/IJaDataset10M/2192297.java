package net.sourceforge.configured.examples.domain.dao.product;

import net.sourceforge.configured.examples.domain.dao.GenericDao;
import net.sourceforge.configured.examples.domain.entities.product.Category;

public class CategoryDao extends GenericDao<Category> {

    public CategoryDao() {
        super(Category.class);
    }
}
