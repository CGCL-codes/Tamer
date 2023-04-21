package com.shine.sourceflow.dao.show.trend;

import java.util.HashMap;
import java.util.Map;
import com.shine.DBUtil.model.DBModel;
import com.shine.sourceflow.dao.show.trend.strategy.SessionTrafficTrendStdQuery;

public class SessionTrafficTrendDao extends TrendGenericDao {

    public SessionTrafficTrendDao() {
        this.queryStrategy = new SessionTrafficTrendStdQuery();
    }

    @Override
    protected Map<String, DBModel> handleDbModels(Map<String, DBModel> dbSrcModels, Map<String, DBModel> dbDstModels) {
        Map<String, DBModel> dbModels = new HashMap<String, DBModel>();
        dbModels = dbSrcModels;
        for (Map.Entry<String, DBModel> entry : dbSrcModels.entrySet()) {
            dbModels.get(entry.getKey()).get(0).put("src_ip_total_bytes", dbSrcModels.get(entry.getKey()).get(0).get("total_bytes"));
            dbModels.get(entry.getKey()).get(0).put("dst_ip_total_bytes", dbDstModels.get(entry.getKey()).get(0).get("total_bytes"));
        }
        return dbModels;
    }
}
