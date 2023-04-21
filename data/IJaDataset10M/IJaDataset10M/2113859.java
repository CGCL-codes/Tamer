package org.fudaa.fudaa.sipor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import com.memoire.bu.BuCommonInterface;
import com.memoire.bu.BuDialogMessage;
import com.memoire.bu.BuLabel;
import com.memoire.bu.BuPanel;
import com.memoire.bu.BuTextField;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.dodico.corba.sipor.SParametresCategories;
import org.fudaa.dodico.corba.sipor.SParametresQuai;
import org.fudaa.dodico.corba.sipor.SParametresQuais;

/**
 * Description de l'onglet des param�tres des quais.
 *
 * @version      $Revision: 1.7 $ $Date: 2006-09-19 15:08:57 $ by $Author: deniger $
 * @author       Bertrand Audinet
 */
public class SiporQuaisParametres extends BuPanel implements ActionListener {

    protected JComboBox quais;

    protected JCheckBox active;

    protected BuTextField nom;

    protected BuTextField longueur;

    protected JCheckBox dehalage;

    protected JComboBox bassin;

    private BuLabel label;

    private JDialog message;

    private int oldIndex = -1;

    SiporImplementation sipor;

    BuCommonInterface appli_;

    SParametresQuais localQuais;

    SParametresCategories localCategories;

    /**
   * Constructeur du panneau d'affichage de l'onglet quais.
   */
    public SiporQuaisParametres(final BuCommonInterface _appli) {
        appli_ = _appli;
        sipor = ((SiporImplementation) appli_.getImplementation());
        localQuais = sipor.outils_.getQuais();
        localCategories = sipor.outils_.getCategories();
        final GridBagLayout baglayout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        setLayout(baglayout);
        c.fill = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        quais = new JComboBox();
        for (int i = 0; i < 20; i++) {
            quais.addItem("Quai n� " + (i + 1));
        }
        baglayout.setConstraints(quais, c);
        add(quais);
        label = new BuLabel(" ");
        baglayout.setConstraints(label, c);
        add(label);
        label = new BuLabel("Nom du quai :");
        baglayout.setConstraints(label, c);
        add(label);
        label = new BuLabel("Longueur du quai :");
        baglayout.setConstraints(label, c);
        add(label);
        label = new BuLabel("D�halage du quai :");
        baglayout.setConstraints(label, c);
        add(label);
        label = new BuLabel("Bassin d'appartenance :");
        baglayout.setConstraints(label, c);
        add(label);
        c.gridx = 1;
        active = new JCheckBox("Activer ce quai");
        baglayout.setConstraints(active, c);
        add(active);
        label = new BuLabel(" ");
        baglayout.setConstraints(label, c);
        add(label);
        nom = new BuTextField();
        nom.setColumns(20);
        baglayout.setConstraints(nom, c);
        add(nom);
        longueur = BuTextField.createIntegerField();
        longueur.setColumns(10);
        longueur.setHorizontalAlignment(SwingConstants.RIGHT);
        baglayout.setConstraints(longueur, c);
        add(longueur);
        dehalage = new JCheckBox("Activer");
        baglayout.setConstraints(dehalage, c);
        add(dehalage);
        bassin = new JComboBox();
        baglayout.setConstraints(bassin, c);
        add(bassin);
    }

    /**
   * Accesseur de l'onglet des quais.
   */
    public SParametresQuais getQuais() {
        if (localQuais.nombreQuais != 0) {
            sauvegardeQuai(oldIndex);
            return localQuais;
        }
        return null;
    }

