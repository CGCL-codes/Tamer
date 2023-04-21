package org.fudaa.fudaa.tr.common;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.media.j3d.RenderingAttributes;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.vecmath.Point3d;
import org.fudaa.ctulu.CtuluAnalyze;
import org.fudaa.ctulu.CtuluPermanentList;
import org.fudaa.ctulu.CtuluRange;
import org.fudaa.ctulu.CtuluTaskDelegate;
import org.fudaa.ctulu.CtuluUI;
import org.fudaa.ctulu.CtuluVariable;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.ctulu.ProgressionUpdater;
import org.fudaa.ctulu.collection.CtuluCollectionDouble;
import org.fudaa.ctulu.interpolation.InterpolationVectorContainer;
import org.fudaa.dodico.ef.EfData;
import org.fudaa.dodico.ef.EfElement;
import org.fudaa.dodico.ef.EfElementType;
import org.fudaa.dodico.ef.EfGridData;
import org.fudaa.dodico.ef.EfGridInterface;
import org.fudaa.dodico.h2d.type.H2dVariableType;
import org.fudaa.ebli.calque.BGroupeCalque;
import org.fudaa.ebli.commun.EbliActionSimple;
import org.fudaa.ebli.commun.EbliLib;
import org.fudaa.ebli.controle.BSelecteurListComboBox;
import org.fudaa.ebli.geometrie.GrBoite;
import org.fudaa.ebli.ressource.EbliResource;
import org.fudaa.ebli.volume.BCalqueBoite;
import org.fudaa.ebli.volume.BCanvas3D;
import org.fudaa.ebli.volume.BGrille;
import org.fudaa.ebli.volume.BGroupeStandard;
import org.fudaa.ebli.volume.BGroupeVolume;
import org.fudaa.ebli.volume.ZVue3DPanel;
import org.fudaa.ebli.volume.controles.BArbreVolume;
import org.fudaa.fudaa.meshviewer.export.MvExportToNodeDataActivity;
import org.fudaa.fudaa.meshviewer.export.MvExportToT3Activity;
import org.fudaa.fudaa.meshviewer.layer.MvFrontierLayerAbstract;
import org.fudaa.fudaa.tr.data.TrVisuPanel;
import org.fudaa.fudaa.tr.post.TrPostSource;
import org.fudaa.fudaa.tr.post.TrPostSourceTelemac3D;
import com.memoire.bu.BuCheckBox;
import com.memoire.bu.BuIcon;
import com.memoire.bu.BuLib;
import com.memoire.fu.Fu;
import com.memoire.fu.FuLog;
import com.vividsolutions.jts.geom.Envelope;

/**
 * @author fred deniger
 * @version $Id: Tr3DFactory.java,v 1.20 2007-06-11 13:08:20 deniger Exp $
 */
public final class Tr3DFactory {

    private Tr3DFactory() {
    }

    public static void afficheFrame(final JFrame _f, final EfGridData _grid, final InterpolationVectorContainer _vects, final CtuluUI _ui, final BGroupeCalque _fond, final MvFrontierLayerAbstract _fr) {
        if (_grid == null) {
            return;
        }
        final CtuluTaskDelegate task = _ui.createTask(Tr3DInitialiser.get3dName());
        task.start(new Runnable() {

            public void run() {
                afficheFrameAction(_f, _grid, _vects, _ui, _fond, _fr, task.getStateReceiver());
            }
        });
    }

    public static class Action3D extends EbliActionSimple {

        final TrVisuPanel visu_;

        JFrame f_;

        final String name_;

        Action3D(final TrVisuPanel _visu, final String _name) {
            super(Tr3DInitialiser.get3dName(), EbliResource.EBLI.getToolIcon("3d"), Tr3DInitialiser.get3dName());
            visu_ = _visu;
            name_ = _name;
            setEnabled(true);
            super.setDefaultToolTip(EbliLib.getS("Afficher la vue 3D"));
        }

        public ZVue3DPanel getVue3D() {
            return (ZVue3DPanel) f_.getContentPane();
        }

