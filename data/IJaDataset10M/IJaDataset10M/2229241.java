package org.domain.analyticalcrm.session;

import static org.jboss.seam.ScopeType.SESSION;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Remove;
import javax.persistence.EntityManager;
import org.domain.analyticalcrm.interfaces.StatusList;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;

@Scope(SESSION)
@Name("statusList")
public class StatusListAction implements StatusList, Serializable {

    private static final long serialVersionUID = 1L;

    @In(create = true)
    EntityManager entityManager;

    @DataModel
    private List<String> lists;

    public StatusListAction() {
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Factory("lists")
    public void getLists() {
        lists = entityManager.createQuery("select s.name from Status s").getResultList();
    }

    public String getStatusname(int id) {
        entityManager = (EntityManager) Component.getInstance("entityManager", true);
        String name = (String) entityManager.createQuery("select s.name from Status s where s.id=:id").setParameter("id", id).getSingleResult();
        return name;
    }

    public int getStatusid(String name) {
        entityManager = (EntityManager) Component.getInstance("entityManager", true);
        int id = (Integer) entityManager.createQuery("select s.id from Status s where s.name=:name").setParameter("name", name).getSingleResult();
        return id;
    }

    @Remove
    @Destroy
    public void destroy() {
    }
}
