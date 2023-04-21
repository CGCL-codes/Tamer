package test.googlecode.genericdao.dao.hibernate.dao;

import java.util.List;
import test.googlecode.genericdao.model.Person;
import com.googlecode.genericdao.dao.hibernate.GenericDAO;

public interface PersonDAO extends GenericDAO<Person, Long> {

    public List<Person> findByName(String firstName, String lastName);
}
