package beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import utility.DBConnection;
import utility.GlobalVariables;

/**
 * This is the bean which is used to get the list of product items.
 * @author krishna
 */
public class ProductListBean {

    private String jdbcURL = null;

    DBConnection connection = null;

    private Collection productList;

    /**
     * Constructor
     * @throws Exception
     */
    public ProductListBean() throws Exception {
        this(GlobalVariables.getDatabaseURL());
    }

    /**
     * Constructor
     * @param url jdbcURL
     * @throws Exception
     */
    public ProductListBean(String url) throws Exception {
        this.jdbcURL = url;
        this.connection = new DBConnection(this.jdbcURL);
        this.productList = new ArrayList();
        this.poplulateProducts();
    }

    /**
     * Get the product lists
     * @return product list
     */
    public Collection getProductList() {
        return this.productList;
    }

    /**
     * Gets the product with specified ID
     * @param id product id
     * @return ProductBean
     */
    public ProductBean getProduct(int id) {
        ProductBean item = null;
        Iterator iterator = this.productList.iterator();
        while (iterator.hasNext()) {
            ProductBean temp = (ProductBean) iterator.next();
            if (temp.getId() == id) {
                item = temp;
                break;
            }
        }
        return item;
    }

    /**
     * Load the product items from the database
     * @throws Exception
     */
    public void poplulateProducts() throws Exception {
        String query = "SELECT product_id,name,description FROM products";
        ResultSet resultSet = this.connection.ExecuteQuery(query);
        while (resultSet.next()) {
            ProductBean item = new ProductBean();
            item.setId(resultSet.getInt("product_id"));
            item.setName(resultSet.getString("name"));
            item.setDescription(resultSet.getString("description"));
            productList.add(item);
        }
        this.connection.close();
    }

    /**
     * Gets the list of components of specific product
     * @param productId
     * @return List of Components
     */
    public ArrayList getComponents(int productId) throws Exception, SQLException {
        ArrayList componentList = new ArrayList();
        String query = "SELECT a.comp_id,a.name,a.rate,a.quantity,a.description " + "FROM components a INNER JOIN mapping b ON a.comp_id=b.comp_id WHERE b.product_id='" + productId + "'";
        ResultSet resultSet = this.connection.ExecuteQuery(query);
        while (resultSet.next()) {
            ComponentBean component = new ComponentBean();
            component.setId(resultSet.getInt("comp_id"));
            component.setName(resultSet.getString("name"));
            component.setRate(resultSet.getDouble("rate"));
            component.setQuantity(resultSet.getInt("quantity"));
            component.setDescription(resultSet.getString("description"));
            componentList.add(component);
        }
        this.connection.close();
        return componentList;
    }

    /**
     * XML representation for dislay
     * Suitable for XSLT
     * @return XML string
     */
    public String getXml() {
        Iterator iterator = this.productList.iterator();
        StringBuilder xmlOut = new StringBuilder();
        xmlOut.append("<productlist>");
        while (iterator.hasNext()) {
            ProductBean item = (ProductBean) iterator.next();
            xmlOut.append(item.getXml());
        }
        xmlOut.append("</productlist>");
        return xmlOut.toString();
    }

    /**
     * For testing purpose
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        ProductListBean list = new ProductListBean();
        ArrayList alist = list.getComponents(2);
        System.out.println(alist.size());
        for (int i = 0; i < alist.size(); i++) {
            System.out.println(((ComponentBean) alist.get(i)).getDescription());
        }
    }
}
