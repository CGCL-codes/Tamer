package gui.actions;

import dht.identifier.identifier.Identifier;
import dht.identifier.identifier.Identifiers;
import dht.network.Network;
import dht.node.DHTDataModel;
import dht.node.DHTDataRow;
import dht.node.DHTNodeModel;
import dht.node.DHTNodeRow;
import gui.main.DHTDataTableModel;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

/**
 * todo write javadoc
 */
public final class GetAction extends AbstractAction {

    private final DHTNodeModel nodeModel;

    private final DHTDataModel dataModel;

    private final JTextField getText;

    private final Network network;

    private final DHTDataTableModel getDataTableModel;

    public GetAction(DHTNodeModel nodeModel, DHTDataModel dataModel, JTextField getText, Network network, DHTDataTableModel getDataTableModel) {
        super("Найти");
        this.nodeModel = nodeModel;
        this.dataModel = dataModel;
        this.getText = getText;
        this.network = network;
        this.getDataTableModel = getDataTableModel;
        putValue(SHORT_DESCRIPTION, "Найти данные");
        putValue(MNEMONIC_KEY, KeyEvent.VK_G);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = getText.getText();
        if (text != null && !text.isEmpty()) {
            getDataTableModel.newData(null);
            String[] parts = text.split(" ");
            for (final String part : parts) {
                if (part.length() > 3) {
                    Identifier partId = Identifiers.createIdentifier(part, 20);
                    for (DHTDataRow row : dataModel.get(partId)) {
                        getDataTableModel.addRow(row);
                    }
                    List<DHTNodeRow> nearestNodes = nodeModel.get(partId);
                    if (nearestNodes.size() > 0) {
                        try {
                            network.sendGetValueRequest(nearestNodes.get(0).getInetAddress(), nearestNodes.get(0).getPort(), Identifiers.createIdentifier(part, 20));
                        } catch (IOException e1) {
                        }
                    }
                }
            }
        }
    }
}
