package agilehk.wineBar.stock;

public class StockItem {

    private String id = "";

    /**
	 * @return the id
	 */
    public final String getId() {
        return id;
    }

    private String desc = "";

    /**
	 * @return the desc
	 */
    public final String getDesc() {
        return desc;
    }

    private int cost = 0;

    public final int getItemCost() {
        return cost;
    }

    private int stockLevel = 0;

    /**
	 * @return the stockLevel
	 */
    public final int getStockLevel() {
        return stockLevel;
    }

    public StockItem(final String newId, final String desc, final int cost, final int initialStockLevel) throws StockCannotBeNegativeException {
        if (newId == null || newId.trim().equals("")) {
            throw new IllegalArgumentException("Stock ID cannot be blank.");
        }
        if (desc == null || desc.trim().equals("")) {
            throw new IllegalArgumentException("Stock description cannot be blank.");
        }
        if (initialStockLevel < 0) {
            throw new StockCannotBeNegativeException(initialStockLevel);
        }
        this.id = newId;
        this.desc = desc;
        this.cost = cost;
        this.stockLevel = initialStockLevel;
    }

    public final int removeStock(final int amount) {
        if (stockLevel - amount < 0) {
            throw new StockCannotBeNegativeException(stockLevel - amount);
        }
        stockLevel -= amount;
        return stockLevel;
    }
}
