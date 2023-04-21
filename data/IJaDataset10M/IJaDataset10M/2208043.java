package phex.gui.dialogs.options;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import phex.gui.common.GUIRegistry;
import phex.gui.common.GUIUtils;
import phex.gui.common.IntegerTextField;
import phex.prefs.core.ConnectionPrefs;
import phex.prefs.core.NetworkPrefs;
import phex.servent.Servent;
import phex.utils.Localizer;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class NetworkPane extends OptionsSettingsPane {

    private static final String LISTENING_PORT_KEY = "ListeningPort";

    private static final String LISTENING_PORT_EXCESS_ERROR_KEY = "ListeningPortExcessErrorKey";

    private static final String CONNECTION_TIMEOUT_KEY = "ConnectionTimeout";

    private static final String UP_2_UP_CONNECTIONS_KEY = "UP2UPConnections";

    private static final String UP_2_LEAF_CONNECTIONS_KEY = "UP2LeafConnections";

    private static final String LEAF_2_UP_CONNECTIONS_KEY = "Leaf2UPConnections";

    private static final String LEAF_2_UP_CONNECTIONS_EXCESS_ERROR_KEY = "Leaf2UPConnectionsExcessErrorKey";

    private static final String MAX_CONCURRENT_CONNECT_ATTEMPTS_KEY = "MaxConcurrentConnectAttempts";

    private IntegerTextField listeningPortTF;

    private IntegerTextField connectionTimeoutTF;

    private IntegerTextField maxConcurrentConnectAttemptsTF;

    private JCheckBox autoConnectChkbx;

    private JCheckBox connectedToLANChkbx;

    private JCheckBox allowToBeUPChkbx;

    private JCheckBox forceToBeUPChkbx;

    private JLabel up2upConnectionsLabel;

    private IntegerTextField up2upConnectionsTF;

    private JLabel up2LeafConnectionsLabel;

    private IntegerTextField up2LeafConnectionsTF;

    private JLabel leaf2upConnectionsLabel;

    private IntegerTextField leaf2upConnectionsTF;

    public NetworkPane() {
        super("Network");
    }

    /**
     * Called when preparing this settings pane for display the first time. Can
     * be overriden to implement the look of the settings pane.
     */
    protected void prepareComponent() {
        setLayout(new BorderLayout());
        JPanel contentPanel = new JPanel();
        add(contentPanel, BorderLayout.CENTER);
        FormLayout layout = new FormLayout("10dlu, right:d, 2dlu, d, " + "10dlu, right:d, 2dlu, d, 2dlu:grow", "p, 3dlu, p, 3dlu, p, 3dlu, p, 8dlu, " + "p, 3dlu, p, 3dlu, p, 8dlu, " + "p, 3dlu, p");
        layout.setColumnGroups(new int[][] { { 4, 8 }, { 2, 6 } });
        layout.setRowGroups(new int[][] { { 3, 5, 9, 11, 13, 17 } });
        PanelBuilder builder = new PanelBuilder(layout, contentPanel);
        CellConstraints cc = new CellConstraints();
        int row = 1;
        builder.addSeparator(Localizer.getString("NetworkSettings"), cc.xywh(1, row, 9, 1));
        row += 2;
        builder.addLabel(Localizer.getString("ListeningPort") + ": ", cc.xy(2, row));
        listeningPortTF = new IntegerTextField(NetworkPrefs.ListeningPort.get().toString(), 5, 5);
        builder.add(listeningPortTF, cc.xy(4, row));
        builder.addLabel(Localizer.getString("ConnectionTimeout") + ": ", cc.xy(6, row));
        connectionTimeoutTF = new IntegerTextField(String.valueOf(NetworkPrefs.TcpConnectTimeout.get().intValue() / 1000), 3, 3);
        builder.add(connectionTimeoutTF, cc.xy(8, row));
        row += 2;
        JLabel label = builder.addLabel(Localizer.getString("NetworkSettings_MaxConcurrentConnectAttempts") + ": ", cc.xy(6, row));
        label.setToolTipText(Localizer.getString("NetworkSettings_TTTMaxConcurrentConnectAttempts"));
        maxConcurrentConnectAttemptsTF = new IntegerTextField(NetworkPrefs.MaxConcurrentConnectAttempts.get().toString(), 2, 2);
        maxConcurrentConnectAttemptsTF.setToolTipText(Localizer.getString("NetworkSettings_TTTMaxConcurrentConnectAttempts"));
        builder.add(maxConcurrentConnectAttemptsTF, cc.xy(8, row));
        row += 2;
        autoConnectChkbx = new JCheckBox(Localizer.getString("AutoConnectOnStartup"), ConnectionPrefs.AutoConnectOnStartup.get().booleanValue());
        builder.add(autoConnectChkbx, cc.xywh(2, row, 3, 1));
        connectedToLANChkbx = new JCheckBox(Localizer.getString("ConnectedToLAN"), NetworkPrefs.ConnectedToLAN.get().booleanValue());
        connectedToLANChkbx.setToolTipText(Localizer.getString("TTTConnectedToLAN"));
        builder.add(connectedToLANChkbx, cc.xywh(6, row, 3, 1));
        row += 2;
        builder.addSeparator(Localizer.getString("UltrapeerSettings"), cc.xywh(1, row, 9, 1));
        row += 2;
        allowToBeUPChkbx = new JCheckBox(Localizer.getString("AllowToBecomeUltrapeer"), ConnectionPrefs.AllowToBecomeUP.get().booleanValue());
        allowToBeUPChkbx.setToolTipText(Localizer.getString("TTTAllowToBecomeUltrapeer"));
        allowToBeUPChkbx.addActionListener(new CheckboxActionListener());
        builder.add(allowToBeUPChkbx, cc.xywh(2, row, 3, 1));
        forceToBeUPChkbx = new JCheckBox(Localizer.getString("ForceToBeUltrapeer"), ConnectionPrefs.ForceToBeUltrapeer.get().booleanValue());
        forceToBeUPChkbx.setToolTipText(Localizer.getString("TTTForceToBeUltrapeer"));
        forceToBeUPChkbx.addActionListener(new CheckboxActionListener());
        builder.add(forceToBeUPChkbx, cc.xywh(6, row, 3, 1));
        row += 2;
        up2upConnectionsLabel = builder.addLabel(Localizer.getString("ConnectionsToUltrapeers") + ": ", cc.xy(2, row));
        up2upConnectionsLabel.setToolTipText(Localizer.getString("TTTToUltrapeers"));
        up2upConnectionsTF = new IntegerTextField(ConnectionPrefs.Up2UpConnections.get().toString(), 2, 2);
        up2upConnectionsTF.setToolTipText(Localizer.getString("TTTToUltrapeers"));
        builder.add(up2upConnectionsTF, cc.xy(4, row));
        up2LeafConnectionsLabel = builder.addLabel(Localizer.getString("ConnectionsToLeafs") + ": ", cc.xy(6, row));
        up2LeafConnectionsLabel.setToolTipText(Localizer.getString("TTTToLeafs"));
        up2LeafConnectionsTF = new IntegerTextField(ConnectionPrefs.Up2LeafConnections.get().toString(), 3, 3);
        up2LeafConnectionsTF.setToolTipText(Localizer.getString("TTTToLeafs"));
        builder.add(up2LeafConnectionsTF, cc.xy(8, row));
        row += 2;
        builder.addSeparator(Localizer.getString("LeafSettings"), cc.xywh(1, row, 9, 1));
        row += 2;
        leaf2upConnectionsLabel = builder.addLabel(Localizer.getString("ConnectionsToUltrapeers") + ": ", cc.xy(2, row));
        leaf2upConnectionsLabel.setToolTipText(Localizer.getString("TTTToUltrapeers"));
        leaf2upConnectionsTF = new IntegerTextField(ConnectionPrefs.Leaf2UpConnections.get().toString(), 2, 1);
        leaf2upConnectionsTF.setToolTipText(Localizer.getString("TTTToUltrapeers"));
        builder.add(leaf2upConnectionsTF, cc.xy(4, row));
        refreshEnableState();
    }

    /**
     * Override this method if you like to verify inputs before storing them.
     * A input dictionary is given to the pane. It can be used to store values
     * like error flags or prepared values for saving. The dictionary is given
     * to every settings pane checkInput(), displayErrorMessage() and
     * saveAndApplyChanges() method.
     * When the input has been flaged as invalid with the method setInputValid()
     * the method displayErrorMessage() is called directly after return of
     * checkInput() and the focus is given to settings pane.
     * After checking all settings pane without any error the method
     * saveAndApplyChanges() is called for all settings panes to save the
     * changes.
     */
    public void checkInput(HashMap inputDic) {
        try {
            String listeningPortStr = listeningPortTF.getText();
            Integer listeningPort = Integer.valueOf(listeningPortStr);
            if (listeningPort.intValue() > 65500) {
                inputDic.put(LISTENING_PORT_EXCESS_ERROR_KEY, listeningPortTF);
                setInputValid(inputDic, false);
                return;
            }
            inputDic.put(LISTENING_PORT_KEY, listeningPort);
        } catch (NumberFormatException exp) {
            inputDic.put(NUMBER_FORMAT_ERROR_KEY, listeningPortTF);
            setInputValid(inputDic, false);
            return;
        }
        try {
            String connTimeoutStr = connectionTimeoutTF.getText();
            Integer connTimeout = Integer.valueOf(connTimeoutStr);
            inputDic.put(CONNECTION_TIMEOUT_KEY, connTimeout);
        } catch (NumberFormatException exp) {
            inputDic.put(NUMBER_FORMAT_ERROR_KEY, connectionTimeoutTF);
            setInputValid(inputDic, false);
            return;
        }
        try {
            String up2upConnStr = up2upConnectionsTF.getText();
            Integer up2upConn = Integer.valueOf(up2upConnStr);
            inputDic.put(UP_2_UP_CONNECTIONS_KEY, up2upConn);
        } catch (NumberFormatException exp) {
            inputDic.put(NUMBER_FORMAT_ERROR_KEY, up2upConnectionsTF);
            setInputValid(inputDic, false);
            return;
        }
        try {
            String up2leafConnStr = up2LeafConnectionsTF.getText();
            Integer up2leafConn = Integer.valueOf(up2leafConnStr);
            inputDic.put(UP_2_LEAF_CONNECTIONS_KEY, up2leafConn);
        } catch (NumberFormatException exp) {
            inputDic.put(NUMBER_FORMAT_ERROR_KEY, up2LeafConnectionsTF);
            setInputValid(inputDic, false);
            return;
        }
        try {
            String leaf2upConnStr = leaf2upConnectionsTF.getText();
            Integer leaf2upConn = Integer.valueOf(leaf2upConnStr);
            if (leaf2upConn.compareTo(ConnectionPrefs.Leaf2UpConnections.max()) > 0) {
                inputDic.put(LEAF_2_UP_CONNECTIONS_EXCESS_ERROR_KEY, leaf2upConnectionsTF);
                setInputValid(inputDic, false);
                return;
            }
            inputDic.put(LEAF_2_UP_CONNECTIONS_KEY, leaf2upConn);
        } catch (NumberFormatException exp) {
            inputDic.put(NUMBER_FORMAT_ERROR_KEY, leaf2upConnectionsTF);
            setInputValid(inputDic, false);
            return;
        }
        try {
            String maxConcurrentConnectAttemptsStr = maxConcurrentConnectAttemptsTF.getText();
            Integer maxConcurrentConnectAttempts = Integer.valueOf(maxConcurrentConnectAttemptsStr);
            inputDic.put(MAX_CONCURRENT_CONNECT_ATTEMPTS_KEY, maxConcurrentConnectAttempts);
        } catch (NumberFormatException exp) {
            inputDic.put(NUMBER_FORMAT_ERROR_KEY, maxConcurrentConnectAttemptsTF);
            setInputValid(inputDic, false);
            return;
        }
        setInputValid(inputDic, true);
    }

    /**
     * When isInputValid() returns a false this method is called.
     * The input dictionary should contain the settings pane specific information
     * of the error.
     * The settings pane should override this method to display a error
     * message. Before calling the method the focus is given to the
     * settings pane.
     */
    public void displayErrorMessage(HashMap inputDic) {
        if (inputDic.containsKey(NUMBER_FORMAT_ERROR_KEY)) {
            displayNumberFormatError(inputDic);
        } else if (inputDic.containsKey(LEAF_2_UP_CONNECTIONS_EXCESS_ERROR_KEY)) {
            leaf2upConnectionsTF.setText(ConnectionPrefs.Leaf2UpConnections.max().toString());
            leaf2upConnectionsTF.requestFocus();
            leaf2upConnectionsTF.selectAll();
            JOptionPane.showMessageDialog(this, Localizer.getFormatedString("NetworkSettings_Leaf2UpLimitExceeded", new Object[] { ConnectionPrefs.Leaf2UpConnections.max() }), Localizer.getString("NetworkSettings_LimitExceeded"), JOptionPane.ERROR_MESSAGE);
        } else if (inputDic.containsKey(LISTENING_PORT_EXCESS_ERROR_KEY)) {
            listeningPortTF.setText(NetworkPrefs.ListeningPort.get().toString());
            listeningPortTF.requestFocus();
            listeningPortTF.selectAll();
            JOptionPane.showMessageDialog(this, Localizer.getString("NetworkSettings_ListeningPortLimitExceeded"), Localizer.getString("NetworkSettings_LimitExceeded"), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Override this method if you like to apply and save changes made on
     * settings pane. To trigger saving of the configuration if any value was
     * changed call triggerConfigSave().
     */
    public void saveAndApplyChanges(HashMap inputDic) {
        Integer listeningPortInt = (Integer) inputDic.get(LISTENING_PORT_KEY);
        int listeningPort = listeningPortInt.intValue();
        if (NetworkPrefs.ListeningPort.get().intValue() != listeningPort) {
            NetworkPrefs.ListeningPort.set(Integer.valueOf(listeningPort));
            try {
                GUIRegistry.getInstance().getServent().restartServer();
            } catch (Exception e) {
                GUIUtils.showErrorMessage(Localizer.getString("FailedToListenOnNewPort"), Localizer.getString("ListenerError"));
            }
        }
        Integer connectionTimeoutInt = (Integer) inputDic.get(CONNECTION_TIMEOUT_KEY);
        int connectionTimeout = connectionTimeoutInt.intValue() * 1000;
        NetworkPrefs.TcpConnectTimeout.set(Integer.valueOf(connectionTimeout));
        boolean autoConnect = autoConnectChkbx.isSelected();
        ConnectionPrefs.AutoConnectOnStartup.set(Boolean.valueOf(autoConnect));
        boolean connectedToLAN = connectedToLANChkbx.isSelected();
        NetworkPrefs.ConnectedToLAN.set(Boolean.valueOf(connectedToLAN));
        boolean allowToBeUP = allowToBeUPChkbx.isSelected();
        ConnectionPrefs.AllowToBecomeUP.set(Boolean.valueOf(allowToBeUP));
        boolean forceToBeUP = forceToBeUPChkbx.isSelected();
        ConnectionPrefs.ForceToBeUltrapeer.set(Boolean.valueOf(forceToBeUP));
        Integer up2upConnectionsInt = (Integer) inputDic.get(UP_2_UP_CONNECTIONS_KEY);
        ConnectionPrefs.Up2UpConnections.set(up2upConnectionsInt);
        Integer up2leafConnectionsInt = (Integer) inputDic.get(UP_2_LEAF_CONNECTIONS_KEY);
        ConnectionPrefs.Up2LeafConnections.set(up2leafConnectionsInt);
        Integer leaf2upConnectionsInt = (Integer) inputDic.get(LEAF_2_UP_CONNECTIONS_KEY);
        ConnectionPrefs.Leaf2UpConnections.set(leaf2upConnectionsInt);
        Integer maxConcurrentConnectAttemptsInt = (Integer) inputDic.get(MAX_CONCURRENT_CONNECT_ATTEMPTS_KEY);
        NetworkPrefs.MaxConcurrentConnectAttempts.set(maxConcurrentConnectAttemptsInt);
    }

    private void refreshEnableState() {
        up2LeafConnectionsLabel.setEnabled(allowToBeUPChkbx.isSelected());
        up2LeafConnectionsTF.setEnabled(allowToBeUPChkbx.isSelected());
        up2upConnectionsLabel.setEnabled(allowToBeUPChkbx.isSelected());
        up2upConnectionsTF.setEnabled(allowToBeUPChkbx.isSelected());
        forceToBeUPChkbx.setEnabled(allowToBeUPChkbx.isSelected());
    }

    class CheckboxActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            refreshEnableState();
        }
    }
}
