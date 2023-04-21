package org.fudaa.fudaa.sig;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import com.vividsolutions.jts.geom.Envelope;
import com.memoire.bu.BuButton;
import com.memoire.bu.BuCheckBox;
import com.memoire.bu.BuGridLayout;
import com.memoire.bu.BuLabel;
import com.memoire.bu.BuPanel;
import com.memoire.bu.BuTextField;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.gui.CtuluDialogPanel;
import org.fudaa.ebli.calque.ZCalqueGrille;
import org.fudaa.ebli.commun.EbliActionPaletteAbstract;
import org.fudaa.ebli.commun.EbliLib;
import org.fudaa.ebli.geometrie.GrBoite;
import org.fudaa.ebli.palette.BSelecteurReduitFonteNewVersion;
import org.fudaa.ebli.ressource.EbliResource;
import org.fudaa.ebli.trace.TraceIconButton;
import org.fudaa.ebli.volume.controles.BControleVisible;

/**
 * @author fred deniger
 * @version $Id: FSigGrillePalette.java,v 1.2 2006-08-17 16:35:16 deniger Exp $
 */
public final class FSigGrillePalette extends EbliActionPaletteAbstract {

    final ZCalqueGrille target_;

    /**
   * @author fred deniger
   * @version $Id: FSigGrillePalette.java,v 1.2 2006-08-17 16:35:16 deniger Exp $
   */
    final class BoundsActionListener implements ActionListener {

        public void actionPerformed(final ActionEvent _e) {
            Envelope env = target_.getCustomBounds();
            if (env == null) {
                env = new Envelope();
                final GrBoite boite = target_.getDomaine();
                if (boite != null) {
                    env.expandToInclude(boite.getMinX(), boite.getMinY());
                    env.expandToInclude(boite.getMaxX(), boite.getMaxY());
                }
            }
            final CtuluDialogPanel pnBounds = new CtuluDialogPanel();
            pnBounds.setLayout(new BuGridLayout(2, 2, 2));
            final BuTextField tfMinX = pnBounds.addLabelDoubleText("Min X");
            tfMinX.setValue(new Double(env.getMinX()));
            final BuTextField tfMaxX = pnBounds.addLabelDoubleText("Max X");
            tfMaxX.setValue(new Double(env.getMaxX()));
            final BuTextField tfMinY = pnBounds.addLabelDoubleText("Min Y");
            tfMinY.setValue(new Double(env.getMinY()));
            final BuTextField tfMaxY = pnBounds.addLabelDoubleText("Max Y");
            tfMaxY.setValue(new Double(env.getMaxY()));
            if (pnBounds.afficheModaleOk(CtuluLib.getFrameAncestorHelper(target_))) {
                target_.setCustomBounds(new Envelope(((Double) tfMinX.getValue()).doubleValue(), ((Double) tfMaxX.getValue()).doubleValue(), ((Double) tfMinY.getValue()).doubleValue(), ((Double) tfMaxY.getValue()).doubleValue()));
            }
        }
    }

    /**
   * @param _name
   * @param _icon
   * @param _name2
   */
    FSigGrillePalette(final ZCalqueGrille _target) {
        super(EbliResource.EBLI.getString("Rep�re"), EbliResource.EBLI.getIcon("repere"), "CHANGE_REFERENCE");
        target_ = _target;
    }

