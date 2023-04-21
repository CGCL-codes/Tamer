package com.memoire.bu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 * A standard dialog to find, with history.
 */
public class BuDialogFind extends BuDialog implements ActionListener {

    protected BuButton btRechercher_;

    protected BuButton btFermer_;

    protected BuTextComponentInterface tcTexte_;

    protected BuTextField tfMotif_;

    protected BuCheckBox cbCasse_;

    public BuDialogFind(BuCommonInterface _parent, BuInformationsSoftware _isoft, BuTextComponentInterface _texte) {
        super(_parent, _isoft, __("Recherche"));
        tcTexte_ = _texte;
        BuPanel pnb = new BuPanel();
        pnb.setLayout(new BuButtonLayout());
        btRechercher_ = new BuButton(BuResource.BU.loadButtonCommandIcon("RECHERCHER"), _("Rechercher"));
        btRechercher_.addActionListener(this);
        getRootPane().setDefaultButton(btRechercher_);
        pnb.add(btRechercher_);
        btFermer_ = new BuButton(BuResource.BU.loadButtonCommandIcon("QUITTER"), _("Fermer"));
        btFermer_.addActionListener(this);
        pnb.add(btFermer_);
        content_.add(pnb, BuBorderLayout.SOUTH);
    }

    public JComponent getComponent() {
        tfMotif_ = new BuTextField();
        tfMotif_.setColumns(20);
        tfMotif_.addActionListener(this);
        cbCasse_ = new BuCheckBox(_("Distinction majuscules/minuscules"));
        BuPanel p = new BuPanel();
        p.setLayout(new BuVerticalLayout());
        p.setBorder(EMPTY5555);
        p.add(new BuLabel(_("Texte � rechercher:")));
        p.add(tfMotif_);
        p.add(cbCasse_);
        p.add(new BuPanel());
        return p;
    }

    public void actionPerformed(ActionEvent _evt) {
        JComponent source = (JComponent) _evt.getSource();
        if ((source == btRechercher_) || (source == tfMotif_)) {
            reponse_ = JOptionPane.OK_OPTION;
            int pos = tcTexte_.getCaretPosition();
            String motif = tfMotif_.getText();
            boolean casse = cbCasse_.isSelected();
            if (pos == tcTexte_.getLength()) pos = 0;
            if ((!tcTexte_.find(motif, pos, !casse)) && (pos > 0)) tcTexte_.find(motif, 0, !casse);
        }
        if (source == btFermer_) {
            reponse_ = JOptionPane.CANCEL_OPTION;
            setVisible(false);
        }
    }
}
