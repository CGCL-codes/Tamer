package test.view.util;

import junit.framework.TestCase;
import org.scopemvc.core.Control;
import org.scopemvc.core.Controller;
import org.scopemvc.core.Selector;
import org.scopemvc.core.View;
import org.scopemvc.view.util.ActiveBoundModel;
import org.scopemvc.view.util.ModelBindable;
import test.model.basic.BasicTestModel;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.4 $ $Date: 2002/09/12 19:09:37 $
 */
public final class TestActiveBoundModel extends TestCase {

    /**
     * TODO: describe of the Field
     */
    public static BasicTestModel testModel, subModel;

    /**
     * TODO: describe of the Field
     */
    public static TestView testView;

    private static final long WAIT_FOR_MCE = 200;

    /**
     * TODO: describe of the Field
     */
    public ActiveBoundModel activeBoundModel;

    /**
     * Constructor for the TestActiveBoundModel object
     *
     * @param inName Name of the test
     */
    public TestActiveBoundModel(String inName) {
        super(inName);
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetPropertyState() throws Exception {
        assertTrue(activeBoundModel.getPropertyReadOnly());
        assertNull(activeBoundModel.getPropertyValue());
        activeBoundModel.setBoundModel(testModel);
        assertTrue(activeBoundModel.getPropertyReadOnly());
        assertSame(testModel, activeBoundModel.getPropertyValue());
        activeBoundModel.setSelector(Selector.fromString("subModel.name"));
        assertTrue(!activeBoundModel.getPropertyReadOnly());
        assertEquals(testModel.getSubModel().getName(), activeBoundModel.getPropertyValue());
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testActiveBoundModelSetters() throws Exception {
        testView.setBoundModel(testModel);
        assertSame(testView.getViewValue(), testModel);
        assertTrue(!testView.enabled);
        testView.setSelector(Selector.fromString("subModel.name"));
        assertEquals(subModel.getName(), testView.getViewValue());
        assertTrue(testView.enabled);
        testView.setSelector(BasicTestModel.NAME);
        assertEquals(testModel.getName(), testView.getViewValue());
        assertTrue(testView.enabled);
        testView.setBoundModel(null);
        assertTrue(!testView.enabled);
        subModel.makeNameReadOnly(true);
        Thread.currentThread().sleep(WAIT_FOR_MCE);
        testView.setSelector(Selector.fromString("subModel.name"));
        testView.setBoundModel(testModel);
        assertEquals(subModel.getName(), testView.getViewValue());
        assertTrue(!testView.enabled);
        testView.setSelector(Selector.fromString("subModel.subModel.name"));
        assertNull(testView.getViewValue());
        assertTrue(!testView.enabled);
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testAccessChanges() throws Exception {
        testView.setBoundModel(testModel);
        testView.setSelector(Selector.fromString("subModel.name"));
        assertEquals(testView.getViewValue(), subModel.getName());
        assertTrue(testView.enabled);
        subModel.makeNameReadOnly(true);
        Thread.currentThread().sleep(WAIT_FOR_MCE);
        assertEquals(testView.getViewValue(), subModel.getName());
        assertTrue(!testView.enabled);
        subModel.makeNameReadOnly(false);
        Thread.currentThread().sleep(WAIT_FOR_MCE);
        assertEquals(testView.getViewValue(), subModel.getName());
        assertTrue(testView.enabled);
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testValueChanges() throws Exception {
        testView.setBoundModel(testModel);
        testView.setSelector(Selector.fromString("subModel.name"));
        assertEquals(testView.getViewValue(), subModel.getName());
        subModel.setName("New name");
        Thread.currentThread().sleep(WAIT_FOR_MCE);
        assertEquals(testView.getViewValue(), subModel.getName());
        assertEquals(testView.getViewValue(), "New name");
        BasicTestModel newSubModel = new BasicTestModel("New submodel");
        testModel.setSubModel(newSubModel);
        Thread.currentThread().sleep(WAIT_FOR_MCE);
        assertEquals(testView.getViewValue(), newSubModel.getName());
        subModel.makeNameReadOnly(true);
        Thread.currentThread().sleep(WAIT_FOR_MCE);
        testModel.setSubModel(subModel);
        Thread.currentThread().sleep(WAIT_FOR_MCE);
        assertEquals(testView.getViewValue(), subModel.getName());
        assertTrue(!testView.enabled);
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testViewToModel() throws Exception {
        testView.setBoundModel(testModel);
        testView.setSelector(BasicTestModel.NAME);
        testView.value = "New name";
        testView.boundModel.updateModel();
        Thread.currentThread().sleep(WAIT_FOR_MCE);
        assertEquals(testView.value, testModel.getName());
    }

    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {
        testModel = new BasicTestModel("model");
        subModel = new BasicTestModel("submodel");
        testModel.setSubModel(subModel);
        activeBoundModel = new ActiveBoundModel(new TestView());
        testView = new TestView();
    }

    /**
     * TODO: document the class
     *
     * @author lclaude
     * @created 05 September 2002
     */
    public class TestView implements View, ModelBindable {

        /**
         * TODO: describe of the Field
         */
        public ActiveBoundModel boundModel = new ActiveBoundModel(this);

        /**
         * TODO: describe of the Field
         */
        public Object value;

        /**
         * TODO: describe of the Field
         */
        public boolean enabled = true;

        /**
         * Gets the controller
         *
         * @return The controller value
         */
        public Controller getController() {
            return null;
        }

        /**
         * TODO: document the method
         *
         * @param inControl TODO: Describe the Parameter
         */
        public void issueControl(Control inControl) {
        }

        /**
         * Gets the selector
         *
         * @return The selector value
         */
        public Selector getSelector() {
            return boundModel.getSelector();
        }

        /**
         * Gets the bound model
         *
         * @return The boundModel value
         */
        public Object getBoundModel() {
            return boundModel.getBoundModel();
        }

        /**
         * Gets the view value
         *
         * @return The viewValue value
         */
        public Object getViewValue() {
            return value;
        }

        /**
         * Sets the controller
         *
         * @param inController The new controller value
         */
        public void setController(Controller inController) {
        }

        /**
         * Sets the selector
         *
         * @param inSelector The new selector value
         */
        public void setSelector(Selector inSelector) {
            boundModel.setSelector(inSelector);
        }

        /**
         * Sets the bound model
         *
         * @param inModel The new boundModel value
         */
        public void setBoundModel(Object inModel) {
            boundModel.setBoundModel(inModel);
        }

        /**
         * TODO: document the method
         *
         * @param inValue TODO: Describe the Parameter
         * @param inReadOnly TODO: Describe the Parameter
         */
        public void updateFromProperty(Object inValue, boolean inReadOnly) {
            value = inValue;
            enabled = !inReadOnly;
        }

        /**
         * TODO: document the method
         *
         * @param inException TODO: Describe the Parameter
         */
        public void validationFailed(Exception inException) {
        }

        /**
         * TODO: document the method
         */
        public void validationSuccess() {
        }
    }
}
