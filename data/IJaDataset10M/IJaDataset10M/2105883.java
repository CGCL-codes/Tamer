package net.pleso.demo.server;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class SqlMapManager {

    public static SqlMapClient getSqlMapClient(Properties authProps) throws SqlMapperException {
        try {
            Reader reader = Resources.getResourceAsReader("net/pleso/demo/server/SqlMapConfig.xml");
            SqlMapClient sqlMapper = SqlMapClientBuilder.buildSqlMapClient(reader, authProps);
            reader.close();
            return sqlMapper;
        } catch (IOException e) {
            e.printStackTrace();
            throw new SqlMapperException(e.getMessage(), e);
        }
    }
}
