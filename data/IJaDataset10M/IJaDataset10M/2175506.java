package unbbayes.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import unbbayes.prs.bn.ExplanationPhrase;
import unbbayes.prs.bn.SingleEntityNetwork;
import unbbayes.prs.bn.ProbabilisticNode;
import unbbayes.util.NodeList;

/**
 *
 * @author Mário Henrique Paes Vieira (mariohpv@bol.com.br)
 * @version 1.0
 */
public class ExplanationProperties extends JDialog {

    /** Serialization runtime version number */
    private static final long serialVersionUID = 0;

    private JTabbedPane jTabbedPane = new JTabbedPane();

    private JPanel southPanel = new JPanel();

    private JPanel descriptionPanel = new JPanel();

    private JPanel explanationPanel = new JPanel();

    private JButton okButton = new JButton();

    private Border border1;

    private Border border2;

    private JPanel evidencePhrasePanel = new JPanel();

    private JPanel explanationTopPanel = new JPanel();

    private JPanel evidenceTypePanel = new JPanel();

    private JPanel evidenceNodePanel = new JPanel();

    private JPanel evidencePhraseBottomPanel = new JPanel();

    private JLabel evidenceNodeLabel = new JLabel();

    private JComboBox evidenceNodeComboBox = new JComboBox();

    private TitledBorder titledBorder1;

    private JPanel notUsedPanel = new JPanel();

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

    private ButtonGroup buttonGroup1 = new ButtonGroup();

    private Border evidenceTypeBorder;

    private JPanel evidencePhraseTopPanel = new JPanel();

    private JLabel evidencePhraseLabel = new JLabel();

    private JScrollPane evidencePhraseScrollPane = new JScrollPane();

    private JTextArea evidencePhraseTextArea = new JTextArea();

    private NetworkWindow netWindow;

    private ProbabilisticNode node;

    private SingleEntityNetwork net;

    private JButton cancelButton = new JButton();

    private JLabel nodeNameLabel = new JLabel();

    private JScrollPane explanationNodeScrollPane = new JScrollPane();

    private JPanel descriptionTopPanel1 = new JPanel();

    private JPanel descriptionTopPanel2 = new JPanel();

    private JPanel descriptionTopPanel3 = new JPanel();

    private JPanel descriptionTopPanel = new JPanel();

    private JPanel descriptionBottomPanel = new JPanel();

    private JLabel explanationVariableLabel = new JLabel();

    private JLabel descriptionLabel = new JLabel();

    private JTextArea explanationNodeTextArea = new JTextArea();

