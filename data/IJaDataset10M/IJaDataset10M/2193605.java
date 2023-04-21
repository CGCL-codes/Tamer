package br.ufrn.cerescaico.sepe.dao;

import br.ufrn.cerescaico.sepe.Sepe;
import br.ufrn.cerescaico.sepe.beans.Perfil;
import br.ufrn.cerescaico.sepe.beans.Usuario;
import br.ufrn.cerescaico.sepe.dao.util.DataAccessLayerException;
import br.ufrn.cerescaico.sepe.dao.util.HibernateFactory;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

/**
 * @author Taciano Morais Silva
 * @version 23/01/2009, 12h11m
 * @since 15/10/2008, 12h00m
 */
public class UsuarioDao extends AbstractDao<Usuario> implements Dao<Usuario> {

    public UsuarioDao(Sepe sepe) throws DataAccessLayerException {
        super(sepe);
    }

    /**
     *
     * @throws br.cesed.lti.cesuweb.dao.util.DataAccessLayerException
     */
    public UsuarioDao() throws DataAccessLayerException {
        super();
    }

    /**
     *
     * @param username
     * @param password
     * @return
     * @throws br.cesed.lti.cesuweb.dao.util.DataAccessLayerException
     */
    @SuppressWarnings("unchecked")
    public Usuario autenticar(String username, String password) throws DataAccessLayerException {
        Usuario usuario = null;
        try {
            startOperation();
            Criteria criteria = session.createCriteria(Usuario.class);
            if (username != null && !username.equals("") && password != null && !password.equals("")) {
                criteria.add(Restrictions.eq("login", username));
                criteria.add(Restrictions.eq("senha", password));
            }
            List<Usuario> usuarios = criteria.list();
            if (usuarios.size() > 1) {
                throw new DataAccessLayerException("erro.usuario.dao.usuario.duplicado");
            } else if (!usuarios.isEmpty()) {
                usuario = usuarios.get(PRIMEIRO_REGISTRO);
            }
            tx.commit();
        } catch (HibernateException e) {
            handleException(e);
        } finally {
            HibernateFactory.close(session);
        }
        return usuario;
    }

    /**
    * Verifica se o login passado como parâmetro já existe.
    * @param username O login a verificar.
    * @return True se o login existir.
    * @throws br.cesed.lti.cesuweb.dao.util.DataAccessLayerException Caso
    * ocorra erro no acesso ao banco de dados
    */
    public Usuario verificarLoginExistente(Usuario usuario) throws DataAccessLayerException {
        Usuario usuarioBD = null;
        try {
            startOperation();
            Criteria criteria = session.createCriteria(Usuario.class);
            if (usuario != null && usuario.getLogin() != null && !usuario.getLogin().equals("")) {
                criteria.add(Restrictions.eq("login", usuario.getLogin()));
                usuarioBD = (Usuario) criteria.uniqueResult();
            }
            tx.commit();
        } catch (HibernateException e) {
            handleException(e);
        } finally {
            HibernateFactory.close(session);
        }
        return usuarioBD;
    }

    /**
     * Insert a new Event into the database.
     * @param event
     */
    @Override
    public void create(Usuario usuario) throws DataAccessLayerException {
        super.saveOrUpdate(usuario);
    }

    /**
     * Delete a detached Event from the database.
     * @param event
     */
    @Override
    public void delete(Usuario usuario) throws DataAccessLayerException {
        super.delete(usuario);
    }

    /**
     * Delete a detached Event from the database.
     * @param id
     */
    @Override
    public void delete(Integer id) throws DataAccessLayerException {
        super.delete(Usuario.class, id);
    }

    /**
     * Find an Event by its primary key.
     * @param id
     * @return
     */
    @Override
    public Usuario find(Integer id) throws DataAccessLayerException {
        return (Usuario) super.find(Usuario.class, id);
    }

    /**
     *
     * @param login
     * @param nome
     * @param perfil
     * @return
     * @throws br.cesed.lti.cesuweb.dao.util.DataAccessLayerException
     */
    @SuppressWarnings("unchecked")
    public List<Usuario> pesquisar(String login, String nome, Perfil perfil) throws DataAccessLayerException {
        List<Usuario> usuarios = null;
        try {
            startOperation();
            Criteria criteria = session.createCriteria(Usuario.class);
            if (login != null && !login.equals("")) {
                criteria.add(Restrictions.ilike("login", "%" + login + "%"));
            }
            if (nome != null && !nome.equals("")) {
                criteria.add(Restrictions.ilike("nome", "%" + nome + "%"));
            }
            if (perfil != null && perfil.getId() != 0) {
                criteria.add(Restrictions.eq("perfil", perfil));
            }
            usuarios = (List<Usuario>) criteria.list();
            tx.commit();
        } catch (HibernateException e) {
            handleException(e);
        } finally {
            HibernateFactory.close(session);
        }
        return usuarios;
    }

    /**
     * Updates the state of a detached Event.
     *
     * @param event
     */
    @Override
    public void update(Usuario usuario) throws DataAccessLayerException {
        super.saveOrUpdate(usuario);
    }

    /**
     * Finds all Events in the database.
     * @return
     */
    @Override
    public List<Usuario> findAll() throws DataAccessLayerException {
        return super.findAll(Usuario.class);
    }

    /**
     *
     * @param id
     * @throws br.cesed.lti.cesuweb.dao.util.DataAccessLayerException
     */
    public void deleteUser(Integer id) throws DataAccessLayerException {
        try {
            startOperation();
            Query q = session.createQuery("delete from Usuario where id = " + id);
            q.executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            handleException(e);
        } finally {
            HibernateFactory.close(session);
        }
    }
}
