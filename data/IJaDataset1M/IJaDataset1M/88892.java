package be.oniryx.lean.session;

import be.oniryx.lean.entity.*;
import javax.ejb.Stateful;
import javax.ejb.Remove;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.EntityManager;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import java.util.List;

/**
 * User: cedric
 * Date: May 4, 2009
 */
@Name("clientDetailsManager")
@Stateful
@Scope(ScopeType.CONVERSATION)
public class ClientDetailsManagerAction implements ClientDetailsManager {

    @In(create = true)
    private Factories factories;

    private Client current;

    private String action;

    private List<ClientRequestState> clientRequestStates;

    @In
    private FacesMessages facesMessages;

    @In
    private EntityManager em;

    @In
    private Events events;

    public String getManagedEntityName() {
        return "Client";
    }

    public String edit() {
        action = AbstractManager.EDIT;
        return select(factories.getClient());
    }

    private String select(Client c) {
        current = c;
        return "clientSelected";
    }

    public void save() {
        em.merge(current);
        action = null;
        events.raiseEvent(getManagedEntityName() + "Changed");
        select(current);
    }

    public Client getCurrent() {
        return current;
    }

    public void setCurrent(Client current) {
        this.current = current;
    }

    public void cancel() {
        action = null;
        select(factories.getClient());
    }

    public String getAction() {
        return action;
    }

    public List<ClientRequestState> getClientRequestStates() {
        if (clientRequestStates == null) {
            clientRequestStates = em.createNamedQuery("ClientRequestState.findByClient").setParameter("clientId", factories.getClient().getId()).getResultList();
        }
        return clientRequestStates;
    }

    @Remove
    @Destroy
    public void remove() {
    }
}
