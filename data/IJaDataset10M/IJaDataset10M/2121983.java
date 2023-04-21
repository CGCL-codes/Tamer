package org.td4j.swing.binding;

import java.lang.reflect.Method;
import javax.swing.JTextField;
import org.td4j.core.binding.model.IndividualDataProxy;
import org.td4j.core.internal.binding.model.IndividualMethodConnector;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ch.miranet.commons.TK;

public class TextControllerTest {

    private IndividualMethodConnector dataConnector;

    @Test(dataProvider = "harness")
    public void testShowInitialModelValueInWidget(final TestHarness harness) {
        assertTextFieldContent(DataContainer.INITIAL_VALUE, harness);
    }

    @Test(dataProvider = "harness")
    public void testShowChangedModelValueInWidget(final TestHarness harness) {
        harness.dataContainer.setValue(DataContainer.ACCEPTED_VALUE);
        harness.dataProxy.refreshFromContext();
        assertTextFieldContent(DataContainer.ACCEPTED_VALUE, harness);
    }

    @Test(dataProvider = "harness")
    public void testWriteWidgetValueToModel(final TestHarness harness) {
        harness.textField.setText(DataContainer.ACCEPTED_VALUE);
        harness.textController.doUpdateModel();
        assertTextFieldContent(DataContainer.ACCEPTED_VALUE, harness);
        assertDataContainerContent(DataContainer.ACCEPTED_VALUE, harness);
    }

    @Test(dataProvider = "harness", expectedExceptions = { IllegalArgumentException.class })
    public void testWriteRejectedWidgetValueToModelAndRecover(final TestHarness harness) throws Throwable {
        harness.textField.setText(DataContainer.REJECTED_VALUE);
        try {
            harness.textController.doUpdateModel();
        } catch (Exception ex) {
            assertTextFieldContent(DataContainer.INITIAL_VALUE, harness);
            assertDataContainerContent(DataContainer.INITIAL_VALUE, harness);
            throw TK.Exceptions.unwrap(ex);
        }
    }

    private void assertTextFieldContent(final String expectedContent, final TestHarness harness) {
        assert harness.textField.getText().equals(expectedContent);
    }

    private void assertDataContainerContent(final String expectedContent, final TestHarness harness) {
        assert harness.dataContainer.getValue().equals(expectedContent);
    }

    @DataProvider(name = "harness")
    public Object[][] createHarness() {
        if (dataConnector == null) {
            dataConnector = createDataConnector();
        }
        final IndividualDataProxy dataProxy = new IndividualDataProxy(dataConnector, "value");
        final DataContainer dataContainer = new DataContainer();
        dataProxy.setContext(dataContainer);
        final JTextField textField = new JTextField();
        final TestTextController textController = new TestTextController(textField, dataProxy);
        final TestHarness harness = new TestHarness(dataContainer, dataProxy, textController, textField);
        return new Object[][] { { harness } };
    }

    private IndividualMethodConnector createDataConnector() {
        try {
            final Method getter = DataContainer.class.getMethod("getValue");
            final Method setter = DataContainer.class.getMethod("setValue", String.class);
            final IndividualMethodConnector dataConnector = new IndividualMethodConnector(DataContainer.class, getter, setter);
            return dataConnector;
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static class TestHarness {

        public final DataContainer dataContainer;

        public final IndividualDataProxy dataProxy;

        public final TestTextController textController;

        public final JTextField textField;

        private TestHarness(DataContainer dataContainer, IndividualDataProxy dataProxy, TestTextController textController, JTextField textField) {
            this.dataContainer = TK.Objects.assertNotNull(dataContainer, "dataContainer");
            this.dataProxy = TK.Objects.assertNotNull(dataProxy, "dataProxy");
            this.textController = TK.Objects.assertNotNull(textController, "textController");
            this.textField = TK.Objects.assertNotNull(textField, "textField");
        }
    }

    public static class DataContainer {

        public static final String INITIAL_VALUE = "initial";

        public static final String ACCEPTED_VALUE = "accept";

        public static final String REJECTED_VALUE = "reject";

        private String value = INITIAL_VALUE;

        public String getValue() {
            return value;
        }

        public void setValue(String s) {
            if (REJECTED_VALUE.equals(s)) throw new IllegalArgumentException(REJECTED_VALUE);
            this.value = s;
        }
    }

    public static class TestTextController extends TextController {

        private TestTextController(JTextField widget, IndividualDataProxy proxy) {
            super(widget, proxy);
        }

        public void doUpdateModel() {
            super.updateModel();
        }
    }
}
