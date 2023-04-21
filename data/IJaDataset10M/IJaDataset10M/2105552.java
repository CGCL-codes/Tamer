package example.spring.dao.impl;

import java.util.Iterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import com.ibatis.sqlmap.client.SqlMapClient;
import example.spring.dao.api.IHatDao;

public final class HatDao extends SqlMapClientDaoSupport implements IHatDao {

    @Autowired
    public HatDao(final SqlMapClient sqlMapClient) {
        this.setSqlMapClient(sqlMapClient);
    }

    public Iterator<String> getHats(final int hatId) {
        return null;
    }
}
