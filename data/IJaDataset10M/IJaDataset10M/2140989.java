package megamek.client.ui.swing.widget;

import java.util.Vector;
import megamek.common.Entity;

/**
 * Generic set of PicMap areas do represent various units in MechDisplay class
 */
public interface DisplayMapSet {

    public PMAreasGroup getContentGroup();

    public Vector<BackGroundDrawer> getBackgroundDrawers();

    public void setEntity(Entity e);
}
