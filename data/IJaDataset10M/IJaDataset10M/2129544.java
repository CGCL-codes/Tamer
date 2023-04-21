package fr.inria.zvtm.common.gui.menu;

import java.awt.event.MouseEvent;
import fr.inria.zvtm.engine.ViewPanel;

/**
 * Used for move action (either in attached or free mode)
 * @author Julien Altieri
 *
 */
public class DragItem extends DragableItem {

    private static final long serialVersionUID = 1L;

    private int lastJPX;

    private int lastJPY;

    private double pvx;

    private double pvy;

    private double vx;

    private double vy;

    public DragItem(PopMenu parent) {
        super(parent);
    }

    @Override
    protected String getState1ImageName() {
        return "drag.png";
    }

    @Override
    protected String getState2ImageName() {
        return "dragp.png";
    }

    @Override
    public void press1(ViewPanel v, int mod, int jpxx, int jpyy, MouseEvent e) {
        super.press1(v, mod, jpxx, jpyy, e);
        if (parent.parentFrame == null) {
            ((GlyphEventDispatcherForMenu) parent.ged).getCursor().setVisible(false);
        }
        if (parent.parentFrame != null) setFreedom(false); else setFreedom(true);
        pvx = ((GlyphEventDispatcherForMenu) parent.ged).getCursor().getVSXCoordinate() - parent.viewer.getNavigationManager().getCamera().vx;
        pvy = ((GlyphEventDispatcherForMenu) parent.ged).getCursor().getVSYCoordinate() - parent.viewer.getNavigationManager().getCamera().vy;
        int[] co = project(pvx, pvy);
        lastJPX = co[0];
        lastJPY = co[1];
    }

    @Override
    public void release1(ViewPanel v, int mod, int jpxx, int jpyy, MouseEvent e) {
        super.release1(v, mod, jpxx, jpyy, e);
        if (parent.parentFrame == null) {
            if (parent.viewer.getCursorHandler() != null) parent.viewer.getCursorHandler().move(jpxx, jpyy, e); else ((GlyphEventDispatcherForMenu) parent.ged).getCursor().moveCursorTo(vx, vy, jpxx, jpyy);
            parent.viewer.getNavigationManager().getCamera().setXspeed(0);
            parent.viewer.getNavigationManager().getCamera().setYspeed(0);
            ((GlyphEventDispatcherForMenu) parent.ged).getCursor().setVisible(true);
        }
    }

    @Override
    public void mouseDragged(ViewPanel v, int mod, int buttonNumber, int jpxx, int jpyy, MouseEvent e) {
        super.mouseDragged(v, mod, buttonNumber, jpxx, jpyy, e);
        if (buttonNumber != 1) return;
        vx = ((GlyphEventDispatcherForMenu) parent.ged).getCursor().getVSXCoordinate() - parent.viewer.getNavigationManager().getCamera().vx;
        vy = ((GlyphEventDispatcherForMenu) parent.ged).getCursor().getVSYCoordinate() - parent.viewer.getNavigationManager().getCamera().vy;
        int[] co = project(vx, vy);
        double jpx = co[0];
        double jpy = co[1];
        if (parent.parentFrame == null) {
            parent.viewer.getNavigationManager().getCamera().setXspeed(((jpx - lastJPX) * (parent.getAltFactor() / parent.viewer.getNavigationManager().PAN_SPEED_COEF)));
            parent.viewer.getNavigationManager().getCamera().setYspeed(((lastJPY - jpy) * (parent.getAltFactor() / parent.viewer.getNavigationManager().PAN_SPEED_COEF)));
        } else {
            parent.parentFrame.moveGlyphOf(vx - pvx, vy - pvy);
            parent.move(vx - pvx, vy - pvy);
        }
        pvx = vx;
        pvy = vy;
    }
}
