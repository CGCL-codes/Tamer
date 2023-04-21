package org.fudaa.ebli.palette;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import com.memoire.bu.*;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.ctulu.CtuluRange;
import org.fudaa.ctulu.gui.CtuluDialogPanel;
import org.fudaa.ctulu.gui.CtuluLibDialog;
import org.fudaa.ctulu.gui.CtuluLibSwing;
import org.fudaa.ctulu.gui.CtuluTitledPanelCheckBox;
import org.fudaa.ebli.commun.EbliLib;
import org.fudaa.ebli.ressource.EbliResource;
import org.fudaa.ebli.trace.BPlageInterface;
import org.fudaa.ebli.trace.TraceIcon;

/**
 * @author DENIGER
 */
public class PaletteRefreshPanel extends CtuluDialogPanel implements ActionListener, ItemListener {

    private final JTextField newNb_;

    JButton btMax_;

    JButton btMin_;

    BuComboBox cbPalette_;

    JCheckBox cbModifyRange_;

    TraceIcon icMax_;

    TraceIcon icMin_;

    JComponent newMax_;

    JComponent newMin_;

    final PaletteSelecteurCouleurPlage plage_;

    BuGlassPaneStop stop_;

    JDialog owner_;

    final void setOwner(final JDialog _owner) {
        owner_ = _owner;
    }

    private String getHelpTextString() {
        return "<html><body>" + EbliLib.getS("Les modifications sont automatiquement sauvegard�es") + ".<br><br>" + EbliLib.getS("Il est possible de partager des palettes gr�ce aux fonction d'import/export") + ".<br><br>" + EbliLib.getS("Il y a deux types de palettes:") + "<br><ul><li>" + EbliLib.getS("Palette lin�aire: toutes les couleurs de l'intervalle seront utilis�es") + "</li><li>" + EbliLib.getS("Palette sp�cifi�e par l'utilisateur: seules les couleurs sp�cifi�es seront utilis�es (si suffisantes)") + "</li></ul></body></html>";
    }

    CtuluTitledPanelCheckBox pnMinMaxRanges_;

