package com.gever.sysman.level.dao.impl;

import java.sql.*;
import java.util.*;
import com.gever.sysman.db.SequenceMan;
import com.gever.sysman.level.dao.LevelDAO;
import com.gever.sysman.level.model.Level;
import com.gever.config.Constants;
import com.gever.exception.db.DAOException;
import com.gever.sysman.level.model.impl.DefaultLevel;
import com.gever.sysman.organization.dao.OrganizationFactory;
import com.gever.sysman.util.OrderList;
import com.gever.jdbc.connection.ConnectionProvider;
import com.gever.jdbc.connection.ConnectionProviderFactory;
import com.gever.jdbc.database.dialect.Global;
import org.apache.struts.util.LabelValueBean;

public class DefaultLevelDAO implements LevelDAO {

    private static String ADD_LEVEL = "INSERT INTO T_LEVEL (ID,Code,Name,Description) VALUES(?,?,?,?) ";

    private static String UPDATE_LEVEL = "UPDATE T_LEVEL SET CODE=?,NAME=?,Description=? WHERE ID=?";

    private static String LIST_LEVEL = "SELECT ID,CODE,NAME,DESCRIPTION FROM T_LEVEL order by ID";

    private static String DEL_LEVEL = "DELETE FROM T_LEVEL WHERE 1=2";

    private static String GET_COUNT = "SELECT COUNT(ID) FROM T_LEVEL ";

    private static String GET_LEVEL = "SELECT ID,CODE,Name,Description FROM T_LEVEL WHERE ID=?";

    public void createLevel(Level level) throws DAOException {
        ConnectionProvider cp = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            cp = ConnectionProviderFactory.getConnectionProvider(Constants.DATA_SOURCE);
            conn = cp.getConnection();
            pstmt = conn.prepareStatement(ADD_LEVEL);
            pstmt.setString(1, String.valueOf(SequenceMan.nextID(0)));
            pstmt.setString(2, level.getCode());
            pstmt.setString(3, level.getName());
            pstmt.setString(4, level.getDescription());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new DAOException("PRV_ROLE_001", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public Collection getLevel() throws DAOException {
        ConnectionProvider cp = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Collection result = new ArrayList();
        try {
            cp = ConnectionProviderFactory.getConnectionProvider(Constants.DATA_SOURCE);
            conn = cp.getConnection();
            pstmt = conn.prepareStatement(LIST_LEVEL);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Level level = new DefaultLevel();
                level.setId(rs.getInt("ID"));
                level.setCode(rs.getString("code"));
                level.setName(rs.getString("Name"));
                level.setDescription(rs.getString("Description"));
                result.add(level);
            }
            return result;
        } catch (Exception e) {
            throw new DAOException("PRV_ROLE_016", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public void deleteLevelByIds(String[] ids) throws DAOException {
        ConnectionProvider cp = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        StringBuffer delsql = new StringBuffer(DEL_LEVEL);
        if (ids != null) {
            for (int i = 0; i < ids.length; i++) {
                delsql.append(" or ID=" + ids[i]);
            }
        }
        try {
            cp = ConnectionProviderFactory.getConnectionProvider(Constants.DATA_SOURCE);
            conn = cp.getConnection();
            pstmt = conn.prepareStatement(delsql.toString());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new DAOException("PRV_ROLE_012", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
        }
        try {
            OrganizationFactory.getInstance().getUserDAO().updateUserLevel(ids);
        } catch (Exception e) {
            throw new DAOException("PRV_ROLE_0167", e);
        } finally {
            try {
                conn.close();
                pstmt.close();
            } catch (Exception e) {
            }
        }
    }

    public void updateLevel(Level level) throws DAOException {
        ConnectionProvider cp = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            cp = ConnectionProviderFactory.getConnectionProvider(Constants.DATA_SOURCE);
            conn = cp.getConnection();
            pstmt = conn.prepareStatement(UPDATE_LEVEL);
            pstmt.setString(1, level.getCode());
            pstmt.setString(2, level.getName());
            pstmt.setString(3, level.getDescription());
            pstmt.setString(4, String.valueOf(level.getId()));
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new DAOException("PRV_ROLE_015", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private String[] orderby;

    public void setOrderby(String[] s) {
        this.orderby = s;
    }

    public Collection getLevels(long start, long count) throws DAOException {
        ConnectionProvider cp = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Collection result = new ArrayList();
        try {
            cp = ConnectionProviderFactory.getConnectionProvider(Constants.DATA_SOURCE);
            conn = cp.getConnection();
            String sql = Global.getDialect().getLimitString(LIST_LEVEL);
            sql = OrderList.getInstance().formatSQL(orderby, sql);
            pstmt = conn.prepareStatement(sql);
            pstmt = Global.getDialect().setStatementPageValue(pstmt, 1, (int) start, 2, (int) (start + count));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Level level = new DefaultLevel();
                level.setId(rs.getInt("ID"));
                level.setCode(rs.getString("CODE"));
                level.setName(rs.getString("Name"));
                level.setDescription(rs.getString("Description"));
                result.add(level);
            }
            return result;
        } catch (Exception e) {
            throw new DAOException("PRV_ROLE_018", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public int getLevelCount() throws DAOException {
        ConnectionProvider cp = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Collection result = new ArrayList();
        int rolecount = 0;
        try {
            cp = ConnectionProviderFactory.getConnectionProvider(Constants.DATA_SOURCE);
            conn = cp.getConnection();
            pstmt = conn.prepareStatement(GET_COUNT);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                rolecount = rs.getInt(1);
            }
            return rolecount;
        } catch (Exception e) {
            throw new DAOException("PRV_ROLE_017", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public Collection findLevelByName() throws DAOException {
        Collection opgroups = new ArrayList();
        for (Iterator i = getLevel().iterator(); i.hasNext(); ) {
            Level level = (Level) i.next();
            opgroups.add(new LabelValueBean(level.getName(), String.valueOf(level.getId())));
        }
        return opgroups;
    }

    public Level findLevelByID(long id) throws DAOException {
        ConnectionProvider cp = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Level level = new DefaultLevel();
        try {
            cp = ConnectionProviderFactory.getConnectionProvider(Constants.DATA_SOURCE);
            conn = cp.getConnection();
            pstmt = conn.prepareStatement(GET_LEVEL);
            pstmt.setString(1, String.valueOf(id));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                level.setId(rs.getInt("ID"));
                level.setCode(rs.getString("code"));
                level.setName(rs.getString("Name"));
                level.setDescription(rs.getString("Description"));
            }
            return level;
        } catch (Exception e) {
            throw new DAOException("PRV_ROLE_009", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
        }
    }
}
