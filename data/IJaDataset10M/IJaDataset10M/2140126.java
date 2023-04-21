package org.fudaa.fudaa.tr.reflux;

import org.fudaa.dodico.h2d.H2dBcFrontierInterface;
import org.fudaa.dodico.h2d.H2dBoundary;
import org.fudaa.dodico.h2d.reflux.H2dRefluxBcListener;
import org.fudaa.dodico.h2d.reflux.H2dRefluxBcManager;
import org.fudaa.dodico.h2d.type.H2dBoundaryType;
import org.fudaa.dodico.h2d.type.H2dVariableType;
import org.fudaa.ebli.calque.BCalqueLegende;
import org.fudaa.fudaa.tr.data.TrBcBoundaryMiddleModelDefault;
import org.fudaa.fudaa.tr.data.TrRefluxBcBoundaryMiddleLayer;

/**
 * @author Fred Deniger
 * @version $Id: TrRefluxBoundaryMiddleLayer.java,v 1.5 2006-10-19 13:55:15 deniger Exp $
 */
public class TrRefluxBoundaryMiddleLayer extends TrRefluxBcBoundaryMiddleLayer implements H2dRefluxBcListener {

    /**
   * @param _mng
   */
    public TrRefluxBoundaryMiddleLayer(final H2dRefluxBcManager _mng, final BCalqueLegende _cq, final TrRefluxInfoSenderDefault _info) {
        super(new TrBcBoundaryMiddleModelDefault(_mng), _cq);
        _mng.addClListener(this);
        m_.setDelegate(_info);
    }

    public void bcBoundaryTypeChanged(final H2dBoundary _bound, final H2dBoundaryType _old) {
        updateLegende();
        repaint();
    }

    public void bcFrontierStructureChanged(final H2dBcFrontierInterface _b) {
        updateLegende();
        repaint();
    }

    public void bcParametersChanged(final H2dBoundary _b, final H2dVariableType _t) {
    }

    public void bcPointsNormalChanged() {
    }

    public void bcPointsParametersChanged(final H2dVariableType _t) {
    }
}
