package beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import utility.DBConnection;
import utility.GlobalVariables;

/**
 *
 * @author Suman
 */
public class MappingListBean {

    private String url;

    DBConnection connection;

    ResultSet rs;

    Collection mappingList;

    MappingBean mb;

    /**
     * sets the global variable for jdbc connection url
     * @throws Exception
     */
    public MappingListBean() throws Exception {
        this(GlobalVariables.getDatabaseURL());
    }

    /**
     * constructor that creates mapping list
     * @param _url
     * @throws Exception
     */
    public MappingListBean(String _url) throws Exception {
        try {
            init(_url);
            String query = "SELECT product_id, comp_id, count FROM mapping";
            rs = connection.ExecuteQuery(query);
            while (rs.next()) {
                mb = new MappingBean();
                mb.setProductId(rs.getInt("product_id"));
                mb.setComponentId(rs.getInt("comp_id"));
                mb.setCount(rs.getInt("count"));
                mappingList.add(mb);
            }
            connection.close();
        } catch (SQLException sqle) {
            throw new Exception(sqle);
        }
    }

    /**
     * initializes the variables
     * @param _url
     */
    private void init(String _url) {
        url = _url;
        connection = new DBConnection(url);
        rs = null;
        mappingList = new ArrayList();
    }

    /**
     * checks whether the product is avaliable or not
     * @param productId
     * @return flag
     */
    public boolean hasProduct(int productId) {
        Iterator iterator = mappingList.iterator();
        mb = null;
        boolean flag = false;
        while (iterator.hasNext()) {
            mb = (MappingBean) iterator.next();
            if (mb.getProductId() == productId) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
