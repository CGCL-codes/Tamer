package photocard.controleurs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import photocard.modeles.EmplacementPhoto;
import photocard.modeles.ModeleCarte;
import photocard.modeles.OutilsManager;
import photocard.vues.VuePrincipale;

/**
 * Class ControlPrincipal
 */
public class ControlOutils implements MouseListener {

    private JButton buttonLuminositePlus, buttonContrastePlus, buttonNoirBlanc, buttonSepia, buttonRotationPlus, buttonRotationMoins, buttonZoom, buttonDezoom, buttonGauche, buttonDroite, buttonHaut, buttonBas, buttonContrasteMoins, buttonLuminositeMoins;

    private JButton buttonAnnuler;

    private OutilsManager outils;

    private ModeleCarte modeleCarte;

    private VuePrincipale vue;

    public ControlOutils(VuePrincipale vueParam, ModeleCarte modeleCarte, OutilsManager outils, JButton buttonLuminositePlus, JButton buttonLuminositeMoins, JButton buttonContrastePlus, JButton buttonContrasteMoins, JButton buttonNoirBlanc, JButton buttonSepia, JButton buttonAnnuler, JButton buttonRotationPlus, JButton buttonRotationMoins, JButton buttonZoom, JButton buttonDezoom, JButton buttonGauche, JButton buttonDroite, JButton buttonHaut, JButton buttonBas) {
        this.vue = vueParam;
        this.modeleCarte = modeleCarte;
        this.outils = outils;
        this.buttonLuminositePlus = buttonLuminositePlus;
        this.buttonLuminositeMoins = buttonLuminositeMoins;
        this.buttonContrastePlus = buttonContrastePlus;
        this.buttonContrasteMoins = buttonContrasteMoins;
        this.buttonNoirBlanc = buttonNoirBlanc;
        this.buttonSepia = buttonSepia;
        this.buttonAnnuler = buttonAnnuler;
        this.buttonRotationPlus = buttonRotationPlus;
        this.buttonRotationMoins = buttonRotationMoins;
        this.buttonZoom = buttonZoom;
        this.buttonDezoom = buttonDezoom;
        this.buttonGauche = buttonGauche;
        this.buttonDroite = buttonDroite;
        this.buttonHaut = buttonHaut;
        this.buttonBas = buttonBas;
    }

    ;

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (modeleCarte.getEmplacementSelectionne() != null) {
            if (e.getSource() == this.buttonSepia) {
                this.outils.sepia(modeleCarte.getEmplacementSelectionne());
            } else if (e.getSource() == this.buttonNoirBlanc) {
                this.outils.noirBlanc(modeleCarte.getEmplacementSelectionne());
            } else if (e.getSource() == this.buttonAnnuler) {
                this.outils.annuler(modeleCarte.getEmplacementSelectionne());
            } else if (e.getSource() == this.buttonLuminositePlus) {
                this.outils.luminosite(-10, modeleCarte.getEmplacementSelectionne());
            } else if (e.getSource() == this.buttonLuminositeMoins) {
                this.outils.luminosite(10, modeleCarte.getEmplacementSelectionne());
            } else if (e.getSource() == this.buttonContrastePlus) {
                this.outils.contraste(-10, modeleCarte.getEmplacementSelectionne());
            } else if (e.getSource() == this.buttonContrasteMoins) {
                this.outils.contraste(10, modeleCarte.getEmplacementSelectionne());
            } else if (e.getSource() == this.buttonRotationPlus) {
                this.outils.rotation("+", modeleCarte.getEmplacementSelectionne());
            } else if (e.getSource() == this.buttonRotationMoins) {
                this.outils.rotation("-", modeleCarte.getEmplacementSelectionne());
            } else if (e.getSource() == this.buttonZoom) {
                EmplacementPhoto emp = modeleCarte.getEmplacementSelectionne();
                emp.setHauteurAffichage((int) (emp.getHauteurAffichage() * 1.1));
                emp.setLargeurAffichage((int) (emp.getLargeurAffichage() * 1.1));
                this.vue.updateCarte();
            } else if (e.getSource() == this.buttonDezoom) {
                EmplacementPhoto emp = modeleCarte.getEmplacementSelectionne();
                emp.setHauteurAffichage((int) (emp.getHauteurAffichage() / 1.1));
                emp.setLargeurAffichage((int) (emp.getLargeurAffichage() / 1.1));
                this.vue.updateCarte();
            } else if (e.getSource() == this.buttonGauche) {
                EmplacementPhoto emp = modeleCarte.getEmplacementSelectionne();
                emp.setCentreXAffichage(emp.getCentreXAffichage() - 10);
                this.vue.updateCarte();
            } else if (e.getSource() == this.buttonDroite) {
                EmplacementPhoto emp = modeleCarte.getEmplacementSelectionne();
                emp.setCentreXAffichage(emp.getCentreXAffichage() + 10);
                this.vue.updateCarte();
            } else if (e.getSource() == this.buttonHaut) {
                EmplacementPhoto emp = modeleCarte.getEmplacementSelectionne();
                emp.setCentreYAffichage(emp.getCentreYAffichage() - 10);
                this.vue.updateCarte();
            } else if (e.getSource() == this.buttonBas) {
                EmplacementPhoto emp = modeleCarte.getEmplacementSelectionne();
                emp.setCentreYAffichage(emp.getCentreYAffichage() + 10);
                this.vue.updateCarte();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
