package com.googlecode.articulando.framework.persistencia;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import org.hibernate.*;
import org.hibernate.criterion.Criterion;
import com.googlecode.articulando.framework.integracao.Entidade;
import com.googlecode.articulando.framework.util.log.Logger;
import com.googlecode.articulando.framework.util.log.TipoLog;

public abstract class DAOArticulando<T extends Entidade> {

    private Class<T> classeDaEntidade;

    @SuppressWarnings("unchecked")
    protected Class<T> getClasseDaEntidade() {
        if (this.classeDaEntidade == null) {
            this.classeDaEntidade = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return this.classeDaEntidade;
    }

    protected DAOArticulando() {
    }

    public void salvar(T obj) {
        Session s = currentSession();
        Transaction tx = s.beginTransaction();
        try {
            s.saveOrUpdate(obj);
            tx.commit();
        } catch (HibernateException ex) {
            tx.rollback();
            Logger.getInstancia().log(TipoLog.ERRO, ex);
            throw ex;
        } finally {
            closeSession();
        }
    }

    public void deletar(T obj) {
        Session s = currentSession();
        Transaction tx = s.beginTransaction();
        try {
            s.delete(obj);
            tx.commit();
        } catch (HibernateException ex) {
            tx.rollback();
            Logger.getInstancia().log(TipoLog.ERRO, ex);
            throw ex;
        } finally {
            closeSession();
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> listar(Criterion... criterios) {
        List<T> listaResultados;
        try {
            Criteria c = currentSession().createCriteria(getClasseDaEntidade());
            for (Criterion criterion : criterios) {
                c.add(criterion);
            }
            listaResultados = c.list();
        } finally {
            closeSession();
        }
        return listaResultados;
    }

    @SuppressWarnings("unchecked")
    public T recupera(Serializable pk) {
        T obj = null;
        Session s = currentSession();
        try {
            obj = (T) s.get(getClasseDaEntidade(), pk);
        } finally {
            closeSession();
        }
        return obj;
    }

    public Session currentSession() {
        return HibernateUtil.currentSession();
    }

    public void closeSession() {
        HibernateUtil.closeSession();
    }
}
