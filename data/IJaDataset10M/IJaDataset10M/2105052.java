package org.helios.utils;

import javax.swing.BorderFactory;
import javax.swing.*;

public class FattorizzatoreGui extends Pannello {

    JRadioButton fattori, divisori, potenze;

    ButtonGroup gruppo = new ButtonGroup();

    public FattorizzatoreGui() {
        super();
        pannello.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Fattorizzatore.titolo), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        etichetta.setText(Fattorizzatore.etichetta);
        creaBottoni();
        pannelloOpzioni.add(potenze);
        pannelloOpzioni.add(fattori);
        pannelloOpzioni.add(divisori);
    }

    public JPanel getPanel() {
        return pannello;
    }

    public void creaBottoni() {
        potenze = new JRadioButton("Potenze", true);
        divisori = new JRadioButton("Divisori");
        fattori = new JRadioButton("Fattori");
        gruppo.add(potenze);
        gruppo.add(divisori);
        gruppo.add(fattori);
    }
}
