package blue.ui.core.score.soundLayer;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import blue.soundObject.PolyObject;

/**
 * 
 * @author steven
 */
public class SoundLayerLayout implements LayoutManager {

    PolyObject pObj = null;

    /** Creates a new instance of SoundLayerLayout */
    public SoundLayerLayout() {
    }

    public void setPolyObject(PolyObject pObj) {
        this.pObj = pObj;
    }

    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }

    public Dimension preferredLayoutSize(Container parent) {
        return minimumLayoutSize(parent);
    }

    public Dimension minimumLayoutSize(Container parent) {
        int count = parent.getComponentCount();
        if (count == 0) {
            return new Dimension(0, 0);
        }
        if (parent.getParent() == null) {
            return new Dimension(0, 0);
        }
        if (pObj == null) {
            return new Dimension(0, 0);
        }
        int w = parent.getWidth();
        int h = pObj.getTotalHeight();
        return new Dimension(w, h);
    }

    public void layoutContainer(Container parent) {
        int count = parent.getComponentCount();
        if (count == 0) {
            return;
        }
        if (parent.getParent() == null) {
            return;
        }
        if (pObj == null) {
            return;
        }
        int w = parent.getWidth();
        int runningY = 0;
        for (int i = 0; i < count; i++) {
            Component temp = parent.getComponent(i);
            int h = pObj.getSoundLayerHeight(i);
            temp.setLocation(0, runningY);
            temp.setSize(w, h);
            runningY += h;
        }
    }
}
