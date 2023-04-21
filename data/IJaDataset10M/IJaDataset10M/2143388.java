package fireteam.orb.server.processors;

import fireteam.orb.server.oraDB;
import fireteam.orb.server.FTDSessionImpl;
import fireteam.orb.server.stub.FTDObject;
import fireteam.orb.server.stub.StandardException;
import fireteam.orb.server.stub.FTDSessionFactory;
import fireteam.orb.server.stub.FTDSession;
import fireteam.orb.util.ObjUtil;
import org.omg.CORBA.ORB;
import javax.management.Attribute;
import javax.management.AttributeList;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleCallableStatement;

/**
 * Класс для работы
 * User: tolik1
 * Date: 01.02.2008
 * Time: 10:45:44
*/
public final class FTTreeItemImpl extends FTDObjectImpl {

    private final ResourceBundle m_Resources = ResourceBundle.getBundle(getClass().getName());

    public FTTreeItemImpl(oraDB db, FTDObject obj, ORB orb) throws StandardException {
        super(db, obj, orb);
        if (!obj.Type.equals("DIR")) throw (new StandardException("Unknown type of object"));
        getAttributes();
    }

    public FTTreeItemImpl(oraDB db, String ID, ORB orb) throws StandardException {
        m_oraDB = db;
        m_ORB = orb;
        m_object = FTDObjectImpl.getObjectByID(db, orb, ID);
        Connection con = null;
        try {
            con = m_oraDB.getConnection();
            setAttributes(m_object, con);
        } catch (SQLException e) {
            throw (ObjUtil.throwStandardException(e));
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                Logger.getLogger("fireteam").log(Level.SEVERE, null, e);
            }
        }
    }

    private void getAttributes() throws StandardException {
        if (m_object.typeTable == null) return;
        AttributeList attributes = new AttributeList();
        Connection con = null;
        try {
            con = m_oraDB.getConnection();
            PreparedStatement stmtPar = con.prepareStatement("SELECT * FROM " + m_object.typeTable + " WHERE ITREEID = :A");
            stmtPar.setString(1, m_object.ID);
            ResultSet rSet = stmtPar.executeQuery();
            while (rSet.next()) {
                attributes.add(new Attribute(rSet.getString("CPARNAME"), rSet.getString("CPARVAL")));
            }
            rSet.close();
            stmtPar.close();
            con.close();
            m_object.Attributes.insert_Value(attributes);
        } catch (Exception e) {
            throw (ObjUtil.throwStandardException(e));
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                Logger.getLogger("fireteam").log(Level.SEVERE, null, e);
            }
        }
    }

    private void setAttributes(FTDObject obj, Connection con) throws SQLException {
        if (obj.typeTable == null) return;
        AttributeList attributes = new AttributeList();
        PreparedStatement stmtPar = con.prepareStatement("SELECT * FROM " + obj.typeTable + " WHERE ITREEID = :A");
        stmtPar.setString(1, obj.ID);
        ResultSet rSet = stmtPar.executeQuery();
        while (rSet.next()) {
            attributes.add(new Attribute(rSet.getString("CPARNAME"), rSet.getString("CPARVAL")));
        }
        rSet.close();
        stmtPar.close();
        obj.Attributes.insert_Value(attributes);
    }

    public FTDObject[] getChildrenByType(String sTypeName) throws StandardException {
        FTDObject[] children = new FTDObject[0];
        Connection con = null;
        try {
            con = m_oraDB.getConnection();
            String sSql = m_Resources.getString("LIST_BY_TYPE");
            PreparedStatement ps = con.prepareStatement(sSql);
            ps.setString(1, sTypeName);
            ps.setString(2, m_object.ID);
            ResultSet rSet = ps.executeQuery();
            children = getChildren(rSet, con);
            rSet.close();
            ps.close();
            for (FTDObject obj : children) setAttributes(obj, con);
            con.close();
            con = null;
        } catch (Exception e) {
            throw (ObjUtil.throwStandardException(e));
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                Logger.getLogger("fireteam").log(Level.SEVERE, null, e);
            }
        }
        return children;
    }

    @Override
    public FTDObject[] getChildren() throws StandardException {
        FTDObject[] children = new FTDObject[0];
        Connection con = null;
        try {
            con = m_oraDB.getConnection();
            String sSql = m_Resources.getString("LIST");
            PreparedStatement ps = con.prepareStatement(sSql);
            ps.setString(1, m_object.ID);
            ResultSet rSet = ps.executeQuery();
            children = getChildren(rSet, con);
            rSet.close();
            ps.close();
            for (FTDObject obj : children) setAttributes(obj, con);
            con.close();
            con = null;
        } catch (Exception e) {
            throw (ObjUtil.throwStandardException(e));
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                Logger.getLogger("fireteam").log(Level.SEVERE, null, e);
            }
        }
        return children;
    }

    @Override
    public FTDObject add(FTDObject obj) throws StandardException {
        obj = super.add(obj);
        Connection con = null;
        try {
            String sSql = m_Resources.getString("PARADD");
            con = m_oraDB.getConnection();
            OracleCallableStatement ps = (OracleCallableStatement) con.prepareCall(sSql);
            ArrayList<fireteam.orb.server.processors.types.Attribute> arAttr = new ArrayList<fireteam.orb.server.processors.types.Attribute>();
            AttributeList attrs = (AttributeList) obj.Attributes.extract_Value();
            for (Object o : attrs) {
                Attribute atr = (Attribute) o;
                arAttr.add(new fireteam.orb.server.processors.types.Attribute(atr.getName(), atr.getValue().toString()));
            }
            ps.setString(1, obj.ID);
            ps.setORAData(2, new fireteam.orb.server.processors.types.AttributeList(arAttr.toArray(new fireteam.orb.server.processors.types.Attribute[arAttr.size()])));
            ps.executeUpdate();
            con.commit();
        } catch (Exception e) {
            throw (ObjUtil.throwStandardException(e));
        } finally {
            if (con != null) try {
                con.close();
            } catch (SQLException e) {
                throw (ObjUtil.throwStandardException(e));
            }
        }
        return obj;
    }

    @Override
    public void change(FTDObject obj) throws StandardException {
        Connection con = null;
        try {
            String sSql = m_Resources.getString("PARADD");
            con = m_oraDB.getConnection();
            OracleCallableStatement ps = (OracleCallableStatement) con.prepareCall(sSql);
            ArrayList<fireteam.orb.server.processors.types.Attribute> arAttr = new ArrayList<fireteam.orb.server.processors.types.Attribute>();
            AttributeList attrs = (AttributeList) obj.Attributes.extract_Value();
            for (Object o : attrs) {
                Attribute atr = (Attribute) o;
                arAttr.add(new fireteam.orb.server.processors.types.Attribute(atr.getName(), atr.getValue().toString()));
            }
            ps.setString(1, obj.ID);
            ps.setORAData(2, new fireteam.orb.server.processors.types.AttributeList(arAttr.toArray(new fireteam.orb.server.processors.types.Attribute[arAttr.size()])));
            ps.executeUpdate();
            con.commit();
        } catch (Exception e) {
            throw (ObjUtil.throwStandardException(e));
        } finally {
            if (con != null) try {
                con.close();
            } catch (SQLException e) {
                throw (ObjUtil.throwStandardException(e));
            }
        }
    }

    @Override
    public void delete() throws StandardException {
        super.delete();
    }

    @Override
    public FTDObject[] getContent(String sType) throws StandardException {
        FTDObject[] children = new FTDObject[0];
        Connection con = null;
        try {
            con = m_oraDB.getConnection();
            String sSql = m_Resources.getString("LIST.CONTENT");
            PreparedStatement ps = con.prepareStatement(sSql);
            ps.setString(1, sType);
            ps.setString(2, m_object.ID);
            ResultSet rSet = ps.executeQuery();
            children = getChildren(rSet, con);
            rSet.close();
            ps.close();
            con.close();
            con = null;
        } catch (Exception e) {
            throw (ObjUtil.throwStandardException(e));
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                Logger.getLogger("fireteam").log(Level.SEVERE, null, e);
            }
        }
        return children;
    }
}