    /**
   * Construit le panneau.
   */
    public PaletteRefreshPanel(final PaletteSelecteurCouleurPlage _plage) {
        super(false);
        this.plage_ = _plage;
        setLayout(new BuVerticalLayout(5, true, true));
        setHelpText(getHelpTextString());
        if (this.plage_.isDiscreteTarget_) {
            newNb_ = null;
        } else {
            pnMinMaxRanges_ = CtuluLibSwing.createTitleCheckBox(EbliLib.getS("Modifier les plages"));
            cbModifyRange_ = pnMinMaxRanges_.getTitleCb();
            cbModifyRange_.setToolTipText("<html>" + EbliLib.getS("Si s�lectionn�, les plages de la palette seront modifi�es") + "<br>" + EbliLib.getS("Si non s�lectionn�, seules les couleurs seront mises � jour") + "</html>");
            pnMinMaxRanges_.setLayout(new BuGridLayout(2, 5, 5));
            newNb_ = addLabelIntegerText(pnMinMaxRanges_, EbliLib.getS("Nombre de plages"));
            String nb = CtuluLibString.DIX;
            if (plage_.plages_.size() > 0) {
                nb = Integer.toString(plage_.plages_.size());
            }
            newNb_.setText(nb);
            addLabel(pnMinMaxRanges_, EbliLib.getS("Min"));
            newMin_ = plage_.valueEditor_.createEditorComponent();
            pnMinMaxRanges_.add(newMin_);
            addLabel(pnMinMaxRanges_, EbliLib.getS("Max"));
            newMax_ = plage_.valueEditor_.createEditorComponent();
            pnMinMaxRanges_.add(newMax_);
            final double[] ds = this.plage_.getMinMax();
            if (ds != null) {
                plage_.valueEditor_.setValue(Double.toString(ds[1]), newMax_);
                plage_.valueEditor_.setValue(Double.toString(ds[0]), newMin_);
            }
            if ((this.plage_.target_ != null) && (this.plage_.target_.isDonneesBoiteAvailable())) {
                final BuPanel right = new BuPanel();
                right.setLayout(new BuVerticalLayout());
                right.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), EbliLib.getS("Initialiser les valeurs avec") + ':'));
                BuButton bt = createBtFindMinMax();
                right.add(bt);
                if (this.plage_.target_.isDonneesBoiteTimeAvailable()) {
                    bt = createBtFindMinMaxTime();
                    right.add(bt);
                }
                add(right);
                add(pnMinMaxRanges_);
            }
        }
        btMax_ = new JButton();
        btMax_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent _e) {
                icMax_.setCouleur(JColorChooser.showDialog(owner_, EbliResource.EBLI.getString("Couleur pour le max"), icMax_.getCouleur()));
                paletteContinu_.setMax(icMax_.getCouleur());
                cbPalette_.repaint();
            }
        });
        icMax_ = new TraceIcon();
        icMax_.setTaille(5);
        icMax_.setType(TraceIcon.CARRE_PLEIN);
        BPlageAbstract plage = null;
        BPlageAbstract plageMin = null;
        if (this.plage_.model_.getSize() > 0) {
            plage = this.plage_.getMaxPlage();
            plageMin = this.plage_.getMinPlage();
        }
        icMax_.setCouleur(plage == null ? Color.CYAN : plage.getCouleur());
        btMin_ = new JButton();
        btMin_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent _e) {
                icMin_.setCouleur(JColorChooser.showDialog(owner_, EbliResource.EBLI.getString("Couleur pour le min"), icMin_.getCouleur()));
                paletteContinu_.setMin(icMin_.getCouleur());
                cbPalette_.repaint();
            }
        });
        icMin_ = new TraceIcon();
        icMin_.setCouleur(plageMin == null ? Color.RED : plageMin.getCouleur());
        if (icMin_.getCouleur() == null || icMin_.getCouleur().equals(icMax_.getCouleur())) {
            icMin_.setCouleur(Color.CYAN);
            icMax_.setCouleur(Color.RED);
        }
        icMin_.setTaille(5);
        icMin_.setType(TraceIcon.CARRE_PLEIN);
        btMax_.setIcon(icMax_);
        btMin_.setIcon(icMin_);
        pnContinu_ = new BuPanel();
        pnContinu_.setLayout(new BuGridLayout(4, 2, 2, false, false, false, false, false));
        pnContinu_.add(new BuLabel("min:"));
        pnContinu_.add(btMin_);
        pnContinu_.add(new BuLabel("max:"));
        pnContinu_.add(btMax_);
        paletteContinu_ = new PaletteCouleurContinu(icMin_.getCouleur(), icMax_.getCouleur());
        cbPalette_ = new BuComboBox();
        cbPalette_.setRenderer(new PaletteCouleurRendererTitle());
        buildCbModel();
        palette_ = new BuPanel();
        palette_.setLayout(new BuGridLayout(2, 5, 5, false, false));
        palette_.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), EbliLib.getS("Palette") + ':'));
        palette_.add(cbPalette_);
        pnDisc_ = new BuPanel();
        pnDisc_.setLayout(new BuVerticalLayout(5, false, false));
        BuButton bt = new BuButton();
        bt.setText(EbliLib.getS("Editer la palette"));
        bt.setActionCommand("EDIT_SELECTED_PALETTE");
        bt.addActionListener(this);
        pnDisc_.add(bt);
        bt = new BuButton();
        bt.setText(EbliLib.getS("Gestionnaire de palettes"));
        bt.setActionCommand("EDIT_PALETTES");
        bt.addActionListener(this);
        pnDisc_.add(bt);
        palette_.add(pnDisc_);
        editPalette_ = pnDisc_;
        add(palette_);
        cbPalette_.addItemListener(this);
        updateEditPanel();
    }

    /**
   * @return le bouton a utiliser pour rechercher le min max sur le pas de temps en cours
   */
    private BuButton createBtFindMinMaxTime() {
        BuButton bt;
        bt = new BuButton();
        bt.setText(EbliLib.getS("les valeurs max/min du pas de temps"));
        bt.setToolTipText(EbliLib.getS("Initialiser les champs avec le maximum et le minimum des donn�es sur la pas de temps"));
        bt.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent _e) {
                BuLib.invokeNow(new Runnable() {

                    public void run() {
                        startComputing();
                    }
                });
                new Thread() {

                    public void run() {
                        if (PaletteRefreshPanel.this.plage_.dataBoite_ == null) {
                            PaletteRefreshPanel.this.plage_.dataBoite_ = new CtuluRange();
                        }
                        PaletteRefreshPanel.this.plage_.target_.getTimeRange(PaletteRefreshPanel.this.plage_.dataBoite_);
                        if (PaletteRefreshPanel.this.plage_.dataBoite_.isNill()) {
                            plage_.valueEditor_.setValue(null, newMax_);
                            plage_.valueEditor_.setValue(null, newMin_);
                            erreurNotFound();
                        } else {
                            plage_.valueEditor_.setValue(Double.toString(PaletteRefreshPanel.this.plage_.dataBoite_.max_), newMax_);
                            plage_.valueEditor_.setValue(Double.toString(PaletteRefreshPanel.this.plage_.dataBoite_.min_), newMin_);
                        }
                        final Runnable r = new Runnable() {

                            public void run() {
                                stopComputing();
                            }
                        };
                        BuLib.invokeNow(r);
                    }
                }.start();
            }
        });
        return bt;
    }

    protected void erreurNotFound() {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                CtuluLibDialog.showWarn(owner_, "Min/max", EbliLib.getS("Les bornes n'ont pas �t� trouv�es.\n Avez-vous s�lectionner une variable et �ventuellement un pas de temps ?"));
            }
        });
    }

    /**
   * @return le bouton a utiliser pour trouver le min max.
   */
    private BuButton createBtFindMinMax() {
        final BuButton bt = new BuButton();
        bt.setText(EbliLib.getS("les valeurs max/min pour tous les pas de temps"));
        bt.setToolTipText(EbliLib.getS("Initialiser les champs avec le maximum et le minimum des donn�es sur tous les pas de temps"));
        bt.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent _e) {
                BuLib.invokeNow(new Runnable() {

                    public void run() {
                        startComputing();
                    }
                });
                new Thread() {

                    public void run() {
                        if (PaletteRefreshPanel.this.plage_.dataBoite_ == null) {
                            PaletteRefreshPanel.this.plage_.dataBoite_ = new CtuluRange();
                        }
                        PaletteRefreshPanel.this.plage_.target_.getRange(PaletteRefreshPanel.this.plage_.dataBoite_);
                        if (PaletteRefreshPanel.this.plage_.dataBoite_.isNill()) {
                            plage_.valueEditor_.setValue(null, newMax_);
                            plage_.valueEditor_.setValue(null, newMin_);
                            erreurNotFound();
                        } else {
                            plage_.valueEditor_.setValue(Double.toString(PaletteRefreshPanel.this.plage_.dataBoite_.max_), newMax_);
                            plage_.valueEditor_.setValue(Double.toString(PaletteRefreshPanel.this.plage_.dataBoite_.min_), newMin_);
                        }
                        final Runnable r = new Runnable() {

                            public void run() {
                                stopComputing();
                            }
                        };
                        BuLib.invokeNow(r);
                    }
                }.start();
            }
        });
        return bt;
    }

    private void buildCbModel() {
        final List pals = new ArrayList();
        pals.add(paletteContinu_);
        pals.add(null);
        final PaletteCouleurDiscontinu[] ps = PaletteManager.INSTANCE.getAllPalette();
        pals.addAll(Arrays.asList(ps));
        cbPalette_.setModel(new DefaultComboBoxModel(pals.toArray()));
        cbPalette_.setSelectedIndex(0);
        final String lastName = PaletteManager.INSTANCE.getLastChoosePaletteName();
        if (ps.length > 0 && lastName != null) {
            for (int i = ps.length - 1; i >= 0; i--) {
                if (ps[i].getTitle().equals(lastName)) {
                    cbPalette_.setSelectedItem(ps[i]);
                    return;
                }
            }
        }
    }

    public void actionPerformed(final ActionEvent _evt) {
        if ("EDIT_SELECTED_PALETTE".equals(_evt.getActionCommand())) {
            final Object o = cbPalette_.getSelectedItem();
            if (o instanceof PaletteCouleurDiscontinu) {
                final PaletteCouleurDiscontinu d = (PaletteCouleurDiscontinu) o;
                final PaletteEditorPanel panel = new PaletteEditorPanel(d.getTitle(), PaletteManager.copy(d.getCs()));
                final CtuluDialogPanel pn = new CtuluDialogPanel() {

                    public boolean isDataValid() {
                        if (panel.getColors().length == 0) {
                            setErrorText(EbliLib.getS("D�finir au moins une couleur"));
                            return false;
                        }
                        String newTitle = panel.getTitle();
                        if (!newTitle.equals(d.getTitle()) && PaletteManager.INSTANCE.containsPaletteWithTitle(newTitle)) {
                            setErrorText(EbliLib.getS("Nom d�j� utilis�"));
                            return false;
                        }
                        return true;
                    }
                };
                pn.add(panel.buildPanel());
                if (CtuluDialogPanel.isOkResponse(pn.afficheModale(owner_))) {
                    d.setCs(panel.getColors());
                    PaletteManager.INSTANCE.modifyPalette(d, panel.getTitle());
                    cbPalette_.repaint();
                    PaletteManager.INSTANCE.savePref();
                }
            }
        } else if ("EDIT_PALETTES".equals(_evt.getActionCommand())) {
            final CtuluDialogPanel pn = new PaletteListEditor();
            if (CtuluDialogPanel.isOkResponse(pn.afficheModale(owner_))) {
                buildCbModel();
                updateEditPanel();
            }
        }
    }

    BuPanel palette_;

    BuPanel pnContinu_;

    BuPanel pnDisc_;

    PaletteCouleurContinu paletteContinu_;

    JComponent editPalette_;

    void startComputing() {
        if (owner_ != null) {
            stop_ = new BuGlassPaneStop();
            if (owner_ != null) {
                owner_.setGlassPane(stop_);
                stop_.setVisible(true);
                owner_.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            }
        }
    }

    void stopComputing() {
        if (stop_ != null) {
            stop_.setVisible(false);
            if (owner_ != null) {
                owner_.getRootPane().remove(stop_);
            }
        }
        owner_.setCursor(Cursor.getDefaultCursor());
    }

    public void apply() {
        final PaletteCouleur c = (PaletteCouleur) cbPalette_.getSelectedItem();
        if (!this.plage_.isDiscreteTarget_) {
            if (cbModifyRange_.isSelected()) {
                final List l = new ArrayList();
                final BPalettePlage newPalette = (BPalettePlage) this.plage_.target_.createPaletteCouleur();
                final double min = Double.parseDouble(plage_.valueEditor_.getStringValue(newMin_));
                final double max = Double.parseDouble(plage_.valueEditor_.getStringValue(newMax_));
                final int nb = Integer.parseInt(newNb_.getText());
                initPlages(nb, min, max, l, newPalette);
                c.updatePlages(l);
                newPalette.setPlages((BPlageInterface[]) l.toArray(new BPlageInterface[l.size()]));
                this.plage_.model_.setPlages(newPalette);
                this.plage_.ajusteAllLegendes();
            } else {
                plage_.model_.modifyColors(c);
            }
        }
        PaletteManager.INSTANCE.setLastChoosePaletteName(c == paletteContinu_ ? null : c.getTitle());
    }

    public void itemStateChanged(final ItemEvent _e) {
        if (_e.getStateChange() == ItemEvent.SELECTED) {
            updateEditPanel();
        }
    }

    private void updateEditPanel() {
        final Object o = cbPalette_.getSelectedItem();
        if (o == null) {
            cbPalette_.setSelectedIndex(0);
        } else {
            if ((o == paletteContinu_) && (editPalette_ != pnContinu_)) {
                palette_.remove(editPalette_);
                palette_.add(pnContinu_);
                editPalette_ = pnContinu_;
            } else if ((o != paletteContinu_) && (editPalette_ != pnDisc_)) {
                palette_.remove(editPalette_);
                palette_.add(pnDisc_);
                editPalette_ = pnDisc_;
            }
            palette_.revalidate();
            revalidate();
            if (owner_ != null) {
                owner_.pack();
            }
            repaint();
        }
    }

    public boolean isDataValid() {
        boolean r = true;
        if (this.plage_.isDiscreteTarget_) {
            return true;
        }
        if (!plage_.valueEditor_.isValueValidFromComponent(newMax_)) {
            r = false;
            newMax_.setForeground(Color.RED);
        }
        if (!plage_.valueEditor_.isValueValidFromComponent(newMin_)) {
            r = false;
            newMin_.setForeground(Color.RED);
        }
        if (newNb_.getText().trim().length() == 0) {
            r = false;
            newNb_.setForeground(Color.RED);
        }
        return r;
    }

    public static void initPlages(final int _nb, final double _min, final double _max, final List _l, final BPalettePlage _p) {
        int nb = Math.max(_nb, 1);
        if (_min == _max) {
            nb = 1;
        }
        _l.clear();
        for (int i = 0; i < nb; i++) {
            _l.add(_p.createPlage(null));
        }
        double max = _max;
        final double min = _min;
        if (max <= min) {
            max = min;
        }
        if (nb == 1) {
            final BPlage p0 = (BPlage) _l.get(0);
            p0.setMin(min);
            p0.setMax(max);
        } else {
            for (int i = 0; i < nb; i++) {
                final BPlage pi = (BPlage) _l.get(i);
                pi.setMin(min + i * (max - min) / nb);
                pi.setMax(min + (i + 1) * (max - min) / nb);
            }
        }
        _p.updatePlageLegendes();
    }
}
