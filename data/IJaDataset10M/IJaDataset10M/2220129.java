package net.sunji.ibatis.plus;

import java.sql.SQLException;
import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

/**
 * @author seyoung
 * 
 */
public class OracleRawHandler implements TypeHandlerCallback {

    public OracleRawHandler() {
        super();
    }

    public void setParameter(ParameterSetter setter, Object param) throws SQLException {
        try {
            setter.setBytes((byte[]) param);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to set Blob parameter.  Cause: " + e, e);
        }
    }

    public Object getResult(ResultGetter getter) throws SQLException {
        byte[] bf;
        try {
            bf = getter.getBytes();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to set Blob result property. Cause: " + e, e);
        }
        return bf;
    }

    public Object valueOf(String arg0) {
        return arg0;
    }
}
