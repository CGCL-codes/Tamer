package org.fudaa.fudaa.hydraulique1d.ihmhelper;

import javax.swing.JComponent;
import org.fudaa.dodico.hydraulique1d.metier.MetierEtude1d;
import org.fudaa.fudaa.hydraulique1d.Hydraulique1dBaseApplication;
import org.fudaa.fudaa.hydraulique1d.Hydraulique1dResource;
import org.fudaa.fudaa.hydraulique1d.editor.Hydraulique1dModeleQEauEditor;
import com.memoire.bu.BuAssistant;

/**
 * Classe faisant le lien entre l'�diteur du noyau du modele de qualit� d'eau et l'aide.
 * G�r� par Hydraulique1dIHMRepository.<br>
 * Utilis� par MascaretImplementation.<br>
 * @version      $Revision: 1.6 $ $Date: 2007-11-20 11:43:17 $ by $Author: bmarchan $
 * @author       Olivier Pasteur
 */
public class Hydraulique1dIHM_ModeleQEau extends Hydraulique1dIHM_Base {

    Hydraulique1dModeleQEauEditor edit_;

    public Hydraulique1dIHM_ModeleQEau(MetierEtude1d e) {
        super(e);
    }

    public void editer() {
        if (edit_ == null) {
            edit_ = new Hydraulique1dModeleQEauEditor();
            edit_.setObject(etude_.qualiteDEau());
            edit_.setObject(etude_.donneesHydro());
            edit_.setObject(etude_.reseau());
            installContextHelp(edit_);
            listenToEditor(edit_);
            BuAssistant ass = Hydraulique1dResource.getAssistant();
            if (ass != null) ass.addEmitters(edit_);
        }
        edit_.show();
    }

    protected void installContextHelp(JComponent e) {
        if (e == null) return;
        ((Hydraulique1dBaseApplication) Hydraulique1dBaseApplication.FRAME).getImplementation().installContextHelp(e.getRootPane(), "mascaret/choix_modeleQEau.html");
    }
}
