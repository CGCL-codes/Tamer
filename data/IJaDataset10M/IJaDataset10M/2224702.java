package mable.web;

import java.io.Reader;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class SqlMapClientInstance {

    private static SqlMapClient sqlMap;

    public static SqlMapClient getSqlMapInstance() {
        try {
            String resource = "com/microflower/dao/ibatis/sql/sql-map-config.xml";
            Reader reader = Resources.getResourceAsReader(resource);
            sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing SqlMapClientMB class. Cause:" + e);
        }
        return sqlMap;
    }
}
