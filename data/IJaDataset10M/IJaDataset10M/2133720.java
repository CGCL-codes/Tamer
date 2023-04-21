package org.fudaa.fudaa.sipor;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.WindowConstants;
import com.memoire.bu.BuButton;
import com.memoire.bu.BuCommonInterface;
import com.memoire.bu.BuDialogMessage;
import com.memoire.bu.BuLabel;
import com.memoire.bu.BuLib;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.dodico.corba.sipor.SParametresCategorie;
import org.fudaa.dodico.corba.sipor.SParametresCategories;
import org.fudaa.dodico.corba.sipor.SParametresQuaiPreferentiel;
import org.fudaa.dodico.corba.sipor.SParametresQuais;

/**
 * une boite de dialogue pour la saisie des quais pr�f�rentiels.
 *
 * @version      $Revision: 1.6 $ $Date: 2006-09-19 15:08:57 $ by $Author: deniger $
 * @author       Bertrand Audinet
 */
public class SiporQuaisPreferentielsParametres extends JDialog implements FocusListener, ActionListener {

    protected JComboBox numeroPreferentiel;

    protected JCheckBox active;

    protected JComboBox listeQuais;

    protected DureeField dureeAttente;

    protected DureeField dureeMinOperation;

    protected DureeField dureeMoyOperation;

    protected DureeField dureeMaxOperation;

    protected JComboBox ordreErlang;

    protected BuButton BOk;

    protected BuButton BAnnuler;

    private BuLabel label;

    private SParametresCategorie Categorie;

    private int oldIndexPreference;

    private int oldIndexQuais;

    private BuDialogMessage message;

    private SiporImplementation sipor;

    private BuCommonInterface appli_;

    private SParametresCategories localCategories;

    private SParametresQuais localQuais;

