package ogv.gui.dialogs;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import ogv.OGV;
import ogv.data.Group;
import ogv.data.Race;
import ogv.util.ConfigNode;
import ogv.util.SwingUtils;

public class GiftDialog {

    private final ConfigNode config = OGV.getConfig().subnode("Dialog.GiftGroups");

    private JPanel panel;

    private final JComboBox races = new JComboBox();

    private final JSpinner ships = new JSpinner();

    private GiftDialog(Component parent, List<Group> groups) {
        initGUI();
        int count = 0;
        for (Group g : groups) count += g.getSize();
        SpinnerNumberModel model = new SpinnerNumberModel(count, 1, count, 1);
        ships.setModel(model);
        ships.setEnabled(groups.size() == 1 && count > 1);
        for (Race race : OGV.getGame().getRaces()) if (!race.isRip() && !race.isYour()) races.addItem(race.getName());
        if (!SwingUtils.okCancelDialog(parent, panel, config.getString(SwingUtils.TITLE), races)) return;
        String selectedRace = (String) races.getSelectedItem();
        if (selectedRace.isEmpty()) return;
        if (groups.size() == 1) OGV.getOrder().giftGroup(groups.get(0), selectedRace, model.getNumber().intValue()); else {
            OGV.getOrder().beginGroup();
            try {
                for (Group g : groups) OGV.getOrder().giftGroup(g, selectedRace, g.getSize());
            } finally {
                OGV.getOrder().endGroup();
            }
        }
        OGV.updateAll();
    }

    private void initGUI() {
        panel = new JPanel(new GridBagLayout());
        SwingUtils.addRow(panel, SwingUtils.makeLabelFor(races, config.subnode("labels.To")), races);
        SwingUtils.addRow(panel, SwingUtils.makeLabelFor(ships, config.subnode("labels.Ships")), ships);
    }

    public static void showDialog(Component parent, List<Group> groups) {
        new GiftDialog(parent, groups);
    }
}
