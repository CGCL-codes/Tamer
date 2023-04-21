package org.argouml.uml.ui.behavior.collaborations;

import junit.framework.TestCase;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.util.ThreadHelper;

/**
 * @since Oct 30, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLMessageActionListModel extends TestCase {

    private UMLMessageActionListModel model;

    private Object elem;

    /**
     * Constructor for TestUMLMessageActionListModel.
     *
     * @param arg0 is the name of the test case.
     */
    public TestUMLMessageActionListModel(String arg0) {
        super(arg0);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        elem = Model.getCollaborationsFactory().createMessage();
        model = new UMLMessageActionListModel();
        model.setTarget(elem);
        ThreadHelper.synchronize();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Model.getUmlFactory().delete(elem);
        model = null;
    }

    /**
     * Test setAction().
     */
    public void testSetAction() throws Exception {
        Object action = Model.getCommonBehaviorFactory().createUninterpretedAction();
        Model.getCollaborationsHelper().setAction(elem, action);
        ThreadHelper.synchronize();
        assertEquals(1, model.getSize());
        assertEquals(action, model.getElementAt(0));
    }

    /**
     * Test setAction() for removing.
     */
    public void testRemoveAction() throws Exception {
        Object action = Model.getCommonBehaviorFactory().createUninterpretedAction();
        Model.getCollaborationsHelper().setAction(elem, action);
        Model.getCollaborationsHelper().setAction(elem, null);
        ThreadHelper.synchronize();
        assertEquals(0, model.getSize());
        assertTrue(model.isEmpty());
    }
}