    /**
   * Constructeur de la boite de dialogue pour la saisie des quais pr�f�rentiels.
   */
    public SiporQuaisPreferentielsParametres(final BuCommonInterface _appli) {
        super(_appli instanceof Frame ? (Frame) _appli : (Frame) null, "Description des quais Pr�f�rentiels", true);
        appli_ = _appli;
        sipor = ((SiporImplementation) appli_.getImplementation());
        localCategories = sipor.outils_.getCategories();
        localQuais = sipor.outils_.getQuais();
        setResizable(false);
        final GridBagLayout baglayout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        getContentPane().setLayout(baglayout);
        c.fill = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridy = GridBagConstraints.RELATIVE;
        c.gridx = 0;
        c.anchor = GridBagConstraints.EAST;
        numeroPreferentiel = new JComboBox();
        baglayout.setConstraints(numeroPreferentiel, c);
        getContentPane().add(numeroPreferentiel);
        c.anchor = GridBagConstraints.WEST;
        label = new BuLabel(CtuluLibString.ESPACE);
        baglayout.setConstraints(label, c);
        getContentPane().add(label);
        label = new BuLabel("Noms du quai pr�f�rentiel :");
        baglayout.setConstraints(label, c);
        getContentPane().add(label);
        label = new BuLabel("Dur�e d'attente admissible :");
        baglayout.setConstraints(label, c);
        getContentPane().add(label);
        label = new BuLabel("Dur�e minimum des op�rations :");
        baglayout.setConstraints(label, c);
        getContentPane().add(label);
        label = new BuLabel("Dur�e moyenne des op�rations :");
        baglayout.setConstraints(label, c);
        getContentPane().add(label);
        label = new BuLabel("Dur�e maximum des op�rations :");
        baglayout.setConstraints(label, c);
        getContentPane().add(label);
        label = new BuLabel("Ordre de la loi Erlang :");
        baglayout.setConstraints(label, c);
        getContentPane().add(label);
        label = new BuLabel(CtuluLibString.ESPACE);
        baglayout.setConstraints(label, c);
        getContentPane().add(label);
        c.anchor = GridBagConstraints.EAST;
        BOk = new BuButton(BuLib.loadCommandIcon("VALIDER"), "Valider");
        BOk.addActionListener(this);
        baglayout.setConstraints(BOk, c);
        getContentPane().add(BOk);
        c.gridx = 1;
        c.anchor = GridBagConstraints.WEST;
        active = new JCheckBox("Activer");
        baglayout.setConstraints(active, c);
        getContentPane().add(active);
        label = new BuLabel(CtuluLibString.ESPACE);
        baglayout.setConstraints(label, c);
        getContentPane().add(label);
        listeQuais = new JComboBox();
        baglayout.setConstraints(listeQuais, c);
        getContentPane().add(listeQuais);
        dureeAttente = new DureeField(false, false, true, true);
        dureeAttente.addFocusListener(this);
        baglayout.setConstraints(dureeAttente, c);
        getContentPane().add(dureeAttente);
        dureeMinOperation = new DureeField(false, false, true, true);
        dureeMinOperation.addFocusListener(this);
        baglayout.setConstraints(dureeMinOperation, c);
        getContentPane().add(dureeMinOperation);
        dureeMoyOperation = new DureeField(false, false, true, true);
        dureeMoyOperation.addFocusListener(this);
        baglayout.setConstraints(dureeMoyOperation, c);
        getContentPane().add(dureeMoyOperation);
        dureeMaxOperation = new DureeField(false, false, true, true);
        dureeMaxOperation.addFocusListener(this);
        baglayout.setConstraints(dureeMaxOperation, c);
        getContentPane().add(dureeMaxOperation);
        ordreErlang = new JComboBox();
        ordreErlang.addItem("   ");
        ordreErlang.addItem(" 1");
        ordreErlang.addItem(" 2");
        ordreErlang.addItem(" 3");
        ordreErlang.addItem(" 4");
        ordreErlang.addFocusListener(this);
        baglayout.setConstraints(ordreErlang, c);
        getContentPane().add(ordreErlang);
        label = new BuLabel(CtuluLibString.ESPACE);
        baglayout.setConstraints(label, c);
        getContentPane().add(label);
        BAnnuler = new BuButton(BuLib.loadCommandIcon("EFFACER"), "Effacer");
        BAnnuler.addActionListener(this);
        baglayout.setConstraints(BAnnuler, c);
        getContentPane().add(BAnnuler);
        final Point point = new Point(((Frame) _appli).getLocation());
        point.translate(100, 100);
        setLocation(point.getLocation());
        pack();
    }

    /**
   * initialise la boite de dialogue aux quais pr�f�rentiels de la cat�gorie active.
   */
    public void setValue(final int index) {
        if ((index >= 0) && (index < localCategories.nombreCategories)) {
            Categorie = localCategories.categories[index];
            oldIndexPreference = 0;
            oldIndexQuais = 0;
            if (numeroPreferentiel.getItemCount() != 0) {
                numeroPreferentiel.removeAllItems();
            }
            for (int i = 0; i < localQuais.nombreQuais; i++) {
                numeroPreferentiel.addItem("Pr�f�rence n� " + (new Integer(i + 1)).toString());
            }
            if (listeQuais.getItemCount() != 0) {
                listeQuais.removeAllItems();
            }
            listeQuais.addItem("          ");
            for (int i = 0; i < localQuais.nombreQuais; i++) {
                if (localQuais.quais[i].nomQuai.length() > 14) {
                    listeQuais.addItem("" + (i + 1) + ": " + localQuais.quais[i].nomQuai.substring(0, 10) + "...");
                } else {
                    listeQuais.addItem("" + (i + 1) + ": " + localQuais.quais[i].nomQuai);
                }
            }
            numeroPreferentiel.addActionListener(this);
            afficheQuaiPreferentiel(0);
        }
    }

    public void focusGained(final FocusEvent e) {
    }

