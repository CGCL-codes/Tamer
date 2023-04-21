package org.fudaa.fudaa.tr.post.profile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import com.memoire.bu.BuBorderLayout;
import com.memoire.bu.BuLib;
import com.memoire.bu.BuMenu;
import org.fudaa.ctulu.CtuluLibImage;
import org.fudaa.ctulu.CtuluListSelectionInterface;
import org.fudaa.ctulu.CtuluUI;
import org.fudaa.ctulu.CtuluVariable;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.dodico.h2d.type.H2dVariableType;
import org.fudaa.ebli.calque.action.EbliCalqueActionTimeChooser;
import org.fudaa.ebli.commun.BPalettePanelInterface;
import org.fudaa.ebli.commun.EbliActionInterface;
import org.fudaa.ebli.commun.EbliActionPaletteTreeModel;
import org.fudaa.ebli.commun.EbliActionSimple;
import org.fudaa.ebli.commun.EbliLib;
import org.fudaa.ebli.courbe.EGAxeHorizontal;
import org.fudaa.ebli.courbe.EGAxeVertical;
import org.fudaa.ebli.courbe.EGCourbe;
import org.fudaa.ebli.courbe.EGFillePanel;
import org.fudaa.ebli.courbe.EGGraphe;
import org.fudaa.ebli.courbe.EGGroup;
import org.fudaa.ebli.palette.BPalettePlageDefault;
import org.fudaa.fudaa.commun.courbe.FudaaGrapheTimeAnimatedVisuPanel;
import org.fudaa.fudaa.commun.impl.FudaaCommonImplementation;
import org.fudaa.fudaa.meshviewer.MvResource;
import org.fudaa.fudaa.ressource.FudaaResource;
import org.fudaa.fudaa.tr.common.TrLib;

/**
 * @author fred deniger
 * @version $Id: MvProfileFillePanel.java,v 1.11 2007-06-13 12:58:10 deniger Exp $
 */
public class MvProfileFillePanel extends FudaaGrapheTimeAnimatedVisuPanel {

    JComponent splitComp_;

    JSplitPane twoSpane_;

    final CtuluUI ui_;

    /**
   * @return the ui
   */
    public CtuluUI getCtuluUI() {
        return ui_;
    }

    @Override
    public EGFillePanel duplicate() {
        return new MvProfileFillePanel(getGraphe().duplicate(), ui_);
    }

    private MvProfileFillePanel(final EGGraphe _a, final CtuluUI _ui) {
        super(_a);
        ui_ = _ui;
        initActions((MvProfileTreeModel) _a.getModel(), _ui);
    }

    public MvProfileFillePanel(final MvProfileTreeModel _a, final CtuluUI _ui) {
        super(new EGGraphe(_a));
        ui_ = _ui;
        final EGAxeHorizontal h = new EGAxeHorizontal();
        h.setTitre(EbliLib.getS("abscisse"));
        h.setUnite("m");
        getGraphe().setXAxe(h);
        initActions(_a, _ui);
    }

    private void initActions(final MvProfileTreeModel _a, final CtuluUI _ui) {
        final EGGroup gBathy = _a.getGroup(H2dVariableType.BATHYMETRIE, false);
        if (gBathy != null) {
            final EGAxeVertical axeBathy = gBathy.getAxeY();
            final EGGroup gCote = _a.getGroup(H2dVariableType.COTE_EAU, false);
            if (gCote != null) {
                getGraphe().useOneAxe(new EGAxeVertical[] { axeBathy, gCote.getAxeY() }, "Z", null);
                if (gCote.getChildCount() == 1 && gBathy.getChildCount() == 1) {
                    final EGCourbe cCote = gCote.getCourbeAt(0);
                    cCote.createSurface();
                    final EGCourbe cBathy = gBathy.getCourbeAt(0);
                    cBathy.createSurface();
                    cBathy.getSurfacePainter().setSelectedPainterBottom();
                    cCote.getSurfacePainter().setSelectedPainterCourbe(cBathy);
                    cCote.getSurfacePainter().setAlpha(128);
                    cBathy.getSurfacePainter().setAlpha(128);
                }
            }
        }
        final EbliActionPaletteTreeModel act = new EbliActionPaletteTreeModel(MvResource.getS("Vue 2D"), MvResource.MV.getIcon("maillage"), "VUE2D") {

            @Override
            protected BPalettePanelInterface buildPaletteContent() {
                return new MvProfileGridPalette(MvProfileFillePanel.this, _ui);
            }
        };
        act.setResizable(true);
        getTreeModel().getSelectionModel().addTreeSelectionListener(act);
        final List acts = new ArrayList(5);
        acts.add(act);
        if (_a.containsTime()) {
            acts.add(new EbliCalqueActionTimeChooser(getTreeModel().getSelectionModel()));
            createVideo();
            acts.add(getVideo());
        }
        if (isFlowrateEnable()) {
            acts.add(new EbliActionSimple(MvResource.getS("Calculer le d�bit"), FudaaResource.FUDAA.getToolIcon("debit"), "FLOWRATE") {

                @Override
                public void actionPerformed(final ActionEvent _e) {
                    computeFlowrate();
                }
            });
        }
        setPersonnalAction((EbliActionInterface[]) acts.toArray(new EbliActionInterface[acts.size()]));
    }

