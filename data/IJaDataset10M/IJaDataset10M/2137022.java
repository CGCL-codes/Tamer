package org.fudaa.fudaa.sipor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import org.fudaa.ctulu.CtuluLibFile;
import org.fudaa.ctulu.table.CtuluTableExcelWriter;
import org.fudaa.fudaa.ressource.FudaaResource;
import com.memoire.bu.BuButton;
import com.memoire.bu.BuDialogMessage;
import com.memoire.bu.BuTable;
import com.memoire.fu.FuLog;

/**
 * @version $Version$
 * @author hadoux
 */
public class SiporPanelReglesParcoursCercle extends SiporInternalFrame {

    /**
   * Tableau contenant les donn�es du tableau affich� en java (definition attribut car sert pour transformation excel)
   */
    Object[][] ndata;

    /**
   * Descriptif des elements des colonnes
   */
    String[] titreColonnes_;

    /**
   * Tableau de type JTable qui contiendra les donnees des navires
   */
    BuTable tableau_;

    /**
   * Modele qui contient la partie metier des donn�es.
   * Utilisee comme mod�le du tableau
   */
    SiporModeleDureeParcoursCercles modeleTableau_;

    /**
   * Nombre d'objets
   */
    int nbElem_;

    /**
   * Bouton de validation des regles de navigations.
   */
    final BuButton validation_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_oui"), "valider");

    private final BuButton impression_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_generer"), "Excel");

    /**
   * Panel qui contiendra le tableau.
   */
    JPanel global_ = new JPanel();

    /**
   * panel de controle
   */
    JPanel controlPanel = new JPanel();

    /**
   * Bordure du tableau.
   */
    Border borduretab_ = BorderFactory.createLoweredBevelBorder();

    /**
   * donn�es de la simulation
   */
    SiporDataSimulation donnees_;

    /**
   * Indice du cercle choisi (par defaut 0 => le premier cercle.
   */
    int cercleChoisi_ = 0;

    /**
   * constructeur du panel d'affichage des bassins.
   * 
   * @param d donn�es de la simulation.
   */
    SiporPanelReglesParcoursCercle(final SiporDataSimulation _donnees) {
        super("", true, true, true, true);
        donnees_ = _donnees;
        modeleTableau_ = new SiporModeleDureeParcoursCercles(donnees_);
        global_.setLayout(new BorderLayout());
        this.affichage();
        setTitle("Duree de parcours au sein des cercles d'evitage (en Heures.Minutes) ");
        setSize(800, 400);
        setBorder(SiporBordures.compound_);
        getContentPane().setLayout(new BorderLayout());
        final JScrollPane ascenceur = new JScrollPane(global_);
        getContentPane().add(ascenceur, BorderLayout.CENTER);
        controlPanel.setBorder(this.borduretab_);
        controlPanel.add(this.impression_);
        controlPanel.add(validation_);
        getContentPane().add(controlPanel, BorderLayout.SOUTH);
        this.impression_.setToolTipText("Genere un fichier excel du tableau. Attention, ce bouton ne genere que le tableau du cercle affich�!!");
        this.impression_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                tableau_.editCellAt(0, 0);
                File fichier;
                final JFileChooser fc = new JFileChooser();
                final int returnVal = fc.showOpenDialog(SiporPanelReglesParcoursCercle.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    fichier = fc.getSelectedFile();
                    final File f = CtuluLibFile.appendExtensionIfNeeded(fichier, "xls");
                    final SiporModeleExcel modele = modeleTableau_;
                    final CtuluTableExcelWriter ecrivain = new CtuluTableExcelWriter(modele, f);
                    try {
                        ecrivain.write(null);
                        new BuDialogMessage(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Fichier Excel g�n�r� avec succ�s.").activate();
                    } catch (final RowsExceededException _err) {
                        FuLog.error(_err);
                    } catch (final WriteException _err) {
                        FuLog.error(_err);
                    } catch (final IOException _err) {
                        FuLog.error(_err);
                    }
                }
            }
        });
        this.validation_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    /**
   * Methode d affichage des composants du BuTable et du tableau de combo Cette methode est a impl�menter dans les
   * classes d�riv�es pour chaque composants
   */
    void affichage() {
        tableau_ = new BuTable(new SiporModeleDureeParcoursCercles(donnees_));
        tableau_.setDefaultEditor(Object.class, new SiporCellEditorDureeParcours());
        this.global_.add(tableau_.getTableHeader(), BorderLayout.PAGE_START);
        this.global_.add(tableau_, BorderLayout.CENTER);
        this.global_.revalidate();
        this.global_.updateUI();
        this.revalidate();
        this.updateUI();
    }
}
