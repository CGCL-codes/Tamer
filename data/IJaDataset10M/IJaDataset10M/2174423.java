package blue.tools.blueShare.effects;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.apache.xmlrpc.XmlRpcException;
import blue.BlueSystem;
import blue.mixer.Effect;
import blue.ui.core.mixer.EffectsLibrary;
import blue.tools.blueShare.BlueShareRemoteCaller;
import electric.xml.ParseException;

public class EffectImportPane extends JComponent {

    private String AVAILABLE_INSTRUMENTS_LABEL = BlueSystem.getString("blueShare.effect.availableEffects");

    JSplitPane mainSplitPane = new JSplitPane();

    JPanel categoriesPanel = new JPanel();

    JSplitPane rightSplitPane = new JSplitPane();

    JPanel iListPanel = new JPanel();

    JLabel iOptionsLabel = new JLabel();

    JPanel instrumentInfoPanel = new JPanel();

    JPanel instrumentTopPanel = new JPanel();

    JLabel iNameLabel = new JLabel();

    JLabel submittedByLabel = new JLabel();

    JTextField iNameText = new JTextField();

    JTextField userText = new JTextField();

    JTextArea iDescription = new JTextArea();

    JPanel importButtonPanel = new JPanel();

    JButton importButton = new JButton();

    JTree categoryTree = new JTree();

    JTable instrumentTable = new JTable();

    EffectOptionTableModel iTableModel = new EffectOptionTableModel();

    JPanel instrumentPanel = new JPanel();

    CardLayout cards = new CardLayout();

    JPanel instrCardPanel = new JPanel();

    CardLayout iListCard = new CardLayout();