    public JComponent buildContentPane() {
        final BuPanel pn = new BuPanel();
        pn.setBorder(CtuluLib.createTitleBorder(super.getTitle()));
        pn.setLayout(new BuGridLayout(2, 3, 3));
        final BControleVisible vi = new BControleVisible();
        vi.setTarget(target_);
        pn.add(new BuLabel(EbliLib.getS("Visible")));
        pn.add(vi);
        pn.add(new BuLabel(EbliLib.getS("Grille")));
        final BuCheckBox cb = new BuCheckBox();
        cb.setSelected(target_.isDrawGrid());
        cb.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent _e) {
                target_.setDrawGrid(cb.isSelected());
            }
        });
        pn.add(cb);
        buildDrawAxes(pn);
        buildGraduation(pn);
        final BuButton bt = new BuButton(EbliLib.getS("D�finir"));
        pn.add(new BuLabel(EbliLib.getS("Limites des axes")));
        final BuCheckBox cbLimite = new BuCheckBox();
        cbLimite.setSelected(target_.isUseCustom());
        cbLimite.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent _e) {
                target_.setUseCustom(cbLimite.isSelected());
                if (target_.getCustomBounds() == null) {
                    bt.doClick();
                }
            }
        });
        final BuPanel pnLimite = new BuPanel();
        pnLimite.setLayout(new BuGridLayout(2));
        pnLimite.add(cbLimite);
        pnLimite.add(bt);
        pn.add(pnLimite);
        bt.addActionListener(new BoundsActionListener());
        pn.add(new BuLabel(EbliLib.getS("Couleur")));
        final TraceIconButton btColor = new TraceIconButton();
        btColor.setForeground(target_.getForeground());
        btColor.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent _e) {
                final Color c = JColorChooser.showDialog(target_, EbliLib.getS("Couleur"), target_.getForeground());
                if (c != null) {
                    target_.setForeground(c);
                    btColor.setForeground(c);
                }
            }
        });
        pn.add(btColor);
        pn.add(new BuLabel(EbliLib.getS("Fonte")));
        final BuButton btFont = new BuButton("1234.567");
        pn.add(btFont);
        btFont.setFont(target_.getFont());
        btFont.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent _e) {
                final JDialog d = new JDialog(CtuluLib.getFrameAncestorHelper(target_));
                final BSelecteurReduitFonteNewVersion ft = new BSelecteurReduitFonteNewVersion(btFont.getFont(), d);
                ft.setTarget(target_);
                d.setContentPane(ft);
                d.setModal(true);
                d.pack();
                d.setLocationRelativeTo(target_);
                d.show();
                btFont.setFont(target_.getFont());
            }
        });
        return pn;
    }

    private void buildGraduation(final BuPanel _pn) {
        final BuTextField btGraduationX = BuTextField.createIntegerField();
        _pn.add(new BuLabel(EbliLib.getS("Graduation selon X")));
        btGraduationX.setValue(new Integer(target_.getNbXGraduations()));
        _pn.add(btGraduationX);
        btGraduationX.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent _e) {
                final int newVal = ((Integer) btGraduationX.getValue()).intValue();
                if (newVal < 0 || newVal > 5000) {
                    btGraduationX.setValue(new Integer(target_.getNbXGraduations()));
                } else {
                    target_.setNbGraduationX(newVal);
                }
            }
        });
        final BuTextField btGraduationY = BuTextField.createIntegerField();
        _pn.add(new BuLabel(EbliLib.getS("Graduation selon Y")));
        btGraduationY.setValue(new Integer(target_.getNbYGraduations()));
        _pn.add(btGraduationY);
        btGraduationY.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent _e) {
                final int newVal = ((Integer) btGraduationY.getValue()).intValue();
                if (newVal < 0 || newVal > 5000) {
                    btGraduationY.setValue(new Integer(target_.getNbYGraduations()));
                } else {
                    target_.setNbGraduationY(newVal);
                }
            }
        });
    }

    private void buildDrawAxes(final BuPanel _pn) {
        _pn.add(new BuLabel(EbliLib.getS("Dessiner l'axe des X")));
        final BuCheckBox cbX = new BuCheckBox();
        cbX.setSelected(target_.isDrawX());
        cbX.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent _e) {
                target_.setDrawX(cbX.isSelected());
            }
        });
        _pn.add(cbX);
        _pn.add(new BuLabel(EbliLib.getS("Dessiner l'axe des Y")));
        final BuCheckBox cbY = new BuCheckBox();
        cbY.setSelected(target_.isDrawY());
        cbY.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent _e) {
                target_.setDrawY(cbY.isSelected());
            }
        });
        _pn.add(cbY);
    }

    public FSigGrillePalette(final String _name, final Icon _icon, final String _actionName, final ZCalqueGrille _target) {
        super(_name, _icon, _actionName);
        target_ = _target;
    }
}
