package twoadw.website.productrebate;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelibra.Entities;
import org.modelibra.IDomainModel;
import org.modelibra.ISelector;
import org.modelibra.DomainModel;
import org.modelibra.Oid;
import org.modelibra.PropertySelector;
import twoadw.website.product.Product;
import twoadw.website.rebate.Rebate;
import twoadw.website.rebate.Rebates;

/**
 * ProductRebate generated entities. This class should not be changed manually. 
 * Use a subclass to add specific code.
 * 
 * @author TeamFcp
 * @version 2009-03-27
 */
public abstract class GenProductRebates extends Entities<ProductRebate> {

    private static final long serialVersionUID = 1236704565608L;

    private static Log log = LogFactory.getLog(GenProductRebates.class);

    private Product product;

    private Rebate rebate;

    /**
	 * Constructs productRebates within the domain model.
	 * 
	 * @param model
	 *            model
	 */
    public GenProductRebates(IDomainModel model) {
        super(model);
    }

    /**
		 * Constructs productRebates for the product parent.
		 * 
		 * @param product
		 *            product
		 */
    public GenProductRebates(Product product) {
        this(product.getModel());
        setProduct(product);
    }

    /**
		 * Constructs productRebates for the rebate parent.
		 * 
		 * @param rebate
		 *            rebate
		 */
    public GenProductRebates(Rebate rebate) {
        this(rebate.getModel());
        setRebate(rebate);
    }

    /**
 * Retrieves the productRebate with a given oid. 
 * Null if not found. 
	 * 
	 * @param oid
	 *            oid
	 * @return productRebate
	 */
    public ProductRebate getProductRebate(Oid oid) {
        return retrieveByOid(oid);
    }

    /**
 * Retrieves the productRebate with a given oid unique number. 
 * Null if not found. 
	 * 
	 * @param oidUniqueNumber
	 *            oid unique number
	 * @return productRebate
	 */
    public ProductRebate getProductRebate(Long oidUniqueNumber) {
        return getProductRebate(new Oid(oidUniqueNumber));
    }

    /**
 * Retrieves the first productRebate whose property with a  
 * property code is equal to a property object. Null if not found. 
	 * 
	 * @param propertyCode
	 *            property code
	 * @param property
	 *            property
	 * @return productRebate
	 */
    public ProductRebate getProductRebate(String propertyCode, Object property) {
        return retrieveByProperty(propertyCode, property);
    }

    /**
	 * Selects productRebates whose property with a property code is equal to a property
	 * object. Returns empty entities if no selection.
	 * 
	 * @param propertyCode
	 *            property code
	 * @param property
	 *            property
	 * @return productRebates
	 */
    public ProductRebates getProductRebates(String propertyCode, Object property) {
        return (ProductRebates) selectByProperty(propertyCode, property);
    }

    /**
	 * Gets productRebates ordered by a property.
	 * 
	 * @param propertyCode
	 *            property code
	 * @param ascending
	 *            <code>true</code> if the order is ascending
	 * @return ordered productRebates
	 */
    public ProductRebates getProductRebates(String propertyCode, boolean ascending) {
        return (ProductRebates) orderByProperty(propertyCode, ascending);
    }

    /**
	 * Gets productRebates selected by a selector. Returns empty productRebates if there are no
	 * productRebates that satisfy the selector.
	 * 
	 * @param selector
	 *            selector
	 * @return selected productRebates
	 */
    public ProductRebates getProductRebates(ISelector selector) {
        return (ProductRebates) selectBySelector(selector);
    }

    /**
	 * Gets productRebates ordered by a composite comparator.
	 * 
	 * @param comparator
	 *            comparator
	 * @param ascending
	 *            <code>true</code> if the order is ascending
	 * @return ordered productRebates
	 */
    public ProductRebates getProductRebates(Comparator comparator, boolean ascending) {
        return (ProductRebates) orderByComparator(comparator, ascending);
    }

    /**
		 * Gets productRebate based on many-to-many parents.
		 * 
				 * @param Product product
				 * @param Rebate rebate
			 */
    public ProductRebate getProductRebate(Product product, Rebate rebate) {
        for (ProductRebate productRebate : this) {
            if (productRebate.getProduct() == product && productRebate.getRebate() == rebate) {
                return productRebate;
            }
        }
        return null;
    }

