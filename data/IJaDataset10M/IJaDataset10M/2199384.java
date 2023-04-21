package net.taylor.event.entity;

import java.util.Date;
import java.util.HashSet;
import javax.persistence.EntityManager;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.taylor.embedded.Bootstrap;
import net.taylor.testing.SeamCrudTest;

/**
 * Unit tests for the Event Seam components.
 * 
 * @author jgilbert01
 * @version $Id: EventSeamTest.java,v 1.10 2008/05/13 18:46:47 jgilbert01 Exp $
 * @generated
 */
public class EventSeamTest extends SeamCrudTest<Event> {

    /** @generated */
    public EventSeamTest(String name) {
        super(name, 20);
    }

    /** @generated */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new EventSeamTest("testCrud"));
        return new Bootstrap(suite);
    }

    /** @NOT generated */
    public void initData(EntityManager em) {
        addValue("id", new Long(System.currentTimeMillis()).toString(), false);
        addValue("source", "source", false);
        addValue("name", "name", false);
        addValue("arguments", null, false);
        addValue("output", null, false);
        addValue("exception", null, false);
        addValue("host", "host", false);
        addValue("timestamp", new Date(), false);
        addValue("userId", "userId", false);
        addValue("principal", null, false);
        addValue("subject", null, false);
        addValue("executionTime", null, false);
        addValue("correlationID", "correlationID", false);
        addValue("replyTo", "replyTo", false);
        addValue("process", "process", false);
        addValue("ids", new HashSet<String>(), true);
        addList("parentComboBox.filter('')");
        addChart("parentPieChart");
    }

    protected void login() throws Exception {
        login("admin", "admin");
    }

    protected String getEditName() {
        return "/jsf/bpm/" + getEntityName() + "/Edit.xhtml";
    }

    protected String getViewName() {
        return "/jsf/bpm/" + getEntityName() + "/View.xhtml";
    }

    protected String getSearchName() {
        return "/jsf/bpm/" + getEntityName() + "/Search.xhtml";
    }
}
