package net.sf.jzeno.tests;

import net.sf.jzeno.echo.databinding.DynaCheckBox;
import nextapp.echoservlet.EchoTestSupport;
import nextapp.echoservlet.ui.CheckBoxUI;
import org.apache.log4j.Logger;

/**
 * @author ddhondt
 * 
 * Testcase for the DynaCheckBox.
 */
public class DynaCheckBoxTest extends AbstractTest2 {

    protected static Logger log = Logger.getLogger(DynaCheckBoxTest.class);

    private DynaCheckBox dynaCheckBox = null;

    MockEventListener mockEventListener = null;

    MockModelBean mockModelBean = null;

    CheckBoxUI checkBoxUI = null;

    protected void setUp() throws Exception {
        dynaCheckBox = new DynaCheckBox();
        mockEventListener = new MockEventListener();
        mockModelBean = new MockModelBean();
        dynaCheckBox.addPropertyChangeListener(mockEventListener);
        dynaCheckBox.setBeanClass(mockModelBean.getClass());
        dynaCheckBox.setBean(mockModelBean);
        dynaCheckBox.setProperty("bool");
        dynaCheckBox.rebind();
        checkBoxUI = (CheckBoxUI) EchoTestSupport.createComponentPeer(dynaCheckBox);
        super.setUp();
    }

    /**
     * Check that changing properties that have impact on visual representation
     * generate a property change event (Echo uses this to determine
     * re-rendering)
     * 
     * @throws Exception
     */
    public void testCustomProperties() throws Exception {
        String newValue;
        newValue = "new value text";
        dynaCheckBox.setText(newValue);
        assertTrue(mockEventListener.checkEvent());
    }

    public void testCheckingWritesBackToModel() throws Exception {
        checkBoxUI.clientInput("1");
        assertTrue(mockModelBean.isBool());
        checkBoxUI.clientInput("1");
        assertTrue(mockModelBean.isBool());
        checkBoxUI.clientInput("0");
        assertFalse(mockModelBean.isBool());
    }

    public void testRebinding() throws Exception {
        mockModelBean.setBool(false);
        dynaCheckBox.rebind();
        assertEquals(false, dynaCheckBox.isSelected());
        checkBoxUI.clientInput("1");
        assertEquals(true, dynaCheckBox.isSelected());
        assertEquals(true, mockModelBean.isBool());
        mockModelBean.setBool(false);
        dynaCheckBox.rebind();
        assertEquals(false, dynaCheckBox.isSelected());
    }

    public void testEventSource() throws Exception {
        EventSourceTestSupport testSupport = null;
        testSupport = new EventSourceTestSupport(getComponentClass(), createBean(), getBeanProperty());
        assertTrue(testSupport.testComponentGeneratesActionCommand());
        testSupport = new EventSourceTestSupport(getComponentClass(), createBean(), getBeanProperty());
        assertTrue(testSupport.testDataBindingNoBean());
        testSupport = new EventSourceTestSupport(getComponentClass(), createBean(), getBeanProperty());
        assertTrue(testSupport.testEventBoundValue());
        testSupport = new EventSourceTestSupport(getComponentClass(), createBean(), getBeanProperty());
        assertTrue(testSupport.testDoubleWireEvent());
        testSupport = new EventSourceTestSupport(getComponentClass(), createBean(), getBeanProperty());
        assertTrue(testSupport.testEventSinkEndsEventChain());
        testSupport = new EventSourceTestSupport(getComponentClass(), createBean(), getBeanProperty());
        assertTrue(testSupport.testEventSinkEndsEventChain2());
        testSupport = new EventSourceTestSupport(getComponentClass(), createBean(), getBeanProperty());
        assertTrue(testSupport.testREwireEvent());
        testSupport = new EventSourceTestSupport(getComponentClass(), createBean(), getBeanProperty());
        assertTrue(testSupport.testDataBindingDirectToBeanThrowsException());
        testSupport = new EventSourceTestSupport(getComponentClass(), createBean(), getBeanProperty());
        assertTrue(testSupport.testDataBindingNullPropertyPathThrowsException());
        testSupport = new EventSourceTestSupport(getComponentClass(), createBean(), getBeanProperty());
        assertFalse(testSupport.testDataBindingDirectToBean());
        testSupport = new EventSourceTestSupport(getComponentClass(), createBean(), getBeanProperty());
        assertFalse(testSupport.testDataBindingNullPropertyPath());
    }

    public void testPropertyComponent() throws Exception {
        PropertyComponentTestSupport support = null;
        support = new PropertyComponentTestSupport(getComponentClass(), createBean(), getBeanProperty(), createNewValueForProperty());
        assertTrue(support.testBeanProperty());
        support = new PropertyComponentTestSupport(getComponentClass(), createBean(), getBeanProperty(), createNewValueForProperty());
        assertTrue(support.testDecoratorClassNameProperty());
        support = new PropertyComponentTestSupport(getComponentClass(), createBean(), getBeanProperty(), createNewValueForProperty());
        assertTrue(support.testPropertyProperty());
        support = new PropertyComponentTestSupport(getComponentClass(), createBean(), getBeanProperty(), createNewValueForProperty());
        assertTrue(support.testRequiredProperty());
        support = new PropertyComponentTestSupport(getComponentClass(), createBean(), getBeanProperty(), createNewValueForProperty());
        assertTrue(support.testValidProperty());
        support = new PropertyComponentTestSupport(getComponentClass(), createBean(), getBeanProperty(), createNewValueForProperty());
        assertTrue(support.testWriteValue());
    }

    protected Class getComponentClass() {
        return DynaCheckBox.class;
    }

    protected Object createBean() {
        return new MockModelBean();
    }

    protected String getBeanProperty() {
        return "bool";
    }

    protected Object createNewValueForProperty() {
        return Boolean.FALSE;
    }
}
