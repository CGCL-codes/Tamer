package org.fudaa.fudaa.tr.post.actions;

import java.awt.event.ActionEvent;
import org.fudaa.ctulu.CtuluResource;
import org.fudaa.ebli.commun.EbliActionSimple;
import org.fudaa.fudaa.meshviewer.MvResource;
import org.fudaa.fudaa.tr.post.TrPostCommonImplementation;
import org.fudaa.fudaa.tr.post.TrPostVisuPanel;
import org.fudaa.fudaa.tr.post.dialogSpec.TrPostDialogBilan;
import org.fudaa.fudaa.tr.post.profile.MvProfileTreeModel;

/**
 * Action pour le calcul des bilans. Cette action peut etre executee depuis le mode edition du calque pu le mode edition
 * d'un profil spoatial.
 * 
 * @author Adrien Hadoux
 */
public class TrPostActionBilan extends EbliActionSimple {

    final TrPostVisuPanel panel_;

    final MvProfileTreeModel modelGraphe_;

    final boolean startWithCalque;

    TrPostCommonImplementation impl_;

    public TrPostActionBilan(final TrPostVisuPanel _visu, final TrPostCommonImplementation impl) {
        super(MvResource.getS("Calcul des bilans"), CtuluResource.CTULU.getIcon("crystal_calc1.png"), "BILAN");
        panel_ = _visu;
        modelGraphe_ = null;
        startWithCalque = true;
        impl_ = impl;
    }

    public TrPostActionBilan(final MvProfileTreeModel _model, final TrPostCommonImplementation impl) {
        super(MvResource.getS("Calcul des bilans"), CtuluResource.CTULU.getIcon("crystal_calc1.png"), "BILAN");
        modelGraphe_ = _model;
        panel_ = null;
        startWithCalque = false;
        impl_ = impl;
    }

    @Override
    public void actionPerformed(final ActionEvent _e) {
        if (startWithCalque) {
            new TrPostDialogBilan(panel_, impl_);
        } else {
            new TrPostDialogBilan(modelGraphe_, impl_);
        }
    }
}
