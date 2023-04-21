package Product;

import java.util.*;
import java.io.Serializable;

/**need addItem(prod), lookupItem(prod), and removeItem(upc)?
 *
 * this class creates a table of all the products. It can also
 * look up or remove an item from the list by matching the UPC code
 * @author Ken Nguyen
 */
public class ProductCatalog implements Serializable {

    private Map<Integer, Product> productLog;

    public ProductCatalog() {
        productLog = new HashMap<Integer, Product>();
    }

    public void addItem(Product newProduct) {
        this.productLog.put(newProduct.getUPC(), newProduct);
    }

    public Product lookupItem(int upc) {
        return productLog.get(upc);
    }

    public void removeItem(int upc) {
        productLog.remove(upc);
    }

    public Map<Integer, Product> getProductLog() {
        return this.productLog;
    }
}
