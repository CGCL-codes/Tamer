package br.com.sigep.dao;

import java.util.List;
import org.hibernate.Query;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.hibernate.Session;
import br.com.sigep.bean.Usuario;

public class UsuarioDaoHibernate extends HibernateDaoSupport implements UsuarioDaoInterface {

    public void atualizar(Usuario transientObject) throws DaoException {
        try {
            Session s = getHibernateTemplate().getSessionFactory().openSession();
            s.beginTransaction();
            s.update(transientObject);
            s.getTransaction().commit();
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw new DaoException("Erro ao Atualizar Usuario", "Usuario");
        }
    }

    public Usuario carregar(long id) throws DaoException {
        try {
            return (Usuario) getHibernateTemplate().get(Usuario.class, id);
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw new DaoException("Erro ao Carregar Usuario", "Usuario");
        }
    }

    public List<Usuario> listar() throws DaoException {
        try {
            Query consulta = getSession().createQuery("from Usuario");
            return consulta.list();
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw new DaoException("Erro ao Listar Usuario", "Usuario");
        }
    }

    public void remover(Usuario persistentObject) throws DaoException {
        try {
            Session s = getHibernateTemplate().getSessionFactory().openSession();
            s.beginTransaction();
            s.delete(persistentObject);
            s.getTransaction().commit();
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw new DaoException("Erro ao Remover Usuario", "Usuario");
        }
    }

    public void remover(long id) throws DaoException {
        try {
            Object record = this.carregar(id);
            this.remover((Usuario) record);
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw new DaoException("Erro ao Remover Usuario", "Usuario");
        }
    }

    public void salvar(Usuario newInstance) throws DaoException {
        try {
            Session s = getHibernateTemplate().getSessionFactory().openSession();
            s.beginTransaction();
            s.saveOrUpdate(newInstance);
            s.getTransaction().commit();
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw new DaoException("Erro ao Salvar Usuario", "Usuario");
        }
    }
}
