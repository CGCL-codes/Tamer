package net.grinder.tools.tcpproxy;

import java.awt.Component;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import net.grinder.testutility.RandomStubFactory;
import junit.framework.TestCase;

/**
 * Unit test case for {@link TCPProxyConsole}.
 *
 * @author Philip Aston
 * @version $Revision: 3762 $
 */
public class TestTCPProxyConsole extends TestCase {

    public void testConstructor() throws Exception {
        final RandomStubFactory engineStubFactory = new RandomStubFactory(TCPProxyEngine.class);
        final TCPProxyEngine engine = (TCPProxyEngine) engineStubFactory.getStub();
        final UpdatableCommentSource commentSource = new CommentSourceImplementation();
        final TCPProxyConsole console = new TCPProxyConsole(engine, commentSource);
        console.dispose();
        engineStubFactory.assertNoMoreCalls();
    }

    public void testButton() throws Exception {
        final RandomStubFactory engineStubFactory = new RandomStubFactory(TCPProxyEngine.class);
        final TCPProxyEngine engine = (TCPProxyEngine) engineStubFactory.getStub();
        final UpdatableCommentSource commentSource = new CommentSourceImplementation();
        final TCPProxyConsole console = new TCPProxyConsole(engine, commentSource);
        JButton stopButton = null;
        final Component[] components = console.getContentPane().getComponents();
        for (int i = 0; i < components.length; ++i) {
            if (components[i] instanceof JButton) {
                final JButton b = (JButton) components[i];
                if ("Stop".equals(b.getText())) {
                    stopButton = (JButton) components[i];
                }
            }
        }
        assertNotNull(stopButton);
        if (stopButton != null) {
            stopButton.doClick();
        }
        engineStubFactory.assertSuccess("stop");
        engineStubFactory.assertNoMoreCalls();
        console.dispatchEvent(new WindowEvent(console, WindowEvent.WINDOW_CLOSING));
        engineStubFactory.assertSuccess("stop");
        engineStubFactory.assertNoMoreCalls();
        console.dispose();
        engineStubFactory.assertNoMoreCalls();
    }
}
