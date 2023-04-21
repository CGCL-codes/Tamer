package vue;

import javax.swing.*;
import vue.util.Util;
import controle.*;

public class BarreMenu extends JMenuBar {

    private ListeOnglets listeOnglets;

    private JMenu menu, menu2;

    private JMenuItem it2D, it3D, itOuvrir, itFermer, itFermertout, itEnregistrer, itEnregistrersous, itImprimer, itExportPdf, itQuitter, itAide, itApropos;

    public BarreMenu(ListeOnglets listeOnglets) {
        this.listeOnglets = listeOnglets;
        menu = new JMenu("Fichier");
        menu.setMnemonic('F');
        menu2 = new JMenu("Nouveau");
        menu2.setMnemonic('N');
        menu2.setIcon(Util.createImageIcon("img/new.png"));
        it2D = new JMenuItem("2D");
        it2D.setMnemonic('2');
        it2D.setActionCommand("nouveau2D");
        it2D.setAccelerator(KeyStroke.getKeyStroke("ctrl F2"));
        it2D.addActionListener(new ControleActionMenu(this, listeOnglets));
        menu2.add(it2D);
        it3D = new JMenuItem("3D");
        it3D.setMnemonic('3');
        it3D.setActionCommand("nouveau3D");
        it3D.setAccelerator(KeyStroke.getKeyStroke("ctrl F3"));
        it3D.addActionListener(new ControleActionMenu(this, listeOnglets));
        menu2.add(it3D);
        menu.add(menu2);
        itOuvrir = new JMenuItem("Ouvrir...");
        itOuvrir.setMnemonic('O');
        itOuvrir.setActionCommand("ouvrir");
        itOuvrir.setIcon(Util.createImageIcon("img/open.png"));
        itOuvrir.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        itOuvrir.addActionListener(new ControleActionMenu(this, listeOnglets));
        menu.add(itOuvrir);
        menu.addSeparator();
        itFermer = new JMenuItem("Fermer");
        itFermer.setMnemonic('F');
        itFermer.setActionCommand("fermer");
        itFermer.setIcon(Util.createImageIcon("img/no.png"));
        itFermer.setAccelerator(KeyStroke.getKeyStroke("ctrl W"));
        itFermer.addActionListener(new ControleActionMenu(this, listeOnglets));
        menu.add(itFermer);
        itFermertout = new JMenuItem("Fermer tout");
        itFermertout.setMnemonic('T');
        itFermertout.setActionCommand("fermer_tout");
        itFermertout.setIcon(Util.createImageIcon("img/no.png"));
        itFermertout.setAccelerator(KeyStroke.getKeyStroke("ctrl shift W"));
        itFermertout.addActionListener(new ControleActionMenu(this, listeOnglets));
        menu.add(itFermertout);
        menu.addSeparator();
        itEnregistrer = new JMenuItem("Enregistrer");
        itEnregistrer.setMnemonic('g');
        itEnregistrer.setActionCommand("enregistrer");
        itEnregistrer.setIcon(Util.createImageIcon("img/save.png"));
        itEnregistrer.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        itEnregistrer.addActionListener(new ControleActionMenu(this, listeOnglets));
        menu.add(itEnregistrer);
        itEnregistrersous = new JMenuItem("Enregistrer sous");
        itEnregistrersous.setMnemonic('r');
        itEnregistrersous.setActionCommand("enregistrer_sous");
        itEnregistrersous.setIcon(Util.createImageIcon("img/save_as.png"));
        itEnregistrersous.addActionListener(new ControleActionMenu(this, listeOnglets));
        menu.add(itEnregistrersous);
        menu.addSeparator();
        itImprimer = new JMenuItem("Imprimer...");
        itImprimer.setMnemonic('I');
        itImprimer.setActionCommand("imprimer");
        itImprimer.setIcon(Util.createImageIcon("img/print.png"));
        itImprimer.setAccelerator(KeyStroke.getKeyStroke("ctrl P"));
        itImprimer.addActionListener(new ControleActionMenu(this, listeOnglets));
        menu.add(itImprimer);
        menu.addSeparator();
        itExportPdf = new JMenuItem("Exporter en PDF");
        itExportPdf.setMnemonic('E');
        itExportPdf.setActionCommand("export_pdf");
        itExportPdf.setIcon(Util.createImageIcon("img/pdf.gif"));
        itExportPdf.addActionListener(new ControleActionMenu(this, listeOnglets));
        menu.add(itExportPdf);
        menu.addSeparator();
        itQuitter = new JMenuItem("Quitter");
        itQuitter.setMnemonic('Q');
        itQuitter.setActionCommand("quitter");
        itQuitter.setIcon(Util.createImageIcon("img/exit.png"));
        itQuitter.setAccelerator(KeyStroke.getKeyStroke("alt F4"));
        itQuitter.addActionListener(new ControleActionMenu(this, listeOnglets));
        menu.add(itQuitter);
        this.add(menu);
        menu = new JMenu("?");
        menu.setMnemonic('?');
        itAide = new JMenuItem("Aide sur Simplexe");
        itAide.setMnemonic('A');
        itAide.setActionCommand("aide");
        itAide.setIcon(Util.createImageIcon("img/help.png"));
        itAide.setAccelerator(KeyStroke.getKeyStroke("F1"));
        itAide.addActionListener(new ControleActionMenu(this, listeOnglets));
        menu.add(itAide);
        itApropos = new JMenuItem("       A propos de...");
        itApropos.setMnemonic('p');
        itApropos.setActionCommand("aproposde");
        itApropos.addActionListener(new ControleActionMenu(this, listeOnglets));
        menu.add(itApropos);
        this.add(menu);
        this.agirItemsOnglet();
    }

    public void agirItemsOnglet() {
        if (listeOnglets.getComponentCount() == 0) {
            this.desactiverItemsOnglet();
        } else {
            this.activerItemsOnglet();
        }
    }

    private void activerItemsOnglet() {
        itFermer.setEnabled(true);
        itFermertout.setEnabled(true);
        itEnregistrer.setEnabled(true);
        itEnregistrersous.setEnabled(true);
        itImprimer.setEnabled(true);
        itExportPdf.setEnabled(true);
    }

    private void desactiverItemsOnglet() {
        itFermer.setEnabled(false);
        itFermertout.setEnabled(false);
        itEnregistrer.setEnabled(false);
        itEnregistrersous.setEnabled(false);
        itImprimer.setEnabled(false);
        itExportPdf.setEnabled(false);
    }
}
