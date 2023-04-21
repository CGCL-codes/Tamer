package model.db.impl;

import java.math.*;
import java.sql.*;
import java.net.URL;
import java.util.*;
import model.db.exception.*;
import model.db.vo.*;
import model.db.dao.*;

/**
* 
* Implementation of AirportRecordCountDAO interface 
* 
*/
public class AirportRecordCountDAOImpl implements AirportRecordCountDAO {

    public AirportRecordCountDAOImpl() {
    }

    public AirportRecordCount getRecordCount(Connection con) throws AirportRecordCountException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("select count(*) from airport");
            rs = stmt.executeQuery();
            rs.next();
            AirportRecordCount vo = new AirportRecordCount();
            vo.setCount(rs.getInt(1));
            return vo;
        } catch (SQLException sqle) {
            throw new AirportRecordCountException(sqle);
        } catch (Exception e) {
            throw new AirportRecordCountException(e);
        }
    }

    public AirportRecordCount getRecordCount(Connection con, String whereClause) throws AirportRecordCountException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("select count(*) from airport  " + whereClause);
            rs = stmt.executeQuery();
            rs.next();
            AirportRecordCount vo = new AirportRecordCount();
            vo.setCount(rs.getInt(1));
            return vo;
        } catch (SQLException sqle) {
            throw new AirportRecordCountException(sqle);
        } catch (Exception e) {
            throw new AirportRecordCountException(e);
        }
    }
}
