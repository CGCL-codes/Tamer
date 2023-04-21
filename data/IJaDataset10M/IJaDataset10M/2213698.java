package com.google.appengine.datanucleus.jdo;

import org.datanucleus.util.NucleusLogger;
import com.google.appengine.datanucleus.test.Issue165Child;
import com.google.appengine.datanucleus.test.Issue165Parent;

public class Issue165Test extends JDOTestCase {

    public void testInsert() {
        Issue165Child c = new Issue165Child();
        c.setAString("Child info");
        Issue165Parent p = new Issue165Parent();
        p.setAString("Not important");
        p.setChild(c);
        c.setParent(p);
        assertTrue(p.getChild() == c && c.getParent() == p);
        NucleusLogger.GENERAL.info(">> pm.makePersistent");
        pm.makePersistent(p);
        NucleusLogger.GENERAL.info(">> pm.makePersistent done");
    }
}
