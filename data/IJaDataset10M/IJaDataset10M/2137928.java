package org.waveprotocol.wave.client.gadget.renderer;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * Tests Gadget Controller functions that relay JavaScript calls to GWT.
 *
 */
public class ControllerGwtTest extends GWTTestCase {

    private static class TestController extends Controller {

        private static final JavaScriptObject LIBRARY = getFakeRpcLibrary();

        /**
     * Test controller constructor.
     */
        public TestController() {
            super(LIBRARY);
        }

        /**
     * Issues a simulated gadget RPC call at JS level.
     *
     * @param gadgetId ID of the gadget that would generate this call.
     * @param service name of the service.
     * @param args call arguments.
     */
        public void simulateCallFromGadget(String gadgetId, String service, String... args) {
            JsArrayMixed arr = JsArrayMixed.create();
            for (int i = 0; i < args.length; ++i) {
                arr.put(i, args[i]);
            }
            simulateCallFromGadgetHelper(gadgetId, service, arr);
        }

        /**
     * Issues a simulated gadget RPC call at JS level.
     *
     * @param gadgetId ID of the gadget that would generate this call.
     * @param service name of the service.
     * @param args call arguments.
     */
        public void simulateCallFromGadget(String gadgetId, String service, JavaScriptObject... args) {
            JsArrayMixed arr = JsArrayMixed.create();
            for (int i = 0; i < args.length; ++i) {
                arr.put(i, args[i]);
            }
            simulateCallFromGadgetHelper(gadgetId, service, arr);
        }

        private native void simulateCallFromGadgetHelper(String gadgetId, String service, JsArrayMixed args);

        /**
     * Sets singleton value for testing. This is necessary evil because the
     * registered JS functions require a reference to the Controller instance in
     * the static context. Because of that a completely clean testing of the
     * controller is not possible.
     * TODO(user): Explore other options for binding JS and Java contexts.
     *
     * @param controller controller that substitutes the singleton.
     */
        public static void setTestInstance(Controller controller) {
            instance = controller;
        }

        /**
     * Returns a fake gadget RPC library.
     *
     * @return gadget RPC library as JS object.
     */
        private static native JavaScriptObject getFakeRpcLibrary();
    }

    /**
   * Test gadget RPC listener with methods to receive service calls and verify
   * the results.
   *
   * TODO(user): Convert to mockito implementation.
   */
    private static class Listener implements GadgetRpcListener {

        private String gadgetTitle = "";

        private int setTitleCounter = 0;

        private String[] prefKeyValue = {};

        private int setPrefCounter = 0;

        private String iframeHeight = "";

        private int setIframeHeightCounter = 0;

        private String iframeWidth = "";

        private int setIframeWidthCounter = 0;

        private String navigateUrl = "";

        private int requestNavigateToCounter = 0;

        private String podiumState = "";

        private int updatePodiumStateCounter = 0;

        private String apiVersion = "";

        private int waveEnableCounter = 0;

        private JavaScriptObject stateDelta = JavaScriptObject.createObject();

        private int waveGadgetStateUpdateCounter = 0;

        private JavaScriptObject statePrivateDelta = JavaScriptObject.createObject();

        private int wavePrivateGadgetStateUpdateCounter = 0;

        private String logMessage = "";

        private int logMessageCounter = 0;

        private String gadgetSnippet = "";

        private int setSnippetCounter = 0;

        @Override
        public void setTitle(String title) {
            gadgetTitle = title;
            setTitleCounter++;
        }

        @Override
        public void setPrefs(String... keyValue) {
            prefKeyValue = keyValue;
            setPrefCounter++;
        }

        @Override
        public void setIframeHeight(String height) {
            iframeHeight = height;
            setIframeHeightCounter++;
        }

        @Override
        public void setIframeWidth(String width) {
            iframeWidth = width;
            setIframeWidthCounter++;
        }

        @Override
        public void requestNavigateTo(String url) {
            navigateUrl = url;
            requestNavigateToCounter++;
        }

        @Override
        public void updatePodiumState(String state) {
            podiumState = state;
            updatePodiumStateCounter++;
        }

