package fr.ign.cogit.appli.geopensim.appli.rules;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.apache.log4j.Logger;
import fr.ign.cogit.appli.geopensim.evolution.EvolutionRuleUnitaire.Possibilites;
import fr.ign.cogit.geoxygene.filter.expression.Literal;

/**
 * @author Florence Curie
 *
 */
public class PossibiliteCreationDialog extends JDialog implements ActionListener, FocusListener {

    private static final long serialVersionUID = 7835704040491692178L;

    private JButton boutonValidation, boutonAnnulation;

    private JTextField proba, expression;

    private Float prob;

    private static Logger logger = Logger.getLogger(PossibiliteCreationDialog.class.getName());

    private boolean valider = false;

    private Possibilites possibilit;

    private JComboBox comboProprieteObj = new JComboBox();

    private String[] propObj = { "densiteBut", "classificationFonctionnelleBut" };

    public PossibiliteCreationDialog(JDialog parent, Possibilites possib) {
        this(parent);
        if (possib.getProbability() != -1) {
            proba.setText(((Float) possib.getProbability()).toString());
        }
        String propO = possib.getPropertyName();
        comboProprieteObj.setSelectedItem(propO);
        if (possib.getExpression() != null) expression.setText(possib.getExpression().toString());
    }

    public PossibiliteCreationDialog(JDialog parent) {
        super(parent, "Ajout d'une possibilité", true);
        this.setBounds(570, 100, 450, 185);
        this.setResizable(false);
        Container contenu = this.getContentPane();
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                dispose();
            }
        });
        proba = new JTextField(5);
        proba.setMaximumSize(proba.getPreferredSize());
        proba.addFocusListener(this);
        Box hBoxProbabilite = Box.createHorizontalBox();
        hBoxProbabilite.add(new JLabel("probabilité : "));
        hBoxProbabilite.add(proba);
        hBoxProbabilite.add(Box.createHorizontalGlue());
        comboProprieteObj = new JComboBox(propObj);
        comboProprieteObj.setMaximumSize(comboProprieteObj.getPreferredSize());
        Box hBoxProprieteObj = Box.createHorizontalBox();
        hBoxProprieteObj.add(new JLabel("Propriété objectif : "));
        hBoxProprieteObj.add(comboProprieteObj);
        hBoxProprieteObj.add(Box.createHorizontalGlue());
        expression = new JTextField(15);
        expression.setMaximumSize(expression.getPreferredSize());
        expression.addFocusListener(this);
        Box hBoxNomRegle = Box.createHorizontalBox();
        hBoxNomRegle.add(new JLabel("Expression : "));
        hBoxNomRegle.add(expression);
        hBoxNomRegle.add(Box.createHorizontalGlue());
        boutonValidation = new JButton("valider");
        boutonValidation.addActionListener(this);
        boutonAnnulation = new JButton("annuler");
        boutonAnnulation.addActionListener(this);
        Box hBoxValidAnnul = Box.createHorizontalBox();
        hBoxValidAnnul.add(Box.createHorizontalGlue());
        hBoxValidAnnul.add(boutonValidation);
        hBoxValidAnnul.add(Box.createRigidArea(new Dimension(10, 0)));
        hBoxValidAnnul.add(boutonAnnulation);
        Box vBox = Box.createVerticalBox();
        vBox.add(hBoxProbabilite);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(hBoxProprieteObj);
        vBox.add(Box.createVerticalStrut(10));
        vBox.add(hBoxNomRegle);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(hBoxValidAnnul);
        vBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contenu.add(vBox, BorderLayout.CENTER);
    }

    public void focusLost(FocusEvent e) {
        if (e.getSource() == this.proba) {
            prob = 1f;
            String strDateD1 = "La probabilité entrée n'est pas un float";
            String strDateD2 = "Voulez vous entrer une autre probabilité ?";
            String strDateD3 = "Entrez une nouvelle probabilité pour la possibilité";
            prob = PossibiliteCreationDialog.verifFloat(this.proba, strDateD1, strDateD2, strDateD3);
        }
    }

    public Possibilites getPossib() {
        valider = false;
        setVisible(true);
        if (valider) return possibilit; else return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.boutonValidation) {
            possibilit = new Possibilites();
            if (this.proba.getText().isEmpty()) {
                prob = 1f;
            } else {
                prob = Float.parseFloat(this.proba.getText());
            }
            possibilit.setProbability(prob);
            possibilit.setPropertyName(this.comboProprieteObj.getSelectedItem().toString());
            Literal literal = new Literal();
            literal.setValue(this.expression.getText());
            possibilit.setExpression(literal);
            valider = true;
            setVisible(false);
            dispose();
        } else if (e.getSource() == this.boutonAnnulation) {
            setVisible(false);
            dispose();
        }
    }

    protected static Float verifFloat(JTextField textField, String str1, String str2, String str3) {
        float valeurDouble = -1;
        if (!textField.getText().isEmpty()) {
            try {
                valeurDouble = Float.parseFloat(textField.getText());
            } catch (Exception e2) {
                logger.error(str1);
                int rep = JOptionPane.showConfirmDialog(null, str2, str1, JOptionPane.YES_NO_OPTION);
                String valeurString = "";
                int rep2 = 0;
                while ((rep == 0) && (valeurDouble == -1) && (rep2 == 0)) {
                    valeurString = JOptionPane.showInputDialog(null, str3, str1, JOptionPane.QUESTION_MESSAGE);
                    try {
                        valeurDouble = Float.parseFloat(valeurString);
                    } catch (Exception e21) {
                        logger.error(str1);
                    }
                    if (valeurString == null) {
                        rep2 = JOptionPane.showConfirmDialog(null, str2, str1, JOptionPane.YES_NO_OPTION);
                    }
                }
                if (valeurDouble == -1) {
                    textField.setText("");
                } else {
                    textField.setText(valeurString);
                }
            }
        }
        return valeurDouble;
    }

    @Override
    public void focusGained(FocusEvent e) {
    }
}
