package model;

import java.util.Map;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import org.hibernate.validator.Valid;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.IfInvalid;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Outcome;
import org.jboss.seam.ejb.SeamInterceptor;

@Name("tblagendaEditor")
@Stateful
@Interceptors(SeamInterceptor.class)
public class TblAgendaEditorBean implements TblAgendaEditor {

    @In(create = true)
    private EntityManager entityManager;

    @Valid
    private TblAgenda instance = new TblAgenda();

    @TransactionAttribute(NOT_SUPPORTED)
    public TblAgenda getInstance() {
        return instance;
    }

    public void setInstance(TblAgenda instance) {
        this.instance = instance;
    }

    private boolean isNew = true;

    @TransactionAttribute(NOT_SUPPORTED)
    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    private String doneOutcome = "find";

    public void setDoneOutcome(String outcome) {
        doneOutcome = outcome;
    }

    @In(required = false)
    private transient TblAgendaFinder tblagendaFinder;

    @In(create = true)
    private transient Map messages;

    @Begin(join = true)
    @IfInvalid(outcome = Outcome.REDISPLAY)
    public String create() {
        if (entityManager.find(TblAgenda.class, instance.getIdAgenda()) != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(messages.get("TblAgenda_idAgenda") + " " + messages.get("AlreadyExists")));
            return null;
        }
        entityManager.persist(instance);
        isNew = false;
        refreshFinder();
        return "editTblAgenda";
    }

    @IfInvalid(outcome = Outcome.REDISPLAY)
    public String update() {
        refreshFinder();
        return null;
    }

    @End(ifOutcome = "find")
    public String delete() {
        entityManager.remove(instance);
        refreshFinder();
        return doneOutcome;
    }

    @End(ifOutcome = "find")
    public String done() {
        if (!isNew) entityManager.refresh(instance);
        return doneOutcome;
    }

    private void refreshFinder() {
        if (tblagendaFinder != null) tblagendaFinder.refresh();
    }

    @Destroy
    @Remove
    public void destroy() {
    }
}