        @Override
        public void waveEnable(String waveApiVersion) {
            apiVersion = waveApiVersion;
            waveEnableCounter++;
        }

        @Override
        public void waveGadgetStateUpdate(JavaScriptObject delta) {
            stateDelta = delta;
            waveGadgetStateUpdateCounter++;
        }

        @Override
        public void logMessage(String message) {
            logMessage = message;
            logMessageCounter++;
        }

        @Override
        public void wavePrivateGadgetStateUpdate(JavaScriptObject delta) {
            statePrivateDelta = delta;
            wavePrivateGadgetStateUpdateCounter++;
        }

        @Override
        public void setSnippet(String snippet) {
            gadgetSnippet = snippet;
            setSnippetCounter++;
        }

        /**
     * Verify set title results.
     *
     * @param title
     * @param counter
     */
        public void assertSetTitle(String title, int counter) {
            assertEquals(title, gadgetTitle);
            assertEquals(counter, setTitleCounter);
        }

        /**
     * Verify set preferences results.
     *
     * @param counter
     * @param keyValue
     */
        public void assertSetPref(int counter, String... keyValue) {
            if (keyValue == null) {
                assertTrue((prefKeyValue == null) || (prefKeyValue.length == 0));
            } else if (prefKeyValue == null) {
                assertTrue((keyValue == null) || (keyValue.length == 0));
            } else {
                assertEquals(keyValue.length, prefKeyValue.length);
                for (int i = 0; i < keyValue.length; ++i) {
                    assertEquals(keyValue[i], prefKeyValue[i]);
                }
            }
            assertEquals(counter, setPrefCounter);
        }

        /**
     * Verify set IFrame height results.
     * @param height
     * @param counter
     */
        public void assertSetIframeHeight(String height, int counter) {
            assertEquals(height, iframeHeight);
            assertEquals(counter, setIframeHeightCounter);
        }

        /**
     * Verify set IFrame width results.
     * @param width
     * @param counter
     */
        public void assertSetIframeWidth(String width, int counter) {
            assertEquals(width, iframeWidth);
            assertEquals(counter, setIframeWidthCounter);
        }

        /**
     * Verify request navigate to results.
     *
     * @param url
     * @param counter
     */
        public void assertRequestNavigateTo(String url, int counter) {
            assertEquals(url, navigateUrl);
            assertEquals(counter, requestNavigateToCounter);
        }

        /**
     * Verify update Podium state results.
     *
     * @param state
     * @param counter
     */
        public void assertUpdatePodiumState(String state, int counter) {
            assertEquals(state, podiumState);
            assertEquals(counter, updatePodiumStateCounter);
        }

        /**
     * Verify wave enable results.
     *
     * @param waveApiVersion
     * @param counter
     */
        public void assertWaveEnable(String waveApiVersion, int counter) {
            assertEquals(waveApiVersion, apiVersion);
            assertEquals(counter, waveEnableCounter);
        }

        /**
     * Verify wave gadget state update results.
     *
     * @param delta
     * @param counter
     */
        public void assertWaveGadgetStateUpdate(JavaScriptObject delta, int counter) {
            assertEquals(delta.toString(), stateDelta.toString());
            assertEquals(counter, waveGadgetStateUpdateCounter);
        }

        /**
     * Verify wave gadget state update results.
     *
     * @param delta
     * @param counter
     */
        public void assertWavePrivateGadgetStateUpdate(JavaScriptObject delta, int counter) {
            assertEquals(delta.toString(), statePrivateDelta.toString());
            assertEquals(counter, wavePrivateGadgetStateUpdateCounter);
        }

        /**
     * Verify logMessage results.
     *
     * @param message
     * @param counter
     */
        public void assertLogMessage(String message, int counter) {
            assertEquals(message, logMessage);
            assertEquals(counter, logMessageCounter);
        }

        /**
     * Verify setSnippet results.
     *
     * @param snippet
     * @param counter
     */
        public void assertSetSnippet(String snippet, int counter) {
            assertEquals(snippet, gadgetSnippet);
            assertEquals(counter, setSnippetCounter);
        }
    }

