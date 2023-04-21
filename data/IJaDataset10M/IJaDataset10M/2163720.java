package br.com.bafonline.model.dao;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import br.com.bafonline.model.dao.implementation.GenericDAOImpl;
import br.com.bafonline.model.dto.UsuarioDTO;
import br.com.bafonline.util.exception.hibernate.HibernateSessionFactoryException;

/**
 * Classe DAO (Data Access Object) que tem por finalidade persistir e pesquisar dados 
 * da entidade Usu�rio. <br>
 * Herda todas as funcionalidades de GenericDAO.
 * @see br.com.bafonline.model.dto.UsuarioDTO
 * @see br.com.bafonline.model.dao.implementation.GenericDAOImpl
 * @author bafonline
 *
 */
public class UsuarioDAO extends GenericDAOImpl {

    private static final Log log = LogFactory.getLog(UsuarioDAO.class);

    public static final String NOME = "nome";

    public static final String ENDERECO = "endereco";

    public static final String CEP = "cep";

    public static final String TELEFONE = "telefone";

    public static final String EMAIL = "email";

    public static final String APELIDO = "apelido";

    public static final String SENHA = "senha";

    public static final String STATUS = "status";

    public static final String PERFIL = "perfil";

    public static final String QUALIFICACAO = "qualificacao";

    public static final String PONTUACAO = "pontuacao";

    public List<?> findByProperty(String propertyName, Object value) {
        log.debug("finding Usuario instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from UsuarioDTO as model where model." + propertyName + "= ?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, value);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        } catch (HibernateSessionFactoryException e) {
            log.error("find by property name failed", e);
        }
        return null;
    }

    public List<?> findByNome(Object nome) {
        return findByProperty(NOME, nome);
    }

    public List<?> findByEndereco(Object endereco) {
        return findByProperty(ENDERECO, endereco);
    }

    public List<?> findByCep(Object cep) {
        return findByProperty(CEP, cep);
    }

    public List<?> findByTelefone(Object telefone) {
        return findByProperty(TELEFONE, telefone);
    }

    public List<?> findByEmail(Object email) {
        return findByProperty(EMAIL, email);
    }

    public List<?> findByApelido(Object apelido) {
        return findByProperty(APELIDO, apelido);
    }

    public List<?> findBySenha(Object senha) {
        return findByProperty(SENHA, senha);
    }

    public List<?> findByStatus(Object status) {
        return findByProperty(STATUS, status);
    }

    public List<?> findByPerfil(Object perfil) {
        return findByProperty(PERFIL, perfil);
    }

    public List<?> findByQualificacao(Object qualificacao) {
        return findByProperty(QUALIFICACAO, qualificacao);
    }

    public List<?> findByPontuacao(Object pontuacao) {
        return findByProperty(PONTUACAO, pontuacao);
    }

    public UsuarioDTO merge(UsuarioDTO detachedInstance) {
        log.debug("merging Usuario instance");
        try {
            UsuarioDTO result = (UsuarioDTO) getSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        } catch (HibernateSessionFactoryException e) {
            log.error("merge failed", e);
        }
        return null;
    }

    public void attachDirty(UsuarioDTO instance) {
        log.debug("attaching dirty Usuario instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        } catch (HibernateSessionFactoryException e) {
            log.error("attach failed", e);
        }
    }

    public void attachClean(UsuarioDTO instance) {
        log.debug("attaching clean Usuario instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        } catch (HibernateSessionFactoryException e) {
            log.error("attach failed", e);
        }
    }
}
