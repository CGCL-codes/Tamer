package org.nexopenframework.example.business;

import org.nexopenframework.business.BusinessService;
import org.nexopenframework.business.annotations.BusinessType;
import org.nexopenframework.example.model.CEntity;
import org.nexopenframework.persistence.PersistenceManager;
import org.nexopenframework.persistence.annotations.PersistenceManagerContext;
import org.nexopenframework.persistence.annotations.PersistenceManagerUnit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @author Francesc Xavier Magdaleno
 * @version $Revision $,$Date: 2009-01-18 18:48:44 +0100 $ 
 * @since 0.3.0
 */
@org.nexopenframework.business.annotations.BusinessService(type = BusinessType.APPLICATION)
public class SimpleService implements BusinessService {

    /**Specific contract for dealing with RDBMS operations*/
    @PersistenceManagerContext
    PersistenceManager pm;

    /**
	 * 
	 * @see org.nexopenframework.core.Component#getName()
	 */
    public String getName() {
        return "SimpleService with PersistenceManager ::" + pm;
    }

    public String getHello() {
        return "Hello from an Application Service";
    }

    @PersistenceManagerUnit(name = "jdbc/simpleDS2", resourceRef = true)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String getHelloWithOtherPMUnit() {
        final CEntity entity = new CEntity();
        entity.setDescription("somedescription2");
        entity.setValue("value2");
        pm.persist(entity);
        return "Hello from an Application Service";
    }
}
