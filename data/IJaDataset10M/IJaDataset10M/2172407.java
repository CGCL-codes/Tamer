package org.fudaa.fudaa.sipor;

import java.awt.Component;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.fudaa.fudaa.commun.projet.FudaaFiltreFichier;
import org.fudaa.fudaa.commun.projet.FudaaProjet;
import com.memoire.bu.BuCommonInterface;
import com.memoire.bu.BuDesktop;
import com.memoire.bu.BuDialogError;
import com.memoire.bu.BuDialogMessage;
import com.memoire.bu.BuEmptyList;
import com.memoire.fu.FuLib;

/**
 * Liste g�rant l'ouverture de plusieurs simulations � la fois.
 * 
 * @version $Revision: 1.13 $ $Date: 2007-07-31 16:16:43 $ by $Author: hadouxad $
 * @author Nicolas Chevalier
 */
public class SiporListeSimulations extends BuEmptyList implements ListSelectionListener {

    DefaultListModel model_;

    SiporImplementation sipor;

    public SiporListeSimulations(final BuCommonInterface appli) {
        super();
        sipor = (SiporImplementation) appli.getImplementation();
        model_ = new DefaultListModel();
        setModel(model_);
        setEmptyText("Aucune simulation");
        setCellRenderer(new AfficheurNomsFichiers());
        addListSelectionListener(this);
    }

    /** Ajoute une simulation dans la liste. */
    public void ajouteSimulation(final String nomFichier, final FudaaProjet projet) {
        sipor.projets_.put(nomFichier, projet);
        if (!model_.contains(nomFichier)) {
            model_.addElement(nomFichier);
        }
        revalidate();
        repaint(200);
    }

    /** Retire une simulation de la liste. */
    public void enleveSimulation(final String nomFichier) {
        sipor.projets_.remove(nomFichier);
        model_.removeElement(nomFichier);
        revalidate();
        repaint(200);
    }

    /** Changement du projet en cours dans l'impl�mentation. */
    public void valueChanged(final ListSelectionEvent evt) {
        if (evt.getValueIsAdjusting()) {
            return;
        }
        final JList source = (JList) evt.getSource();
        sipor.projet_ = (FudaaProjet) sipor.projets_.get(source.getSelectedValue());
        sipor.outils_.setProjet(sipor.projet_);
        sipor.donnees_.changerProjet(sipor.projet_);
        sipor.setTitle(sipor.projet_.getFichier());
        System.out.println("valueChanged : " + sipor.projet_.getFichier());
        sipor.activeActionsExploitation();
        final BuDesktop dk = sipor.getMainPanel().getDesktop();
        if (sipor.fRappelDonnees_ != null) {
            try {
                sipor.fRappelDonnees_.setClosed(true);
                sipor.fRappelDonnees_.setSelected(false);
            } catch (final PropertyVetoException ex) {
            }
        }
        if (sipor.fStatistiquesFinales_ != null) {
            try {
                sipor.fStatistiquesFinales_.setClosed(true);
                sipor.fStatistiquesFinales_.setSelected(false);
            } catch (final PropertyVetoException ex) {
            }
            dk.removeInternalFrame(sipor.fStatistiquesFinales_);
            sipor.fStatistiquesFinales_ = null;
        }
    }

    /** Pour enregistrer la liste des simulations ouvertes dans un fihcier .pro. */
    protected void enregistre() {
        final String[] ext = { "pro" };
        String contenu = "";
        final Object[] tempo = model_.toArray();
        final String[] cles = new String[tempo.length];
        for (int i = 0; i < tempo.length; i++) {
            cles[i] = (String) tempo[i];
        }
        String rpt = cles[0];
        if (rpt.lastIndexOf(File.separator) == -1) {
            rpt = System.getProperty("user.dir") + File.separator + "exemples" + File.separator + "sipor" + File.separator;
        } else {
            rpt = rpt.substring(0, rpt.lastIndexOf(File.separator) + 1);
        }
        String cle = "";
        for (int i = 0; i < cles.length; i++) {
            cle = cles[i];
            if ((cle.startsWith(rpt)) && ((cle.substring(rpt.length())).lastIndexOf(File.separator) == -1)) {
                contenu += cle.substring(rpt.length()) + "\n";
            } else if (rpt.equalsIgnoreCase(System.getProperty("user.dir") + File.separator + "exemples" + File.separator + "sipor" + File.separator)) {
                contenu += cle + "\n";
            } else {
                new BuDialogError(sipor.getApp(), SiporImplementation.isSipor_, "Les simulations doivent etre dans le meme r�pertoire").activate();
                return;
            }
        }
        String nomFichier = MethodesUtiles.choisirFichierEnregistrement(ext, (Component) sipor.getApp(), rpt);
        if (nomFichier == null) {
            return;
        }
        if (!nomFichier.substring(0, nomFichier.lastIndexOf(File.separator) + 1).equals(rpt)) {
            new BuDialogError(sipor.getApp(), SiporImplementation.isSipor_, "La liste doit etre enregistr�e dans\nle meme repertoire que les simulations").activate();
            return;
        }
        if (nomFichier.lastIndexOf(".pro") != (nomFichier.length() - 4)) {
            nomFichier += ".pro";
        }
        MethodesUtiles.enregistrer(nomFichier, contenu, sipor.getApp());
    }

    /** Pour ouvrir un ensemble de simulations (d�fini dans un fichier .pro). */
    protected void ouvrir() {
        final Object[] liste = model_.toArray();
        for (int i = 0; i < liste.length; i++) {
            setSelectedValue(liste[i], true);
            sipor.fermer();
        }
        final String[] ext = { "pro" };
        final String nomFichier = MethodesUtiles.choisirFichier(ext, (Component) sipor.getApp(), null, 1);
        if (nomFichier == null) {
            return;
        }
        final String rpt = nomFichier.substring(0, nomFichier.lastIndexOf(File.separator) + 1);
        final Vector simulations = new Vector();
        BufferedReader lecteur = null;
        try {
            lecteur = new BufferedReader(new FileReader(nomFichier));
            String line = "";
            while ((line = lecteur.readLine()) != null) {
                simulations.add(rpt + line);
            }
        } catch (final IOException ex) {
            new BuDialogError(sipor.getApp(), SiporImplementation.isSipor_, "Lecture du fichier impossible");
        } finally {
            FuLib.safeClose(lecteur);
        }
        for (int i = 0; i < simulations.size(); i++) {
            final FudaaProjet projet = new FudaaProjet(sipor.getApp(), new FudaaFiltreFichier("sipor"));
            projet.setEnrResultats(true);
            projet.ouvrir((String) simulations.elementAt(i));
            if (projet.estConfigure()) {
                ajouteSimulation((String) simulations.elementAt(i), projet);
            }
        }
        if (sipor.projets_.size() > 0) {
            new BuDialogMessage(sipor.getApp(), SiporImplementation.isSipor_, "Fichiers charg�s").activate();
            setSelectedIndex(0);
            sipor.activerCommandesSimulation();
            sipor.activeActionsExploitation();
        }
    }
}
