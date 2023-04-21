package com.google.code.donkirkby.events.ui;

import org.hibernate.Session;
import com.google.code.donkirkby.events.Event;
import com.google.code.donkirkby.events.Person;
import com.google.code.donkirkby.util.HibernateUtil;
import java.util.Date;
import java.util.List;

public class EventManager {

    public static void main(String[] args) {
        EventManager mgr = new EventManager();
        if (args[0].equals("store")) {
            mgr.createAndStoreEvent("Their Event", new Date());
        } else if (args[0].equals("list")) {
            List events = mgr.listEvents();
            for (int i = 0; i < events.size(); i++) {
                Event theEvent = (Event) events.get(i);
                System.out.println("Event: " + theEvent.getTitle() + " Time: " + theEvent.getDate());
            }
        } else if (args[0].equals("addpersontoevent")) {
            Long eventId = mgr.createAndStoreEvent("My Event", new Date());
            Long personId = mgr.createAndStorePerson("Foo", "Bar");
            mgr.addPersonToEvent(personId, eventId);
            System.out.println("Added person " + personId + " to event " + eventId);
        } else if (args[0].equals("addaddresstoperson")) {
            Long personId = mgr.createAndStorePerson("Foo", "Bar");
            String address = "bob@bob.com";
            mgr.addEmailToPerson(personId, address);
            System.out.println("Added address " + address + " to event " + personId);
        }
        HibernateUtil.getSessionFactory().close();
    }

    private Long createAndStoreEvent(String title, Date theDate) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Event theEvent = new Event();
        theEvent.setTitle(title);
        theEvent.setDate(theDate);
        session.save(theEvent);
        session.getTransaction().commit();
        return theEvent.getId();
    }

    private Long createAndStorePerson(String firstName, String lastName) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Person thePerson = new Person();
        thePerson.setFirstname(firstName);
        thePerson.setLastname(lastName);
        session.save(thePerson);
        session.getTransaction().commit();
        return thePerson.getId();
    }

    private List listEvents() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List result = session.createQuery("from Event").list();
        session.getTransaction().commit();
        return result;
    }

    @SuppressWarnings("unchecked")
    private void addPersonToEvent(Long personId, Long eventId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Person aPerson = (Person) session.load(Person.class, personId);
        Event anEvent = (Event) session.load(Event.class, eventId);
        aPerson.getEvents().add(anEvent);
        session.getTransaction().commit();
    }

    @SuppressWarnings("unchecked")
    private void addEmailToPerson(Long personId, String emailAddress) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Person aPerson;
        if (false) {
            aPerson = (Person) session.load(Person.class, personId);
        } else {
            aPerson = (Person) session.createQuery("select p from Person p left join fetch p.emailAddresses where p.id = :pid").setParameter("pid", personId).uniqueResult();
        }
        aPerson.getEmailAddresses().add(emailAddress);
        session.getTransaction().commit();
    }
}
