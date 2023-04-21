package co.edu.unal.ungrid.services.client.applet.atlas.view;

import java.awt.event.MouseEvent;
import co.edu.unal.ungrid.core.Point2D;
import co.edu.unal.ungrid.services.client.applet.atlas.model.curve.ControlPointCurve;
import co.edu.unal.ungrid.services.client.applet.atlas.model.curve.CurveListener.CurveEvent;

public class TabletMouseAdapter extends StandardMouseAdapter {

    public TabletMouseAdapter() {
        super();
    }

    @Override
    protected void curveAction(MouseEvent me) {
        Point2D pt = m_view.screenToWorld(me.getPoint());
        ControlPointCurve curve = m_view.getCurve(pt);
        if (curve == null) {
            curve = m_view.addTmpCurve();
            if (curve != null) {
                m_bNewCurveAdded = true;
                m_view.notifyCurveListeners(curve, CurveEvent.CREATED);
                curve.addPoint(pt);
                m_view.notifyCurveListeners(curve, CurveEvent.MODIFIED);
            }
        } else {
            m_nSelPoint = curve.selectPoint(pt);
            if (m_nSelPoint < 0) {
                m_nSelPoint = curve.addPoint(pt);
                m_view.notifyCurveListeners(curve, CurveEvent.MODIFIED);
            }
        }
        if (curve != null) {
            setSelCurve(curve);
            m_view.repaint();
        }
    }

    @Override
    public boolean processLeftButtonUp(MouseEvent me) {
        boolean b = super.processLeftButtonUp(me);
        if (m_bNewCurveAdded) {
            ControlPointCurve curve = m_view.getTmpCurve();
            if (curve != null) {
                if (curve.numPoints() > 1) {
                    curve.purge(0.01);
                    m_view.commitTmpCurve();
                }
            }
        }
        return b;
    }

    @Override
    protected void movePoint(MouseEvent me) {
        if (movingPoint()) {
            super.movePoint(me);
        } else {
            if (m_bNewCurveAdded) {
                Point2D pt = m_view.screenToWorld(me.getPoint());
                m_selCurve.addPoint(pt);
                m_view.repaint();
                m_view.notifyCurveListeners(m_selCurve, CurveEvent.MODIFIED);
            }
        }
    }
}
