package br.org.acessobrasil.facade;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SuppressWarnings("unchecked")
public class GenericFacade<A, E> {

    private static Logger logger = Logger.getLogger(GenericFacade.class);

    private Class<E> entidadePersistente;

    private static ResourceBundle MESSAGE_RESOURCES = ResourceBundle.getBundle("MessageResources");

    private EntityManager em;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        logger.debug("EntityManager atribuido");
        this.em = em;
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public GenericFacade() {
        try {
            Type[] types = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
            entidadePersistente = (Class<E>) types[1];
        } catch (Exception e) {
        }
    }

    /**
	 * Cuida da transacao
	 * @param ator
	 * @param obj
	 * @throws FacadeException
	 */
    public void criar(A ator, E obj) throws FacadeException {
        EntityManager em = getEntityManager();
        try {
            criar(ator, obj, em);
        } catch (FacadeException e) {
            throw e;
        } catch (Exception e) {
            throw new FacadeException(e);
        } finally {
        }
    }

    /**
	 * Cuida da permissao e nao cuida da transacao
	 * @param ator
	 * @param obj
	 * @param em
	 * @throws FacadeException
	 */
    public void criar(A ator, E obj, EntityManager em) throws FacadeException {
        em.persist(obj);
    }

    public E buscar(A ator, Long id) throws FacadeException {
        EntityManager em = getEntityManager();
        try {
            return buscar(ator, id, em);
        } finally {
        }
    }

    public E buscar(A ator, Long id, EntityManager em) throws FacadeException {
        return em.find(entidadePersistente, id);
    }

    /**
	 * Cuida da transacao,
	 * atualiza todos os tipos do pacote iniciado com "java.",
	 * atualiza todos os primitivos.
	 * Nao atualiza list e set.
	 * Nao atualiza propriedades nulas
	 * @param ator usuario
	 * @param obj entidade
	 * @return E obj entidade
	 * @throws FacadeException
	 */
    public E atualizar(A ator, E obj) throws FacadeException {
        EntityManager em = getEntityManager();
        E objB = obj;
        try {
            Class cls = obj.getClass();
            Field[] fields = cls.getDeclaredFields();
            Long id = null;
            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class)) {
                    boolean acessivel = field.isAccessible();
                    if (!acessivel) field.setAccessible(true);
                    id = (Long) field.get(obj);
                    if (!acessivel) field.setAccessible(acessivel);
                    break;
                }
            }
            objB = buscar(ator, id, em);
            BeanInfo info = Introspector.getBeanInfo(entidadePersistente);
            PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; ++i) {
                PropertyDescriptor pd = propertyDescriptors[i];
                if ((!pd.getPropertyType().getCanonicalName().equals("java.util.List") && !pd.getPropertyType().getCanonicalName().equals("java.util.Set") && !pd.getPropertyType().getCanonicalName().equals("java.lang.Class") && pd.getPropertyType().getCanonicalName().startsWith("java.")) || pd.getPropertyType().getCanonicalName().indexOf('.') == -1) {
                    try {
                        Object valor = pd.getReadMethod().invoke(obj);
                        if (valor != null) {
                            pd.getWriteMethod().invoke(objB, valor);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        } catch (FacadeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Erro: ", e);
            FacadeException fe = new FacadeException(e);
            throw fe;
        } finally {
        }
        return objB;
    }

    /**
	 * Cuida da transacao
	 * @param ator
	 * @param obj
	 * @throws FacadeException
	 */
    public void excluir(A ator, E obj) throws FacadeException {
        EntityManager em = getEntityManager();
        try {
            excluir(ator, obj, em);
        } catch (FacadeException e) {
            throw e;
        } catch (Exception e) {
            throw new FacadeException(e);
        } finally {
        }
    }

    public void excluir(A ator, E obj, EntityManager em) throws FacadeException {
        em.remove(obj);
    }

    /**
	 * Lista todos
	 * @param ator usuario
	 * @return List
	 * @throws FacadeException
	 */
    public List<E> buscar(A ator) throws FacadeException {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("select o from " + entidadePersistente.getCanonicalName() + " o").getResultList();
        } catch (Exception e) {
            throw new FacadeException(e);
        } finally {
        }
    }

    /**
	 * Pega as mensagens do MessageResources.properties
	 * @param key chave
	 * @return String valor
	 */
    public static String getString(String key) {
        try {
            return MESSAGE_RESOURCES.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
