package ebadat.dao.sqlmap;

import ebadat.dao.PeriodosDAO;
import ebadat.domain.Centro;
import ebadat.domain.Periodo;
import java.util.List;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 *
 * @author DSILVA
 */
public class SqlMapPeriodosDAO extends SqlMapClientDaoSupport implements PeriodosDAO {

    public void insert(Periodo periodo) {
        periodo.setCodPeriodo((Integer) getSqlMapClientTemplate().insert("insert-periodo", periodo));
    }

    public void update(Periodo periodo) {
        getSqlMapClientTemplate().update("update-periodo", periodo);
    }

    public List<Periodo> getAll() {
        return getSqlMapClientTemplate().queryForList("select-periodos", Centro.getIdActual());
    }
}
