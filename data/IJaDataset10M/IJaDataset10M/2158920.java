package br.ufrgs.inf.biblioteca.business.implementation;

import java.io.Serializable;
import java.util.Collection;
import br.gov.framework.demoiselle.core.bean.IPojo;
import br.gov.framework.demoiselle.core.context.ContextLocator;
import br.ufrgs.inf.biblioteca.business.IGenericBC;
import br.ufrgs.inf.biblioteca.message.InfoMessage;
import br.ufrgs.inf.biblioteca.persistence.dao.IGenericDAO;

public abstract class GenericBC<A extends IPojo> implements IGenericBC<A> {

    public abstract IGenericDAO<A> getDAO();

    public A findById(Serializable id) {
        return getDAO().findById(id);
    }

    public Object insert(A pojo) {
        Object id = getDAO().insert(pojo);
        ContextLocator.getInstance().getMessageContext().addMessage(InfoMessage.INFO_001_INCLUIR_OK);
        return id;
    }

    public void update(A pojo) {
        getDAO().update(pojo);
        ContextLocator.getInstance().getMessageContext().addMessage(InfoMessage.INFO_002_ALTERAR_OK);
    }

    public void remove(A pojo) {
        getDAO().remove(pojo);
        ContextLocator.getInstance().getMessageContext().addMessage(InfoMessage.INFO_003_EXCLUIR_OK);
    }

    public Collection<A> list() {
        return getDAO().list();
    }

    public void saveOrUpdate(A pojo) {
        getDAO().saveOrUpdate(pojo);
        ContextLocator.getInstance().getMessageContext().addMessage(InfoMessage.INFO_004_SALVAR_OK);
    }
}