    public ExplanationProperties(NetworkWindow netWindow, SingleEntityNetwork net) {
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
        this.setResizable(false);
        this.setTitle("Propriedades da Variável de Explicação");
        border1 = BorderFactory.createEmptyBorder(0, 10, 0, 10);
        border2 = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "Tipo de Evidência:");
        evidenceTypeBorder = BorderFactory.createCompoundBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "Tipo de Evidência:"), BorderFactory.createEmptyBorder(0, 10, 0, 0));
        okButton.setMaximumSize(new Dimension(85, 27));
        okButton.setMinimumSize(new Dimension(85, 27));
        okButton.setPreferredSize(new Dimension(85, 27));
        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okButton_actionPerformed(e);
            }
        });
        descriptionPanel.setLayout(new BorderLayout(0, 5));
        explanationPanel.setLayout(new GridLayout(2, 0, 0, 10));
        explanationTopPanel.setLayout(new BorderLayout(0, 10));
        evidencePhrasePanel.setLayout(new BorderLayout(0, 5));
        evidenceNodeLabel.setText("Evidencia : ");
        evidenceNodePanel.setLayout(new BorderLayout());
        evidenceTypePanel.setBorder(evidenceTypeBorder);
        evidenceTypePanel.setLayout(new GridLayout(2, 3, 10, 0));
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
        triggerPanel.setLayout(new BorderLayout());
        complementaryPanel.setLayout(new BorderLayout());
        naPanel.setLayout(new BorderLayout());
        necessaryPanel.setLayout(new BorderLayout());
        exclusivePanel.setLayout(new BorderLayout());
        notUsedPanel.setLayout(new BorderLayout());
        evidencePhraseLabel.setText("Texto para Explanação :");
        evidencePhraseTopPanel.setLayout(new BorderLayout());
        evidencePhraseBottomPanel.setLayout(new BorderLayout());
        cancelButton.setText("Cancelar");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancelButton_actionPerformed(e);
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
        descriptionTopPanel2.setLayout(new BorderLayout());
        descriptionTopPanel3.setLayout(new BorderLayout());
        descriptionTopPanel.setLayout(new GridLayout(3, 0));
        descriptionBottomPanel.setLayout(new BorderLayout());
        explanationVariableLabel.setText("Variável de explicação : ");
        descriptionPanel.setBorder(border2);
        descriptionTopPanel1.setLayout(new BorderLayout());
        descriptionLabel.setText("Descrição:");
        explanationPanel.setBorder(border2);
        this.getContentPane().add(southPanel, BorderLayout.SOUTH);
        descriptionTopPanel.add(descriptionTopPanel1, null);
        descriptionTopPanel1.add(descriptionLabel, BorderLayout.CENTER);
        descriptionTopPanel.add(descriptionTopPanel2, null);
        descriptionTopPanel.add(descriptionTopPanel3, null);
        descriptionTopPanel3.add(explanationVariableLabel, BorderLayout.WEST);
        descriptionTopPanel3.add(nodeNameLabel, BorderLayout.CENTER);
        descriptionPanel.add(descriptionBottomPanel, BorderLayout.CENTER);
        descriptionBottomPanel.add(explanationNodeScrollPane, BorderLayout.CENTER);
        explanationNodeScrollPane.getViewport().add(explanationNodeTextArea, null);
        descriptionPanel.add(descriptionTopPanel, BorderLayout.NORTH);
        southPanel.add(okButton, null);
        southPanel.add(cancelButton, null);
        explanationPanel.add(explanationTopPanel, null);
        explanationPanel.add(evidencePhrasePanel, null);
        explanationTopPanel.add(evidenceTypePanel, BorderLayout.CENTER);
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
        evidenceTypePanel.add(notUsedPanel, null);
        explanationTopPanel.add(evidenceNodePanel, BorderLayout.NORTH);
        evidenceNodePanel.add(evidenceNodeLabel, BorderLayout.WEST);
        evidenceNodePanel.add(evidenceNodeComboBox, BorderLayout.CENTER);
        evidencePhrasePanel.add(evidencePhraseBottomPanel, BorderLayout.CENTER);
        evidencePhraseBottomPanel.add(evidencePhraseScrollPane, BorderLayout.CENTER);
        evidencePhraseScrollPane.getViewport().add(evidencePhraseTextArea, null);
        evidencePhrasePanel.add(evidencePhraseTopPanel, BorderLayout.NORTH);
        evidencePhraseTopPanel.add(evidencePhraseLabel, BorderLayout.CENTER);
        buttonGroup1.add(triggerRadioButton);
        buttonGroup1.add(complementaryRadioButton);
        buttonGroup1.add(naRadioButton);
        buttonGroup1.add(necessaryRadioButton);
        buttonGroup1.add(exclusiveRadioButton);
        jTabbedPane.setBorder(border1);
        jTabbedPane.add(descriptionPanel, "Descrição");
        jTabbedPane.add(explanationPanel, "Explanação");
        this.getContentPane().add(jTabbedPane, BorderLayout.CENTER);
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
        explanationNodeTextArea.setText(node.getExplanationDescription());
        if (evidenceNodeComboBox.getItemCount() != 0) {
            updateExplanationInformation(evidenceNodeComboBox.getItemAt(0).toString());
        }
        nodeNameLabel.setText(node.getName());
        descriptionLabel.setText("Descrição: " + node.getDescription());
    }

    void okButton_actionPerformed(ActionEvent e) {
        node.setExplanationDescription(explanationNodeTextArea.getText());
        dispose();
    }

    void cancelButton_actionPerformed(ActionEvent e) {
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
