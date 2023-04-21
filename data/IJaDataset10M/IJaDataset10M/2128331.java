package com.elibera.ccs.panel.question;

import java.awt.ComponentOrientation;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import com.elibera.ccs.app.MLEConfig;
import com.elibera.ccs.panel.HelperPanel;
import com.elibera.ccs.panel.InterfaceDialogClose;
import com.elibera.ccs.panel.question.tbl.TableDataQuestionRoot;
import com.elibera.ccs.res.Msg;
import com.elibera.ccs.util.HelperStd;

/**
 * @author meisi
 *
 */
public abstract class PanelNewQuestionRoot extends JPanel {

    private JTable jTable = null;

    protected JFormattedTextField jTextFieldQuestionPoints = null;

    protected JTextArea jTextArea = null;

    protected TableDataQuestionRoot data;

    protected InterfaceDialogClose dialogClose;

    protected MLEConfig conf;

    private JScrollPane jScrollPane1 = null;

    /**
	 * This is the default constructor
	 */
    public PanelNewQuestionRoot(MLEConfig conf, InterfaceDialogClose dialogClose) {
        super();
        this.conf = conf;
        this.dialogClose = dialogClose;
        initialize();
        panelSettingsRandomize.setVisible(allowQuestionRandomize());
    }

    public void reset(MLEConfig conf, InterfaceDialogClose dialogClose) {
        this.conf = conf;
        this.dialogClose = dialogClose;
        jCheckBoxRandomize.setSelected(false);
        jTextArea.setText("");
        jTextFieldQuestionPoints.setText(Msg.getConfSetting("QUESTION_DEF_POINTS"));
        data.reset(conf, jTable);
        panelSettingsRandomize.setVisible(allowQuestionRandomize());
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        HelperPanel.formatPanel(this);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(getJPanel(), null);
        this.add(getJPanel1(), null);
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel() {
        JPanel jPanelFrage = new JPanel();
        HelperPanel.formatPanelWithBorder(jPanelFrage, Msg.getMsg("PANEL_NEW_QUESTION_ROOT_PANEL_FRAGE_TITEL"));
        jPanelFrage.setLayout(new BoxLayout(jPanelFrage, BoxLayout.Y_AXIS));
        jPanelFrage.add(getJPanel2(), null);
        jPanelFrage.add(getJPanel3(), null);
        panelSettingsRandomize = getJPanelSettingsRandomize();
        jPanelFrage.add(panelSettingsRandomize, null);
        return jPanelFrage;
    }

    private JPanel panelSettingsRandomize;

    /**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel1() {
        JPanel jPanelTblRoot = new JPanel();
        HelperPanel.formatPanelWithBorder(jPanelTblRoot, getTitelForPanelTblRoot());
        jPanelTblRoot.setLayout(new BoxLayout(jPanelTblRoot, BoxLayout.Y_AXIS));
        jPanelTblRoot.add(getJPanel4(), null);
        jPanelTblRoot.add(getJScrollPane(), null);
        jPanelTblRoot.add(getJPanel5(), null);
        return jPanelTblRoot;
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel2() {
        JLabel jLabelQuestionText = new JLabel();
        jLabelQuestionText.setText(Msg.getMsg("PANEL_NEW_QUESTION_ROOT_QUESTION_TEXT"));
        JPanel jPanelQuestionText = new JPanel();
        HelperPanel.formatPanel(jPanelQuestionText);
        jPanelQuestionText.add(jLabelQuestionText, null);
        jPanelQuestionText.add(getJScrollPane1(), null);
        return jPanelQuestionText;
    }

    private JPanel getJPanelSettingsRandomize() {
        JLabel jLabelRandmoize = new JLabel();
        jLabelRandmoize.setText(Msg.getMsg("PanelNewQuestionRoot.LabelCheckBoxRandomize"));
        JPanel jPanelRandmoize = new JPanel();
        HelperPanel.formatPanel(jPanelRandmoize);
        jPanelRandmoize.add(jLabelRandmoize, null);
        jPanelRandmoize.add(this.getJCheckBoxRandomize(), null);
        return jPanelRandmoize;
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel3() {
        JLabel jLabelQuestionPoints = new JLabel();
        jLabelQuestionPoints.setText(Msg.getMsg("PANEL_NEW_QUESTION_ROOT_QUESTION_POINTS"));
        JPanel jPanelQuestionPoints = new JPanel();
        HelperPanel.formatPanel(jPanelQuestionPoints);
        jPanelQuestionPoints.add(jLabelQuestionPoints, null);
        jPanelQuestionPoints.add(getJTextField1(), null);
        return jPanelQuestionPoints;
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel4() {
        JLabel jLabelTblInfo = new JLabel();
        jLabelTblInfo.setText(getJLabelInfoTextInTblRoot());
        JPanel jPanelTblInfo = new JPanel();
        HelperPanel.formatPanel(jPanelTblInfo);
        jPanelTblInfo.add(jLabelTblInfo, null);
        return jPanelTblInfo;
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel5() {
        JPanel jPanelButtons = new JPanel();
        HelperPanel.formatPanel(jPanelButtons);
        jPanelButtons.add(getJButton(), null);
        jPanelButtons.add(getJButton1(), null);
        return jPanelButtons;
    }

    /**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPane() {
        jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(getJTable());
        return jScrollPane;
    }

    private JScrollPane jScrollPane;

    /**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
    private JTable getJTable() {
        jTable = new JTable();
        data = constructTableAndGetTableData(jTable, jScrollPane);
        return jTable;
    }

    /**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButton() {
        JButton jButtonAddRow = new JButton();
        HelperPanel.formatButton(jButtonAddRow);
        jButtonAddRow.setText(getTitelForButtonAddNewRow());
        jButtonAddRow.setToolTipText(getToolTipForButtonAddNewRow());
        jButtonAddRow.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                data.addBlankRow();
            }
        });
        return jButtonAddRow;
    }

    /**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButton1() {
        JButton jButtonInsert = new JButton();
        HelperPanel.formatButton(jButtonInsert);
        jButtonInsert.setToolTipText(Msg.getMsg("PANEL_NEW_QUESTION_ROOT_BUTTON_INSERT_TOOLTIP"));
        jButtonInsert.setText(Msg.getMsg("PANEL_NEW_QUESTION_ROOT_BUTTON_INSERT"));
        jButtonInsert.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                String text = jTextArea.getText();
                int points = HelperStd.parseInt(jTextFieldQuestionPoints.getText(), 12);
                if (!data.isReadyForInsert() || text == null || text.length() <= 0) {
                    JOptionPane.showMessageDialog(conf.ep, Msg.getMsg("PANEL_NEW_QUESTION_ROOT_INSERT_ERROR_TEXT"), Msg.getMsg("WORD_ERROR") + "!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                data.insertIntoEditor(text, points, getJCheckBoxRandomize().isSelected(), allowQuestionRandomize());
                dialogClose.closeDialog();
            }
        });
        return jButtonInsert;
    }

    /**
	 * This method initializes jTextField1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JFormattedTextField getJTextField1() {
        if (jTextFieldQuestionPoints == null) {
            jTextFieldQuestionPoints = new JFormattedTextField();
            HelperPanel.makeTextFieldNumberTextfield(jTextFieldQuestionPoints, 100);
            jTextFieldQuestionPoints.setText(Msg.getConfSetting("QUESTION_DEF_POINTS"));
            jTextFieldQuestionPoints.setToolTipText(Msg.getMsg("PANEL_NEW_QUESTION_ROOT_QUESTION_POINTS_TOOLTIP"));
        }
        return jTextFieldQuestionPoints;
    }

    /**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
    private JTextArea getJTextArea() {
        if (jTextArea == null) {
            jTextArea = new JTextArea();
            HelperPanel.formatTextArea(jTextArea, 20, 3, jScrollPane1);
            jTextArea.setToolTipText(Msg.getMsg("PANEL_NEW_QUESTION_ROOT_QUESTION_TEXT_TOOLTIP"));
        }
        return jTextArea;
    }

    /**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPane1() {
        if (jScrollPane1 == null) {
            jScrollPane1 = new JScrollPane();
            jScrollPane1.setViewportView(getJTextArea());
        }
        return jScrollPane1;
    }

    protected JCheckBox jCheckBoxRandomize = null;

    private JCheckBox getJCheckBoxRandomize() {
        if (jCheckBoxRandomize == null) {
            jCheckBoxRandomize = new JCheckBox();
            HelperPanel.formatCheckBox(jCheckBoxRandomize);
            jCheckBoxRandomize.setToolTipText(Msg.getString("PanelNewQuestionRoot.TOOLTIP_CHECKBOX_RANDOMIZE"));
        }
        return jCheckBoxRandomize;
    }

    /**
	 * dürfen die Frage-elemente randomiziert werden
	 * @return
	 */
    protected abstract boolean allowQuestionRandomize();

    /**
	 * erstellt die TableDataQuestionRoot-Instanz anhand des Tables und geht auch noch die Größe des Tables anpassen
	 * gibt dann die erstellte TableDataQuestionRoot-Instanz zurück
	 * @param table
	 * @return
	 */
    protected abstract TableDataQuestionRoot constructTableAndGetTableData(JTable table, JScrollPane scrollPane);

    /**
	 * Gibt den Rahmen-Titel für das Root Panel für die Tabelle zurück
	 * @return
	 */
    protected abstract String getTitelForPanelTblRoot();

    /**
	 * gibt den Titel für den Button zum Erstellen einer neuen Zeile zurück
	 * @return
	 */
    protected abstract String getTitelForButtonAddNewRow();

    /**
	 * gibt den Tool-Tip für den Button zum Erstellen einer neuen Zeile zurück
	 * @return
	 */
    protected abstract String getToolTipForButtonAddNewRow();

    /**
	 * gibt den Info-Text zurück, der oberhalb der Tabelle angezeigt wird
	 * @return
	 */
    protected abstract String getJLabelInfoTextInTblRoot();
}
