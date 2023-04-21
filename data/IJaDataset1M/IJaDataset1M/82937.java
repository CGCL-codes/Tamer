package siouxsie.mvc.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;
import junit.framework.TestCase;
import siouxsie.mvc.IActionTrigger;
import siouxsie.mvc.IScreen;
import siouxsie.mvc.IScreenHandler;
import siouxsie.mvc.IValueStackAware;
import siouxsie.mvc.Message;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionProxyFactory;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.ConfigurationProvider;
import com.opensymphony.xwork2.config.providers.XWorkConfigurationProvider;
import com.opensymphony.xwork2.config.providers.XmlConfigurationProvider;
import com.opensymphony.xwork2.inject.ContainerBuilder;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.inject.Scope;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.location.LocatableProperties;

/**
 * @author Arnaud Cogoluegnes
 * @version $Id: TestPanelScreenHandler.java 156 2008-07-02 14:29:28Z acogo $
 */
@SuppressWarnings("unchecked")
public class TestPanelScreenHandler extends TestCase {

    ActionProxyFactory actionProxyFactory;

    Map extraContext;

    Configuration conf;

    public void testScreenCreation() throws Exception {
        ActionProxy actionProxy = actionProxyFactory.createActionProxy("/", "helloWorld", extraContext, true, false);
        actionProxy.getInvocation().getInvocationContext().setParameters(new HashMap());
        actionProxy.execute();
        SimpleAction action = (SimpleAction) actionProxy.getAction();
        assertTrue(action.executed);
        PanelScreenHandler handler = (PanelScreenHandler) conf.getContainer().getInstance(IScreenHandler.class);
        assertNotNull(handler);
        Thread.sleep(100);
        assertEquals(SimpleScreen.class, handler.getInstalledScreen().getClass());
        SimpleScreen screen = (SimpleScreen) handler.getInstalledScreen();
        assertTrue(screen.built);
        assertTrue(screen.init);
        assertTrue(screen.actionTrigger);
        assertTrue(screen.component);
        assertTrue(screen.valueStack);
        assertEquals(0, screen.countMessages);
    }

    public void testScreenMessages() throws Exception {
        ActionProxy actionProxy = actionProxyFactory.createActionProxy("/", "helloWorld_messages", extraContext, true, false);
        actionProxy.getInvocation().getInvocationContext().setParameters(new HashMap());
        actionProxy.execute();
        SimpleAction action = (SimpleAction) actionProxy.getAction();
        assertTrue(action.executed);
        PanelScreenHandler handler = (PanelScreenHandler) conf.getContainer().getInstance(IScreenHandler.class);
        assertNotNull(handler);
        Thread.sleep(100);
        assertEquals(SimpleScreen.class, handler.getInstalledScreen().getClass());
        SimpleScreen screen = (SimpleScreen) handler.getInstalledScreen();
        assertTrue(screen.built);
        assertTrue(screen.init);
        assertTrue(screen.actionTrigger);
        assertTrue(screen.component);
        assertTrue(screen.valueStack);
        assertEquals(2, screen.countMessages);
    }

    public void testEmptyScreen() throws Exception {
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ConfigurationManager confManager = new ConfigurationManager();
        confManager.addContainerProvider(new SiouxsieMVCConfigurationProvider());
        confManager.addContainerProvider(new TestConfigurationProvider());
        confManager.addContainerProvider(new XmlConfigurationProvider("siouxsie/mvc/impl/test-panelscreenhandler-conf.xml", true));
        conf = confManager.getConfiguration();
        extraContext = new HashMap();
        actionProxyFactory = conf.getContainer().getInstance(ActionProxyFactory.class);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        actionProxyFactory = null;
        extraContext = null;
    }

    public static class TestConfigurationProvider implements ConfigurationProvider {

        public void destroy() {
        }

        public void init(Configuration configuration) throws ConfigurationException {
        }

        public boolean needsReload() {
            return false;
        }

        public void register(ContainerBuilder builder, LocatableProperties props) throws ConfigurationException {
            builder.factory(IScreenHandler.class, PanelScreenHandler.class, Scope.SINGLETON);
        }

        public void loadPackages() throws ConfigurationException {
        }
    }

    public static class SimpleAction extends ActionSupport {

        private static final long serialVersionUID = -1101475776770683198L;

        boolean executed = false;

        public String execute() {
            executed = true;
            return "success";
        }

        public String messages() {
            executed = true;
            addActionError("error message");
            addActionMessage("message");
            return "success";
        }

        public String toEmptyScreen() {
            return "emptyScreen";
        }
    }

    public static class SimpleScreen implements IScreen, IValueStackAware {

        boolean built = false;

        boolean init = false;

        boolean component = false;

        boolean actionTrigger = false;

        boolean valueStack = false;

        int countMessages = 0;

        public void buildGUI() {
            built = true;
        }

        public void displayMessages(Collection<Message> messages) {
            countMessages = messages.size();
        }

        public JComponent getComponent() {
            component = true;
            return new JPanel();
        }

        public void initGUI() {
            init = true;
        }

        @Inject
        public void setActionTrigger(IActionTrigger trigger) {
            actionTrigger = true;
        }

        public void setValueStack(ValueStack stack) {
            valueStack = true;
        }
    }

    public static class EmptyScreen implements IScreen {

        public void buildGUI() {
        }

        public void displayMessages(Collection<Message> messages) {
        }

        public JComponent getComponent() {
            return null;
        }

        public void initGUI() {
        }

        public void setActionTrigger(IActionTrigger trigger) {
        }
    }
}
