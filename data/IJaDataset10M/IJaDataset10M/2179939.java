package tags;

import beans.ProductBean;
import beans.ShoppingBean;
import java.awt.PageAttributes;
import java.sql.ResultSet;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import utility.DBConnection;

/**
 * This is the class for tag productInfo.
 * It displays all product info including the price
 * @author krishna
 */
public class ProductInfoTag extends TagSupport {

    public ProductInfoTag() {
        super();
    }

    @Override
    public int doStartTag() throws JspException {
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();
            HttpSession session = pageContext.getSession();
            ShoppingBean shoppingCart = (ShoppingBean) session.getAttribute("shoppinglist");
            Iterator iterator = shoppingCart.getCart().iterator();
            Object[] shoppingInfo = null;
            while (iterator.hasNext()) {
                shoppingInfo = new Object[2];
                shoppingInfo = (Object[]) iterator.next();
                ProductBean product = (ProductBean) shoppingInfo[0];
                int quantity = (Integer) shoppingInfo[1];
                double productCost = this.getCost(product.getId());
                double totalCost = productCost * quantity;
                StringBuilder builder = new StringBuilder();
                out.print("<shoppingitem>");
                builder.append("<shoppingcart>");
                out.print(product.getXml());
                builder.append(product.getXml());
                out.print("<quantity>");
                builder.append("<quantity>");
                out.print(quantity);
                builder.append(quantity);
                out.print("</quantity>");
                builder.append("</quantity>");
                out.print("<cost>");
                builder.append("<cost>");
                out.print(String.valueOf(totalCost));
                builder.append(String.valueOf(totalCost));
                out.print("</cost>");
                builder.append("</cost>");
                out.print("</shoppingitem>");
                builder.append("</shoppingcart>");
            }
        } catch (Exception ex) {
            throw new JspException(ex);
        }
        return EVAL_PAGE;
    }

    /**
     * Gets the cost of the product
     * @param productId product id
     * @return total cost
     * @throws Exception
     */
    public double getCost(int productId) throws Exception {
        String jdbcURL = pageContext.getServletContext().getInitParameter("JDBC_URL");
        String query = "SELECT SUM(a.rate) product_cost ";
        query += "FROM components a ";
        query += "INNER JOIN ( ";
        query += "SELECT b.product_id, c.comp_id ";
        query += "FROM products b ";
        query += "INNER JOIN mapping c ON b.product_id = c.product_id WHERE b.product_id=" + productId;
        query += ")d ON a.comp_id = d.comp_id";
        DBConnection connection = new DBConnection(jdbcURL);
        ResultSet resultSet = connection.ExecuteQuery(query);
        resultSet.next();
        double quantity = resultSet.getDouble("product_cost");
        return quantity;
    }
}
