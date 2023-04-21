package net.sourceforge.theba.trackers;

import net.sourceforge.theba.core.ImageFunctions;
import net.sourceforge.theba.core.Stack;
import net.sourceforge.theba.core.Tracker;
import net.sourceforge.theba.core.gui.ThebaGUI;
import net.sourceforge.theba.core.math.Point3D;
import javax.swing.*;

/**
 * This class represents a simple segmentation of the volume using 6-connected
 * regions as the segmentation criteria
 *
 * @author jensbw
 * @author perchrh
 */
public class FloodTracker3D extends Tracker {

    public FloodTracker3D(ThebaGUI f) {
        super(f);
    }

    @Override
    public void setup() {
        Runnable lumenAction = new Runnable() {

            public void run() {
                track();
            }
        };
        control.addMenuItem("Label regions", lumenAction);
    }

    /**
     * This is the code executed to track all currently selected seeds
     */
    @Override
    public void track() {
        Stack input = control.getStack();
        String min = JOptionPane.showInputDialog("Set minimum region size in voxels", "100");
        if (min == null) {
            return;
        }
        long totalSize = 0;
        int removeCount = 0;
        int count = 0;
        short id = 255;
        for (int z = 0; z < input.getDepth(); z++) {
            for (int y = 0; y < input.getHeight(); y++) {
                for (int x = 0; x < input.getWidth(); x++) {
                    short val = input.getVoxelUnchecked(x, y, z);
                    if (val == 0xff) {
                        id++;
                        long size = ImageFunctions.floodFill3D(input, x, y, z, id);
                        totalSize += size;
                        if (size < 100) {
                            ImageFunctions.delete3D(input, x, y, z);
                            id--;
                            removeCount++;
                        } else {
                            count++;
                        }
                    }
                }
            }
            StringBuffer total = new StringBuffer();
            total.append("Regions found : " + (count) + "\n");
            total.append("Removed: " + removeCount + "\n");
            total.append("Avgsize : " + (totalSize / (count)) + "\n");
            control.showResults(total, "Results");
            control.setProgress(z);
        }
        control.updateImage();
    }

    @Override
    public void mouseClicked(Point3D point3D) {
    }

    @Override
    public void stop() {
    }

    @Override
    public void reset() {
    }
}
