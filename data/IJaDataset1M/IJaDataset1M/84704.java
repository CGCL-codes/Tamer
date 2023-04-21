package org.jugile.daims;

import org.jugile.proto2.domain.*;
import org.jugile.util.JugileTestCase;
import org.jugile.util.Time;

public class DBUseTest extends JugileTestCase {

    public void testUpdateDB() {
        Domain.reset();
        Domain d = Domain.getDomain();
        print("persons: " + d.getPersons().size());
        for (Person p : d.getPersons()) {
            print("  del: " + p);
            p.delete();
        }
        d.commit();
        Person p = d.createPerson();
        p.setName("Scott");
        d.commit();
    }
}
