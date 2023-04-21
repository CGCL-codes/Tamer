package br.usp.poli.dao;

import java.util.List;
import org.hibernate.Session;
import br.usp.poli.entity.Variable;

public class VariableDao {

    private GenericDao<Variable, Long> dao;

    public VariableDao(Session session) {
        super();
        this.dao = GenericDao.getInstance(session, Variable.class);
    }

    public void delete(Variable t) {
        dao.delete(t);
    }

    public List<Variable> list() {
        return dao.list();
    }

    public Variable load(Long id) {
        return dao.load(id);
    }

    public void merge(Variable t) {
        dao.merge(t);
    }

    public void save(Variable t) {
        dao.save(t);
    }
}
