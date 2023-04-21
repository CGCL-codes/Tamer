package org.fudaa.ebli.visuallibrary.actions;

import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JDialog;
import org.fudaa.ctulu.gui.CtuluLibSwing;
import org.fudaa.ebli.commun.EbliLib;
import org.fudaa.ebli.controle.*;
import org.fudaa.ebli.visuallibrary.EbliWidget;

/**
 * classe qui permet de creer les selecteurs necessaires pour la configuration.
 * 
 * @author Adrien Hadoux TODO a revoir: ce sont les widgets qui doivent la responsabilite de savoir ce qui peut etre
 *         configurer ou non
 */
public class WidgetConfigure implements BConfigurableInterface {

    EbliWidget widget_;

    boolean lineModel_;

    boolean colorsContours_;

    boolean colorFonds_;

    boolean rotations_;

    boolean police_;

    public WidgetConfigure(final boolean lineModel_, final boolean colorsContours_, final boolean colorFonds_, final boolean rotations_, final boolean police_, final EbliWidget widget_) {
        this.colorFonds_ = colorFonds_;
        this.colorsContours_ = colorsContours_;
        this.lineModel_ = lineModel_;
        this.police_ = police_;
        this.rotations_ = rotations_;
        this.widget_ = widget_;
    }

    public WidgetConfigure(final EbliWidget widget) {
        this(true, true, true, true, true, widget);
    }

    /**
   * Methode qui creer les selecteurs avec les actions qui vont bien
   */
    public BSelecteurInterface[] createSelecteurs() {
        final ArrayList<BSelecteurInterface> listeComposants = new ArrayList<BSelecteurInterface>();
        if (lineModel_) {
            final BSelecteurLineModel modelL = new BSelecteurLineModel(EbliWidget.LINEMODEL);
            modelL.setAddColor(false);
            listeComposants.add(modelL);
        }
        if (colorsContours_) {
            final BSelecteurColorChooserBt modelCc = new BSelecteurColorChooserBt(EbliWidget.COLORCONTOUR);
            modelCc.setTitle("Choix couleur contour");
            listeComposants.add(modelCc);
        }
        if (colorFonds_) {
            final BSelecteurColorChooserBt modelCf = new BSelecteurColorChooserBt(EbliWidget.COLORFOND);
            modelCf.setTitle("Choix couleur de fond");
            listeComposants.add(modelCf);
        }
        if (rotations_) {
            final BSelecteurSlider slider = new BSelecteurSlider(EbliWidget.ROTATION, 0, 360);
            slider.setTitle("Rotation");
            slider.slider_.setMajorTickSpacing(30);
            slider.slider_.setMinorTickSpacing(10);
            slider.slider_.setPaintTicks(true);
            listeComposants.add(slider);
        }
        if (police_) {
            final BSelecteurFont modelF = new BSelecteurFont(EbliWidget.FONT) {

                @Override
                public void actionPerformed(final ActionEvent _e) {
                    final Frame f = CtuluLibSwing.getFrameAncestorHelper(bt_);
                    final JDialog d = new JDialog(f);
                    d.setResizable(true);
                    d.setModal(true);
                    final BSelecteurReduitFonteNewVersion selecteur = new BSelecteurReduitFonteNewVersion(getProperty(), bt_.getFont(), d) {

                        @Override
                        public void firePropertyChange(final String _prop, final Object _newVal) {
                            super.firePropertyChange(_prop, _newVal);
                        }
                    };
                    selecteur.setAddListenerToTarget(false);
                    selecteur.setSelecteurTarget(target_);
                    d.setContentPane(selecteur.getComponent());
                    d.pack();
                    d.setLocationRelativeTo(bt_);
                    d.setTitle(EbliLib.getS("Police"));
                    d.setVisible(true);
                }
            };
            modelF.bt_.setText("Choisir");
            modelF.setTitle("Police");
            modelF.bt_.setFont(new Font("Helvetica.Italic", Font.PLAIN, 12));
            listeComposants.add(modelF);
        }
        BSelecteurCheckBox selectionTransparence = new BSelecteurCheckBox(EbliWidget.TRANSPARENCE, "Transparence");
        listeComposants.add(selectionTransparence);
        final BSelecteurInterface[] tableau = new BSelecteurInterface[listeComposants.size()];
        return listeComposants.toArray(tableau);
    }

    /**
   * Methode qui envoie les configurations specifiques en fonctions du type de widget qui les demande
   * 
   * @param found la widget qui fait appel au service
   * @return la configuration disponible
   */
    public static BConfigurableComposite configurePalette(final EbliWidget found) {
        BConfigurableComposite palette = null;
        palette = new BConfigurableComposite(found.getConfigureInterfaces(found.canTraceLigneModel(), found.canColorForeground(), found.canColorBackground(), found.canRotate(), found.canFont()), "palette");
        return palette;
    }

    public BConfigurableInterface[] getSections() {
        return null;
    }

    public String getTitle() {
        return "Configuration graphique";
    }

    public void stopConfiguration() {
    }

    public BSelecteurTargetInterface getTarget() {
        return widget_;
    }
}
