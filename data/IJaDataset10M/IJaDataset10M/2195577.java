package net.sf.colossus.client;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import net.sf.colossus.server.Constants;
import net.sf.colossus.server.Start;
import net.sf.colossus.util.KFrame;
import net.sf.colossus.util.Options;

/**
 *  Startup code for network Client
 *  @version $Id: StartClient.java 2975 2008-01-06 10:34:55Z peterbecker $
 *  @author David Ripton
 */
public class StartClient extends KFrame implements WindowListener, ActionListener {

    private static final Logger LOGGER = Logger.getLogger(StartClient.class.getName());

    private final Object mutex;

    private final Options netclientOptions;

    private final Options stOptions;

    private final Start startObject;

    private String playerName;

    private String hostname;

    private int port;

    private final SaveWindow saveWindow;

    private final JComboBox nameBox;

    private final JComboBox hostBox;

    private final JComboBox portBox;

    public StartClient(Object mutex, Start startObject) {
        super("Client startup options");
        getContentPane().setLayout(new GridLayout(0, 2));
        net.sf.colossus.webcommon.InstanceTracker.register(this, "only one");
        this.mutex = mutex;
        this.startObject = startObject;
        this.stOptions = startObject.getStartOptions();
        this.playerName = stOptions.getStringOption(Options.runClientPlayer);
        this.hostname = stOptions.getStringOption(Options.runClientHost);
        this.port = stOptions.getIntOption(Options.runClientPort);
        netclientOptions = new Options(Constants.optionsNetClientName);
        netclientOptions.loadOptions();
        Container panel = getContentPane();
        panel.add(new JLabel("Player name"));
        Set<String> nameChoices = new TreeSet<String>();
        nameChoices.add(playerName);
        nameChoices.add(Constants.username);
        nameBox = new JComboBox(new Vector<String>(nameChoices));
        nameBox.setEditable(true);
        nameBox.addActionListener(this);
        nameBox.setSelectedItem(playerName);
        panel.add(nameBox);
        panel.add(new JLabel("Server hostname"));
        Set<String> hostChoices = new TreeSet<String>();
        String preferred = initServerNames(hostname, hostChoices, netclientOptions);
        this.hostname = preferred;
        hostBox = new JComboBox(new Vector<String>(hostChoices));
        hostBox.setEditable(true);
        hostBox.setSelectedItem(preferred);
        hostBox.addActionListener(this);
        panel.add(hostBox);
        panel.add(new JLabel("Server port"));
        Set<String> portChoices = new TreeSet<String>();
        portChoices.add("" + port);
        portChoices.add("" + Constants.defaultPort);
        portBox = new JComboBox(portChoices.toArray(new String[portChoices.size()]));
        portBox.setEditable(true);
        portBox.addActionListener(this);
        panel.add(portBox);
        JButton goButton = new JButton("Go");
        goButton.addActionListener(this);
        panel.add(goButton);
        JButton quitButton = new JButton(Constants.quitGame);
        quitButton.addActionListener(this);
        panel.add(quitButton);
        addWindowListener(this);
        pack();
        saveWindow = new SaveWindow(netclientOptions, "StartClient");
        saveWindow.restoreOrCenter(this);
        setVisible(true);
    }

    public static String initServerNames(String wantedHost, Set<String> hostChoices, Options netclientOptions) {
        String preferred = null;
        try {
            InetAddress ia = InetAddress.getLocalHost();
            String hostAddr = ia.getHostAddress();
            if (hostAddr != null) {
                hostChoices.add(hostAddr);
                preferred = ia.getHostAddress();
            }
            String hostName = ia.getHostName();
            if (hostName != null) {
                hostChoices.add(hostName);
                preferred = ia.getHostName();
            }
        } catch (UnknownHostException ex) {
            LOGGER.log(Level.SEVERE, "Can not resolve host", ex);
        }
        for (int i = Constants.numSavedServerNames - 1; i >= 0; i--) {
            String serverName = netclientOptions.getStringOption(Options.serverName + i);
            if (serverName != null) {
                hostChoices.add(serverName);
                preferred = serverName;
            }
        }
        if (wantedHost != null && !wantedHost.equals("")) {
            hostChoices.add(wantedHost);
            preferred = wantedHost;
        } else if (preferred == null) {
            preferred = "localhost";
        } else {
        }
        return preferred;
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(350, 200);
    }

    @Override
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(Constants.quitGame)) {
            startObject.setWhatToDoNext(Start.QuitAll);
            dispose();
        } else if (e.getActionCommand().equals("Go")) {
            doRunNetClient();
        } else {
            Object source = e.getSource();
            if (source == nameBox) {
                playerName = (String) nameBox.getSelectedItem();
            } else if (source == hostBox) {
                hostname = (String) hostBox.getSelectedItem();
            } else if (source == portBox) {
                port = Integer.parseInt((String) portBox.getSelectedItem());
            }
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        startObject.setWhatToDoNext(Start.GetPlayersDialog);
        dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
        synchronized (mutex) {
            mutex.notify();
        }
    }

    private void doRunNetClient() {
        stOptions.setOption(Options.runClientPlayer, playerName);
        stOptions.setOption(Options.runClientHost, hostname);
        stOptions.setOption(Options.runClientPort, port);
        saveHostname(netclientOptions);
        saveWindow.save(this);
        netclientOptions.saveOptions();
        startObject.setWhatToDoNext(Start.StartNetClient);
        dispose();
    }

    /** 
     *  Put the chosen hostname as first to the LRU sorted list
     *  in NetClient cf file. 
     */
    private void saveHostname(Options netclientOptions) {
        if (netclientOptions == null) {
            return;
        }
        List<String> names = new ArrayList<String>();
        names.add(hostname);
        for (int i = 0; i < Constants.numSavedServerNames; i++) {
            String serverName = netclientOptions.getStringOption(Options.serverName + i);
            if (serverName != null) {
                if (!serverName.equals(hostname)) {
                    names.add(serverName);
                }
            }
        }
        for (int i = 0; i < names.size() && i < Constants.numSavedServerNames; i++) {
            netclientOptions.setOption(Options.serverName + i, names.get(i));
        }
    }
}