    public void focusLost(final FocusEvent e) {
        if ((oldIndexPreference == 1) && ((e.getSource() == dureeMinOperation) || (e.getSource() == dureeMoyOperation) || (e.getSource() == dureeMaxOperation) || (e.getSource() == ordreErlang))) {
            Categorie.quaisPreferentiels[1].dureeMinOperations = dureeMinOperation.getDureeFieldLong();
            Categorie.quaisPreferentiels[1].dureeMoyOperations = dureeMoyOperation.getDureeFieldLong();
            Categorie.quaisPreferentiels[1].dureeMaxOperations = dureeMaxOperation.getDureeFieldLong();
            Categorie.quaisPreferentiels[1].ordreLoiErlang = ordreErlang.getSelectedIndex();
            for (int i = 2; i < Categorie.nombreQuaisPreferentiels; i++) {
                Categorie.quaisPreferentiels[i].dureeMinOperations = Categorie.quaisPreferentiels[1].dureeMinOperations;
                Categorie.quaisPreferentiels[i].dureeMoyOperations = Categorie.quaisPreferentiels[1].dureeMoyOperations;
                Categorie.quaisPreferentiels[i].dureeMaxOperations = Categorie.quaisPreferentiels[1].dureeMaxOperations;
                Categorie.quaisPreferentiels[i].ordreLoiErlang = Categorie.quaisPreferentiels[1].ordreLoiErlang;
            }
        }
    }

    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == BOk) {
            sauvegardeQuaiPreferentiel(oldIndexPreference);
            listeQuais.removeActionListener(this);
            active.removeActionListener(this);
            numeroPreferentiel.removeActionListener(this);
            setVisible(false);
        } else if (e.getSource() == BAnnuler) {
            listeQuais.removeActionListener(this);
            active.removeActionListener(this);
            numeroPreferentiel.removeActionListener(this);
            for (int i = 0; i < Categorie.nombreQuaisPreferentiels; i++) {
                Categorie.quaisPreferentiels[i] = null;
            }
            Categorie.nombreQuaisPreferentiels = 0;
            setVisible(false);
        } else if (e.getSource() == numeroPreferentiel) {
            sauvegardeQuaiPreferentiel(oldIndexPreference);
            oldIndexPreference = numeroPreferentiel.getSelectedIndex();
        } else if (e.getSource() == active) {
            if (active.isSelected()) {
                if (oldIndexPreference == Categorie.nombreQuaisPreferentiels) {
                    Categorie.nombreQuaisPreferentiels++;
                    Categorie.quaisPreferentiels[oldIndexPreference] = new SParametresQuaiPreferentiel(0, 0, 0, 0, 0, 0);
                    sauvegardeQuaiPreferentiel(oldIndexPreference);
                } else {
                    messageAssistant("Activez les pr�f�rences dans l'ordre\n" + "croissant de leur numero.");
                }
            } else {
                if (oldIndexPreference == (Categorie.nombreQuaisPreferentiels - 1)) {
                    Categorie.nombreQuaisPreferentiels--;
                    Categorie.quaisPreferentiels[oldIndexPreference] = null;
                } else {
                    messageAssistant("D�sactivez les Pr�f�rences dans\n" + "l'ordre d�croissant de leur num�ro.");
                }
            }
        } else if (e.getSource() == listeQuais) {
            if (listeQuais.getSelectedIndex() != 0) {
                if (dejaAccessible(listeQuais.getSelectedIndex()) != -1) {
                    messageAssistant("Ce quai est d�j� utilis� pour la\n" + "pr�f�rence n� " + (dejaAccessible(listeQuais.getSelectedIndex()) + 1) + CtuluLibString.DOT);
                } else {
                    oldIndexQuais = listeQuais.getSelectedIndex();
                    Categorie.quaisPreferentiels[oldIndexPreference].numeroQuai = oldIndexQuais;
                }
            }
        }
        afficheQuaiPreferentiel(oldIndexPreference);
    }

    private void composantsEnabled(final boolean b) {
        listeQuais.setEnabled(b);
        dureeAttente.setEnabled(b);
        dureeMinOperation.setEnabled(b);
        dureeMoyOperation.setEnabled(b);
        dureeMaxOperation.setEnabled(b);
        ordreErlang.setEnabled(b);
    }

    private void afficheQuaiPreferentiel(final int index) {
        listeQuais.removeActionListener(this);
        active.removeActionListener(this);
        if (Categorie.quaisPreferentiels[index] != null) {
            if ((index == 0) || (index == 1)) {
                composantsEnabled(true);
            } else {
                composantsEnabled(false);
                listeQuais.setEnabled(true);
                dureeAttente.setEnabled(true);
            }
            active.setSelected(true);
            listeQuais.setSelectedIndex(Categorie.quaisPreferentiels[index].numeroQuai);
            dureeAttente.setDureeField(Categorie.quaisPreferentiels[index].dureeAttenteAdmissible);
            dureeMinOperation.setDureeField(Categorie.quaisPreferentiels[index].dureeMinOperations);
            dureeMoyOperation.setDureeField(Categorie.quaisPreferentiels[index].dureeMoyOperations);
            dureeMaxOperation.setDureeField(Categorie.quaisPreferentiels[index].dureeMaxOperations);
            ordreErlang.setSelectedIndex(Categorie.quaisPreferentiels[index].ordreLoiErlang);
        } else {
            composantsEnabled(false);
            active.setSelected(false);
            listeQuais.setSelectedIndex(0);
            dureeAttente.setDureeField(0);
            dureeMinOperation.setDureeField(0);
            dureeMoyOperation.setDureeField(0);
            dureeMaxOperation.setDureeField(0);
            ordreErlang.setSelectedIndex(0);
        }
        oldIndexQuais = listeQuais.getSelectedIndex();
        listeQuais.addActionListener(this);
        active.addActionListener(this);
    }

    private void sauvegardeQuaiPreferentiel(final int index) {
        if (Categorie.quaisPreferentiels[index] != null) {
            if (index < 2) {
                Categorie.quaisPreferentiels[index].numeroQuai = listeQuais.getSelectedIndex();
                Categorie.quaisPreferentiels[index].dureeAttenteAdmissible = dureeAttente.getDureeFieldLong();
                Categorie.quaisPreferentiels[index].dureeMinOperations = dureeMinOperation.getDureeFieldLong();
                Categorie.quaisPreferentiels[index].dureeMoyOperations = dureeMoyOperation.getDureeFieldLong();
                Categorie.quaisPreferentiels[index].dureeMaxOperations = dureeMaxOperation.getDureeFieldLong();
                Categorie.quaisPreferentiels[index].ordreLoiErlang = ordreErlang.getSelectedIndex();
            } else {
                Categorie.quaisPreferentiels[index].numeroQuai = listeQuais.getSelectedIndex();
                Categorie.quaisPreferentiels[index].dureeAttenteAdmissible = dureeAttente.getDureeFieldLong();
                Categorie.quaisPreferentiels[index].dureeMinOperations = Categorie.quaisPreferentiels[1].dureeMinOperations;
                Categorie.quaisPreferentiels[index].dureeMoyOperations = Categorie.quaisPreferentiels[1].dureeMoyOperations;
                Categorie.quaisPreferentiels[index].dureeMaxOperations = Categorie.quaisPreferentiels[1].dureeMaxOperations;
                Categorie.quaisPreferentiels[index].ordreLoiErlang = Categorie.quaisPreferentiels[1].ordreLoiErlang;
            }
        }
    }

    private int dejaAccessible(final int index) {
        if (index != 0) {
            for (int i = 0; i < Categorie.nombreQuaisPreferentiels; i++) {
                if ((Categorie.quaisPreferentiels[i].numeroQuai == index) && (i != index)) {
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
