package org.desimeter.xmlJava;

import junit.framework.TestCase;
import org.desimeter.statemachine.core.Context;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestXmlToJava extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        Logger.global.setLevel(Level.ALL);
    }

    public void testXmlAdapter() {
        XmlToJava xmlToJava = XmlToJava.getInstance();
        Context context = xmlToJava.createObjectFromXML("<Context name='Start'>" + "<State name='StudentList' ></State>" + "<State name='SchoolsList' >" + "<Event name='showStudents' target='StudentList'></Event></State>" + "</Context>", Context.class);
        Logger.global.log(Level.INFO, "context object==" + context);
        assertEquals("Context Name does not match", "Start", context.getName());
    }
}
