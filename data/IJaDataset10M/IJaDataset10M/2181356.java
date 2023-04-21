package twoadw.website.specificationcategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelibra.IDomainModel;
import twoadw.website.product.Product;

/**
 * SpecificationCategory specific entities.
 * 
 * @author TeamFcp
 * @version 2009-03-16
 */
public class SpecificationCategories extends GenSpecificationCategories {

    private static final long serialVersionUID = 1236796091294L;

    private static Log log = LogFactory.getLog(SpecificationCategories.class);

    /**
	 * Constructs specificationCategories within the domain model.
	 * 
	 * @param model
	 *            model
	 */
    public SpecificationCategories(IDomainModel model) {
        super(model);
    }

    /**
		 * Constructs specificationCategories for the product parent.
		 * 
		 * @param product
		 *            product
		 */
    public SpecificationCategories(Product product) {
        super(product);
    }
}
