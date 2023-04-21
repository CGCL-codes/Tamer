package pl.zgora.uz.issi.viewbeans;

import javax.persistence.EntityManager;
import model.Paper;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

@Name("paperViewBean")
public class PaperViewBean {

    private Paper instance;

    @In
    EntityManager entityManager;

    public void setId(Long id) {
        instance = (Paper) entityManager.createQuery("from Paper as p where p.id=" + id).getSingleResult();
    }

    public Long getId() {
        if (instance != null) return instance.getId();
        return null;
    }

    public Paper getInstance() {
        return instance;
    }

    public void setInstance(Paper instance) {
        this.instance = instance;
    }
}
