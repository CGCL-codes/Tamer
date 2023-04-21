package com.ivis.xprocess.ui.diagram.actions;

import org.eclipse.draw2d.DefaultRangeModel;
import org.eclipse.draw2d.RangeModel;
import org.eclipse.draw2d.Viewport;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.ui.IWorkbenchPart;
import com.ivis.xprocess.ui.UIType;

public abstract class AbstractZoomAction extends WorkbenchPartAction implements ZoomListener {

    protected static final double ZOOM_STEP = 1.1;

    private final GraphicalViewer myViewer;

    public AbstractZoomAction(IWorkbenchPart part, GraphicalViewer viewer, UIType uiType, String id) {
        super(part);
        myViewer = viewer;
        setImageDescriptor(uiType.getImageDescriptor());
        setText(uiType.label);
        setToolTipText(getText());
        setActionDefinitionId(id);
        getZoomer().addZoomListener(this);
    }

    public void zoomChanged(double zoom) {
        setEnabled(calculateEnabled());
    }

    @Override
    public void dispose() {
        super.dispose();
        getZoomer().removeZoomListener(this);
    }

    @Override
    protected boolean calculateEnabled() {
        return calculateEnabled(getZoomer());
    }

    protected abstract boolean calculateEnabled(ZoomManager zoomer);

    private GraphicalViewer getViewer() {
        return myViewer;
    }

    private ZoomManager getZoomer() {
        RootEditPart root = getViewer().getRootEditPart();
        if (!(root instanceof ScalableFreeformRootEditPart)) {
            throw new RuntimeException("root must be ScalableFreeformRootEditPart");
        }
        ZoomManager zoomer = ((ScalableFreeformRootEditPart) root).getZoomManager();
        if (zoomer == null) {
            throw new RuntimeException("zoomer cannot be null");
        }
        return zoomer;
    }

    @Override
    public void run() {
        GraphicalViewer viewer = getViewer();
        RootEditPart root = viewer.getRootEditPart();
        if (!(root instanceof ScalableFreeformRootEditPart)) {
            return;
        }
        ScalableFreeformRootEditPart scalableRoot = (ScalableFreeformRootEditPart) root;
        ZoomManager zoomer = scalableRoot.getZoomManager();
        run(zoomer);
        Viewport vp = (Viewport) scalableRoot.getFigure();
        setSavePositionFlag(vp.getVerticalRangeModel());
        setSavePositionFlag(vp.getHorizontalRangeModel());
        EditPart contents = viewer.getContents();
        if (contents != null) {
            contents.refresh();
        }
    }

    protected abstract void run(ZoomManager zoomer);

    private void setSavePositionFlag(RangeModel rangeModel) {
        if (rangeModel instanceof PositionSavingRangeModel) {
            ((PositionSavingRangeModel) rangeModel).savePositionOnResize();
        }
    }

    class PositionSavingRangeModel extends DefaultRangeModel {

        private boolean mySavePositionFlag = false;

        private float myRatio = 0;

        public void savePositionOnResize() {
            if (isEnabled()) {
                mySavePositionFlag = true;
            }
        }

        public void setMaximum(int maximum) {
            if (getMaximum() == maximum) {
                super.setMaximum(maximum);
                return;
            }
            if (mySavePositionFlag) {
                storeRelativePosition();
            }
            super.setMaximum(maximum);
            if (mySavePositionFlag) {
                restoreRelativePosition();
            }
        }

        public void setMinimum(int minimum) {
            if (getMinimum() == minimum) {
                super.setMinimum(minimum);
                return;
            }
            if (mySavePositionFlag) {
                storeRelativePosition();
            }
            super.setMinimum(minimum);
            if (mySavePositionFlag) {
                restoreRelativePosition();
            }
        }

        private void storeRelativePosition() {
            myRatio = getMaximum() - getMinimum();
            myRatio = (getValue() + (getExtent() / 2)) / myRatio;
        }

        private void restoreRelativePosition() {
            mySavePositionFlag = false;
            float absoluteRange = getMaximum() - getMinimum();
            setValue(Math.round((myRatio * absoluteRange) - (getExtent() / 2)));
        }
    }
}
