package unbbayes.fronteira;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.*;
import unbbayes.jprs.jbn.*;
import unbbayes.util.*;

/**
 *
 * @author M�rio Henrique Paes Vieira (mariohpv@bol.com.br)
 * @version 1.0
 */
public class ExplanationProperties extends JDialog {

    private JPanel jPanel1 = new JPanel();

    private BorderLayout borderLayout1 = new BorderLayout();

    private JTabbedPane jTabbedPane1 = new JTabbedPane();

    private JPanel jPanel2 = new JPanel();

    private JPanel descriptionPanel = new JPanel();

    private JPanel explanationPanel = new JPanel();

    private JButton jButton1 = new JButton();

    private Border border1;

    private JPanel jPanel5 = new JPanel();

    private BorderLayout borderLayout2 = new BorderLayout();

    private JPanel jPanel7 = new JPanel();

    private BorderLayout borderLayout3 = new BorderLayout();

    private Border border2;

    private JPanel jPanel6 = new JPanel();

    private JPanel jPanel8 = new JPanel();

    private BorderLayout borderLayout4 = new BorderLayout();

    private GridLayout gridLayout1 = new GridLayout();

    private JPanel jPanel9 = new JPanel();

    private JPanel jPanel10 = new JPanel();

    private JPanel jPanel11 = new JPanel();

    private JScrollPane jScrollPane1 = new JScrollPane();

    private BorderLayout borderLayout5 = new BorderLayout();

    private JTextArea jTextArea1 = new JTextArea();

    private JLabel jLabel1 = new JLabel();

    private JLabel jLabel2 = new JLabel();

    private BorderLayout borderLayout6 = new BorderLayout();

    private BorderLayout borderLayout7 = new BorderLayout();

    private BorderLayout borderLayout8 = new BorderLayout();

    private JPanel jPanel3 = new JPanel();

    private GridLayout gridLayout2 = new GridLayout();

    private JPanel evidencePhrasePanel = new JPanel();

    private JPanel jPanel12 = new JPanel();

    private BorderLayout borderLayout9 = new BorderLayout();

    private BorderLayout borderLayout10 = new BorderLayout();

    private JPanel evidenceTypePanel = new JPanel();

    private JPanel evidenceNodePanel = new JPanel();

    private JPanel jPanel15 = new JPanel();

    private JLabel evidenceNodeLabel = new JLabel();

    private JComboBox evidenceNodeComboBox = new JComboBox();

    private BorderLayout borderLayout11 = new BorderLayout();

    private Border border3;

    private TitledBorder titledBorder1;

    private GridLayout gridLayout3 = new GridLayout();

    private JPanel jPanel17 = new JPanel();

    private JPanel exclusivePanel = new JPanel();

    private JPanel necessaryPanel = new JPanel();

    private JPanel naPanel = new JPanel();

    private JPanel complementaryPanel = new JPanel();

    private JPanel triggerPanel = new JPanel();

    private JRadioButton triggerRadioButton = new JRadioButton();

    private JRadioButton complementaryRadioButton = new JRadioButton();

    private JRadioButton naRadioButton = new JRadioButton();

    private JRadioButton necessaryRadioButton = new JRadioButton();

    private JRadioButton exclusiveRadioButton = new JRadioButton();

    private BorderLayout borderLayout12 = new BorderLayout();

    private BorderLayout borderLayout13 = new BorderLayout();

    private BorderLayout borderLayout14 = new BorderLayout();

    private BorderLayout borderLayout15 = new BorderLayout();

    private BorderLayout borderLayout16 = new BorderLayout();

    private BorderLayout borderLayout17 = new BorderLayout();

    private ButtonGroup buttonGroup1 = new ButtonGroup();

    private Border evidenceTypeBorder;

    private JPanel jPanel24 = new JPanel();

    private JLabel jLabel4 = new JLabel();

    private BorderLayout borderLayout18 = new BorderLayout();

    private JScrollPane evidencePhraseScrollPane = new JScrollPane();

    private BorderLayout borderLayout19 = new BorderLayout();

    private JTextArea evidencePhraseTextArea = new JTextArea();

    private NetWindow netWindow;

    private ProbabilisticNode node;

    private ProbabilisticNetwork net;

    private JButton jButton2 = new JButton();

    private JLabel nodeNameLabel = new JLabel();

