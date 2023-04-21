package org.fudaa.fudaa.sinavi3;

import java.awt.Color;
import java.awt.event.FocusEvent;
import com.memoire.bu.BuDialogError;

public class Sinavi3TextFieldDureeJournee extends Sinavi3TextFieldDuree {

    public Sinavi3TextFieldDureeJournee() {
        super();
    }

    public Sinavi3TextFieldDureeJournee(int columns) {
        super(columns);
    }

    public Sinavi3TextFieldDureeJournee(String text, int columns) {
        super(text, columns);
    }

    public Sinavi3TextFieldDureeJournee(String text) {
        super(text);
    }

    public void traitementApresFocus(FocusEvent e) {
        String contenu = this.getText();
        if (contenu.equals("")) return;
        try {
            float valeur = Float.parseFloat(contenu);
            if (valeur < 0) {
                new BuDialogError(null, Sinavi3Implementation.isSinavi_, "Le nombre doit �tre compris entre 0 et 24.").activate();
                setText("");
                this.requestFocus();
            } else {
                if (contenu.lastIndexOf(".") != -1) {
                    String unite = contenu.substring(contenu.lastIndexOf(".") + 1, contenu.length());
                    if (unite.length() > 2) {
                        new BuDialogError(null, Sinavi3Implementation.isSinavi_, "Il doit y avoir 2 chiffres maximum apr�s la virgule").activate();
                        setText("");
                        this.requestFocus();
                        return;
                    }
                    float valUnite = Float.parseFloat(unite);
                    if (valUnite >= 60) {
                        new BuDialogError(null, Sinavi3Implementation.isSinavi_, "Les unites doivent �tre inf�rieures � 60 minutes.").activate();
                        setText("");
                        this.requestFocus();
                        return;
                    }
                }
                float format = valeur;
                if (format > 24) {
                    new BuDialogError(null, Sinavi3Implementation.isSinavi_, "Le nombre doit �tre compris entre 0 et 24.").activate();
                    setText("");
                    this.requestFocus();
                } else this.setText("" + format);
            }
        } catch (NumberFormatException e1) {
            new BuDialogError(null, Sinavi3Implementation.isSinavi_, "Ce nombre n'existe pas.").activate();
            setText("");
            this.requestFocus();
        }
        Sinavi3TextFieldDureeJournee.this.setForeground(Color.black);
    }

    protected int taille() {
        return 5;
    }
}
