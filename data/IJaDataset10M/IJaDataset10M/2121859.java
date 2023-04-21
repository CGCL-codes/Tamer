package org.shopformat.domain.product;

import java.util.List;
import org.apache.lucene.queryParser.ParseException;
import org.shopformat.domain.BaseGenericRepository;

/**
 * @author Stephen Vangasse
 *
 * @version $Id$
 */
public interface ProductRepository extends BaseGenericRepository<ProductGroup, Long> {

    /**
	 * 
	 * @param displayId
	 * @return
	 */
    ProductGroup findByDisplayId(String displayId);

    /**
	 * @param category
	 * @return a list of top-sellers under this category
	 */
    List<ProductGroup> getTopSellingProductsForCategory(Category category);

    /**
	 * @param category
	 * @return a list of new products under this category
	 */
    List<ProductGroup> getNewProductsForCategory(Category category);

    /**
	 * @param searchString
	 * @return a list of {@link ProductGroup}s that match the keywords in the search string
	 */
    List<ProductGroup> search(String searchString) throws ParseException;

    /**
	 * Rebuilds the search index for the complete product catalogue
	 */
    void reBuildSearchIndex();
}
