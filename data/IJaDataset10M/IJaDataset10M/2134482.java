package gumbo.net.test.awt.util;

import gumbo.net.NetBroker;
import gumbo.net.NetBrokerListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * A TestNetWindow for a client network node, with default actions and status.
 * @author Jon Barrilleaux (jonb@jmbaai.com) of JMB and Associates Inc.
 */
public class TestClientWindow extends TestNetWindow {

    /**
	 * Creates an instance. Must call setBrokerProxy() before use.
	 */
    public TestClientWindow(String name, JFrame window, TestBrokerProxy brokerProxy, TestNetPinger.Client<?> pinger) {
        super(name, window);
        _brokerProxy = brokerProxy;
        _pinger = pinger;
        setTitleText("(not connected)");
        _pinger.setStatusWriter(getStatusBarWriter());
        NetBrokerListener listener = new NetBrokerListener() {

            @Override
            public void handleNetBroker(NetBroker source, NetBroker.EventType type, String msg, Throwable cause) {
                switch(type) {
                    case DATA_READY:
                        setStatusText(msg);
                        setTitleText("to " + _brokerProxy.getRemoteHostport());
                        break;
                    case DATA_DONE:
                        setStatusText(msg);
                        setTitleText("(not connected)");
                        break;
                    default:
                        setStatusText(msg);
                        break;
                }
            }
        };
        _pinger.getBroker().addNetBrokerListener(listener);
        KillClientAction killClientAction = new KillClientAction(_pinger.getBroker().getConnector(), "Kill client", null);
        PingAction pingServerAction = new PingAction(_pinger, "Ping server", null);
        LocalWhoAction clientWhoAction = new LocalWhoAction(_pinger.getBroker().getConnector(), "This client?", null);
        RemoteWhoAction serverWhoAction = new RemoteWhoAction(_pinger.getBroker().getConnector(), "Server?", null);
        RetryAction retryAction = new RetryAction(_brokerProxy, "Manual Retry", null, "Auto Retry", null, false);
        JMenu menu;
        menu = new JMenu("Net");
        getMenuBar().add(menu);
        menu.add(new JMenuItem(killClientAction));
        menu.add(new JMenuItem(pingServerAction));
        menu.add(new JMenuItem(clientWhoAction));
        menu.add(new JMenuItem(serverWhoAction));
        menu.add(new JMenuItem(retryAction.getManualAction()));
        menu.add(new JMenuItem(retryAction.getAutoAction()));
        getToolBar().add(new JButton(killClientAction));
        getToolBar().add(new JButton(pingServerAction));
        getToolBar().add(new JButton(clientWhoAction));
        getToolBar().add(new JButton(serverWhoAction));
        getToolBar().add(new JButton(retryAction.getManualAction()));
        getToolBar().add(new JButton(retryAction.getAutoAction()));
    }

    TestNetPinger.Client<?> _pinger;

    TestBrokerProxy _brokerProxy;
}
