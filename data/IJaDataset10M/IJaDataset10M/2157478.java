package org.jbug.mcsample.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

/**
 * DAO implementation for <code>StockDAO</code>, <code>OrderDAO</code> and 
 * <code>ProductDAO</code>.
 * Holds the data in memory. 
 * 
 * @author gaston.scapusio
 *
 */
public class GenericDAOInMemory implements StockDAO, OrderDAO, ProductDAO {

    /**
	 * Stock sequence.
	 */
    private long stockSequence;

    /**
	 * Product sequence.
	 */
    private long productSequence;

    /**
	 * Order sequence.
	 */
    private long orderSequence;

    /**
	 * Stock map.
	 * Key: product id.
	 */
    private HashMap<Long, Stock> stockMap;

    /**
	 * Product map.
	 * Key: product id.
	 */
    private HashMap<Long, Product> productMap;

    /**
	 * Order map.
	 * Key: order id.
	 */
    private HashMap<Long, Order> orderMap;

    /**
	 * Default constructor.
	 */
    public GenericDAOInMemory() {
        stockMap = new HashMap<Long, Stock>();
        productMap = new HashMap<Long, Product>();
        orderMap = new HashMap<Long, Order>();
        initial();
    }

    public Stock getStock(Product product) {
        return stockMap.get(product.getId());
    }

    public void save(Stock stock) {
        stock.setId(++stockSequence);
        stockMap.put(stock.getId(), stock);
    }

    public void update(Stock stock) {
        stockMap.put(stock.getId(), stock);
    }

    public void save(Order order) {
        order.setId(++orderSequence);
        orderMap.put(order.getId(), order);
    }

    public void save(Product product) {
        product.setId(++productSequence);
        productMap.put(product.getId(), product);
    }

    /**
	 * {@inheritDoc}
	 */
    public Set<Product> loadProducts() {
        Set<Product> products = new HashSet<Product>();
        Set<Entry<Long, Product>> entries = productMap.entrySet();
        for (Entry<Long, Product> entry : entries) {
            products.add(entry.getValue());
        }
        return products;
    }

    private void initial() {
        Product product = new Product();
        product.setName("Bayer Aspirin x 10");
        product.setDescription("Bayer Aspirin x 10.");
        product.setPrice(5);
        save(product);
        Stock stock = new Stock();
        stock.setProduct(product);
        stock.setAmount(10);
        save(stock);
    }
}