    public EffectImportPane() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(new BorderLayout());
        categoriesPanel.setLayout(new BorderLayout());
        JLabel categoriesLabel = new JLabel(BlueSystem.getString("common.categories"));
        rightSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        iListPanel.setLayout(new BorderLayout());
        iOptionsLabel.setText(AVAILABLE_INSTRUMENTS_LABEL);
        instrumentInfoPanel.setLayout(new BorderLayout());
        instrumentTopPanel.setLayout(new GridBagLayout());
        iNameLabel.setText(BlueSystem.getString("blueShare.effect.effectNameLabel"));
        submittedByLabel.setText(BlueSystem.getString("blueShare.submittedByLabel"));
        instrumentInfoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        iListPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        categoriesPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        importButton.setText(BlueSystem.getString("blueShare.effect.importEffect"));
        JScrollPane categoryScrollPane = new JScrollPane(categoryTree);
        this.add(mainSplitPane, BorderLayout.CENTER);
        mainSplitPane.add(categoriesPanel, JSplitPane.LEFT);
        categoriesPanel.add(categoryScrollPane, BorderLayout.CENTER);
        categoriesPanel.add(categoriesLabel, BorderLayout.NORTH);
        mainSplitPane.add(rightSplitPane, JSplitPane.RIGHT);
        rightSplitPane.add(iListPanel, JSplitPane.TOP);
        JLabel tempLabel = new JLabel(BlueSystem.getString("blueShare.effect.noEffectsForCategory"));
        tempLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JScrollPane instrScrollPane = new JScrollPane(instrumentTable);
        instrCardPanel.setLayout(iListCard);
        instrCardPanel.add(instrScrollPane, "iList");
        instrCardPanel.add(tempLabel, "none");
        iListPanel.add(instrCardPanel, BorderLayout.CENTER);
        iListPanel.add(iOptionsLabel, BorderLayout.NORTH);
        instrumentPanel.setLayout(cards);
        instrumentPanel.add(instrumentInfoPanel, "instrumentInfo");
        tempLabel = new JLabel(BlueSystem.getString("blueShare.effect.selectEffect"));
        tempLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instrumentPanel.add(tempLabel, "blank");
        setInstrumentEnabled(false);
        rightSplitPane.add(instrumentPanel, JSplitPane.BOTTOM);
        instrumentInfoPanel.add(instrumentTopPanel, BorderLayout.NORTH);
        instrumentTopPanel.add(iNameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 3, 3), 0, 0));
        instrumentTopPanel.add(submittedByLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 3, 3), 0, 0));
        instrumentTopPanel.add(userText, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 3, 1), 0, 0));
        JScrollPane descriptionScroll = new JScrollPane();
        instrumentInfoPanel.add(descriptionScroll, BorderLayout.CENTER);
        instrumentInfoPanel.add(importButtonPanel, BorderLayout.SOUTH);
        descriptionScroll.getViewport().add(iDescription, null);
        instrumentTopPanel.add(iNameText, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 3, 1), 0, 0));
        importButtonPanel.add(importButton, null);
        instrumentTable.setModel(iTableModel);
        iDescription.setLineWrap(true);
        mainSplitPane.setDividerLocation(200);
        rightSplitPane.setDividerLocation(200);
        instrumentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryTree.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                int row = categoryTree.getClosestRowForLocation(e.getX(), e.getY());
                TreePath path = categoryTree.getClosestPathForLocation(e.getX(), e.getY());
                if (row == -1) {
                    return;
                }
                try {
                    DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                    BlueShareEffectCategory tempCat = (BlueShareEffectCategory) tempNode.getUserObject();
                    populateInstruments(tempCat);
                } catch (ClassCastException cce) {
                }
            }
        });
        instrumentTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                populateInstrument();
            }
        });
        importButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                importEffect();
            }
        });
    }

    private void populateInstruments(BlueShareEffectCategory category) {
        iOptionsLabel.setText(AVAILABLE_INSTRUMENTS_LABEL + " - " + BlueSystem.getString("blueShare.loading"));
        try {
            EffectOption[] options;
            if (category.getCategoryId() == Integer.MIN_VALUE) {
                options = BlueShareRemoteCaller.getLatestTenEffects();
            } else {
                options = BlueShareRemoteCaller.getEffectOptions(category);
            }
            iTableModel.setInstrumentOptions(options);
            setInstrumentEnabled(false);
            if (options.length > 0) {
                iListCard.show(instrCardPanel, "iList");
            } else {
                iListCard.show(instrCardPanel, "none");
            }
            iOptionsLabel.setText(AVAILABLE_INSTRUMENTS_LABEL + " - " + category.getName());
        } catch (ParseException pe) {
            String error = BlueSystem.getString("blueShare.selectServer.couldNotReadResponse");
            JOptionPane.showMessageDialog(null, error, BlueSystem.getString("message.error"), JOptionPane.ERROR_MESSAGE);
            iTableModel.setInstrumentOptions(null);
            iOptionsLabel.setText(AVAILABLE_INSTRUMENTS_LABEL + " - " + BlueSystem.getString("message.error"));
            return;
        } catch (XmlRpcException xre) {
            String error = "Error: " + xre.getLocalizedMessage();
            JOptionPane.showMessageDialog(null, error, BlueSystem.getString("message.error"), JOptionPane.ERROR_MESSAGE);
            iTableModel.setInstrumentOptions(null);
            iOptionsLabel.setText(AVAILABLE_INSTRUMENTS_LABEL + " - " + BlueSystem.getString("message.error"));
            return;
        } catch (IOException ioe) {
            String error = "Error: " + ioe.getLocalizedMessage();
            JOptionPane.showMessageDialog(null, error, BlueSystem.getString("message.error"), JOptionPane.ERROR_MESSAGE);
            iTableModel.setInstrumentOptions(null);
            iOptionsLabel.setText(AVAILABLE_INSTRUMENTS_LABEL + " - " + BlueSystem.getString("message.error"));
            return;
        }
    }

    private void populateInstrument() {
        EffectOption iOption = iTableModel.getInstrumentOption(instrumentTable.getSelectedRow());
        if (iOption != null) {
            iNameText.setText(iOption.getName());
            userText.setText(iOption.getScreenName());
            iDescription.setText(iOption.getDescription());
        }
        setInstrumentEnabled(true);
    }

    private void setInstrumentEnabled(boolean val) {
        if (val) {
            cards.show(instrumentPanel, "instrumentInfo");
        } else {
            cards.show(instrumentPanel, "blank");
        }
    }

    private void importEffect() {
        EffectOption effectOption = iTableModel.getInstrumentOption(instrumentTable.getSelectedRow());
        if (effectOption == null) {
            return;
        }
        try {
            Effect effect = BlueShareRemoteCaller.getEffect(effectOption);
            if (effect == null) {
                String error = BlueSystem.getString("blueShare.effect.importError");
                JOptionPane.showMessageDialog(null, error, BlueSystem.getString("message.error"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            EffectsLibrary library = EffectsLibrary.getInstance();
            library.importEffect(effect);
            library.save();
            String message = BlueSystem.getString("blueShare.effect.importSuccess");
            JOptionPane.showMessageDialog(null, message, BlueSystem.getString("common.success"), JOptionPane.PLAIN_MESSAGE);
        } catch (ParseException pe) {
            String error = BlueSystem.getString("blueShare.selectServer.couldNotReadResponse");
            JOptionPane.showMessageDialog(null, error, BlueSystem.getString("message.error"), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (XmlRpcException xre) {
            String error = "Error: " + xre.getLocalizedMessage();
            JOptionPane.showMessageDialog(null, error, BlueSystem.getString("message.error"), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IOException ioe) {
            String error = "Error: " + ioe.getLocalizedMessage();
            JOptionPane.showMessageDialog(null, error, BlueSystem.getString("message.error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    /**
     * Passes in the categories retrieved from the server using
     * BlueShareRemoteCaller
     */
    public void setCategories(BlueShareEffectCategory[] categories) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(BlueSystem.getString("common.categories"));
        BlueShareEffectCategory latestInstruments = new BlueShareEffectCategory(Integer.MIN_VALUE, ">Latest Effects", "Newest Effects in Repository", null);
        DefaultMutableTreeNode temp = new DefaultMutableTreeNode(latestInstruments);
        root.add(temp);
        addSubCategories(root, categories);
        categoryTree.setModel(new DefaultTreeModel(root));
    }

    private void addSubCategories(DefaultMutableTreeNode parent, BlueShareEffectCategory[] categories) {
        DefaultMutableTreeNode temp;
        for (int i = 0; i < categories.length; i++) {
            temp = new DefaultMutableTreeNode(categories[i]);
            parent.add(temp);
            addSubCategories(temp, categories[i].getSubCategories());
        }
    }

    public static void main(String[] args) {
        EffectImportPane instrumentImportPane1 = new EffectImportPane();
        blue.utility.GUI.showComponentAsStandalone(instrumentImportPane1, "Instrument Import Pane Test", true);
    }
}
