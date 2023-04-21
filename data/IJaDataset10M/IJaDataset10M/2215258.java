package br.com.sigep.dao;

import java.util.List;
import org.hibernate.Query;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.hibernate.Session;
import br.com.sigep.bean.ResultadoFinal;

public class ResultadoFinalDaoHibernate extends HibernateDaoSupport implements ResultadoFinalDaoInterface {

    public void atualizar(ResultadoFinal transientObject) throws DaoException {
        try {
            Session s = getHibernateTemplate().getSessionFactory().openSession();
            s.beginTransaction();
            s.update(transientObject);
            s.getTransaction().commit();
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw new DaoException("Erro ao Atualizar ResultadoFinal", "ResultadoFinal");
        }
    }

    public ResultadoFinal carregar(long id) throws DaoException {
        try {
            return (ResultadoFinal) getHibernateTemplate().get(ResultadoFinal.class, id);
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw new DaoException("Erro ao Carregar ResultadoFinal", "ResultadoFinal");
        }
    }

    public List<ResultadoFinal> listar() throws DaoException {
        try {
            Query consulta = getSession().createQuery("from ResultadoFinal");
            return consulta.list();
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw new DaoException("Erro ao Listar ResultadoFinal", "ResultadoFinal");
        }
    }

    public List<ResultadoFinal> listarNotasPorPeriodo(long idAluno, long idTurma) throws DaoException {
        try {
            Query consulta = getSession().createQuery("from ResultadoFinal r where r.aluno.id = ? and r.registroDeDisciplina.turma.id = ?");
            consulta.setLong(0, idAluno);
            consulta.setLong(1, idTurma);
            return consulta.list();
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw new DaoException("Erro ao Listar ResultadoFinal", "ResultadoFinal");
        }
    }

    public void remover(ResultadoFinal persistentObject) throws DaoException {
        try {
            Session s = getHibernateTemplate().getSessionFactory().openSession();
            s.beginTransaction();
            s.delete(persistentObject);
            s.getTransaction().commit();
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw new DaoException("Erro ao Remover ResultadoFinal", "ResultadoFinal");
        }
    }

    public void remover(long id) throws DaoException {
        try {
            Object record = this.carregar(id);
            this.remover((ResultadoFinal) record);
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw new DaoException("Erro ao Remover ResultadoFinal", "ResultadoFinal");
        }
    }

    public void salvar(ResultadoFinal newInstance) throws DaoException {
        try {
            Session s = getHibernateTemplate().getSessionFactory().openSession();
            s.beginTransaction();
            s.saveOrUpdate(newInstance);
            s.getTransaction().commit();
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw new DaoException("Erro ao Salvar ResultadoFinal", "ResultadoFinal");
        }
    }
}