        @Override
        public void actionPerformed(final ActionEvent _e) {
            if (f_ == null) {
                f_ = new JFrame(super.getTitle() + " : " + name_);
                f_.setIconImage(((BuIcon) getIcon()).getImage());
                f_.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowClosed(final WindowEvent _evt) {
                        f_ = null;
                    }
                });
                visu_.view3D(f_);
            } else {
                if (f_.getExtendedState() == Frame.ICONIFIED) {
                    f_.setExtendedState(Frame.NORMAL);
                }
                f_.toFront();
            }
        }
    }

    private static EfGridData convertGrid(final EfGridData _grid, final CtuluUI _ui, final ProgressionInterface _prog, final CtuluVariable[] _vars, final InterpolationVectorContainer _vects) {
        final EfElementType elt = _grid.getGrid().getEltType();
        EfGridData endData = _grid;
        final CtuluAnalyze res = new CtuluAnalyze();
        if (elt == null || elt != EfElementType.T3 || elt != EfElementType.T3_FOR_3D) {
            if (Fu.DEBUG && FuLog.isDebug()) {
                FuLog.debug("FTR: 3D convert to T3");
            }
            final MvExportToT3Activity act = new MvExportToT3Activity(_grid);
            endData = act.process(_prog, res);
            if (_ui.manageAnalyzeAndIsFatal(res)) {
                return null;
            }
        }
        boolean isElt = false;
        for (int i = _vars.length - 1; i >= 0; i--) {
            if (_grid.isElementVar(_vars[i])) {
                isElt = true;
                break;
            }
        }
        if (isElt) {
            final MvExportToNodeDataActivity toNode = new MvExportToNodeDataActivity(endData, _vects);
            endData = toNode.process(_prog, res);
        }
        return endData;
    }

    public static void afficheFrameAction(final JFrame _f, final EfGridData _initSrc, final InterpolationVectorContainer _vects, final CtuluUI _ui, final BGroupeCalque _cqFond, final MvFrontierLayerAbstract _building, final ProgressionInterface _prog) {
        H2dVariableType varBathy = H2dVariableType.BATHYMETRIE;
        boolean containsBathy = _initSrc.isDefined(varBathy);
        H2dVariableType[] varZe = new H2dVariableType[] { H2dVariableType.COTE_EAU };
        boolean containsZe = _initSrc.isDefined(varZe[0]);
        boolean isTelemac3D = false;
        if (!containsBathy && !containsZe) {
            boolean ok = false;
            if (_initSrc instanceof TrPostSourceTelemac3D) {
                isTelemac3D = true;
                final TrPostSourceTelemac3D src = (TrPostSourceTelemac3D) _initSrc;
                final CtuluPermanentList vars = src.getCotes();
                final int size = vars.size();
                ok = size > 0;
                if (ok) {
                    containsBathy = true;
                    varBathy = (H2dVariableType) vars.get(0);
                    containsZe = size > 1;
                    varZe = new H2dVariableType[size - 1];
                    for (int i = 0; i < size - 1; i++) {
                        varZe[i] = (H2dVariableType) vars.get(i + 1);
                    }
                }
            }
            if (!ok) {
                _ui.error(Tr3DInitialiser.get3dName(), TrResource.getS("Pas de variables 3D"), false);
                return;
            }
        }
        final EfGridData endData = isTelemac3D ? _initSrc : convertGrid(_initSrc, _ui, _prog, new CtuluVariable[] { varBathy, varZe[0] }, _vects);
        if (endData == null) {
            return;
        }
        if (_prog != null) {
            _prog.setDesc(TrResource.getS("Conversion 3D"));
        }
        final ProgressionUpdater up = new ProgressionUpdater(_prog);
        final EfGridInterface grid = endData.getGrid();
        up.setValue(5, grid.getPtsNb() * 2, 0, 50);
        Point3d[] ptsBathy = null;
        final Point3d[][] ptsCote = new Point3d[varZe.length][];
        int idx = 0;
        boolean isAnim = _initSrc instanceof TrPostSource;
        if (isAnim) {
            idx = ((TrPostSource) _initSrc).getNbTimeStep() - 1;
            if (idx == 0) {
                isAnim = false;
            }
        }
        final GrBoite[] boiteZ = new GrBoite[varZe.length];
        final Envelope envelope = grid.getEnvelope(null);
        for (int i = 0; i < boiteZ.length; i++) {
            boiteZ[i] = new GrBoite(envelope);
        }
        final GrBoite boiteBathy = new GrBoite(envelope);
        final GrBoite boiteAll = new GrBoite(envelope);
        if (containsBathy) {
            ptsBathy = create3d(endData, varBathy, idx, _ui, up, boiteBathy);
            if (ptsBathy == null) {
                return;
            }
            boiteAll.ajuste(boiteBathy);
        }
        if (containsZe) {
            for (int i = 0; i < varZe.length; i++) {
                ptsCote[i] = create3d(endData, varZe[i], idx, _ui, up, boiteZ[i]);
                if (ptsCote[i] == null) {
                    return;
                }
                boiteAll.ajuste(boiteZ[i]);
            }
        }
        final int[] connect = buildConnections(up, grid);
        final BGroupeVolume gv = new BGroupeVolume();
        gv.setName(EbliLib.getS("Calques"));
        final BCalqueBoite chp = new BCalqueBoite(EbliLib.getS("Axes"));
        boiteAll.factor(1.2);
        chp.setGeometrie(boiteAll);
        gv.add(chp);
        final Tr3DCalque[] cqZe = new Tr3DCalque[varZe.length];
        if (containsZe) {
            for (int i = 0; i < varZe.length; i++) {
                cqZe[i] = createGrille(varZe[i], _initSrc, endData, idx);
                cqZe[i].setBoite(boiteZ[i]);
                cqZe[i].setGeometrie(grid.getPtsNb(), ptsCote[i], connect.length, connect);
                updateWaterLayer(cqZe[i]);
            }
        }
        if (containsBathy) {
            final Tr3DCalque cq = createGrille(varBathy, _initSrc, endData, idx);
            cq.setBoite(boiteBathy);
            cq.setGeometrie(grid.getPtsNb(), ptsBathy, connect.length, connect);
            cq.setCouleur(getBathyColor());
            cq.setBrillance(0);
            Tr3DImage.addTexture(_cqFond, cq, endData.getGrid());
            if (containsZe && cqZe.length == 1 && cqZe[0] != null) {
                cqZe[0].setBathy(cq);
            }
            gv.add(cq);
            final CtuluCollectionDouble height = _building == null ? null : _building.getBuildingHeight();
            if (_building != null && height != null) {
                final BGrille g = Tr3DBathy.addBuilding(endData, _building.get3DType(), height);
                if (g != null) {
                    gv.add(g);
                }
            }
        }
        if (containsZe) {
            for (int i = 0; i < varZe.length; i++) {
                gv.add(cqZe[i]);
            }
        }
        afficheFrame(_f, isAnim, gv, _initSrc);
        chp.setVisible(false);
    }

    private static void updateWaterLayer(final Tr3DCalque _cqZe) {
        _cqZe.setCouleur(Color.BLUE);
        _cqZe.setTransparence(0.1F);
        RenderingAttributes rendering = _cqZe.getRenderingAttributes();
        if (rendering == null) {
            rendering = new RenderingAttributes();
            _cqZe.setRenderingAttributes(rendering);
        }
        rendering.setAlphaTestFunction(RenderingAttributes.GREATER_OR_EQUAL);
        rendering.setAlphaTestValue(0.9f);
    }

    private static int[] buildConnections(final ProgressionUpdater _up, final EfGridInterface _grid) {
        final int nbPtParEle = _grid.getEltType().getNbPt();
        final int[] connect = new int[_grid.getEltNb() * nbPtParEle];
        _up.setValue(5, _grid.getPtsNb(), 50, 50);
        for (int i = 0; i < _grid.getEltNb(); i++) {
            final EfElement element = _grid.getElement(i);
            for (int k = 0; k < nbPtParEle; k++) {
                connect[nbPtParEle * i + k] = element.getPtIndex(k);
            }
        }
        return connect;
    }

    private static void afficheFrame(final JFrame _f, final boolean _isAnim, final BGroupeVolume _gv, final EfGridData _grid) {
        final ZVue3DPanel fille = new ZVue3DPanel(null);
        fille.setRoot(initGroupeStandard(_gv));
        fille.build(_isAnim);
        fille.getUnivers().init();
        initUseH(fille, _grid);
        if (_isAnim) {
            initAnim(fille);
        }
        BuLib.invokeLater(new Runnable() {

            public void run() {
                fille.updateFrame(_f);
                _f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                _f.setContentPane(fille);
                _f.pack();
                _f.show();
                fille.getUnivers().getCanvas3D().requestFocusInWindow();
            }
        });
    }

    private static void initUseH(final ZVue3DPanel _fille, final EfGridData _src) {
        if (!_src.isDefined(H2dVariableType.HAUTEUR_EAU)) {
            return;
        }
        final BArbreVolume arbre = _fille.getArbreVolume();
        final JCheckBox cbUseH = new BuCheckBox();
        cbUseH.setEnabled(false);
        cbUseH.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent _e) {
                final TreePath path = arbre.getSelectionPath();
                if (path != null && path.getLastPathComponent() instanceof Tr3DCalque) {
                    final Tr3DCalque cq = (Tr3DCalque) path.getLastPathComponent();
                    cq.setUseH(cbUseH.isSelected());
                    arbre.propertyChangedForSelectedLayer();
                }
            }
        });
        arbre.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(final TreeSelectionEvent _e) {
                final TreePath path = arbre.getSelectionPath();
                boolean enable = path != null && path.getLastPathComponent() instanceof Tr3DCalque;
                if (enable && path != null) {
                    final Tr3DCalque cq = (Tr3DCalque) path.getLastPathComponent();
                    enable = cq.isHUsable();
                    cbUseH.setEnabled(enable);
                    cbUseH.setSelected(enable && cq.isUseH());
                }
            }
        });
        _fille.addUserComponent(EbliLib.getS("Utiliser la hauteur d'eau pour la palette de couleurs"), cbUseH);
    }

    private static void initAnim(final ZVue3DPanel _fille) {
        final BArbreVolume arbre = _fille.getArbreVolume();
        final BSelecteurListComboBox palette = new BSelecteurListComboBox();
        arbre.getSelectionModel().addTreeSelectionListener(palette);
        _fille.addUserComponent(TrResource.getS("Pas de temps"), palette.getCb());
    }

    private static BGroupeStandard initGroupeStandard(final BGroupeVolume _gv) {
        final BGroupeStandard gs = new BGroupeStandard();
        gs.setName(EbliLib.getS("Univers"));
        gs.add(_gv);
        _gv.setAllVisible();
        gs.add(BCanvas3D.getGroupeLumiere(_gv));
        gs.setVisible(true);
        return gs;
    }

    public static Color getBathyColor() {
        return new Color(255, 200, 100);
    }

    private static Tr3DCalque createGrille(final H2dVariableType _var, final EfGridData _init, final EfGridData _end, final int _idx) {
        Tr3DCalque r = null;
        if (_init instanceof TrPostSource) {
            r = new Tr3DCalque.Time(_var, (TrPostSource) _init, _end, _idx);
        } else {
            r = new Tr3DCalque(_var, _end);
        }
        r.setVisible(true);
        r.setRapide(false);
        r.setEclairage(true);
        return r;
    }

    private static Point3d[] create3d(final EfGridData _grid, final H2dVariableType _var, final int _timeIdx, final CtuluUI _ui, final ProgressionUpdater _up, final GrBoite _r) {
        final EfGridInterface grid = _grid.getGrid();
        final Point3d[] pts = new Point3d[grid.getPtsNb()];
        EfData data;
        try {
            data = _grid.getData(_var, _timeIdx);
            final CtuluRange r = new CtuluRange();
            data.expandTo(r);
            _r.e_.z_ = r.max_;
            _r.o_.z_ = r.min_;
            if (Math.abs(_r.e_.z_ - _r.o_.z_) < 1E-1) {
                _r.e_.z_ = _r.o_.z_ + 1;
            }
        } catch (final IOException _evt) {
            FuLog.error(_evt);
            _ui.error(Tr3DInitialiser.get3dName(), _evt.getMessage(), false);
            return null;
        }
        for (int i = 0; i < grid.getPtsNb(); i++) {
            pts[i] = new Point3d(grid.getPtX(i), grid.getPtY(i), data.getValue(i));
            _up.majAvancement();
        }
        return pts;
    }
}