    public ExplanationProperties(NetWindow netWindow, ProbabilisticNetwork net) {
        this.netWindow = netWindow;
        this.net = net;
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setSize(550, 470);
        border1 = BorderFactory.createEmptyBorder(0, 10, 0, 10);
        border2 = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        border3 = BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140));
        titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "Tipo de Evid�ncia:");
        evidenceTypeBorder = BorderFactory.createCompoundBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "Tipo de Evid�ncia:"), BorderFactory.createEmptyBorder(0, 10, 0, 0));
        this.setResizable(false);
        this.setTitle("Propriedades da Vari�vel de Explica��o");
        jPanel1.setLayout(borderLayout1);
        jButton1.setMaximumSize(new Dimension(85, 27));
        jButton1.setMinimumSize(new Dimension(85, 27));
        jButton1.setPreferredSize(new Dimension(85, 27));
        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jButton1_actionPerformed(e);
            }
        });
        jTabbedPane1.setBorder(border1);
        descriptionPanel.setLayout(borderLayout2);
        jPanel5.setLayout(borderLayout3);
        jPanel5.setBorder(border2);
        jPanel7.setLayout(borderLayout4);
        jPanel8.setLayout(gridLayout1);
        gridLayout1.setRows(3);
        jPanel6.setLayout(borderLayout5);
        jLabel1.setText("Vari�vel de explica��o : ");
        jLabel2.setText("Descri��o:");
        jPanel9.setLayout(borderLayout6);
        jPanel10.setLayout(borderLayout7);
        borderLayout4.setVgap(5);
        explanationPanel.setLayout(borderLayout8);
        jPanel3.setBorder(border2);
        jPanel3.setLayout(gridLayout2);
        gridLayout2.setRows(2);
        gridLayout2.setVgap(10);
        jPanel12.setLayout(borderLayout9);
        evidencePhrasePanel.setLayout(borderLayout10);
        evidenceNodeLabel.setText("Evidencia : ");
        evidenceNodePanel.setLayout(borderLayout11);
        evidenceTypePanel.setBorder(evidenceTypeBorder);
        evidenceTypePanel.setLayout(gridLayout3);
        gridLayout3.setColumns(3);
        gridLayout3.setHgap(10);
        gridLayout3.setRows(2);
        triggerRadioButton.setText("Trigger");
        triggerRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                evidenceRadioButton_actionPerformed(e);
            }
        });
        complementaryRadioButton.setText("Complementar");
        complementaryRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                evidenceRadioButton_actionPerformed(e);
            }
        });
        naRadioButton.setText("N/A");
        naRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                evidenceRadioButton_actionPerformed(e);
            }
        });
        necessaryRadioButton.setText("Essencial");
        necessaryRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                evidenceRadioButton_actionPerformed(e);
            }
        });
        exclusiveRadioButton.setText("Excludente");
        exclusiveRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                evidenceRadioButton_actionPerformed(e);
            }
        });
        triggerPanel.setLayout(borderLayout12);
        complementaryPanel.setLayout(borderLayout13);
        naPanel.setLayout(borderLayout14);
        necessaryPanel.setLayout(borderLayout15);
        exclusivePanel.setLayout(borderLayout16);
        jPanel17.setLayout(borderLayout17);
        jLabel4.setText("Texto para Explana��o :");
        jPanel24.setLayout(borderLayout18);
        jPanel15.setLayout(borderLayout19);
        borderLayout10.setVgap(5);
        borderLayout9.setVgap(10);
        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jButton2_actionPerformed(e);
            }
        });
        evidenceNodeComboBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                evidenceNodeComboBox_actionPerformed(e);
            }
        });
        evidencePhraseTextArea.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(FocusEvent e) {
                evidencePhraseTextArea_focusLost(e);
            }
        });
        this.getContentPane().add(jPanel1, BorderLayout.CENTER);
        jPanel1.add(jTabbedPane1, BorderLayout.CENTER);
        jTabbedPane1.add(descriptionPanel, "Descri��o");
        descriptionPanel.add(jPanel5, BorderLayout.CENTER);
        jPanel5.add(jPanel7, BorderLayout.CENTER);
        jPanel7.add(jPanel8, BorderLayout.NORTH);
        jPanel8.add(jPanel9, null);
        jPanel9.add(jLabel1, BorderLayout.WEST);
        jPanel9.add(nodeNameLabel, BorderLayout.CENTER);
        jPanel8.add(jPanel11, null);
        jPanel8.add(jPanel10, null);
        jPanel10.add(jLabel2, BorderLayout.CENTER);
        jPanel7.add(jPanel6, BorderLayout.CENTER);
        jPanel6.add(jScrollPane1, BorderLayout.CENTER);
        jScrollPane1.getViewport().add(jTextArea1, null);
        jTabbedPane1.add(explanationPanel, "Explana��o");
        jPanel1.add(jPanel2, BorderLayout.SOUTH);
        jPanel2.add(jButton1, null);
        jPanel2.add(jButton2, null);
        explanationPanel.add(jPanel3, BorderLayout.CENTER);
        jPanel3.add(jPanel12, null);
        jPanel12.add(evidenceTypePanel, BorderLayout.CENTER);
        evidenceTypePanel.add(triggerPanel, null);
        triggerPanel.add(triggerRadioButton, BorderLayout.CENTER);
        evidenceTypePanel.add(complementaryPanel, null);
        complementaryPanel.add(complementaryRadioButton, BorderLayout.CENTER);
        evidenceTypePanel.add(naPanel, null);
        naPanel.add(naRadioButton, BorderLayout.CENTER);
        evidenceTypePanel.add(necessaryPanel, null);
        necessaryPanel.add(necessaryRadioButton, BorderLayout.CENTER);
        evidenceTypePanel.add(exclusivePanel, null);
        exclusivePanel.add(exclusiveRadioButton, BorderLayout.CENTER);
        evidenceTypePanel.add(jPanel17, null);
        jPanel12.add(evidenceNodePanel, BorderLayout.NORTH);
        evidenceNodePanel.add(evidenceNodeLabel, BorderLayout.WEST);
        evidenceNodePanel.add(evidenceNodeComboBox, BorderLayout.CENTER);
        jPanel3.add(evidencePhrasePanel, null);
        evidencePhrasePanel.add(jPanel15, BorderLayout.CENTER);
        jPanel15.add(evidencePhraseScrollPane, BorderLayout.CENTER);
        evidencePhraseScrollPane.getViewport().add(evidencePhraseTextArea, null);
        evidencePhrasePanel.add(jPanel24, BorderLayout.NORTH);
        jPanel24.add(jLabel4, BorderLayout.CENTER);
        buttonGroup1.add(triggerRadioButton);
        buttonGroup1.add(complementaryRadioButton);
        buttonGroup1.add(naRadioButton);
        buttonGroup1.add(necessaryRadioButton);
        buttonGroup1.add(exclusiveRadioButton);
        NodeList nodes = net.getDescriptionNodes();
        int size = nodes.size();
        int i;
        String[] stringNodes = new String[size];
        for (i = 0; i < size; i++) {
            stringNodes[i] = nodes.get(i).getDescription();
        }
        Arrays.sort(stringNodes);
        for (i = 0; i < size; i++) {
            evidenceNodeComboBox.addItem(stringNodes[i]);
        }
    }

    public void setProbabilisticNode(ProbabilisticNode node) {
        this.node = node;
        jTextArea1.setText(node.getExplanationDescription());
        if (evidenceNodeComboBox.getItemCount() != 0) updateExplanationInformation(evidenceNodeComboBox.getItemAt(0).toString());
        nodeNameLabel.setText(node.getName());
    }

    void jButton1_actionPerformed(ActionEvent e) {
        node.setExplanationDescription(jTextArea1.getText());
        dispose();
    }

    void jButton2_actionPerformed(ActionEvent e) {
        dispose();
    }

    void evidenceNodeComboBox_actionPerformed(ActionEvent evt) {
        JComboBox source = (JComboBox) evt.getSource();
        String item = (String) source.getSelectedItem();
        updateExplanationInformation(item);
    }

    private void updateExplanationInformation(String item) {
        try {
            ExplanationPhrase explanationPhrase = node.getExplanationPhrase(item);
            int evidenceType = explanationPhrase.getEvidenceType();
            switch(evidenceType) {
                case (ExplanationPhrase.TRIGGER_EVIDENCE_TYPE):
                    triggerRadioButton.setSelected(true);
                    break;
                case (ExplanationPhrase.NECESSARY_EVIDENCE_TYPE):
                    necessaryRadioButton.setSelected(true);
                    break;
                case (ExplanationPhrase.COMPLEMENTARY_EVIDENCE_TYPE):
                    complementaryRadioButton.setSelected(true);
                    break;
                case (ExplanationPhrase.EXCLUSIVE_EVIDENCE_TYPE):
                    exclusiveRadioButton.setSelected(true);
                    break;
                default:
                    naRadioButton.setSelected(true);
            }
            evidencePhraseTextArea.setText(explanationPhrase.getPhrase());
        } catch (Exception e) {
            naRadioButton.setSelected(true);
            evidencePhraseTextArea.setText("");
        }
    }

    void evidenceRadioButton_actionPerformed(ActionEvent e) {
        addEvidence(e);
    }

    void evidencePhraseTextArea_focusLost(FocusEvent e) {
        addEvidence(e);
    }

    private void addEvidence(AWTEvent e) {
        ExplanationPhrase explanationPhrase = new ExplanationPhrase();
        explanationPhrase.setNode(evidenceNodeComboBox.getSelectedItem().toString());
        if (triggerRadioButton.isSelected()) {
            explanationPhrase.setEvidenceType(ExplanationPhrase.TRIGGER_EVIDENCE_TYPE);
        } else if (necessaryRadioButton.isSelected()) {
            explanationPhrase.setEvidenceType(ExplanationPhrase.NECESSARY_EVIDENCE_TYPE);
        } else if (complementaryRadioButton.isSelected()) {
            explanationPhrase.setEvidenceType(ExplanationPhrase.COMPLEMENTARY_EVIDENCE_TYPE);
        } else if (exclusiveRadioButton.isSelected()) {
            explanationPhrase.setEvidenceType(ExplanationPhrase.EXCLUSIVE_EVIDENCE_TYPE);
        } else {
            explanationPhrase.setEvidenceType(ExplanationPhrase.NA_EVIDENCE_TYPE);
        }
        explanationPhrase.setPhrase(evidencePhraseTextArea.getText());
        node.addExplanationPhrase(explanationPhrase);
    }
}
