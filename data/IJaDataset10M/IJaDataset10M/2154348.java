package br.uff.javaavancado.dao.generic;

import br.uff.javaavancado.dao.controle.JPAUtil;
import br.uff.javaavancado.exception.InfraestruturaException;
import br.uff.javaavancado.exception.ObjetoNaoEncontradoException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 * A implementa��o de um DAO gen�rico para a JPA
 * Uma implementa��o "typesafe" dos m�todos CRUD e dos m�todos de busca.
 */
public class JPADaoGenerico<T, PK> implements DaoGenerico<T, PK>, ExecutorDeBuscas<T> {

    private Class<T> tipo;

    public JPADaoGenerico(Class<T> tipo) {
        this.tipo = tipo;
    }

    @SuppressWarnings("unchecked")
    public T inclui(T o) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.persist(o);
        } catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        }
        return o;
    }

    public void altera(T o) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.merge(o);
        } catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        }
    }

    public void exclui(T o) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.remove(o);
        } catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        }
    }

    public T getPorId(PK id) throws ObjetoNaoEncontradoException {
        EntityManager em = JPAUtil.getEntityManager();
        T t = null;
        try {
            t = (T) em.find(tipo, id);
            if (t == null) {
                throw new ObjetoNaoEncontradoException();
            }
        } catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        }
        return t;
    }

    public T getPorIdComLock(PK id) throws ObjetoNaoEncontradoException {
        EntityManager em = JPAUtil.getEntityManager();
        T t = null;
        try {
            t = (T) em.find(tipo, id);
            if (t != null) {
                em.lock(t, LockModeType.READ);
                em.refresh(t);
            } else {
                throw new ObjetoNaoEncontradoException();
            }
        } catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        }
        return t;
    }

    @SuppressWarnings("unchecked")
    public T busca(Method metodo, Object[] argumentos, String nomeQuery) throws ObjetoNaoEncontradoException {
        System.out.println(">>>>>>>>>>>> busca chamado");
        EntityManager em = JPAUtil.getEntityManager();
        T t = null;
        try {
            String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
            if (nomeQuery != null && !nomeQuery.equals("")) {
                nomeDaBusca = tipo.getSimpleName() + "." + nomeQuery;
            }
            Query namedQuery = em.createNamedQuery(nomeDaBusca);
            if (argumentos != null) {
                for (int i = 0; i < argumentos.length; i++) {
                    Object arg = argumentos[i];
                    namedQuery.setParameter(i + 1, arg);
                }
            }
            t = (T) namedQuery.getSingleResult();
            return t;
        } catch (NoResultException e) {
            throw new ObjetoNaoEncontradoException();
        } catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public T buscaUltimoOuPrimeiro(Method metodo, Object[] argumentos, String nomeQuery) throws ObjetoNaoEncontradoException {
        System.out.println(">>>>>>>>>>>> buscaUltimoOuPrimeiro chamado");
        EntityManager em = JPAUtil.getEntityManager();
        T t = null;
        try {
            List lista;
            String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
            if (nomeQuery != null && !nomeQuery.equals("")) {
                nomeDaBusca = tipo.getSimpleName() + "." + nomeQuery;
            }
            Query namedQuery = em.createNamedQuery(nomeDaBusca);
            if (argumentos != null) {
                for (int i = 0; i < argumentos.length; i++) {
                    Object arg = argumentos[i];
                    namedQuery.setParameter(i + 1, arg);
                }
            }
            lista = namedQuery.getResultList();
            t = (lista.size() == 0) ? null : (T) lista.get(0);
            if (t == null) {
                throw new ObjetoNaoEncontradoException();
            }
            return t;
        } catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> buscaLista(Method metodo, Object[] argumentos, String nomeQuery) {
        System.out.println("\n>>>>>>>>>>>> buscaLista chamado\n");
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
            if (nomeQuery != null && !nomeQuery.equals("")) {
                nomeDaBusca = tipo.getSimpleName() + "." + nomeQuery;
            }
            Query namedQuery = em.createNamedQuery(nomeDaBusca);
            if (argumentos != null) {
                for (int i = 0; i < argumentos.length; i++) {
                    Object arg = argumentos[i];
                    namedQuery.setParameter(i + 1, arg);
                }
            }
            return (List<T>) namedQuery.getResultList();
        } catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public Set<T> buscaConjunto(Method metodo, Object[] argumentos, String nomeQuery) {
        System.out.println(">>>>>>>>>>>> buscaConjunto chamado");
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
            if (nomeQuery != null && !nomeQuery.equals("")) {
                nomeDaBusca = tipo.getSimpleName() + "." + nomeQuery;
            }
            Query namedQuery = em.createNamedQuery(nomeDaBusca);
            if (argumentos != null) {
                for (int i = 0; i < argumentos.length; i++) {
                    Object arg = argumentos[i];
                    namedQuery.setParameter(i + 1, arg);
                }
            }
            List<T> lista = namedQuery.getResultList();
            return new LinkedHashSet(lista);
        } catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        }
    }

    public String getNomeDaBuscaPeloMetodo(Method metodo) {
        return tipo.getSimpleName() + "." + metodo.getName();
    }

    public List<T> getListaCompleta() {
        EntityManager em = JPAUtil.getEntityManager();
        List<T> lista = null;
        String entidade = tipo.getSimpleName();
        Query namedQuery = em.createQuery("select t from " + entidade + " t");
        lista = namedQuery.getResultList();
        return lista;
    }
}
