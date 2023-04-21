package com.rwatsh.ejb3;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.rwatsh.ejb3.domain.Cabin;

@Stateless
public class TravelAgentRemoteBean implements TravelAgentRemote {

    @PersistenceContext(unitName = "titan")
    private EntityManager manager;

    public void createCabin(Cabin cabin) {
        manager.persist(cabin);
    }

    public Cabin findCabin(int id) {
        return manager.find(Cabin.class, id);
    }
}
