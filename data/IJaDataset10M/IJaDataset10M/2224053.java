package ftn.edu.ais.service.impl;

import ftn.edu.ais.dao.DeteDao;
import ftn.edu.ais.model.Dete;
import ftn.edu.ais.service.DeteManager;
import javax.jws.WebService;

@WebService(serviceName = "DeteService", endpointInterface = "ftn.edu.ais.service.DeteManager")
public class DeteManagerImpl extends GenericManagerImpl<Dete, Long> implements DeteManager {

    DeteDao deteDao;

    public DeteManagerImpl(DeteDao deteDao) {
        super(deteDao);
        this.deteDao = deteDao;
    }
}
