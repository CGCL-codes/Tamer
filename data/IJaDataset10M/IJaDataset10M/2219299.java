package fi.arcusys.qnet.cygnus.service;

import org.springframework.transaction.annotation.Transactional;
import fi.arcusys.acj.service.EntityDaoBasicEntityService;
import fi.arcusys.cygnus.dao.CustomerDao;
import fi.arcusys.cygnus.model.Customer;

/**
 * @version 1.0 $Rev$
 * @author mikko
 * Copyright (C) 2007 Arcusys Oy
 */
@Transactional
public class DefaultCustomerService extends EntityDaoBasicEntityService<Customer, CustomerDao> implements CustomerService {

    private static final long serialVersionUID = 1L;

    public Customer findByName(String name) {
        return getDao().findUniqueByPropertyEq("name", name);
    }
}
