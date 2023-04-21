package org.fudaa.fudaa.tr.post.actions;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Action;
import com.memoire.bu.BuBorderLayout;
import com.memoire.bu.BuBorders;
import com.memoire.bu.BuLabel;
import com.memoire.bu.BuList;
import com.memoire.bu.BuResource;
import com.memoire.bu.BuScrollPane;
import com.memoire.fu.FuComparator;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.CtuluLibArray;
import org.fudaa.ctulu.ProgressionBuAdapter;
import org.fudaa.ctulu.gui.CtuluDialogPanel;
import org.fudaa.ctulu.gui.CtuluLibSwing;
import org.fudaa.ctulu.gui.CtuluTaskOperationGUI;
import org.fudaa.dodico.h2d.type.H2dVariableType;
import org.fudaa.ebli.commun.EbliActionSimple;
import org.fudaa.ebli.courbe.EGCourbe;
import org.fudaa.ebli.courbe.EGGraphe;
import org.fudaa.fudaa.tr.post.TrPostCommonImplementation;
import org.fudaa.fudaa.tr.post.TrPostCourbeModel;
import org.fudaa.fudaa.tr.post.TrPostCourbeTreeModel;
import org.fudaa.fudaa.tr.post.TrPostSource;

/**
 * @author deniger
 */
public class TrPostCourbeAddVariableAction extends EbliActionSimple {

    final TrPostCommonImplementation impl_;

    final EGGraphe model_;

    public TrPostCourbeAddVariableAction(final TrPostCommonImplementation _impl, final EGGraphe _model) {
        super(CtuluLib.getS("Ajouter des variables"), BuResource.BU.getToolIcon("ajouter"), "ADD_VARIALBES");
        impl_ = _impl;
        model_ = _model;
    }

    public static Object[] select(final Object[] _init, final String _title, final Component _parent) {
        Arrays.sort(_init, FuComparator.STRING_COMPARATOR);
        final BuList l = CtuluLibSwing.createBuList(_init);
        final CtuluDialogPanel pn = new CtuluDialogPanel(false);
        pn.setLayout(new BuBorderLayout());
        pn.setBorder(BuBorders.EMPTY3333);
        pn.add(new BuLabel(CtuluLib.getS("S�lectionner les variables � ajouter")), BuBorderLayout.NORTH);
        pn.add(new BuScrollPane(l));
        if (CtuluDialogPanel.isOkResponse(pn.afficheModale(_parent, _title))) {
            return l.getSelectedValues();
        }
        return null;
    }

    public static int selectSource(final Object[] _init, final String _title, final Component _parent) {
        Arrays.sort(_init, FuComparator.STRING_COMPARATOR);
        final BuList l = CtuluLibSwing.createBuList(_init);
        final CtuluDialogPanel pn = new CtuluDialogPanel(false);
        pn.setLayout(new BuBorderLayout());
        pn.setBorder(BuBorders.EMPTY3333);
        pn.add(new BuLabel(CtuluLib.getS("S�lectionner le fichier r�sultat")), BuBorderLayout.NORTH);
        pn.add(new BuScrollPane(l));
        if (CtuluDialogPanel.isOkResponse(pn.afficheModale(_parent, _title))) {
            return l.getSelectedIndex();
        }
        return -1;
    }

    @Override
    public void actionPerformed(final ActionEvent _e) {
        final TrPostCourbeTreeModel model = (TrPostCourbeTreeModel) model_.getModel();
        final EGCourbe selectedComponent = model_.getSelectedComponent();
        TrPostSource src = null;
        if (selectedComponent == null || !(selectedComponent.getModel() instanceof TrPostCourbeModel)) {
            impl_.error("Impossibe, on ne peut ajouter des variables que pour une �volution temporelle. \n La courbe choisie n'est pas une �volution temporelle.");
            return;
        }
        final TrPostCourbeModel modelEvol = (TrPostCourbeModel) selectedComponent.getModel();
        final String[] values = impl_.getCurrentProject().getListSources(false);
        final int selectedSource = selectSource(values, (String) getValue(Action.NAME), impl_.getFrame());
        if (selectedSource != -1) src = impl_.getCurrentProject().getSource(selectedSource);
        if (src == null) return;
        final TrPostSource finalSrc = src;
        final Object[] selected = select(src.getAllVariablesNonVec(), (String) getValue(Action.NAME), impl_.getFrame());
        if (!CtuluLibArray.isEmpty(selected)) {
            new CtuluTaskOperationGUI(impl_, (String) getValue(Action.NAME)) {

                @Override
                public void act() {
                    model.addVariables(modelEvol, finalSrc, Arrays.asList(selected), new ProgressionBuAdapter(this), model_.getCmd());
                    EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            model_.restore();
                        }
                    });
                }
            }.start();
        }
    }
}
