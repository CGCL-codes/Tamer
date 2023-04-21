package net.googlecode.demenkov.dao.impl;

import net.googlecode.demenkov.dao.PersonDAO;
import net.googlecode.demenkov.domains.Person;
import net.googlecode.demenkov.domains.Vote;
import org.springframework.stereotype.Repository;
import javax.persistence.Query;
import java.sql.Date;

/**
 * DAO-class for actions with student's person
 *
 * @author Demenkov Yura
 */
@Repository
public class PersonDAOImpl extends GenericDAOImpl<Person, Integer> implements PersonDAO {

    public Person findPersonByEmail(String email) {
        Query query = entityManager.createQuery("from Person where email=:email");
        query.setParameter("email", email);
        if (!query.getResultList().isEmpty()) {
            return (Person) query.getResultList().get(0);
        } else {
            return null;
        }
    }

    public void createVote(Person fromPerson, Person toPerson) {
        Vote vote = new Vote();
        vote.setFromPerson(fromPerson);
        vote.setToPerson(toPerson);
        vote.setDate(new Date(System.currentTimeMillis()));
        toPerson.setRating(toPerson.getRating() + 1);
        entityManager.createQuery("update Person set rating=:rating where personId=:personId").setParameter("rating", toPerson.getRating()).setParameter("personId", toPerson.getPersonId()).executeUpdate();
        entityManager.persist(vote);
    }

    public void deleteVote(Person fromPerson, Person toPerson) {
        Query query = entityManager.createQuery("delete from Vote where fromPerson=:fromPerson and toPerson=:toPerson").setParameter("fromPerson", fromPerson).setParameter("toPerson", toPerson);
        toPerson.setRating(toPerson.getRating() - 1);
        entityManager.createQuery("update Person set rating=:rating where personId=:personId").setParameter("rating", toPerson.getRating()).setParameter("personId", toPerson.getPersonId()).executeUpdate();
        query.executeUpdate();
    }
}
