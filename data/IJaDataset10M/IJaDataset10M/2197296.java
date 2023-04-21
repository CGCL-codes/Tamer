package org.openremote.controller.component.control;

import static org.junit.Assert.fail;
import junit.framework.Assert;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.junit.Before;
import org.junit.Test;
import org.openremote.controller.suite.AllTests;
import org.openremote.controller.component.control.gesture.Gesture;
import org.openremote.controller.component.control.gesture.GestureBuilder;
import org.openremote.controller.exception.NoSuchComponentException;
import org.openremote.controller.utils.SpringTestContext;
import org.openremote.controller.utils.XMLUtil;

/**
 * 
 * @author Javen
 *
 */
public class GestureBuilderTest {

    private String controllerXMLPath = null;

    private Document doc = null;

    private GestureBuilder builder = (GestureBuilder) SpringTestContext.getInstance().getBean("gestureBuilder");

    @Before
    public void setUp() throws Exception {
        controllerXMLPath = this.getClass().getClassLoader().getResource(AllTests.FIXTURE_DIR + "controller.xml").getFile();
        doc = XMLUtil.getControllerDocument(controllerXMLPath);
    }

    protected Element getElementByID(String id) throws JDOMException {
        return XMLUtil.getElementByID(doc, id);
    }

    private Gesture getGestureByID(String labelID) throws JDOMException {
        Element controlElement = getElementByID(labelID);
        if (!controlElement.getName().equals("gesture")) {
            throw new NoSuchComponentException("Invalid Gesture.");
        }
        return (Gesture) builder.build(controlElement, "test");
    }

    @Test
    public void testGetGestureforRealID() throws JDOMException {
        Gesture gesture = getGestureByID("7");
        Assert.assertNotNull(gesture);
    }

    @Test
    public void testGetGestureforInvalidGesture() throws JDOMException {
        try {
            getGestureByID("8");
            fail();
        } catch (NoSuchComponentException e) {
        }
    }

    @Test
    public void testGetGestureforNoSuchID() throws JDOMException {
        try {
            getGestureByID("200");
            fail();
        } catch (NoSuchComponentException e) {
        }
    }
}
