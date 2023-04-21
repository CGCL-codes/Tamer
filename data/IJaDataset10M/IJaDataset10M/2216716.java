package org.fudaa.fudaa.tr.telemac;

import javax.swing.Action;
import org.fudaa.dodico.h2d.H2dBcFrontierInterface;
import org.fudaa.dodico.h2d.H2dBoundary;
import org.fudaa.dodico.h2d.telemac.H2dTelemacBcListener;
import org.fudaa.dodico.h2d.telemac.H2dTelemacParameters;
import org.fudaa.dodico.h2d.type.H2dBoundaryType;
import org.fudaa.dodico.h2d.type.H2dVariableType;
import org.fudaa.ebli.find.EbliFindActionInterface;
import org.fudaa.fudaa.tr.common.TrResource;
import org.fudaa.fudaa.tr.data.TrFindActionFrontierPt;
import org.fudaa.fudaa.tr.reflux.TrBcNodeLayer;

/**
 * @author fred deniger
 * @version $Id: TrTelemacBcPointLayer.java,v 1.6 2007-02-02 11:22:26 deniger Exp $
 */
public class TrTelemacBcPointLayer extends TrBcNodeLayer implements H2dTelemacBcListener {

    public TrTelemacBcPointLayer(final H2dTelemacParameters _parameters, final TrTelemacInfoSenderDefault _delegate) {
        super(new TrTelemacBcPointModel(_parameters), new TrTelemacNormaleModel(_parameters.getTelemacCLManager()));
        ((TrTelemacBcPointModel) modeleDonnees()).setDelegate(_delegate);
        _parameters.getTelemacCLManager().addClChangedListener(this);
        putClientProperty(Action.SHORT_DESCRIPTION, TrResource.getS("Permet de cr�er des bords"));
    }

    public void bcBoundaryTypeChanged(final H2dBoundary _b, final H2dBoundaryType _old) {
        repaint();
    }

    public void bcFrontierStructureChanged(final H2dBcFrontierInterface _b) {
        repaint();
    }

    public void bcParametersChanged(final H2dBoundary _b, final H2dVariableType _t) {
        repaint();
    }

    public void bcPointsParametersChanged(final H2dVariableType _t) {
        repaint();
    }

    @Override
    public EbliFindActionInterface getFinder() {
        return new TrFindActionFrontierPt(this);
    }
}
