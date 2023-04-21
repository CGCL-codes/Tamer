package org.simplecart.shopcart.catalog;

/**
 * @version     $LastChangedRevision: 64 $ $LastChangedDate: 2005-04-06 20:24:13 -0400 (Wed, 06 Apr 2005) $
 * @author      Daniel Watrous
 */
public class InternetAssociatedOption extends InternetAssociation {

    private Product product;

    private ProductOption productOption;

    /**
     * No-arg constructor for JavaBean tools
     */
    InternetAssociatedOption() {
    }

    /**
     * Full constructor;
     */
    public InternetAssociatedOption(Product product, ProductOption productOption) {
        this.product = product;
        this.productOption = productOption;
    }

    public ProductOption getProductOption() {
        return productOption;
    }

    public Product getProduct() {
        return product;
    }
}
