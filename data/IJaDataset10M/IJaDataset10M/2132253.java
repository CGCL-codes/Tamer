package net.sf.wicketdemo.service;

import java.io.Serializable;
import java.util.UUID;
import net.sf.wicketdemo.domain.Person;
import net.sf.wicketdemo.tech.service.BaseService;

/**
 * @author Dieter D'haeyere
 */
class PersonServiceImpl extends BaseService<Person, UUID> implements PersonService, Serializable {

    private static final long serialVersionUID = 1L;
}
