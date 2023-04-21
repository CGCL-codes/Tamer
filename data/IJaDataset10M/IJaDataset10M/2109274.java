package com.memoire.bu;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class BuWizardTaskSample extends BuWizardTask {

    public BuWizardTaskSample() {
    }

    public String getTaskTitle() {
        return "Exemple de t�che";
    }

    public int getStepCount() {
        return 3;
    }

    public String getStepTitle() {
        String r = null;
        switch(current_) {
            case 0:
                r = "Int�gration";
                break;
            case 1:
                r = "Confirmation";
                break;
            case 2:
                r = "Fin";
                break;
        }
        return r;
    }

    public String getStepText() {
        String r = null;
        switch(current_) {
            case 0:
                r = "Choisissez dans la liste ci-dessous" + " la t�che � r�aliser.";
                break;
            case 1:
                r = "Merci de confirmer.";
                break;
            case 2:
                r = "C'est la fin !";
                break;
        }
        return r;
    }

    private JRadioButton rb0_0, rb0_1, rb0_2;

    private ButtonGroup bg0;

    private JPanel r0;

    private JLabel lb1_0;

    private JCheckBox cb1_1;

    private JTextField tf1_2;

    private JPanel r1;

    private JLabel r2;

    public JComponent getStepComponent() {
        switch(current_) {
            case 0:
                {
                    if (rb0_0 == null) rb0_0 = new JRadioButton("Traduire un code source");
                    if (rb0_1 == null) rb0_1 = new JRadioButton("G�n�rer un diagramme UML");
                    if (rb0_2 == null) rb0_2 = new JRadioButton("G�n�rer une documentation");
                    if (bg0 == null) {
                        bg0 = new ButtonGroup();
                        bg0.add(rb0_0);
                        bg0.add(rb0_1);
                        bg0.add(rb0_2);
                        rb0_0.setSelected(true);
                    }
                    if (r0 == null) {
                        r0 = new JPanel(new BuVerticalLayout(10));
                        r0.add(rb0_0);
                        r0.add(rb0_1);
                        r0.add(rb0_2);
                    }
                    return r0;
                }
            case 1:
                {
                    if (lb1_0 == null) {
                        lb1_0 = new JLabel();
                    }
                    String s = "";
                    if (rb0_0.isSelected()) s = rb0_0.getText(); else if (rb0_1.isSelected()) s = rb0_1.getText(); else if (rb0_2.isSelected()) s = rb0_2.getText();
                    lb1_0.setText("<html>Vous avez choisi: <i>" + s + "</i></html>");
                    if (cb1_1 == null) {
                        cb1_1 = new JCheckBox("C'est exact !");
                    }
                    if (tf1_2 == null) {
                        tf1_2 = new JTextField(20);
                    }
                    if (r1 == null) {
                        r1 = new JPanel(new BuVerticalLayout(10));
                        r1.add(lb1_0);
                        r1.add(tf1_2);
                        r1.add(cb1_1);
                    }
                    return r1;
                }
            case 2:
                {
                    if (r2 == null) {
                        r2 = new JLabel();
                        r2.setFont(BuLib.deriveFont(r2.getFont(), +6));
                        r2.setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    r2.setText("Au revoir !");
                    return r2;
                }
        }
        return null;
    }

    public int getStepDisabledButtons() {
        int r = super.getStepDisabledButtons();
        if (current_ == 1) {
            if (!cb1_1.isSelected() || "".equals(tf1_2.getText())) r |= BuButtonPanel.SUIVANT;
        }
        return r;
    }
}