    private static native JavaScriptObject getFakeStateObject();

    private static native JavaScriptObject getFakePrivateStateObject();

    /**
   * Runs a mixed chain of service calls to make sure individual calls are
   * correct and do not interfere.
   */
    public void testController() {
        TestController controller = new TestController();
        TestController.setTestInstance(controller);
        Listener listener1 = new Listener();
        Listener listener2 = new Listener();
        Listener hostListener = new Listener();
        controller.registerGadgetListener("1", listener1);
        controller.registerGadgetListener("2", listener2);
        controller.registerGadgetListener("..", hostListener);
        controller.simulateCallFromGadget("1", "set_title", "Title 1");
        listener1.assertSetTitle("Title 1", 1);
        controller.simulateCallFromGadget("2", "set_title", "Title 2");
        listener2.assertSetTitle("Title 2", 1);
        controller.simulateCallFromGadget("1", "set_title", "Title 1 again");
        controller.simulateCallFromGadget("2", "set_title", "Title 2 again");
        controller.call("Target ID", "set_title", callArgument(null));
        controller.simulateCallFromGadget("1", "set_pref", "ignoredToken", "pref 1", "value 1", "pref 2", "value 2");
        controller.simulateCallFromGadget("2", "resize_iframe", "200");
        controller.simulateCallFromGadget("2", "setIframeWidth", "100");
        controller.simulateCallFromGadget("1", "requestNavigateTo", "url 1");
        controller.simulateCallFromGadget("2", "updateState", "state 2");
        controller.simulateCallFromGadget("1", "wave_enable", "1.0");
        controller.simulateCallFromGadget("2", "wave_gadget_state", getFakeStateObject());
        controller.simulateCallFromGadget("1", "wave_log", "Log it!");
        controller.simulateCallFromGadget("2", "set_snippet", "Snip it!");
        controller.simulateCallFromGadget("1", "wave_private_gadget_state", getFakePrivateStateObject());
        listener1.assertSetTitle("Title 1 again", 2);
        listener2.assertSetTitle("Title 2 again", 2);
        hostListener.assertSetTitle("Target ID", 1);
        listener1.assertSetPref(1, "pref 1", "value 1", "pref 2", "value 2");
        listener2.assertSetPref(0);
        listener1.assertSetIframeHeight("", 0);
        listener1.assertSetIframeWidth("", 0);
        listener2.assertSetIframeHeight("200", 1);
        listener2.assertSetIframeWidth("100", 1);
        listener1.assertRequestNavigateTo("url 1", 1);
        listener2.assertRequestNavigateTo("", 0);
        listener1.assertUpdatePodiumState("", 0);
        listener2.assertUpdatePodiumState("state 2", 1);
        listener1.assertWaveEnable("1.0", 1);
        listener2.assertWaveEnable("", 0);
        listener1.assertWaveGadgetStateUpdate(JavaScriptObject.createObject(), 0);
        listener2.assertWaveGadgetStateUpdate(getFakeStateObject(), 1);
        listener1.assertLogMessage("Log it!", 1);
        listener2.assertLogMessage("", 0);
        listener1.assertSetSnippet("", 0);
        listener2.assertSetSnippet("Snip it!", 1);
        listener1.assertWavePrivateGadgetStateUpdate(getFakePrivateStateObject(), 1);
        controller.simulateCallFromGadget("2", "set_pref", "ignoredToken");
        listener2.assertSetPref(1);
        controller.simulateCallFromGadget("2", "set_pref", "ignoredToken", "pref 1", "value 1");
        listener2.assertSetPref(2, "pref 1", "value 1");
    }

    @Override
    public String getModuleName() {
        return "org.waveprotocol.wave.client.gadget.tests";
    }

    @Override
    protected void gwtTearDown() {
        TestController.setTestInstance(null);
    }

    /**
   * @param argument Call argument.
   * @returns Call argument as JsArrayMixed.
   */
    private native Controller.JsArrayMixed callArgument(String argument);
}
