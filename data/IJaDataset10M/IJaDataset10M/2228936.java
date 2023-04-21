package dbgate.complexexample.entities.product;

import dbgate.DBColumnType;
import dbgate.ermanagement.DBColumnInfo;
import dbgate.ermanagement.DBTableInfo;
import dbgate.ermanagement.DefaultServerDBClass;

/**
 * Date: Mar 31, 2011
 * Time: 9:45:55 PM
 */
@DBTableInfo(tableName = "product_item")
public abstract class Item extends DefaultServerDBClass {

    @DBColumnInfo(columnType = DBColumnType.INTEGER, key = true, subClassCommonColumn = true)
    private int itemId;

    @DBColumnInfo(columnType = DBColumnType.VARCHAR)
    private String name;

    public Item() {
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
