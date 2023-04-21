package de.fhdarmstadt.fbi.dtree.ui.wizzards;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import de.fhdarmstadt.fbi.dtree.model.Alphabet;
import de.fhdarmstadt.fbi.dtree.ui.components.ComponentFactory;
import de.fhdarmstadt.fbi.dtree.ui.components.ResourceBundleSupport;
import de.fhdarmstadt.fbi.dtree.ui.components.WizzardPanel;
import de.fhdarmstadt.fbi.dtree.xml.ParseResult;

public final class SelectContentsPanel extends WizzardPanel {

    private final class ValidateInputAction extends AbstractAction {

        public ValidateInputAction(final String text, final String tooltip) {
            putValue(NAME, text);
            putValue(SHORT_DESCRIPTION, tooltip);
        }

        /**
     * Invoked when an action occurs.
     */
        public final void actionPerformed(final ActionEvent e) {
            setHasFinish(isValidInput());
        }
    }

    private final class ListSelectionHandler implements ListSelectionListener {

        public final void valueChanged(final ListSelectionEvent e) {
            setHasFinish(isValidInput());
        }
    }

    private JTextArea alphabetWarning;

    private JCheckBox cbxTree;

    private JList lstTestData;

    private LoadWizzardData wizzardData;

    private DefaultListModel listData;

    private JCheckBox cbxClearData;

    public SelectContentsPanel(final LoadWizzardData wizzardData) {
        this.wizzardData = wizzardData;
        this.listData = new DefaultListModel();
        setHasFinish(true);
        setHasPrev(false);
        setTitle(createTitle());
        setPane(createContent());
    }

    private JPanel createTitle() {
        final JPanel title = new JPanel();
        title.setLayout(new BorderLayout());
        title.setBackground(UIManager.getColor("controlShadow"));
        title.add(ComponentFactory.createInfoArea(ResourceBundleSupport.getString("loadtree.selection.title")));
        return title;
    }

    private JPanel createContent() {
        final JLabel lbSelectContents = new JLabel(ResourceBundleSupport.getString("loadtree.selection.select-contents"));
        final JLabel lbSelectData = new JLabel(ResourceBundleSupport.getString("loadtree.selection.select-dataset"));
        cbxTree = new JCheckBox(new ValidateInputAction(ResourceBundleSupport.getString("loadtree.selection.import-tree"), null));
        cbxClearData = new JCheckBox(new ValidateInputAction(ResourceBundleSupport.getString("loadtree.selection.clear-old-data"), null));
        lstTestData = new JList(listData);
        lstTestData.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        lstTestData.getSelectionModel().addListSelectionListener(new ListSelectionHandler());
        alphabetWarning = ComponentFactory.createInfoArea("");
        final JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        panel.add(lbSelectContents, gbc);
        gbc = new GridBagConstraints();
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 5, 2, 5);
        panel.add(cbxTree, gbc);
        gbc = new GridBagConstraints();
        gbc.gridy = 3;
        gbc.insets = new Insets(2, 5, 2, 5);
        panel.add(lbSelectData, gbc);
        gbc = new GridBagConstraints();
        gbc.gridy = 4;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 5, 2, 5);
        panel.add(new JScrollPane(lstTestData), gbc);
        gbc = new GridBagConstraints();
        gbc.gridy = 5;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 5, 2, 5);
        panel.add(cbxClearData, gbc);
        gbc = new GridBagConstraints();
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(alphabetWarning, gbc);
        return panel;
    }

    private boolean isValidInput() {
        return (cbxTree.isSelected() || lstTestData.getSelectedIndex() != -1);
    }

    public final void start() {
        final ParseResult result = wizzardData.getParseResult();
        if (result == null) {
            cbxTree.setEnabled(false);
            cbxTree.setSelected(false);
            cbxClearData.setSelected(false);
            cbxClearData.setEnabled(false);
            listData.clear();
            lstTestData.setEnabled(false);
            setHasFinish(false);
            return;
        }
        final Alphabet alphabet = wizzardData.getDataBackend().getAlphabet();
        if (alphabet != null) {
            final Alphabet treeAlphabet = wizzardData.getParseResult().getAlphabet();
            if (treeAlphabet.isSuperSet(alphabet) == false) {
                alphabetWarning.setText(ResourceBundleSupport.getString("loadtree.selection.warning"));
            }
        }
        setHasFinish(false);
        if (result.getTree() == null) {
            cbxTree.setEnabled(false);
            cbxTree.setSelected(false);
        } else {
            cbxTree.setEnabled(true);
            cbxTree.setSelected(true);
        }
        if (result.getData() == null) {
            listData.clear();
            lstTestData.setEnabled(false);
            cbxClearData.setSelected(false);
            cbxClearData.setEnabled(false);
        } else {
            cbxClearData.setSelected(true);
            cbxClearData.setEnabled(true);
            final Map data = result.getData();
            listData.clear();
            final Iterator it = data.keySet().iterator();
            while (it.hasNext()) {
                listData.addElement(it.next());
            }
            lstTestData.setEnabled(true);
            if (listData.size() != 0) {
                lstTestData.getSelectionModel().setSelectionInterval(0, listData.size() - 1);
            }
        }
        setHasFinish(true);
    }

    public final void stop() {
        wizzardData.setSelectedData(lstTestData.getSelectedValues());
        wizzardData.setImportTree(cbxTree.isSelected());
        wizzardData.setClearData(cbxClearData.isSelected());
    }
}
