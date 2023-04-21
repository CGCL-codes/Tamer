package controllers.generic;

import java.util.ArrayList;
import controllers.CadastroMB;
import dao.generic.IDAO;
import messages.Message;
import models.generic.IModel;

/**
 * 
 * @author Flávio Gomes da Silva Lisboa
 * @version 0.1
 *
 */
public abstract class AbstractManagedBean extends AuthController implements IManagedBean {

    protected IModel bean = null;

    protected IDAO dao = null;

    protected String order = null;

    protected String where = null;

    protected String searchKey = null;

    public AbstractManagedBean(boolean checkUser) {
        super(checkUser);
    }

    public abstract ArrayList<? extends IModel> getLista();

    public String gravar() {
        if (this.bean.getId() == null) {
            this.dao.insert(this.bean);
        } else {
            this.dao.update(this.bean);
        }
        try {
            this.bean = bean.getClass().newInstance();
        } catch (InstantiationException e) {
            Message.setLastException(e.getMessage());
            this.bean = null;
        } catch (IllegalAccessException e) {
            Message.setLastException(e.getMessage());
            this.bean = null;
        }
        return new CadastroMB().getCadastro();
    }

    public String remover() {
        try {
            this.dao.delete(this.bean);
            this.bean = bean.getClass().newInstance();
        } catch (InstantiationException e) {
            Message.setLastException(e.getMessage());
        } catch (IllegalAccessException e) {
            Message.setLastException(e.getMessage());
        } catch (Exception e) {
            Message.setLastException(e.getMessage());
        }
        return new CadastroMB().getCadastro();
    }

    public IModel getBean() {
        return bean;
    }

    public void setBean(IModel bean) {
        this.bean = bean;
    }

    public IDAO getDao() {
        return dao;
    }

    public void setDao(IDAO dao) {
        this.dao = dao;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    protected void prepareWhere() {
        this.where = this.where == null ? null : this.where + " like '%" + this.searchKey + "%'";
    }

    protected void cleanWhereAndOrder() {
        this.where = null;
        this.order = null;
    }
}
