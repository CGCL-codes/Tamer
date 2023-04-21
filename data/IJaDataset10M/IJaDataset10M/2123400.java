package pe.com.bn.sach.dao.impl;

import java.util.List;
import com.ibatis.sqlmap.client.SqlMapClient;
import pe.com.bn.sach.dao.SituacionLaboralDAO;

public class SituacionLaboralImpl implements SituacionLaboralDAO {

    protected SqlMapClient sqlMap = null;

    public void setSqlMap(SqlMapClient sqlMap) {
        this.sqlMap = sqlMap;
    }

    public List listSituacionLaboral() throws Exception {
        return sqlMap.queryForList("sqlSituacionLaboral.listaSituacionLaboral", "");
    }
}
