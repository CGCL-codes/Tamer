package org.fudaa.fudaa.tr.post;

import java.io.IOException;
import com.memoire.bu.BuWizardDialog;
import com.memoire.fu.FuLog;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.dodico.h2d.type.H2dVariableType;
import org.fudaa.dodico.mesure.EvolutionReguliere;
import org.fudaa.ebli.commun.EbliActionInterface;
import org.fudaa.ebli.courbe.EGGraphe;
import org.fudaa.fudaa.meshviewer.export.MvExportChooseVarAndTime;
import org.fudaa.fudaa.tr.common.TrResource;
import org.fudaa.fudaa.tr.post.actions.TrPostCourbeAddPointsAction;
import org.fudaa.fudaa.tr.post.dialogSpec.TrPostWizardCourbeTemporelle;

/**
 * @author fred deniger
 * @version $Id: TrPostCourbeBuilder.java,v 1.9 2007-05-04 14:01:51 deniger Exp $
 */
public final class TrPostCourbeBuilder {

    private TrPostCourbeBuilder() {
    }

    public static TrPostCourbeTreeModel build(final TrPostProjet _proj, final TrPostSource _src, final H2dVariableType[] _vars, final int[] _idxPtArray, final ProgressionInterface _prog) {
        if (_prog != null) {
            _prog.setDesc(TrResource.getS("Construction des courbes"));
        }
        final TrPostCourbeTreeModel treeModel = new TrPostCourbeTreeModel(_idxPtArray, _vars, _proj);
        treeModel.addAllCourbes(_src, _prog, null);
        return treeModel;
    }

    public static EvolutionReguliere getEvol(final TrPostSource _src, final int _idxPtElt, final H2dVariableType _var) {
        final double[] times = _src.getTime().getTimeListModel().getTimesInSec();
        if (times == null) {
            return null;
        }
        final double[] values = new double[times.length];
        try {
            for (int i = values.length - 1; i >= 0; i--) {
                values[i] = _src.getData(_var, i, _idxPtElt);
            }
        } catch (final IOException _evt) {
            FuLog.error(_evt);
        }
        return new EvolutionReguliere(times, values, true);
    }

    public static TrPostCourbeTreeModel build(final TrPostProjet projet, final TrPostSource _src, final int[] _tidx, final H2dVariableType[] _vars, final TrPostInterpolatePoint _idxPtArray, final ProgressionInterface _prog) {
        if (_prog != null) {
            _prog.setDesc(TrResource.getS("Construction des courbes"));
        }
        final TrPostCourbeTreeModel treeModel = new TrPostCourbeTreeModel(projet.impl_, _src, _tidx, _idxPtArray, _vars, projet);
        treeModel.addAllCourbes(_src, _prog, null);
        return treeModel;
    }

    /**
   * Permet de creer un courbe avec plusieurs point interpoles.
   * 
   * @param projet
   * @param _src
   * @param _tidx
   * @param _vars
   * @param _idxPtArray
   * @param _prog
   * @return
   */
    public static TrPostCourbeTreeModel build(final TrPostProjet projet, final TrPostSource _src, final int[] _tidx, final H2dVariableType[] _vars, final TrPostInterpolatePoint[] _idxPtArray, final ProgressionInterface _prog) {
        if (_prog != null) {
            _prog.setDesc(TrResource.getS("Construction des courbes"));
        }
        final TrPostCourbeTreeModel treeModel = new TrPostCourbeTreeModel(projet.impl_, _src, _tidx, _idxPtArray, _vars, projet);
        treeModel.addAllCourbes(_src, _prog, null);
        return treeModel;
    }

    public static void chooseAndBuild(final TrPostVisuPanel _calque, final int[] _ptIdx, final H2dVariableType _varSelected) {
        chooseAndBuild(_calque, _ptIdx, null, _varSelected);
    }

    /**
   * Methode principale de construction d'une courbe avec l'aide d'un wizard
   */
    public static void chooseAndBuild(final TrPostVisuPanel _calque, final int[] _ptIdx, final TrPostInterpolatePoint _pt, final H2dVariableType _varSelected) {
        final MvExportChooseVarAndTime chooser = new MvExportChooseVarAndTime(_calque.getSource().getNewVarListModel(), null, TrResource.getS("Choisir les variables et les pas de temps � afficher dans les �volutions temporelles"));
        chooser.setVarSelected(_varSelected);
        final TrPostWizardCourbeTemporelle wizard = new TrPostWizardCourbeTemporelle(_calque, chooser, _ptIdx, _pt);
        final BuWizardDialog DialogWizard = new BuWizardDialog(_calque.getImpl().getFrame(), wizard);
        DialogWizard.setSize(600, 500);
        DialogWizard.setLocationRelativeTo(_calque.getPostImpl().getCurrentLayoutFille());
        DialogWizard.setVisible(true);
    }

    public static void chooseAndBuild(final TrPostVisuPanel _calque, final TrPostInterpolatePoint _pt, final H2dVariableType _varSelected) {
        chooseAndBuild(_calque, null, _pt, _varSelected);
    }

    public static EbliActionInterface[] getSpecActions(final EGGraphe _g, final TrPostCommonImplementation _impl, final TrPostVisuPanel _vue2d) {
        return new EbliActionInterface[] { new TrPostCourbeAddVariableAction(_impl, _g), new TrPostCourbeRemoveVariableAction(_impl, _g), new TrPostCourbeAddPointsAction(_impl, _g, _vue2d) };
    }
}