    protected void computeFlowrate() {
        if (isFlowrateEnable()) {
            MvProfileFlowrateVolumeBuilder.computeFlowrate(getProfileTreeModel(), (FudaaCommonImplementation) ui_, (String) getClientProperty("title"));
        }
    }

    protected JMenu createProfileMenu() {
        final BuMenu res = new BuMenu(MvResource.getS("Profil"), "PROFIL");
        res.addMenuItem(MvResource.getS("Inverser le profil"), "INVERT", true, this);
        res.addMenuItem(MvResource.getS("Supprimer les points ext�rieurs"), "DELETE_EXT", !getProfileTreeModel().isOutRemoved(), this);
        final CtuluListSelectionInterface select = getSelection();
        final boolean ok = select != null && !select.isEmpty() && !selection_.isAllSelected() && select.getMinIndex() < select.getMaxIndex();
        res.addMenuItem(MvResource.getS("Garder uniquement les points s�lectionn�s"), "KEEP_SELECTED", ok, this);
        res.addMenuItem(MvResource.getS("R�initialiser les intersections"), "INTER_INIT", !getProfileTreeModel().isInit(), this);
        return res;
    }

    protected boolean isVueHorizontal() {
        return twoSpane_ != null && twoSpane_.getOrientation() == JSplitPane.HORIZONTAL_SPLIT;
    }

    protected void removeVueTop() {
        if (twoSpane_ != null) {
            remove(twoSpane_);
            add(vue_, BuBorderLayout.CENTER);
            revalidate();
            doLayout();
            twoSpane_ = null;
            splitComp_ = null;
        }
    }

    protected void swapVue() {
        if (twoSpane_ != null) {
            int or = twoSpane_.getOrientation();
            or = or == JSplitPane.VERTICAL_SPLIT ? JSplitPane.HORIZONTAL_SPLIT : JSplitPane.VERTICAL_SPLIT;
            remove(twoSpane_);
            twoSpane_ = new JSplitPane(or);
            if (or == JSplitPane.VERTICAL_SPLIT) {
                twoSpane_.setBottomComponent(vue_);
                twoSpane_.setTopComponent(splitComp_);
                twoSpane_.setDividerLocation(getHeight() * 1 / 3);
            } else {
                twoSpane_.setLeftComponent(vue_);
                twoSpane_.setRightComponent(splitComp_);
                twoSpane_.setDividerLocation(getWidth() * 2 / 3);
            }
            add(twoSpane_, BuBorderLayout.CENTER);
            revalidate();
            doLayout();
        }
    }

    protected void updateVueTop(final JComponent _top) {
        splitComp_ = _top;
        if (twoSpane_ != null) {
            remove(twoSpane_);
        }
        remove(vue_);
        twoSpane_ = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        twoSpane_.setTopComponent(_top);
        twoSpane_.setBottomComponent(vue_);
        twoSpane_.setOneTouchExpandable(true);
        twoSpane_.setDividerLocation(getHeight() * 1 / 3);
        add(twoSpane_, BuBorderLayout.CENTER);
        revalidate();
        doLayout();
    }

