package org.topdowntdd.sample.appfusemodularstruts.dao;

import org.appfuse.dao.GenericDao;
import org.topdowntdd.sample.appfusemodularstruts.model.Person;

public interface PersonDao extends GenericDao<Person, Long> {
}
