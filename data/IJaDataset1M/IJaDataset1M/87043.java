package com.apelon.dtswf.db.version.dao;

import com.apelon.common.log4j.Categories;
import com.apelon.common.sql.SQL;
import com.apelon.common.util.GidGenerator;
import com.apelon.common.util.db.DBSystemConfig;
import com.apelon.common.util.db.dao.SQL2KGeneralDAO;
import com.apelon.dtswf.util.DTSWFUtil;
import java.sql.*;
import java.util.StringTokenizer;
import java.io.*;

public class DTSWFSQL2KGeneralDAO extends SQL2KGeneralDAO implements DTSWFGeneralDAO {

    public DTSWFSQL2KGeneralDAO(DBSystemConfig daoConfig) {
        super(daoConfig);
    }

    public long updateVersionBinary(PreparedStatement preStat, int indextoSet, byte[] bytes, Statement st) throws SQLException {
        long versionId = -1;
        try {
            if (bytes == null) {
                preStat.setNull(indextoSet, Types.LONGVARBINARY);
            } else {
                preStat.setBytes(indextoSet, bytes);
            }
            preStat.executeUpdate();
            ResultSet rs = st.executeQuery("select @@identity");
            if (rs.next()) {
                versionId = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return versionId;
    }

    public String[] getItemFromVersion(String sql, Statement st) throws SQLException {
        String[] itemStrings = new String[0];
        ResultSet rs = null;
        InputStream is = null;
        try {
            rs = st.executeQuery(sql);
            while (rs.next()) {
                String type = rs.getString(1);
                is = rs.getBinaryStream(2);
                if (is == null) {
                    return itemStrings;
                }
                String itemString = DTSWFUtil.getStringFromGZIP(is);
                itemStrings = new String[] { type, itemString };
            }
        } catch (IOException e) {
            throw new SQLException("error in extrating item from version: " + e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new SQLException("could not close stream: " + e.getMessage());
                }
            }
        }
        return itemStrings;
    }
}
