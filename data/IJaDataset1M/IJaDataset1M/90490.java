package org.lds.wilmington.christiana.preparedness.gui.map;

import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import com.bbn.openmap.MapBean;
import com.bbn.openmap.event.NavMouseMode;
import com.bbn.openmap.util.Debug;

public class MyMouseNavMode extends NavMouseMode {

    public MyMouseNavMode() {
        ID = "Zoom";
        try {
            ImageIcon zoomIcon = new ImageIcon(getClass().getResource("/icons/SelectZoom.gif"));
            this.setGUIIcon(zoomIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle a mouseDragged MouseMotionListener event. A rectangle is drawn
     * from the mousePressed point, since I'm assuming that I'm drawing a box to
     * zoom the map to. If a previous box was drawn, it is erased.
     * 
     * @param e MouseEvent to be handled
     */
    public void mouseDragged(MouseEvent e) {
        if (Debug.debugging("mousemodedetail")) {
            Debug.output(getID() + "|NavMouseMode.mouseDragged()");
        }
        super.mouseDragged(e);
        if (e.getSource() instanceof MapBean) {
            if (!autoZoom) return;
            paintRectangle((MapBean) e.getSource(), point1, point2);
            point2 = e.getPoint();
            paintRectangle((MapBean) e.getSource(), point1, point2);
        }
    }
}