    /**
   * Mutateur de l'onglet des quais.
   */
    public void setQuais(final SParametresQuais params) {
        localQuais = sipor.outils_.getQuais();
        localCategories = sipor.outils_.getCategories();
        localQuais.nombreQuais = params.nombreQuais;
        for (int i = 0; i < params.nombreQuais; i++) {
            localQuais.quais[i] = params.quais[i];
        }
        if (bassin.getItemCount() != 0) {
            bassin.removeAllItems();
        }
        bassin.addItem("          ");
        if (sipor.fp_.pnBassins.cbUtilisationBassin1.isSelected()) {
            bassin.addItem("bassin n�1");
        }
        if (sipor.fp_.pnBassins.cbUtilisationBassin2.isSelected()) {
            bassin.addItem("bassin n�2");
        }
        if (sipor.fp_.pnBassins.cbUtilisationBassin3.isSelected()) {
            bassin.addItem("bassin n�3");
        }
        quais.removeActionListener(this);
        quais.setSelectedIndex(0);
        quais.addActionListener(this);
        oldIndex = 0;
        afficheQuai(0);
    }

    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == active) {
            if (active.isSelected()) {
                if (oldIndex == localQuais.nombreQuais) {
                    localQuais.nombreQuais++;
                    localQuais.quais[oldIndex] = new SParametresQuai(new String(), 0, false, 0);
                } else {
                    messageAssistant("L'activation des quais se fait dans\n" + "l'ordre croissant des num�ros de quais.");
                }
            } else {
                if (oldIndex == (localQuais.nombreQuais - 1)) {
                    if (quaiLibre(oldIndex) == -1) {
                        localQuais.nombreQuais--;
                        localQuais.quais[oldIndex] = null;
                    } else {
                        messageAssistant("Avant de supprimer ce quai, vous devez\n" + "supprimer ou modifier le ou les cat�gories\n" + "qui l'utilisent comme quai pr�f�rentiel,\n" + "notamment la cat�gorie n� " + (quaiLibre(oldIndex) + 1) + CtuluLibString.DOT);
                    }
                } else {
                    messageAssistant("La suppression des quais se fait dans\n" + "l'ordre d�croissant des num�ros de quais.");
                }
            }
        } else if (e.getSource() == quais) {
            sauvegardeQuai(oldIndex);
            oldIndex = quais.getSelectedIndex();
        }
        afficheQuai(oldIndex);
    }

    /**
   * v�rifie les parametres saisis et renvoie les erreurs fatales.
   */
    public static String chercheErreur(final SParametresQuais params) {
        String mes = "";
        if (params.nombreQuais == 0) {
            mes += "Un quai obligatoire pour la simulation.\n";
        } else {
            for (int i = 0; i < params.nombreQuais; i++) {
                if (params.quais[i].nomQuai.equalsIgnoreCase("")) {
                    mes += "Erreur Quai n� " + (i + 1) + " : nom inconnu.\n";
                } else {
                    for (int j = 0; j < params.nombreQuais; j++) {
                        if ((params.quais[i].nomQuai.equalsIgnoreCase(params.quais[j].nomQuai)) && (i != j)) {
                            mes += "Erreur Quai n� " + (i + 1) + " : nom identique pour le quai n� " + (j + 1) + ".\n";
                        }
                    }
                }
                if (params.quais[i].longueurQuai == 0) {
                    mes += "Erreur Quai n� " + (i + 1) + " : longueur incorrecte.\n";
                }
                if (params.quais[i].bassinAppartenance == 0) {
                    mes += "Erreur Quai n� " + (i + 1) + " : bassin d'appartenance inconnu.\n";
                }
            }
        }
        return mes;
    }

    private void composantsEnabled(final boolean b) {
        nom.setEnabled(b);
        longueur.setEnabled(b);
        dehalage.setEnabled(b);
        bassin.setEnabled(b);
    }

    private void afficheQuai(final int index) {
        active.removeActionListener(this);
        if (localQuais.quais[index] != null) {
            active.setSelected(true);
            composantsEnabled(true);
            nom.setValue(localQuais.quais[index].nomQuai);
            longueur.setValue(new Integer(localQuais.quais[index].longueurQuai));
            dehalage.setSelected(localQuais.quais[index].dehalageQuai);
            if (localQuais.quais[index].bassinAppartenance == 0) {
                bassin.setSelectedIndex(0);
            } else {
                for (int i = 1; i < bassin.getItemCount(); i++) {
                    if ((((String) bassin.getItemAt(i)).substring(9)).equalsIgnoreCase((new Integer(localQuais.quais[index].bassinAppartenance)).toString())) {
                        bassin.setSelectedIndex(i);
                    }
                }
            }
        } else {
            active.setSelected(false);
            composantsEnabled(false);
            nom.setValue("");
            longueur.setValue(new Integer(0));
            dehalage.setSelected(false);
            bassin.setSelectedIndex(0);
        }
        active.addActionListener(this);
    }

    private void sauvegardeQuai(final int index) {
        if (localQuais.quais[index] != null) {
            if (((String) nom.getValue()).length() > 80) {
                localQuais.quais[index].nomQuai = ((String) nom.getValue()).substring(0, 80);
            } else {
                localQuais.quais[index].nomQuai = (String) nom.getValue();
            }
            localQuais.quais[index].longueurQuai = ((Integer) longueur.getValue()).intValue();
            localQuais.quais[index].dehalageQuai = dehalage.isSelected();
            if (bassin.getSelectedIndex() == 0) {
                localQuais.quais[index].bassinAppartenance = 0;
            } else {
                localQuais.quais[index].bassinAppartenance = (new Integer(((String) bassin.getItemAt(bassin.getSelectedIndex())).substring(9))).intValue();
            }
        }
    }

    private int quaiLibre(final int index) {
        for (int i = 0; i < localCategories.nombreCategories; i++) {
            for (int j = 0; j < localCategories.categories[i].nombreQuaisPreferentiels; j++) {
                if (localCategories.categories[i].quaisPreferentiels[j].numeroQuai == (index + 1)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void messageAssistant(final String s) {
        message = new BuDialogMessage(appli_, SiporImplementation.isSipor_, s);
        message.setSize(500, 200);
        message.setResizable(false);
        message.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        final Point pos = this.getLocationOnScreen();
        pos.x = pos.x + this.getWidth() / 2 - message.getWidth() / 2;
        pos.y = pos.y + this.getHeight() / 2 - message.getHeight() / 2;
        message.setLocation(pos);
        message.setVisible(true);
    }
}
