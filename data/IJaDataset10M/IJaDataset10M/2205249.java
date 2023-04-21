package org.jbug.mcsample.domain;

/**
 * Contains info about amount of products and price in an order.
 * 
 * @author gaston.scapusio
 * @see Order
 */
public class OrderItem {

    /**
	 * The order associated to the item.
	 */
    private Order order;

    /**
	 * The product.
	 */
    private Product product;

    /**
	 * The amount of product delivery.
	 */
    private int amount;

    /**
	 * The price per product.
	 */
    private double price;

    /**
	 * @return the order
	 */
    public Order getOrder() {
        return order;
    }

    /**
	 * @param order
	 *            the order to set
	 */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
	 * @return the product
	 */
    public Product getProduct() {
        return product;
    }

    /**
	 * @param product
	 *            the product to set
	 */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
	 * @return the amount
	 */
    public int getAmount() {
        return amount;
    }

    /**
	 * @param amount
	 *            the amount to set
	 */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
	 * @return the price per product
	 */
    public double getPrice() {
        return price;
    }

    /**
	 * @param price
	 *            the price per product to set
	 */
    public void setPrice(double price) {
        this.price = price;
    }
}
