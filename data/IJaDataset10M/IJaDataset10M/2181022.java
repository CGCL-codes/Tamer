package ogv.gui.dialogs;

import java.awt.*;
import java.nio.charset.Charset;
import java.util.Vector;
import javax.swing.*;
import ogv.OGV;
import ogv.data.Server;
import ogv.data.controls.GameData;
import ogv.util.ConfigNode;
import ogv.util.SwingUtils;

public class NewGameDialog extends JFrame {

    private ConfigNode config = OGV.getConfig().subnode("Dialog.NewGame");

    private JPanel panel;

    private JComboBox server;

    private final JTextField gameName = new JTextField();

    private final JTextField turn = new JTextField();

    private JComboBox charset;

    private final JTextField gameDir = new JTextField();

    private NewGameDialog() {
        initGUI();
    }

    private void initGUI() {
        panel = new JPanel(new GridBagLayout());
        server = new JComboBox(OGV.getServerList());
        gameName.setEditable(false);
        turn.setEditable(false);
        charset = new JComboBox(new Vector<String>(Charset.availableCharsets().keySet()));
        ConfigNode node = config.subnode("labels");
        SwingUtils.addRow(panel, SwingUtils.makeLabelFor(server, node.subnode("Server")), server);
        SwingUtils.addRow(panel, SwingUtils.makeLabelFor(gameName, node.subnode("GameName")), gameName);
        SwingUtils.addRow(panel, SwingUtils.makeLabelFor(turn, node.subnode("Turn")), turn);
        SwingUtils.addRow(panel, SwingUtils.makeLabelFor(charset, node.subnode("Charset")), charset);
        SwingUtils.addRow(panel, SwingUtils.makeLabelFor(gameDir, node.subnode("Directory")), gameDir);
    }

    private void load(GameData gameData) {
        server.setSelectedItem(gameData.server);
        gameName.setText(gameData.gameName);
        turn.setText(String.valueOf(gameData.turn));
        charset.setSelectedItem(gameData.charset);
        gameDir.setText(gameData.gameDir);
    }

    private void save(GameData gameData) {
        gameData.server = (Server) server.getSelectedItem();
        gameData.charset = (String) charset.getSelectedItem();
        gameData.gameDir = gameDir.getText();
    }

    public static boolean showDialog(GameData gameData) {
        NewGameDialog dialog = new NewGameDialog();
        dialog.load(gameData);
        if (!SwingUtils.okCancelDialog(OGV.getMainFrame(), dialog.panel, dialog.config.getString(SwingUtils.TITLE), dialog.server)) return false;
        dialog.save(gameData);
        return true;
    }
}
