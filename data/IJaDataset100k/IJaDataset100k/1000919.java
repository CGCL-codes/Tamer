package org.azrul.epice.util;

import com.db4o.ObjectContainer;
import java.util.HashSet;
import java.util.Set;
import org.azrul.epice.domain.Person;

public class Init {

    public static void main(String[] args) {
        ObjectContainer db = DBUtil.getDB();
        try {
            Person person2 = new Person();
            person2.setIcNumber("770321-14-5772");
            person2.setName("Mickey Mouse");
            person2.setUserName("mickey");
            person2.setEmail("mickey.mouse@company.com");
            person2.setPassword("abc123");
            Person person3 = new Person();
            person3.setIcNumber("770321-14-5773");
            person3.setName("Goofy Goof");
            person3.setUserName("goofy");
            person3.setEmail("goofy.goof@company.com");
            person3.setPassword("abc123");
            db.set(person3);
            Set<Person> buddies = new HashSet<Person>();
            buddies.add(person2);
            person3.setBuddies(buddies);
            Person person4 = new Person();
            person4.setIcNumber("770321-14-5774");
            person4.setName("Minnie Mouse");
            person4.setUserName("minnie");
            person4.setEmail("minnie.mouse@company.com");
            person4.setPassword("abc123");
            Person person5 = new Person();
            person5.setIcNumber("770321-14-5775");
            person5.setName("Daisy Duck");
            person5.setUserName("daisy");
            person5.setEmail("daisy.duck@company.com");
            person5.setPassword("abc123");
            Person person = new Person();
            person.setIcNumber("770321-14-5771");
            person.setName("Donald Duck");
            person.setUserName("donald");
            person.setEmail("donald.duck@company.com");
            person.setPassword("abc123");
            Set<Person> buddies2 = new HashSet<Person>();
            buddies2.add(person3);
            person.setBuddies(buddies2);
            Set<Person> supervisor2 = new HashSet<Person>();
            supervisor2.add(person4);
            supervisor2.add(person5);
            person.setSupervisors(supervisor2);
            if (!db.get(person).hasNext()) {
                db.set(person);
            }
            if (!db.get(person2).hasNext()) {
                db.set(person2);
            }
            if (!db.get(person3).hasNext()) {
                db.set(person3);
            }
            if (!db.get(person4).hasNext()) {
                db.set(person4);
            }
            if (!db.get(person5).hasNext()) {
                db.set(person5);
            }
            db.commit();
        } finally {
            db.close();
        }
    }
}