    @Override
    public void actionPerformed(final ActionEvent _evt) {
        if ("DELETE_EXT".equals(_evt.getActionCommand())) {
            getProfileTreeModel().removeExtPoint(getGraphe().getCmd(), ui_);
        } else if ("KEEP_SELECTED".equals(_evt.getActionCommand())) {
            final CtuluListSelectionInterface select = getSelection();
            if (select != null) {
                final int i1 = select.getMinIndex();
                final int i2 = select.getMaxIndex();
                if (i1 < i2) {
                    getProfileTreeModel().extractFrom(getGraphe().getCmd(), ui_, i1, i2);
                }
            }
        } else if ("INTER_INIT".equals(_evt.getActionCommand())) {
            getProfileTreeModel().reinitRes(getGraphe().getCmd(), ui_);
        } else if ("INVERT".equals(_evt.getActionCommand())) {
            getProfileTreeModel().inverse(getGraphe().getCmd(), ui_);
        } else if ("FLOWRATE".equals(_evt.getActionCommand())) {
            computeFlowrate();
        } else {
            super.actionPerformed(_evt);
        }
    }

    @Override
    public void fillSpecificMenu(final JPopupMenu _popup) {
        super.fillSpecificMenu(_popup);
        _popup.add(createProfileMenu());
    }

    @Override
    public Dimension getDefaultImageDimension() {
        return twoSpane_ == null ? super.getDefaultImageDimension() : twoSpane_.getSize();
    }

    public MvProfileTreeModel getProfileTreeModel() {
        return (MvProfileTreeModel) super.getTreeModel();
    }

    public final boolean isFlowrateEnable() {
        return getProfileTreeModel().canComputeFlowrate() && ui_ instanceof FudaaCommonImplementation;
    }

    public boolean isVueIncrustee() {
        return twoSpane_ != null;
    }

    @Override
    public BufferedImage produceImage(final Map _params) {
        if (twoSpane_ == null) {
            return super.produceImage(_params);
        }
        final Dimension dim = twoSpane_.getSize();
        final BufferedImage res = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
        final Graphics g = res.getGraphics();
        if (CtuluLibImage.mustFillBackground(_params)) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        final Point p = new Point();
        vue_.getLocation(p);
        g.translate(p.x, p.y);
        vue_.paint(g);
        g.translate(-p.x, -p.y);
        splitComp_.getLocation(p);
        g.translate(p.x, p.y);
        splitComp_.paint(g);
        g.translate(-p.x, -p.y);
        g.dispose();
        res.flush();
        return res;
    }

    public static MvProfileCourbeGroup createGroupFor(final CtuluVariable _var) {
        final MvProfileCourbeGroup gri = new MvProfileCourbeGroup(_var);
        final EGAxeVertical yi = new EGAxeVertical();
        yi.setTitre(_var.toString());
        if (_var == H2dVariableType.SANS) {
            yi.setTitre(TrLib.getString("Import"));
        }
        yi.setUnite(_var.getCommonUnit());
        gri.setAxeY(yi);
        return gri;
    }

    public static void finishProfilCreation(final FudaaCommonImplementation _impl, final MvProfileFillePanel _panel, final MvProfileTreeFille _fille) {
        _fille.setPreferredSize(new Dimension(400, 400));
        MvProfileTreeFille.updateName(_fille);
        BuLib.invokeLater(new Runnable() {

            public void run() {
                _impl.addInternalFrame(_fille);
                _panel.getGraphe().restore();
            }
        });
    }

    public static Color getColorFor(final int _i, final CtuluVariable _var) {
        Color c;
        if (_var == H2dVariableType.BATHYMETRIE) {
            c = Color.ORANGE.darker().darker();
        } else if (_var == H2dVariableType.COTE_EAU) {
            c = Color.BLUE;
        } else {
            c = BPalettePlageDefault.getColor(_i + 1);
        }
        return c;
    }

    public static void profilCreated(final FudaaCommonImplementation _impl, final MvProfileFillePanel _panel, final ProgressionInterface _prog, final String _name) {
        final MvProfileTreeFille fille = new MvProfileTreeFille(_panel, MvProfileBuilder.getProfileName(_name), _impl, null);
        finishProfilCreation(_impl, _panel, fille);
    }
}
