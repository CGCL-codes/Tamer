package db.dbio;

import java.util.Vector;
import db.core.DBIO;

public class OrderProductDBIOImpl extends DBIO {

    private final String addOrderProductString = "INSERT INTO orderproduct " + " ( order_id , product_id , isAvaliable ) " + " VALUES (?,?,?)";

    private final String getOrderProductString = "SELECT product_id , isAvaliable FROM orderproduct WHERE order_id = ?";

    private final String updateWaitingOrderString = "UPDATE orderproduct " + "SET isAvaible = ? WHERE product_id = ? AND order_id = ? ";

    public boolean addOrderProduct(int order_id, int product_id, boolean isAvaible) {
        Vector<Object> data = new Vector<Object>();
        data.add(order_id);
        data.add(product_id);
        data.add(isAvaible);
        return updateDB(addOrderProductString, data);
    }

    public Vector<Vector<Object>> getOrderProduct(int order_id) {
        Vector<Object> data = new Vector<Object>();
        data.add(order_id);
        return select(getOrderProductString, data);
    }

    public void updateWaitingOrder(int order_id, int[] notAvaliableProduct_id) {
        String sql = "UPDATE orderproduct SET isAvaliable = true WHERE " + "product_id NOT IN ( 0 ";
        for (int i = 0; i < notAvaliableProduct_id.length; i++) {
            sql = sql + "," + notAvaliableProduct_id[i];
        }
        sql = sql + ") AND order_id = ?";
        Vector data = new Vector();
        data.add(order_id);
        System.out.println(updateDB(sql, data));
    }

    public void updateWaitingOrder(int order_id, int product_id, boolean isAvaible) {
        Vector<Object> data = new Vector<Object>();
        data.add(isAvaible);
        data.add(product_id);
        data.add(order_id);
        updateDB(updateWaitingOrderString, data);
    }
}
