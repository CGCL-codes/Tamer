package org.jugile.daims;

import org.jugile.proto2.domain.*;
import org.jugile.util.JugileTestCase;

public class RefreshTest extends JugileTestCase {

    public void testRefresh() {
        Domain d = Domain.getDomain();
        Person p1 = d.createPerson().setName("p1");
        d.commit();
        p1 = d.getPerson(p1.id());
        p1.refresh();
        d.rollback();
    }

    public void testFlush() throws Exception {
    }
}
