package ebadat.dao.sqlmap;

import ebadat.dao.PerifericosDAO;
import ebadat.domain.Centro;
import ebadat.domain.Periferico;
import java.util.List;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 *
 * @author DSILVA
 */
public class SqlMapPerifericosDAO extends SqlMapClientDaoSupport implements PerifericosDAO {

    public List<Periferico> getAll(long intCodCentro) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int delete(Periferico periferico) {
        periferico.setEstadoRegistro("E");
        return update(periferico);
    }

    public int insert(Periferico periferico) {
        Integer ret = (Integer) getSqlMapClientTemplate().insert("insert-periferico", periferico);
        return ret;
    }

    public int update(Periferico periferico) {
        return getSqlMapClientTemplate().update("update-periferico", periferico);
    }

    public Periferico getByCodigo(long codcentro, int codperiferico) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Periferico> getAll() {
        return getSqlMapClientTemplate().queryForList("select-perifericos-por-ceba", Centro.getIdActual());
    }
}
