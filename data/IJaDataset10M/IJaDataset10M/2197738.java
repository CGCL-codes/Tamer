package org.fudaa.fudaa.piv.action;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.SwingUtilities;
import org.fudaa.ctulu.CtuluAnalyze;
import org.fudaa.ctulu.gui.CtuluDialog;
import org.fudaa.ctulu.gui.CtuluTaskOperationGUI;
import org.fudaa.ebli.commun.EbliActionSimple;
import org.fudaa.ebli.geometrie.GrPolyligne;
import org.fudaa.fudaa.piv.PivExePanel;
import org.fudaa.fudaa.piv.PivFlowInfoPanel;
import org.fudaa.fudaa.piv.PivImplementation;
import org.fudaa.fudaa.piv.PivPreferences;
import org.fudaa.fudaa.piv.PivProgressionPanel;
import org.fudaa.fudaa.piv.PivResource;
import org.fudaa.fudaa.piv.PivVisuPanel;
import org.fudaa.fudaa.piv.io.PivExeLauncher;
import org.fudaa.fudaa.piv.metier.PivOrthoParameters;
import org.fudaa.fudaa.piv.metier.PivTransect;

/**
 * Une action pour lancer le calcul de d�bit.
 * 
 * @author Bertrand Marchand (marchand@deltacad.fr)
 * @version $Id: PivComputeFlowAction.java 6677 2011-11-18 10:55:25Z bmarchan $
 */
public class PivComputeFlowAction extends EbliActionSimple {

    PivImplementation impl;

    CtuluDialog diProgress_;

    /**
   * Constructeur.
   * @param _impl L'implementation.
   */
    public PivComputeFlowAction(PivImplementation _impl) {
        super(PivResource.getS("Calcul du d�bit"), null, "COMPUTE_FLOW");
        impl = _impl;
        setEnabled(false);
    }

    /**
   * Lance l'analyse par PIV, dans un thread s�par�.
   * @param _e L'evenement pour l'action.
   */
    @Override
    public void actionPerformed(final ActionEvent _e) {
        if (!isValide()) {
            return;
        }
        if (!PivExeLauncher.instance().areExeOK()) {
            PivExePanel pnExe = new PivExePanel();
            if (!pnExe.afficheModaleOk(impl.getFrame(), PivResource.getS("R�pertoire contenant les executables"))) {
                return;
            } else {
                PivExeLauncher.instance().setExePath(new File(PivPreferences.PIV.getStringProperty(PivPreferences.PIV_EXE_PATH)));
            }
        }
        CtuluTaskOperationGUI r = new CtuluTaskOperationGUI(impl, PivResource.getS("Calcul du d�bit")) {

            @Override
            public void act() {
                try {
                    CtuluAnalyze ana = new CtuluAnalyze();
                    ana.setDesc(this.getName());
                    PivExeLauncher.instance().launchBathyCompute(ana, impl.getCurrentProject(), this);
                    if (ana.containsErrorOrFatalError()) {
                        impl.error(ana.getResume());
                        return;
                    }
                    PivExeLauncher.instance().launchQCompute(ana, impl.getCurrentProject(), this);
                    if (ana.containsErrorOrFatalError()) {
                        impl.error(ana.getResume());
                        return;
                    }
                } finally {
                    diProgress_.dispose();
                }
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        PivFlowInfoPanel pnInfo = new PivFlowInfoPanel(impl.getCurrentProject().getFlowResults());
                        pnInfo.afficheModale(impl.getFrame(), PivResource.getS("Calcul termin�"), CtuluDialog.OK_OPTION);
                        impl.get2dFrame().getVisuPanel().setViewMode(PivVisuPanel.MODE_REAL_VIEW);
                        impl.get2dFrame().getVisuPanel().setFlowLayerVisible(true);
                    }
                });
            }
        };
        PivProgressionPanel pnProgress_ = new PivProgressionPanel(r);
        diProgress_ = pnProgress_.createDialog(impl.getParentComponent());
        diProgress_.setOption(CtuluDialog.ZERO_OPTION);
        diProgress_.setDefaultCloseOperation(CtuluDialog.DO_NOTHING_ON_CLOSE);
        diProgress_.setTitle(r.getName());
        r.start();
        diProgress_.afficheDialogModal();
    }

    /**
   * @return true Si toutes les donn�es sont pr�sentes pour un lancement.
   */
    public boolean isValide() {
        if (impl.getCurrentProject().getTransect() == null) {
            impl.error(PivResource.getS("Erreur"), PivResource.getS("Le transect n'a pas �t� d�fini"));
            return false;
        }
        if (impl.getCurrentProject().getFlowParameters() == null) {
            impl.error(PivResource.getS("Erreur"), PivResource.getS("Les param�tres de calcul d�bit n'ont pas �t� donn�s"));
            return false;
        }
        if (impl.getCurrentProject().getVelResults() == null) {
            impl.error(PivResource.getS("Erreur"), PivResource.getS("Le projet ne contient pas de r�sultats moyenn�s"));
            return false;
        }
        if (impl.getCurrentProject().getOrthoParameters() == null) {
            impl.error(PivResource.getS("Erreur"), PivResource.getS("Les param�tres d'orthorectification n'ont pas �t� donn�s"));
            return false;
        }
        PivTransect trans = impl.getCurrentProject().getTransect();
        PivOrthoParameters param = impl.getCurrentProject().getOrthoParameters();
        GrPolyligne pl = trans.getStraight();
        if (pl.sommet(0).z_ < param.getWaterElevation() || pl.sommet(pl.nombre() - 1).z_ < param.getWaterElevation()) {
            impl.error(PivResource.getS("Erreur"), PivResource.getS("Au moins un point extremit� du transect a un Z inf�rieur au niveau d'eau."));
            return false;
        }
        return true;
    }

    @Override
    public String getEnableCondition() {
        return PivResource.getS("Un transect doit �tre d�fini et des r�sultats moyenn�s doivent exister");
    }
}
