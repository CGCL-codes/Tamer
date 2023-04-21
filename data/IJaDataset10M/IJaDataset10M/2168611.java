package org.fudaa.fudaa.tr.data;

import org.fudaa.ebli.calque.BCalque;
import org.fudaa.ebli.geometrie.GrBoite;
import org.fudaa.fudaa.meshviewer.MvFindAction;
import org.fudaa.fudaa.meshviewer.MvFindActionAbstract;
import org.fudaa.fudaa.meshviewer.MvFindActionDefault;
import org.fudaa.fudaa.meshviewer.MvFindComponent;
import org.fudaa.fudaa.meshviewer.MvResource;
import org.fudaa.fudaa.meshviewer.layer.MvFrontierPointLayer;
import org.fudaa.fudaa.tr.common.TrResource;

/**
 * @author deniger
 * @version $Id: TrFindActionBcPoint.java,v 1.7 2005-01-31 17:14:32 deniger Exp $
 */
public class TrFindActionBcPoint implements MvFindAction {

    MvFrontierPointLayer layer_;

    TrVisuPanel visu_;

    /**
   * 
   */
    public TrFindActionBcPoint(TrVisuPanel _visu, MvFrontierPointLayer _layer) {
        visu_ = _visu;
        layer_ = _layer;
    }

    public String erreur(String _s, String _findId) {
        String s = _s;
        boolean err = false;
        boolean gene = false;
        if (_s.startsWith("G:")) {
            gene = true;
            s = _s.substring(2);
        }
        int[] idx = MvFindActionAbstract.getIndex(s);
        String r = MvFindActionAbstract.testVide(idx);
        if (r != null) {
            if (gene) err = layer_.isGeneralSelectionCorrect(idx); else err = layer_.isFrontierSelectionCorrect(idx);
            if (err) r = MvResource.getS("Des indices sont en dehors des limites");
        }
        return r;
    }

    /**
   *
   */
    public boolean find(String _s, String _action) {
        visu_.clearSelections();
        visu_.setCalqueActif(layer_);
        if (_s.startsWith("G:")) return layer_.setGeneralSelection(MvFindActionAbstract.getIndex(_s.substring(2)));
        return layer_.setFrontierSelection(MvFindActionAbstract.getIndex(_s));
    }

    public BCalque getCalque() {
        return layer_;
    }

    /**
   *
   */
    public MvFindComponent createFindComponent() {
        return new TrFindComponentBcPoint(TrResource.getS("Index(s)"));
    }

    /**
   *
   */
    public String toString() {
        return layer_.getTitle();
    }

    /**
   *
   */
    public boolean isEditableEnable() {
        return true;
    }

    /**
   *
   */
    public GrBoite getZoomOnSelected() {
        return layer_.getZoomOnSelected();
    }

    /**
   *
   */
    public String editSelected() {
        return visu_.editBcPoint();
    }
}
