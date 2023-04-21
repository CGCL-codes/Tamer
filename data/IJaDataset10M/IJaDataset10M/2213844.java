package gui.product.returned.listing;

import javax.swing.table.TableModel;
import db.core.TableAgent;
import db.dbio.UserDBIOImpl;
import domain.register.Drawer;
import domain.store.user.User;
import gui.dynamictable.DynamicTableModel;
import gui.listing.ListingModel;

public class ListingReturnedProductModel extends ListingModel {

    private static String query = "SELECT productName , barcodeNo , returnedproducts.unitPrice, returnedproducts.profit, returnedproducts.discount, productGroupName , returnedproducts.taxPercent , taxName ,product.product_id " + "from product , productgroup ,producttaxgroup,taxgroup , returns, returnedproducts " + "WHERE returnedproducts.product_id=product.product_id and product.group_id=productgroup.group_id and " + "productgroup.group_id=producttaxgroup.group_id and producttaxgroup.tax_id=taxgroup.tax_id and returns.return_id = returnedproducts.return_id";

    private static String[] tableNames = new String[] { "product", "productgroup", "taxgroup", "producttaxgroup" };

    private static String[] requieredColumnNames = new String[] { "product_id", "productName", "barcodeNo", "unitPrice", "profit", "discount", "productGroupName", "taxName", "taxPercent" };

    private static String[] disabledColumns = { "*" };

    public ListingReturnedProductModel() {
        super(query, tableNames, requieredColumnNames, disabledColumns);
    }

    public TableModel getTableModel() {
        String query2 = new String(query);
        if (!isAdmin(Drawer.getCurrentUser_id())) {
            query2 = query2 + " and user_id = " + Drawer.getCurrentUser_id();
        }
        TableAgent tableAgent = new TableAgent(dbTableNames, requieredColumnNames, query2);
        TableModel tabelModel = new DynamicTableModel(tableAgent, disabledColumns);
        return tabelModel;
    }

    private boolean isAdmin(int currentUser_id) {
        UserDBIOImpl userDb = new UserDBIOImpl();
        User user = userDb.getUserById(currentUser_id);
        return (user.getRole().equals("Admin"));
    }
}
