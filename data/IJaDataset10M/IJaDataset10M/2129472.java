package com.submersion.jspshop.ejb;

import java.util.*;
import java.rmi.RemoteException;
import javax.ejb.*;
import javax.naming.*;
import javax.sql.*;
import java.sql.*;

/** Description of product here
 * 
 * @author Jeff Davey (jeffdavey@submersion.com)
 * @see com.submersion.jspshop.ejb.Object
 * @see com.submersion.jspshop.ejb.ObjectSearch
 * @see com.submersion.jspshop.ejb.ObjectSearchHome
 * @see com.submersion.jspshop.ejb.ObjectHelper
 * @version $Revision: 1.1.1.1 $
 * @created: September 7, 2001  
 * @changed: $Date: 2001/10/03 05:13:43 $
 * @changedBy: $Author: jeffdavey $
*/
public class RightSearchBean implements SessionBean {

    private SessionContext ctx;

    private InitialContext jndiContext;

    public Long objectID;

    public String context;

    public Long parentID;

    public String name;

    public String className;

    public HashMap doRightSearch(Long context, Long userID) throws RemoteException {
        HashMap rval = new HashMap();
        Connection con = null;
        PreparedStatement prep = null;
        StringBuffer sbuf = new StringBuffer();
        try {
            con = this.getConnection();
            String securityQuery = getSecurityQuery();
            prep = con.prepareStatement(securityQuery);
            prep.setLong(1, context.longValue());
            prep.setLong(2, userID.longValue());
            prep.setLong(3, userID.longValue());
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                rval.put(new Long(rs.getLong(1)), rs.getString(2));
            }
        } catch (SQLException e) {
            System.err.println("jspShop: Error executing the RightSearch bean with the database: " + e);
            e.printStackTrace();
        } catch (NamingException e) {
            System.err.println("jspShop: Error looking up datasource, jdbc/jspShopDS, from the container: " + e);
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.err.println("jspShop: Error closing connection to datasource in doRightSearch method: " + e);
                    e.printStackTrace();
                }
            }
        }
        return rval;
    }

    private String getSecurityQuery() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("select object.objectID, typeValue.typeValueID from");
        sbuf.append(" jspshop_TypeValue typeValue");
        sbuf.append(", jspshop_Object object");
        sbuf.append(", jspshop_Value value");
        sbuf.append(", jspshop_Property property");
        sbuf.append(", jspshop_Type type");
        sbuf.append(" where");
        sbuf.append(" object.parentID = ?");
        sbuf.append(" and object.objectID = value.objectID");
        sbuf.append(" and value.propertyID = property.propertyID");
        sbuf.append(" and property.name = 'Security'");
        sbuf.append(" and (value.value in (");
        sbuf.append(" select");
        sbuf.append(" value.value");
        sbuf.append(" from");
        sbuf.append(" jspshop_Value value, jspshop_Property property, jspshop_Object object");
        sbuf.append(" where");
        sbuf.append(" object.objectID = ?");
        sbuf.append(" and object.objectID = value.valueID");
        sbuf.append(" and value.propertyID = property.propertyID");
        sbuf.append(" and property.name = 'Membership'");
        sbuf.append(" ) or value.value = ?)");
        sbuf.append(" and value.valueID = typeValue.valueID");
        sbuf.append(" and typeValue.typeID = type.typeID");
        sbuf.append(" and type.name = 'Right'");
        return sbuf.toString();
    }

    private Connection getConnection() throws SQLException, NamingException {
        InitialContext context = new InitialContext();
        DataSource source = (DataSource) context.lookup("jdbc/jspShopDS");
        return source.getConnection();
    }

    public void setSessionContext(SessionContext ctx) {
        this.ctx = ctx;
    }

    public void ejbCreate() {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() {
    }
}