    /**
		 * Sets product.
		 * 
		 * @param product
		 *            product
		 */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
		 * Gets product.
		 * 
		 * @return product
		 */
    public Product getProduct() {
        return product;
    }

    /**
		 * Sets rebate.
		 * 
		 * @param rebate
		 *            rebate
		 */
    public void setRebate(Rebate rebate) {
        this.rebate = rebate;
    }

    /**
		 * Gets rebate.
		 * 
		 * @return rebate
		 */
    public Rebate getRebate() {
        return rebate;
    }

    protected boolean postAdd(ProductRebate productRebate) {
        if (!isPost()) {
            return true;
        }
        boolean post = true;
        if (super.postAdd(productRebate)) {
            DomainModel model = (DomainModel) getModel();
            if (model.isInitialized()) {
                Product product = getProduct();
                if (product == null) {
                    Product productRebateProduct = productRebate.getProduct();
                    if (!productRebateProduct.getProductRebates().contain(productRebate)) {
                        post = productRebateProduct.getProductRebates().add(productRebate);
                    }
                }
                Rebate rebate = getRebate();
                if (rebate == null) {
                    Rebate productRebateRebate = productRebate.getRebate();
                    if (!productRebateRebate.getProductRebates().contain(productRebate)) {
                        post = productRebateRebate.getProductRebates().add(productRebate);
                    }
                }
            }
        } else {
            post = false;
        }
        return post;
    }

    protected boolean postRemove(ProductRebate productRebate) {
        if (!isPost()) {
            return true;
        }
        boolean post = true;
        if (super.postRemove(productRebate)) {
            Product product = getProduct();
            if (product == null) {
                Product productRebateProduct = productRebate.getProduct();
                if (productRebateProduct.getProductRebates().contain(productRebate)) {
                    post = productRebateProduct.getProductRebates().remove(productRebate);
                }
            }
            Rebate rebate = getRebate();
            if (rebate == null) {
                Rebate productRebateRebate = productRebate.getRebate();
                if (productRebateRebate.getProductRebates().contain(productRebate)) {
                    post = productRebateRebate.getProductRebates().remove(productRebate);
                }
            }
        } else {
            post = false;
        }
        return post;
    }

    protected boolean postUpdate(ProductRebate beforeProductRebate, ProductRebate afterProductRebate) {
        if (!isPost()) {
            return true;
        }
        boolean post = true;
        if (super.postUpdate(beforeProductRebate, afterProductRebate)) {
            Product beforeProductRebateProduct = beforeProductRebate.getProduct();
            Product afterProductRebateProduct = afterProductRebate.getProduct();
            if (beforeProductRebateProduct != afterProductRebateProduct) {
                post = beforeProductRebateProduct.getProductRebates().remove(beforeProductRebate);
                if (post) {
                    post = afterProductRebateProduct.getProductRebates().add(afterProductRebate);
                    if (!post) {
                        beforeProductRebateProduct.getProductRebates().add(beforeProductRebate);
                    }
                }
            }
            Rebate beforeProductRebateRebate = beforeProductRebate.getRebate();
            Rebate afterProductRebateRebate = afterProductRebate.getRebate();
            if (beforeProductRebateRebate != afterProductRebateRebate) {
                post = beforeProductRebateRebate.getProductRebates().remove(beforeProductRebate);
                if (post) {
                    post = afterProductRebateRebate.getProductRebates().add(afterProductRebate);
                    if (!post) {
                        beforeProductRebateRebate.getProductRebates().add(beforeProductRebate);
                    }
                }
            }
        } else {
            post = false;
        }
        return post;
    }

    /**
	 * Creates productRebate.
	 *
			 * @param productParent product parent
			 * @param rebateParent rebate parent
			 * @return productRebate
	 */
    public ProductRebate createProductRebate(Product productParent, Rebate rebateParent) {
        ProductRebate productRebate = new ProductRebate(getModel());
        productRebate.setProduct(productParent);
        productRebate.setRebate(rebateParent);
        if (!add(productRebate)) {
            productRebate = null;
        }
        return productRebate;
    }
}
