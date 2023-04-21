package eip.chapter4.guaranteed;

import java.io.Serializable;

public class Grocery implements Serializable {

    private static final long serialVersionUID = -8815185704316877593L;

    private GroceryItem groceryItem;

    private double quantity;

    private String quantityUnit;

    public Grocery() {
    }

    public Grocery(GroceryItem groceryItem, double quantity, String quantityUnit) {
        this.groceryItem = groceryItem;
        this.quantity = quantity;
        this.quantityUnit = quantityUnit;
    }

    public GroceryItem getGroceryItem() {
        return groceryItem;
    }

    public void setGroceryItem(GroceryItem groceryItem) {
        this.groceryItem = groceryItem;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    /**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
    public String toString() {
        final String TAB = "    ";
        return "Grocery ( " + super.toString() + TAB + "groceryIteam = " + this.groceryItem + TAB + "quantity = " + this.quantity + TAB + "quantityUnit = " + this.quantityUnit + " )";
    }
}
