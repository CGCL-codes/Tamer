package org.ru.mse10.cvis.web.bean;

import java.util.HashSet;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.ru.mse10.cvis.service.EntityService;

@SessionScoped
@ManagedBean(name = "baseFormBean")
public class BaseFormBean<E, S extends EntityService> extends BaseUIBean {

    private Set<Integer> permissions = new HashSet<Integer>();

    private E targetEntity;

    private boolean readonly = true;

    public void actionSave() {
        setTargetEntity(getService().save(getTargetEntity()));
        setReadonly(true);
    }

    public void actionEdit() {
        setReadonly(false);
    }

    public String actionCancelEdit() {
        setReadonly(true);
        return null;
    }

    protected S getService() {
        return null;
    }

    /**
	 * @return the readonly
	 */
    public boolean isReadonly() {
        return readonly;
    }

    /**
	 * @param readonly
	 *            the readonly to set
	 */
    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    /**
	 * @return the targetEntity
	 */
    public E getTargetEntity() {
        return targetEntity;
    }

    /**
	 * @param targetEntity
	 *            the targetEntity to set
	 */
    public void setTargetEntity(E targetEntity) {
        this.targetEntity = targetEntity;
    }

    @SuppressWarnings("unchecked")
    public <User> User getUser() {
        return (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
    }

    public <User> void setUser(User user) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", user);
        permissions.clear();
    }
}
