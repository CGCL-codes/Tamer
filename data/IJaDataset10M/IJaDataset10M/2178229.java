package org.fudaa.fudaa.diapre;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import com.memoire.bu.BuButton;
import com.memoire.bu.BuCommonInterface;
import com.memoire.bu.BuLabel;
import com.memoire.bu.BuPanel;
import org.fudaa.dodico.corba.diapre.SOptions;

/**
 * Description de l'onglet des parametres des options.
 *
 * @version      $Revision: 1.7 $ $Date: 2006-09-19 15:02:13 $ by $Author: deniger $
 * @author       Jean de Malafosse
 */
public class DiapreOptionsParametres extends BuPanel implements ActionListener {

    DiapreOptionsDiscret optionsDiscret;

    boolean blocus = true;

    SOptions parametresOptions;

    final String[] ligne = { "D", "S" };

    double ordonnee1;

    double ordonnee2;

    JComboBox boxligneGlissement = new JComboBox(ligne);

    BuButton discret = new BuButton("Discr�tisation calcul�e par d�faut   ");

    public void placeComposant(final GridBagLayout lm, final Component composant, final GridBagConstraints c) {
        lm.setConstraints(composant, c);
        add(composant);
    }

    public DiapreOptionsParametres(final BuCommonInterface _appli) {
        super();
        final GridBagLayout lm = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        this.setLayout(lm);
        c.ipadx = 5;
        c.ipady = 5;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 100;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        c.weighty = 10;
        final BuLabel gli = new BuLabel("La ligne de glissement est   :       ");
        placeComposant(lm, gli, c);
        c.gridy = 3;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.weighty = 30;
        final BuLabel blab = new BuLabel("________________________________________________________________________________________________");
        placeComposant(lm, blab, c);
        c.ipadx = 50;
        c.ipady = 50;
        c.gridy = 4;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.weighty = 50;
        placeComposant(lm, discret, c);
        discret.addActionListener(new DiapreOptionsDiscretListener(_appli));
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.WEST;
        c.ipadx = 5;
        c.ipady = 5;
        boxligneGlissement.setEditable(false);
        placeComposant(lm, boxligneGlissement, c);
        c.gridy = 1;
        c.weighty = 5;
        final BuLabel lab2 = new BuLabel("   S = Succession de segments  ");
        placeComposant(lm, lab2, c);
        c.gridy = 2;
        c.weighty = 5;
        final BuLabel lab3 = new BuLabel("   D = Une seule droite  ");
        placeComposant(lm, lab3, c);
    }

    public void actionPerformed(final ActionEvent e) {
        if (!blocus) {
            getParametresOptions();
        }
        setVisible(true);
    }

    public void setOrdonnee1(final double y1) {
        ordonnee1 = y1;
    }

    public void setOrdonnee2(final double y2) {
        ordonnee2 = y2;
    }

    public int getHauteur() {
        final double h = 100 * (ordonnee1 - ordonnee2);
        int i = 0;
        while (i < h) {
            i++;
        }
        return (i);
    }

    public void setParametresOptions(final SOptions parametresOptions_) {
        parametresOptions = parametresOptions_;
        if (parametresOptions != null) {
            boxligneGlissement.setSelectedItem(parametresOptions.ligneGlissement);
        }
    }

    public SOptions getParametresOptions() {
        if (parametresOptions == null) {
            parametresOptions = new SOptions();
        }
        if (!blocus) {
            parametresOptions = optionsDiscret.getParametresOptions();
            parametresOptions.ligneGlissement = (String) boxligneGlissement.getSelectedItem();
            System.out.println("Ligne de glissement :" + parametresOptions.ligneGlissement);
        }
        return parametresOptions;
    }

    class DiapreOptionsDiscretListener implements ActionListener {

        private BuCommonInterface appli;

        public DiapreOptionsDiscretListener(final BuCommonInterface appli_) {
            appli = appli_;
        }

        public void actionPerformed(final ActionEvent e) {
            if (blocus) {
                optionsDiscret = new DiapreOptionsDiscret(appli, getHauteur());
                blocus = false;
            }
            optionsDiscret.setParametresOptions(parametresOptions);
            optionsDiscret.setOrdonnee1(ordonnee1);
            optionsDiscret.setOrdonnee2(ordonnee2);
            optionsDiscret.setParametresOptions(parametresOptions);
            optionsDiscret.affiche();
            getParametresOptions();
            optionsDiscret.setVisible(true);
        }
    }
}
