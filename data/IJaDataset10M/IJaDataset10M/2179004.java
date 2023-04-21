package org.fudaa.fudaa.diapre;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.PrintJob;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import com.memoire.bu.BuCommonInterface;
import com.memoire.bu.BuInformationsDocument;
import com.memoire.bu.BuPrinter;
import com.memoire.bu.BuToggleButton;
import org.fudaa.dodico.corba.diapre.SParametresDiapre;
import org.fudaa.ebli.graphe.BGraphe;
import org.fudaa.ebli.graphe.EbliFilleGraphe;

/**
 * Fenetre d'affichage d'un graphique.
 *
 * @version      $Revision: 1.8 $ $Date: 2007-05-04 13:59:05 $ by $Author: deniger $
 * @author       Jean de Malafosse 
 */
public class DiapreRappelDonnees extends EbliFilleGraphe implements InternalFrameListener {

    protected DiapreImplementation diapre;

    SParametresDiapre parametresDiapre;

    BuToggleButton valider;

    public void placeComposant(final GridBagLayout lm, final Component composant, final GridBagConstraints c) {
        lm.setConstraints(composant, c);
        getContentPane().add(composant);
    }

    public DiapreRappelDonnees(final BuCommonInterface _appli, final BuInformationsDocument _id) {
        super("Rappel de donn�es - graphique", true, true, true, true, _appli, _id);
        diapre = (DiapreImplementation) _appli.getImplementation();
        setBackground(Color.white);
        getContentPane().setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 450));
        pack();
        addInternalFrameListener(this);
        setDefaultCloseOperation(1);
        diapre.addInternalFrame(this);
    }

    public BGraphe getGraphe() {
        return graphe_;
    }

    public void setGraphe(final BGraphe _graphe) {
        getContentPane().removeAll();
        graphe_ = _graphe;
        diapre.setGraph(graphe_);
        getContentPane().add(graphe_, BorderLayout.CENTER);
        pack();
        diapre.setEnabledForAction("IMPRIMER", true);
        diapre.photoRapp();
    }

    public void actionPerformed(final ActionEvent _evt) {
    }

    /**
   * Autorise l'impression.
   */
    public String[] getEnabledActions() {
        final String[] r = new String[] { "IMPRIMER", "PREVISUALISER" };
        return r;
    }

    /**
   * Methode d'impression d'un graphe.
   */
    public void print(final PrintJob _job, final Graphics _g) {
        BuPrinter.INFO_DOC = new BuInformationsDocument();
        BuPrinter.INFO_DOC.name = getTitle();
        BuPrinter.INFO_DOC.logo = null;
        BuPrinter.printComponent(_job, _g, graphe_);
    }

    public void internalFrameClosed(final InternalFrameEvent e) {
        setVisible(false);
        diapre.removeInternalFrame(this);
    }

    public void internalFrameDeactivated(final InternalFrameEvent e) {
        diapre.setEnabledForAction("FERMER", true);
        diapre.setEnabledForAction("PHOTOGRAPHIE", false);
        diapre.setEnabledForAction("IMPRIMER", false);
    }

    public void internalFrameActivated(final InternalFrameEvent e) {
    }

    public void internalFrameOpened(final InternalFrameEvent e) {
    }

    public void internalFrameClosing(final InternalFrameEvent e) {
    }

    public void internalFrameIconified(final InternalFrameEvent e) {
    }

    public void internalFrameDeiconified(final InternalFrameEvent e) {
    }

    class DiapreValiderListener implements ActionListener {

        public void actionPerformed(final ActionEvent e) {
            dispose();
        }
    }
}
