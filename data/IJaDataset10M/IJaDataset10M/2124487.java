package simpledb.book.util;

import com.xerox.amazonws.sdb.Item;
import com.xerox.amazonws.sdb.SDBException;
import com.xerox.amazonws.sdb.ItemAttribute;
import java.util.List;

/**
 * User: treeder
 * Date: Nov 17, 2008
 * Time: 9:01:21 AM
 */
public class QueryUtils {

    public static void printItems(List<Item> items) throws SDBException {
        for (Item item : items) {
            QueryUtils.printItem(item);
        }
    }

    public static void printItem(Item item) throws SDBException {
        printItem(item.getIdentifier(), item.getAttributes());
    }

    public static void printItem(String identifier, List<ItemAttribute> attributes) {
        System.out.println("Item ID: " + identifier);
        for (ItemAttribute attribute : attributes) {
            System.out.println("\t{" + attribute.getName() + ", " + attribute.getValue() + "}");
        }
    }

    public static String getAttribute(List<ItemAttribute> attributes, String key) {
        for (ItemAttribute attribute : attributes) {
            if (attribute.getName().equals(key)) {
                return attribute.getValue();
            }
        }
        return null;
    }
}
