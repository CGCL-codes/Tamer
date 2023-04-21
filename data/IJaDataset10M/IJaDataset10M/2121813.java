package net.sf.jmp3renamer.plugins.Filename.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.sf.jmp3renamer.FileManager;
import net.sf.jmp3renamer.I18N;
import net.sf.jmp3renamer.MagicCookieBaker;
import net.sf.jmp3renamer.datamanager.DataManager;
import net.sf.jmp3renamer.datamanager.DataSet;
import net.sf.jmp3renamer.gui.components.historycombobox.JHistoryComboBox;
import net.sf.jmp3renamer.gui.components.historycombobox.SuggestingJHistoryComboBox;
import net.sf.jmp3renamer.plugins.Filename.Filename;
import net.sf.jmp3renamer.util.MyFile;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class FilenamePanel extends JPanel implements ActionListener, KeyListener, ItemListener, Observer {

    private JPanel explanationPanel = new JPanel();

    private JPanel actionPanel = new JPanel();

    private JLabel lOldname = new JLabel();

    private JLabel lOldnameView = new JLabel();

    private JLabel lNewname = new JLabel();

    private JLabel lNewnameView = new JLabel();

    private JHistoryComboBox tUserRegex;

    private JLabel lExplanation = new JLabel();

    private JLabel lMagicCookies = new JLabel();

    private JButton extractDataButton = new JButton();

    public FilenamePanel() {
        initGUI();
        FileManager.getInstance().addObserver(this);
    }

    private void initGUI() {
        tUserRegex = new SuggestingJHistoryComboBox(MagicCookieBaker.getCookies(), Filename.getProperties(), "history");
        extractDataButton.setText(I18N.translate("extract_data"));
        lOldname.setText(I18N.translate("oldname"));
        lNewname.setText(I18N.translate("newname"));
        lExplanation.setText(I18N.translate("filename_explanation"));
        lMagicCookies.setText(MagicCookieBaker.getCookieText());
        if (Filename.getProperty("userRegex") != null) {
            tUserRegex.addItem(Filename.getProperty("userRegex"));
        }
        GridBagLayout explanationPanelLayout = new GridBagLayout();
        explanationPanel.setLayout(explanationPanelLayout);
        explanationPanelLayout.rowWeights = new double[] { 0.0, 0.1 };
        explanationPanelLayout.rowHeights = new int[] { 7, 7 };
        explanationPanelLayout.columnWeights = new double[] { 0.1 };
        explanationPanelLayout.columnWidths = new int[] { 7 };
        explanationPanel.add(lExplanation, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        explanationPanel.add(lMagicCookies, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        explanationPanel.setBorder(BorderFactory.createTitledBorder(I18N.translate("explanation")));
        GridBagLayout actionPanelLayout = new GridBagLayout();
        actionPanel.setLayout(actionPanelLayout);
        actionPanelLayout.columnWeights = new double[] { 0.0, 0.0 };
        actionPanelLayout.columnWidths = new int[] { 7, 7 };
        actionPanelLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
        actionPanelLayout.rowHeights = new int[] { 7, 7, 7, 7 };
        actionPanel.add(lOldname, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        actionPanel.add(lOldnameView, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        actionPanel.add(tUserRegex, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        actionPanel.add(lNewname, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        actionPanel.add(lNewnameView, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        actionPanel.add(extractDataButton, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        setLayout(new GridBagLayout());
        add(explanationPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));
        add(actionPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 0, 20, 0), 0, 0));
        tUserRegex.setEnabled(false);
        JTextField editor = (JTextField) tUserRegex.getEditor().getEditorComponent();
        editor.addKeyListener(this);
        editor.addActionListener(this);
        tUserRegex.addItemListener(this);
        extractDataButton.setEnabled(false);
        extractDataButton.addActionListener(this);
    }

    public void update(Observable o, Object obj) {
        @SuppressWarnings("unchecked") List<MyFile> files = (List<MyFile>) obj;
        if (files.size() > 0) {
            MyFile file = (MyFile) files.get(0);
            lOldnameView.setText(file.getName());
            tUserRegex.setEnabled(true);
            extractDataButton.setEnabled(true);
            preview();
        }
    }

    private void preview() {
        String regex = (String) tUserRegex.getEditor().getItem();
        String oldname = lOldnameView.getText();
        DataSet dataset = MagicCookieBaker.evaluateCookie(regex, oldname);
        if (dataset != null) {
            lNewnameView.setText(MagicCookieBaker.evaluateDataSet(dataset));
        }
    }

    public void actionPerformed(ActionEvent e) {
        if ("SUGGEST".equals(e.getActionCommand())) {
            return;
        }
        String regex = (String) tUserRegex.getEditor().getItem();
        List<DataSet> datasets = new LinkedList<DataSet>();
        List<MyFile> files = FileManager.getInstance().getFiles();
        for (int i = 0; i < files.size(); i++) {
            MyFile file = files.get(i);
            String oldname = file.getName();
            DataSet dataset = MagicCookieBaker.evaluateCookie(regex, oldname);
            if (dataset != null) datasets.add(dataset);
        }
        DataManager.getInstance().setDatasets(datasets);
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        preview();
    }

    public void itemStateChanged(ItemEvent arg0) {
        preview();
    }
}
