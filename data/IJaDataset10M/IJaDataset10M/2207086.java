package net.sf.fallfair.BusinessLogic.CRUD;

import net.sf.fallfair.BusinessLogic.Province;
import com.ibatis.sqlmap.client.SqlMapClient;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author nathanj
 */
public class ProvinceCRUD {

    public static List<Province> find(String name) throws SQLException {
        SqlMapClient sqlMap = SQLMap.getSQLMapInstance();
        return sqlMap.queryForList(FIND);
    }

    public static final String FIND = "Province.find";
}
