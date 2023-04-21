package fr.inria.zvtm.common.gui.menu;

import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import fr.inria.zvtm.common.compositor.MetisseWindow;
import fr.inria.zvtm.engine.Camera;
import fr.inria.zvtm.engine.ViewPanel;
import fr.inria.zvtm.master.gui.MasterViewer;

/**
 * Specification for {@link Item} used to reset the visible region (set the {@link Camera} to 0,0,0 or to reset the position of a {@link MetisseWindow} (scale and zvtm offset).
 * @author Julien Altieri
 *
 */
public class ResetItem extends Item {

    public ResetItem(PopMenu parent) {
        super(parent);
    }

    @Override
    protected String getState1ImageName() {
        return "reset.png";
    }

    @Override
    protected String getState2ImageName() {
        return "resetp.png";
    }

    @Override
    public void press1(ViewPanel v, int mod, int jpxx, int jpyy, MouseEvent e) {
        super.press1(v, mod, jpxx, jpyy, e);
        drawDown();
    }

    @Override
    public void release1(ViewPanel v, int mod, int jpxx, int jpyy, MouseEvent e) {
        super.release1(v, mod, jpxx, jpyy, e);
        drawUp();
    }

    @Override
    public void click1(ViewPanel v, int mod, int jpxx, int jpyy, int clickNumber, MouseEvent e) {
        super.click1(v, mod, jpxx, jpyy, clickNumber, e);
        if (parent.parentFrame == null) {
            double[] bounds = parent.viewer.getView().getVisibleRegion(parent.viewer.getMenuCamera());
            parent.viewer.getView().centerOnRegion(parent.viewer.getNavigationManager().getCamera(), 200, bounds[0], bounds[1], bounds[2], bounds[3]);
            if (parent.viewer instanceof MasterViewer) {
                Timer t = new Timer();
                TimerTask ta = new TimerTask() {

                    @Override
                    public void run() {
                        ((MasterViewer) parent.viewer).sendViewUpgrade();
                    }
                };
                t.schedule(ta, 200);
            }
        } else {
            parent.parentFrame.resetTransform();
        }
    }
}
