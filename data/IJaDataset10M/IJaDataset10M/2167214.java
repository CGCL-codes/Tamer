package br.com.gerpro.dao.impl;

import java.util.List;
import javax.persistence.PersistenceException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.com.gerpro.dao.FacadeUsuario;
import br.com.gerpro.model.Usuario;
import br.com.gerpro.util.HibernateUtil;

/**
 * @author M3R
 * 
 */
public class UsuarioDao implements FacadeUsuario {

    private static Session session = null;

    private static Transaction tx = null;

    public void salvar(Usuario usuario) {
        try {
            session = HibernateUtil.getSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(usuario);
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            e.printStackTrace();
        } catch (PersistenceException e) {
            tx.rollback();
            e.printStackTrace();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Usuario> listar() {
        List<Usuario> result = null;
        session = HibernateUtil.getSession();
        tx = session.beginTransaction();
        Query q = session.createQuery(" from Usuario ");
        result = q.list();
        session.close();
        return result;
    }

    public List<Usuario> listarPorNome(String nome) {
        return null;
    }

    public List<Usuario> listarPorEquipe(int id) {
        List<Usuario> result = null;
        session = HibernateUtil.getSession();
        tx = session.beginTransaction();
        result = session.createQuery("from Usuario as user" + " where user.equipe.id =" + id).list();
        session.close();
        return result;
    }

    public List<Usuario> listarProfessores() {
        List<Usuario> result = null;
        session = HibernateUtil.getSession();
        tx = session.beginTransaction();
        result = session.createQuery("from Usuario as user" + " where user.tipoUsuario.id = 2 order by user.nome asc").list();
        session.close();
        return result;
    }

    public List<Usuario> listarAlunos() {
        List<Usuario> result = null;
        session = HibernateUtil.getSession();
        tx = session.beginTransaction();
        result = session.createQuery("from Usuario as user" + " where user.tipoUsuario.id = 1 order by user.nome asc").list();
        session.close();
        return result;
    }

    public Usuario procurarPorMatricula(String matricula) {
        session = HibernateUtil.getSession();
        tx = session.beginTransaction();
        Usuario usuario = (Usuario) session.get(Usuario.class, matricula);
        session.close();
        return usuario;
    }

    public Usuario procurarPorNome(String nome) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Usuario> listarProfessoresParaCorrecao() {
        List<Usuario> result = null;
        session = HibernateUtil.getSession();
        tx = session.beginTransaction();
        result = session.createQuery("from Usuario as user" + " where user.tipoUsuario.id = 2" + " or user.tipoUsuario.id = 3" + " order by user.nome asc").list();
        session.close();
        return result;
    }
}
