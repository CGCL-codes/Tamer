package de.spindler.yahtzee.server.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import de.spindler.yahtzee.common.preferences.PreferenceStore;
import de.spindler.yahtzee.common.ui.preferences.AbstractPreferencesDetailPage;
import de.spindler.yahtzee.server.GameServer;

public class ServerInfoDetailPage extends AbstractPreferencesDetailPage {

    private static final long serialVersionUID = -5449151931660888967L;

    private JTextField txtServerName;

    private JTextArea txtServerDesc;

    public ServerInfoDetailPage(PreferenceStore preferenceStore) {
        super(Messages.ADMIN_FRAME_BUTTONBAR_SERVER, preferenceStore);
    }

    @Override
    protected void createContents(JPanel container) {
        GridBagLayout gbl_container = new GridBagLayout();
        gbl_container.columnWidths = new int[] { 0, 0, 0 };
        gbl_container.rowHeights = new int[] { 0, 0, 0 };
        gbl_container.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gbl_container.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        container.setLayout(gbl_container);
        GridBagConstraints gbc_lblName = new GridBagConstraints();
        gbc_lblName.anchor = GridBagConstraints.EAST;
        gbc_lblName.gridx = 0;
        gbc_lblName.gridy = 0;
        gbc_lblName.insets = new Insets(5, 15, 5, 5);
        JLabel lblServerName = new JLabel("Name");
        container.add(lblServerName, gbc_lblName);
        GridBagConstraints gbc_txtName = new GridBagConstraints();
        gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtName.gridx = 1;
        gbc_txtName.gridy = 0;
        gbc_txtName.insets = new Insets(5, 0, 5, 10);
        txtServerName = new JTextField();
        container.add(txtServerName, gbc_txtName);
        GridBagConstraints gbc_lblDesc = new GridBagConstraints();
        gbc_lblDesc.anchor = GridBagConstraints.NORTH;
        gbc_lblDesc.insets = new Insets(5, 15, 0, 5);
        gbc_lblDesc.gridx = 0;
        gbc_lblDesc.gridy = 1;
        gbc_lblDesc.insets = new Insets(3, 15, 0, 5);
        JLabel lblServerDesc = new JLabel("Beschreibung");
        container.add(lblServerDesc, gbc_lblDesc);
        GridBagConstraints gbc_txtDesc = new GridBagConstraints();
        gbc_txtDesc.insets = new Insets(0, 0, 10, 10);
        gbc_txtDesc.fill = GridBagConstraints.BOTH;
        gbc_txtDesc.gridx = 1;
        gbc_txtDesc.gridy = 1;
        txtServerDesc = new JTextArea();
        txtServerDesc.setLineWrap(true);
        txtServerDesc.setWrapStyleWord(true);
        txtServerDesc.setFont(txtServerName.getFont());
        txtServerDesc.setBorder(txtServerName.getBorder());
        container.add(txtServerDesc, gbc_txtDesc);
    }

    @Override
    protected void store(PreferenceStore preferenceStore) {
        boolean changed = false;
        String newName = txtServerName.getText();
        String name = preferenceStore.get(GameServer.PREF_FILE_SERVER, GameServer.KEY_SERVER_NAME);
        if (!newName.equals(name)) {
            preferenceStore.put(GameServer.PREF_FILE_SERVER, GameServer.KEY_SERVER_NAME, newName);
            changed = true;
        }
        String newDesc = txtServerDesc.getText();
        String desc = preferenceStore.get(GameServer.PREF_FILE_SERVER, GameServer.KEY_SERVER_DESC);
        if (!newDesc.equals(desc)) {
            preferenceStore.put(GameServer.PREF_FILE_SERVER, GameServer.KEY_SERVER_DESC, newDesc);
            changed = true;
        }
        if (changed) preferenceStore.store(GameServer.PREF_FILE_SERVER);
    }

    @Override
    protected void setInitialValues(PreferenceStore preferenceStore) {
        String name = preferenceStore.get(GameServer.PREF_FILE_SERVER, GameServer.KEY_SERVER_NAME);
        txtServerName.setText(name);
        String desc = preferenceStore.get(GameServer.PREF_FILE_SERVER, GameServer.KEY_SERVER_DESC);
        txtServerDesc.setText(desc);
    }
}
