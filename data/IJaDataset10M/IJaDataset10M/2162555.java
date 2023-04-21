package ogv.gui.dialogs;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import ogv.OGV;
import ogv.gui.dialogs.options.*;
import ogv.util.ConfigNode;
import ogv.util.SwingUtils;

public class Options implements TreeSelectionListener {

    private ConfigNode config = OGV.getConfig().subnode("Dialog.Options");

    private final JPanel panel = new JPanel(new BorderLayout());

    private final CardLayout cardLayout = new CardLayout();

    private final JPanel cardPanel = new JPanel(cardLayout);

    private JTree tree;

    private final GeneralOptionsPage generalOptions;

    private final TablesOptionsPage tablesOptions;

    private final MapOptionsPage mapOptions;

    private final GameOptionsPage gameOptions;

    private final RaceColorsPage raceColors;

    private final List<OptionsPage> pages = new ArrayList<OptionsPage>();

    private JDialog dialog;

    private Options() {
        pages.add(generalOptions = new GeneralOptionsPage());
        pages.add(tablesOptions = new TablesOptionsPage());
        pages.add(mapOptions = new MapOptionsPage());
        if (OGV.getGame() != null) {
            pages.add(gameOptions = new GameOptionsPage());
            pages.add(raceColors = new RaceColorsPage());
        } else {
            gameOptions = null;
            raceColors = null;
        }
        initGUI();
    }

    private void initGUI() {
        cardPanel.add(new JPanel(), "");
        panel.add(cardPanel, BorderLayout.CENTER);
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Settings");
        createNodes(top);
        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(this);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(false);
        panel.add(tree, BorderLayout.LINE_START);
        for (int i = 0; i < tree.getRowCount(); ++i) tree.expandRow(i);
        tree.setSelectionRow(1);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) return;
        if (node.isLeaf()) cardLayout.show(cardPanel, node.getUserObject().toString()); else cardLayout.show(cardPanel, "");
    }

    private void createNodes(DefaultMutableTreeNode top) {
        if (OGV.getGame() != null) {
            DefaultMutableTreeNode category = addCategory(top, "GameSettings");
            addPage(category, "Game", gameOptions);
            addPage(category, "RaceColors", raceColors);
        }
        DefaultMutableTreeNode category = addCategory(top, "OGVSettings");
        addPage(category, "General", generalOptions);
        addPage(category, "Tables", tablesOptions);
        addPage(category, "Map", mapOptions);
    }

    private DefaultMutableTreeNode addCategory(DefaultMutableTreeNode top, String name) {
        DefaultMutableTreeNode category = new DefaultMutableTreeNode(config.subnode("pages").subnode(name).getString(SwingUtils.TITLE, name));
        top.add(category);
        return category;
    }

    private void addPage(DefaultMutableTreeNode category, String name, Component comp) {
        name = config.subnode("pages").subnode(name).getString(SwingUtils.TITLE, name);
        category.add(new DefaultMutableTreeNode(name, false));
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(name));
        panel.add(comp);
        cardPanel.add(panel, name);
    }

    private void reset() {
        for (OptionsPage op : pages) op.reset();
    }

    private void apply() {
        for (OptionsPage op : pages) op.apply();
        OGV.updateAll();
        if (!OGV.isApplet()) OGV.saveConfig();
    }

    private void showDialog2() {
        reset();
        ConfigNode node = config.subnode("buttons");
        String ok = node.subnode("OK").getString(SwingUtils.NAME, "OK");
        String cancel = node.subnode("Cancel").getString(SwingUtils.NAME, "Cancel");
        JButton applyButton = SwingUtils.makeButton(node.subnode("Apply"));
        applyButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                apply();
                SwingUtilities.updateComponentTreeUI(dialog);
                dialog.repaint();
            }
        });
        JButton resetButton = SwingUtils.makeButton(node.subnode("Reset"));
        resetButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
                dialog.repaint();
            }
        });
        Object[] options = { ok, cancel, applyButton, resetButton };
        JOptionPane pane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, options);
        dialog = pane.createDialog(OGV.getMainFrame(), config.getString(SwingUtils.TITLE));
        dialog.setVisible(true);
        if (pane.getValue() == ok) apply();
    }

    public static void showDialog() {
        new Options().showDialog2();
    }
}
