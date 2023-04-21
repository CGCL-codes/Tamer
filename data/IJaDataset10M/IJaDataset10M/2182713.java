package com.google.code.wifimaps.persistencia.hibernate;

import java.io.Serializable;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * Classe que � a ponte com o Hibernate
 * @author Marco T�lio
 * @author Tesso
 * 
 */
public class HibernatePersistencia {

    /** Logger */
    private static final org.apache.log4j.Logger logger = Logger.getLogger(HibernatePersistencia.class);

    /**
		 * Abrir uma sessao do Hibernate
		 * @return Nova sessao do Hibernate
		 * @throws NullPointerException Nao existe fabrica
		 * @throws HibernateException Falha no Hibernate
		 * @throws RuntimeException Falha ao tentar abriri sessao.
		 */
    public org.hibernate.Session abrirSessao() {
        return HibernateUtil.getSessionFactory().openSession();
    }

    /** Fechar a sessao */
    public void fecharSessao(Session sessao) {
        if (sessao != null) {
            try {
                sessao.close();
            } catch (HibernateException he) {
                logger.error("Falha fechar sessao", he);
            }
        }
    }

    /**
		 * Salva o objeto informado.
		 * @param o Objeto a ser salvo.
		 */
    public void salvar(Object o) {
        if (o == null) {
            throw new NullPointerException("Objeto nao informado.");
        }
        Session sessao = null;
        try {
            sessao = abrirSessao();
            sessao.beginTransaction();
            sessao.save(o);
            sessao.getTransaction().commit();
        } catch (RuntimeException e) {
            try {
                sessao.getTransaction().rollback();
            } catch (Exception e1) {
                logger.error("Falhar ao salvar " + o, e);
                throw e;
            } finally {
                fecharSessao(sessao);
            }
        }
    }

    /**
		 * Atualizar o objeto informado.
		 * @param o Objeto a ser atualizado.
		 */
    public void atualizar(Object o) {
        if (o == null) {
            throw new NullPointerException("Objeto nao informado.");
        }
        Session sessao = null;
        try {
            sessao = abrirSessao();
            sessao.beginTransaction();
            sessao.update(o);
            sessao.getTransaction().commit();
        } catch (RuntimeException e) {
            try {
                sessao.getTransaction().rollback();
            } catch (Exception e1) {
                logger.error("Falhar ao atualizar " + o, e);
                throw e;
            } finally {
                fecharSessao(sessao);
            }
        }
    }

    /**
		 * Remover o objeto informado.
		 * @param o Objeto a ser removido.
		 */
    public void remover(Object o) {
        if (o == null) {
            throw new NullPointerException("Objeto nao informado.");
        }
        Session sessao = null;
        try {
            sessao = abrirSessao();
            sessao.beginTransaction();
            sessao.delete(o);
            sessao.getTransaction().commit();
        } catch (RuntimeException e) {
            try {
                sessao.getTransaction().rollback();
            } catch (Exception e1) {
                logger.error("Falhar ao remover " + o, e);
                throw e;
            } finally {
                fecharSessao(sessao);
            }
        }
    }

    /**
		 * Salva ou Atualiza o objeto informado.
		 * @param o Objeto a ser Salvo ou Atualizado.
		 */
    public void salvarOuAtualizar(Object o) {
        if (o == null) {
            throw new NullPointerException("Objeto nao informado.");
        }
        Session sessao = null;
        try {
            sessao = abrirSessao();
            sessao.beginTransaction();
            sessao.saveOrUpdate(o);
            sessao.getTransaction().commit();
        } catch (RuntimeException e) {
            try {
                sessao.getTransaction().rollback();
            } catch (Exception e1) {
                logger.error("Falhar ao salvar/atualizar " + o, e);
                throw e;
            } finally {
                fecharSessao(sessao);
            }
        }
    }

    /**
		 * Carrega o objeto baseado na classe informada e na chave primaria.
		 * @param c Classe do objeto
		 * @param chave Chave Primaria
		 * @return Objeto da classe <code>c</code> que possui a
		 * chave primaria <code>chave</code>
		 */
    @SuppressWarnings("unchecked")
    public <E> E carregar(Class<E> c, Serializable chave) {
        try {
            Session sessao = abrirSessao();
            return (E) sessao.load(c, chave);
        } catch (RuntimeException e) {
            logger.error("Falha ao tentar carregar objeto de " + c + " com chave " + chave, e);
            throw e;
        }
    }

    /**
		 * Pesquisa utilizando a linguagem HQL
		 * @param hql Consulta HQL
		 * @param params Parametros da consulta
		 * @return Uma lista conforme a consulta informada
		 */
    public java.util.List<?> pesquisar(String hql, Object... params) {
        if (hql == null) {
            throw new NullPointerException("Sem pewquisa HQL");
        }
        try {
            Session sessao = abrirSessao();
            org.hibernate.Query pesquisa = sessao.createQuery(hql);
            if (params != null && params.length > 0) {
                int i = 0;
                for (Object p : params) {
                    pesquisa.setParameter(i++, p);
                }
            }
            return pesquisa.list();
        } catch (RuntimeException re) {
            logger.error("Falha na pesquisa '" + hql + "'", re);
            throw re;
        }
    }
}
