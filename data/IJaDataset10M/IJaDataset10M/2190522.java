package org.nakedobjects.examples.orders.services;

import java.util.List;
import org.nakedobjects.applib.AbstractFactoryAndRepository;
import org.nakedobjects.applib.Filter;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.examples.orders.domain.Product;

@Named("Products")
public class ProductRepository extends AbstractFactoryAndRepository {

    /**
     * Lists all products in the repository.
     */
    public List<Product> showAll() {
        return allInstances(Product.class);
    }

    /**
     * Returns the Product with given code.
     */
    public Product findByCode(@Named("Code") final String code) {
        return firstMatch(Product.class, new Filter<Product>() {

            public boolean accept(Product obj) {
                return code.equals(obj.getCode());
            }
        });
    }

    /**
     * Creates a new (already persisted) product.
     * 
     * <p>
     * For use by fixtures only.
     * 
     * @return
     */
    @Hidden
    public Product newProduct(String code, String description, int priceInPence) {
        Product product = (Product) newTransientInstance(Product.class);
        product.setCode(code);
        product.setDescription(description);
        product.setPrice(new Double(priceInPence / 100));
        getContainer().persist(product);
        return product;
    }

    /**
     * Use <tt>Product.gif</tt> for icon.
     */
    public String iconName() {
        return "Product";
    }
}
