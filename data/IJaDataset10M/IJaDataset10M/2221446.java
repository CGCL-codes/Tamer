package net.sourceforge.theba.trackers;

import net.sourceforge.theba.core.*;
import net.sourceforge.theba.core.gui.ThebaGUI;
import net.sourceforge.theba.core.math.Point3D;
import net.sourceforge.theba.descriptors.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class represents the methods suggested for composite fiber-segmentation
 * in our thesis
 *
 * @author jensbw
 * @author perchrh
 */
public class FiberTracker extends Tracker {

    private JToggleButton seedButton;

    private LinkedList<Seed> seedQueue;

    private LinkedList<LumenCandidate> seeds = new LinkedList<LumenCandidate>();

    boolean seedStillValid = true;

    private short currentId;

    private short[] map = null;

    private short[] map2;

    private boolean automerge = false;

    private int minimumFiberSize = 50;

    private boolean removeShortFibers = false;

    /**
     * This is the code executed to track all currently selected seeds
     */
    @SuppressWarnings("unchecked")
    @Override
    public void track() {
        int width = control.getStack().getWidth();
        int height = control.getStack().getHeight();
        int depth = control.getStack().getDepth();
        automerge = (1 == control.getPreferences().getInt("autoMerge", 0));
        Point[][] skeleton = new Point[control.MAX_FIBERS][depth];
        Collections.sort(seeds);
        Point3D s;
        Iterator<LumenCandidate> iter = seeds.iterator();
        int count = 0;
        keepTracking = true;
        while (iter.hasNext() && keepTracking && !control.isStopped()) {
            s = iter.next();
            seedQueue = new LinkedList<Seed>();
            count++;
            System.out.println("\nTracking seed " + count + " of " + seeds.size() + " starting at " + s);
            short[] data = control.getSlice(s.z);
            if (data[s.x + s.y * width] != 0) {
                continue;
            }
            currentId = (control.getNewFiberId());
            ImageFunctions.delete3D(control.getStack(), s.x, s.y, s.z);
            seedQueue.addLast(new Seed(s.x, s.y, s.z, 1, null, null, 0));
            log("***************************");
            log("Tracking seed with origin " + s);
            StringBuffer results = new StringBuffer();
            results.append("Tracking seed  " + count + " of " + seeds.size() + "\n from " + s);
            control.showResults(results, "Lumen tracking");
            short[] lumenMask = new short[data.length];
            int slice = s.z;
            ImageFunctions.floodFill2D(s.x, s.y, width, height, data, lumenMask, currentId);
            short[] lcopy = lumenMask.clone();
            seedStillValid = true;
            int length = 0;
            while (slice < depth && seedStillValid && keepTracking) {
                control.setProgress(slice);
                removeCrack(slice, lumenMask);
                removeStuff(slice, lumenMask);
                if (automerge) skeleton[currentId][slice] = ImageFunctions.getAveragePoint(lumenMask, width, height);
                if (slice % 2 == 0) control.showImage(control.getSlice(slice));
                slice++;
            }
            seedStillValid = true;
            slice = s.z - 1;
            lumenMask = lcopy;
            while (slice >= 0 && seedStillValid && keepTracking && !control.isStopped()) {
                control.setProgress(slice);
                removeCrack(slice, lumenMask);
                removeStuff(slice, lumenMask);
                if (slice % 2 == 0) {
                    control.showImage(control.getSlice(slice));
                }
                if (automerge) {
                    skeleton[currentId][slice] = ImageFunctions.getAveragePoint(lumenMask, width, height);
                }
                slice--;
            }
            control.showImage(control.getSlice(slice + 1));
            if (automerge) checkMerges(skeleton);
            length = 0;
            for (int z = 0; z < depth; z++) {
                if (skeleton[currentId][z] != null) {
                    length++;
                }
            }
            if (removeShortFibers) {
                if (length < minimumFiberSize) {
                    for (int z = 0; z < depth; z++) {
                        short[] pixels = control.getSlice(z);
                        for (int i = 0; i < pixels.length; i++) {
                            if (pixels[i] == currentId) {
                                pixels[i] = control.INVALID;
                            }
                        }
                    }
                    control.releaseFiberId(currentId);
                    skeleton[currentId] = new Point[depth];
                }
            }
        }
        control.setProgressComplete();
        control.updateImage();
        seeds.clear();
        control.showResults("Tracking lumens complete");
    }

    public FiberTracker(ThebaGUI f) {
        super(f);
    }

    @Override
    public void setup() {
        seedButton = new JToggleButton("Mark seed");
        control.addToolbarButton(seedButton);
        Runnable lumenAction = new Runnable() {

            public void run() {
                track();
            }
        };
        Runnable wallAction2 = new Runnable() {

            public void run() {
                traceWalls3d();
            }
        };
        FindCandidatesAction findCandidates = new FindCandidatesAction();
        FindAllCandidatesAction findAllCandidates = new FindAllCandidatesAction();
        Runnable clearAction = new Runnable() {

            public void run() {
                seeds.clear();
            }
        };
        control.addMenuItem("Track lumens", lumenAction);
        control.addMenuItem("Track walls 3D", wallAction2);
        control.addMenuSeparator();
        control.addMenuItem("Find seeds in this slice", findCandidates);
        control.addMenuItem("Find seeds for every n'th slice", findAllCandidates);
        control.addMenuSeparator();
        control.addMenuItem("Autosegment", new AutosegmentAction());
        control.addMenuSeparator();
        control.addMenuItem("Clear seeds", clearAction);
    }

    public void autoSegment() {
        Stack v = control.getStack();
        for (int i = 0; i < v.getDepth(); i += 100) {
            findCandidates(i);
        }
        track();
        control.flipVolume();
        v = control.getStack();
        for (int i = 0; i < v.getDepth(); i += 100) {
            findCandidates(i);
        }
        track();
        control.flipVolume();
    }

    static final short max(short x, short y) {
        if ((x) > (y)) return x;
        return y;
    }

    /**
     * Dilates a 3d volume using the elementary 6-connected structure element
     *
     * @param voxels
     */
    public static void dilate3d_toWhite(Stack voxels, long[] counts) {
        int width = voxels.getWidth();
        int height = voxels.getHeight();
        int depth = voxels.getDepth();
        short[] prevdata = new short[width * height];
        short[] data = new short[width * height];
        short[] nextdata = new short[width * height];
        ThebaGUI control = ThebaGUI.getInstance();
        System.arraycopy(voxels.getSlice(1), 0, nextdata, 0, nextdata.length);
        System.arraycopy(voxels.getSlice(0), 0, data, 0, data.length);
        for (int z = 0; z < depth; z++) {
            short[] origdata = voxels.getSlice(z);
            control.setProgress(z);
            if (control.isStopped()) {
                return;
            }
            int index = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    short max = data[index];
                    if (max == 255) {
                        if (x > 0) max = max(max, data[index - 1]);
                        if (x < width - 1) max = max(max, data[index + 1]);
                        if (y > 0) max = max(max, data[index - width]);
                        if (y < height - 1) max = max(max, data[index + width]);
                        if (z > 0) max = max(max, prevdata[index]);
                        if (z < depth - 1) max = max(max, nextdata[index]);
                        int id = max & (~(1 << 12));
                        if (max > data[index] && counts[id] != -1) {
                            origdata[index] = (short) (max | (1 << 12));
                            counts[id]++;
                        }
                    }
                    index++;
                }
            }
            if (z > 0) {
                System.arraycopy(data, 0, prevdata, 0, prevdata.length);
            }
            System.arraycopy(nextdata, 0, data, 0, data.length);
            if (z < depth - 2) {
                System.arraycopy(voxels.getSlice(z + 2), 0, nextdata, 0, nextdata.length);
            }
        }
    }

    /**
     * The wall labelling is based on 3d dilations, while counting the amount of
     * added voxels to each region and stopping the growth if the added number
     * of voxels is less than a quarter the result of the first dilation.
     */
    private void traceWalls3d() {
        Stack stack = control.getStack();
        int width = stack.getWidth();
        int height = stack.getHeight();
        int depth = stack.getDepth();
        long[] startvals = new long[control.MAX_FIBERS];
        long[] lumenvals = new long[control.MAX_FIBERS];
        for (int i = 0; i < 15; i++) {
            for (int k = 0; k < lumenvals.length; k++) {
                if (lumenvals[k] > 0) lumenvals[k] = 0;
            }
            dilate3d_toWhite(control.getStack(), lumenvals);
            for (int k = 0; k < lumenvals.length; k++) {
                if (lumenvals[k] > 0) {
                    if (i == 0) {
                        startvals[k] = lumenvals[k];
                    }
                    if (lumenvals[k] < startvals[k] / 4) {
                        lumenvals[k] = -1;
                    }
                }
            }
            control.updateImage();
        }
        ThebaGUI control = ThebaGUI.getInstance();
        for (int z = 0; z < depth; z++) {
            short[] data = control.getSlice(z);
            control.setProgress(z);
            if (control.isStopped()) {
                return;
            }
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int index = x + y * width;
                    if (data[index] == 255) continue;
                    if ((data[index] & (1 << 12)) != 0) {
                        data[index] = (short) (data[index] & ~(1 << 12));
                    } else {
                        data[index] = 0;
                    }
                }
            }
        }
        control.updateImage();
    }

    public float checkReg(int xp, int yp, final short[] input) {
        short[] mark = new short[input.length];
        int width = control.getStack().getWidth();
        int height = control.getStack().getHeight();
        LinkedList<Point> queue = new LinkedList<Point>();
        queue.addFirst(new Point(xp, yp));
        float whiteCount = 0;
        float borderCount = 0;
        short col = input[xp + yp * width];
        while (!queue.isEmpty()) {
            Point p = queue.removeLast();
            int x = p.x;
            int y = p.y;
            int index = x + y * width;
            if (x + 1 < width) {
                if (input[index + 1] == 255) whiteCount++; else if (input[index + 1] == 0) borderCount++;
                if (mark[index + 1] == 0 && input[index + 1] == col) {
                    mark[index + 1] = 1;
                    queue.addFirst(new Point(x + 1, y));
                }
            }
            if (y + 1 < height) {
                if (input[index + width] == 255) whiteCount++; else if (input[index + width] == 0) borderCount++;
                if (mark[index + width] == 0 && input[index + width] == col) {
                    mark[index + width] = 1;
                    queue.addFirst(new Point(x, y + 1));
                }
            }
            if (y - 1 >= 0) {
                if (input[index - width] == 255) whiteCount++; else if (input[index - width] == 0) borderCount++;
                if (mark[index - width] == 0 && input[index - width] == col) {
                    mark[index - width] = 1;
                    queue.addFirst(new Point(x, y - 1));
                }
            }
            if (x - 1 >= 0) {
                if (input[index - 1] == 255) whiteCount++; else if (input[index - 1] == 0) borderCount++;
                if (mark[index - 1] == 0 && input[index - 1] == col) {
                    mark[index - 1] = 1;
                    queue.addFirst(new Point(x - 1, y));
                }
            }
        }
        borderCount += whiteCount;
        return whiteCount / borderCount;
    }

    /**
     * Returns a centered point in a region marked by search data.
     */
    public void findCandidates(int slice) {
        int width = control.getStack().getWidth();
        int height = control.getStack().getHeight();
        short[] pixels = control.getSlice(slice);
        short[] copy = pixels.clone();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = x + y * width;
                if (copy[index] == 0) {
                    short[] mask = new short[pixels.length];
                    short color = (short) 0xf0;
                    int lumenArea = ImageFunctions.floodFill2D(x, y, width, height, copy, mask, color);
                    for (int i = 0; i < pixels.length; i++) if (mask[i] != 0) copy[i] = (short) 1;
                    boolean okLumen = true;
                    okLumen = okLumen && lumenArea < 8000 && lumenArea > 500;
                    RegionMask2D maskreg = new RegionMask2D(mask, width, height);
                    if (okLumen) {
                        double circularity = (new CircularityDescriptor()).measure(mask, width, height);
                        okLumen = okLumen && circularity < 3.8 && circularity > 0.7;
                    }
                    if (okLumen) {
                        double curvature = (new AvgYoungCurvature()).measure(mask, width, height);
                        okLumen = okLumen && curvature > 0 && curvature < 1.6;
                    }
                    if (okLumen) {
                        double bending_energy = (new YoungBendingEnergy()).measure(mask, width, height);
                        okLumen = okLumen && bending_energy > 0 && bending_energy < 8.0;
                    }
                    if (okLumen) {
                        double convexity = (Double) (new ConvexityDescriptor()).measure(maskreg);
                        okLumen = okLumen && convexity > 0.8 && convexity < 1.0;
                    }
                    if (okLumen) {
                        double solidity = (new SolidityDescriptor()).measure(mask, width, height);
                        okLumen = okLumen && solidity < 1.3 && solidity > 0.7;
                    }
                    if (okLumen) {
                        double eccentricity = (new EccentricityDescriptor()).measure(mask, width, height);
                        okLumen = okLumen && eccentricity < 15 && eccentricity > 1;
                    }
                    if (!okLumen && lumenArea < 200 && lumenArea > 80) {
                        double eccentricity = (new EccentricityDescriptor()).measure(mask, width, height);
                        okLumen = eccentricity > 15;
                    }
                    if (okLumen) {
                        int internalRegionCount = (Integer) (new InternalRegionDescriptor()).measure(maskreg);
                        okLumen = okLumen && internalRegionCount < 2;
                    }
                    if (okLumen) {
                        okLumen = okLumen && !ImageFunctions.touchBorder(mask, width, height);
                    }
                    if (okLumen) {
                        LumenCandidate newSeed = new LumenCandidate(x, y, slice, (lumenArea));
                        seeds.add(newSeed);
                        for (int i = 0; i < pixels.length; i++) if (mask[i] != 0) {
                            copy[i] = 0xcc;
                        }
                    }
                }
            }
        }
        control.showImage(copy);
    }

    public boolean isReserved(short val) {
        if (val == 255 || val == control.INVALID) return true;
        return false;
    }

    @Override
    public void mouseClicked(Point3D p) {
        if (seedButton.isSelected()) selectSeed(new Point(p.x, p.y));
    }

    public boolean removeCrack(int slice, short[] lumen) {
        int width = control.getStack().getWidth();
        int height = control.getStack().getHeight();
        int depth = control.getStack().getDepth();
        short[] data = control.getSlice(slice);
        short[] invdata = ImageFunctions.invertMask(data, false);
        ImageFunctions.invertMask(lumen, false);
        Rectangle bounds = ImageFunctions.getBounds(lumen, width, height, 12);
        if (bounds == null || bounds.x < 0 || bounds.y < 0 || bounds.width > width || bounds.height > height) {
            seedStillValid = false;
            slice = depth;
            return true;
        }
        if (map == null || map.length != data.length) {
            map = new short[data.length];
            map2 = new short[data.length];
        }
        for (int i = 0; i < map.length; i++) {
            if (lumen[i] != 0) map[i] = 1; else map[i] = 0;
        }
        map = ImageFunctions.dilate(lumen, map, width, height, bounds, (short) 2);
        int maxDilations = control.getPreferences().getInt("maxDilations", 12);
        for (int i = 3; i < maxDilations; i++) {
            System.arraycopy(map, 0, map2, 0, map.length);
            map = ImageFunctions.dilateMasked(map2, map, invdata, width, height, bounds, (short) i);
        }
        map = ImageFunctions.mask(map, invdata, true);
        BackTrack.backTrack(map, 9, width, height, bounds);
        for (int i = 0; i < map.length; i++) {
            lumen[i] = map[i];
            if (lumen[i] != 0) data[i] = currentId;
        }
        return false;
    }

    /**
     * Counts the number of lumen regions and orders them
     * by size. Only the largest region is kept, unless the two
     * largest regions are comparable in size indicating a lumen split.
     */
    @SuppressWarnings("unchecked")
    public void removeStuff(int slice, short[] lumen) {
        int width = control.getStack().getWidth();
        int height = control.getStack().getHeight();
        int regId = 1;
        short[] mark = new short[width * height];
        short[] slicedata = control.getSlice(slice);
        ArrayList<LumenCandidate> liste = new ArrayList<LumenCandidate>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (mark[x + y * width] == 0 && lumen[x + y * width] != 0) {
                    regId++;
                    int size = ImageFunctions.floodFill2D(x, y, width, height, lumen, mark, (short) regId);
                    LumenCandidate thisCandidate = new LumenCandidate(x, y, 0, size);
                    float borderRatio = checkReg(x, y, slicedata);
                    if (borderRatio < 0.7) {
                        log("Removed isolated region");
                        ImageFunctions.floodFill2D(x, y, width, height, slicedata, (short) 0);
                        ImageFunctions.floodFill2D(x, y, width, height, lumen, (short) 0);
                    } else {
                        liste.add(thisCandidate);
                    }
                }
            }
        }
        int numberOfRegionsToKeep = 2;
        if (liste.size() > 1) {
            Collections.sort(liste);
            boolean useMR_rules = true;
            if (useMR_rules) {
                LumenCandidate A = liste.get(0);
                LumenCandidate B = liste.get(1);
                if (A.getSize() > 5 * B.getSize()) {
                    numberOfRegionsToKeep--;
                }
            }
            for (int i = numberOfRegionsToKeep; i < liste.size(); i++) {
                LumenCandidate l = liste.remove(i);
                ImageFunctions.floodFill2D(l.x, l.y, width, height, slicedata, (short) 0);
                ImageFunctions.floodFill2D(l.x, l.y, width, height, lumen, (short) 0);
            }
        }
    }

    public void selectSeed(Point e) {
        int width = control.getStack().getWidth();
        int height = control.getStack().getHeight();
        short[] data = control.getCurrentPixels();
        short[] filledRegion = new short[data.length];
        if (data[(int) e.getX() + (int) e.getY() * width] == 255) {
            return;
        }
        int count = ImageFunctions.floodFill2D(data, filledRegion, (int) e.getX(), (int) e.getY(), width, height, (short) 200);
        Point p = e.getLocation();
        short[] displayedImage = data.clone();
        if (p != null) {
            for (int i = 0; i < data.length; i++) {
                if (filledRegion[i] != 0) {
                    displayedImage[i] = filledRegion[i];
                }
            }
            control.showImage(displayedImage);
            if (count > 10000) {
                int ans = JOptionPane.showConfirmDialog(null, "This regions has " + count + " pixels \nAre you sure you want to add this region?", "Confirm seed", JOptionPane.YES_NO_OPTION);
                if (ans == JOptionPane.NO_OPTION) {
                    control.updateImage();
                    return;
                }
            }
            seeds.add(new LumenCandidate(p.x, p.y, control.currentSlice(), count));
        }
    }

    private void checkMerges(Point[][] skel) {
        int width = control.getStack().getWidth();
        int height = control.getStack().getHeight();
        int depth = control.getStack().getDepth();
        int[] mergeList = new int[control.MAX_FIBERS];
        for (int z = 1; z < depth - 1; z++) {
            short[] slice = control.getSlice(z);
            short[] next = control.getSlice(z + 1);
            short[] prev = control.getSlice(z - 1);
            for (int x = 1; x < width - 1; x++) {
                for (int y = 1; y < height - 1; y++) {
                    int index = x + y * width;
                    if (slice[index] == currentId) {
                        mergeList[next[index]] = 1;
                        mergeList[prev[index]] = 1;
                    }
                }
            }
        }
        for (short color = 1; color < control.MAX_FIBERS; color++) {
            if (isReserved(color)) continue;
            if (mergeList[color] != 0 && color != currentId) {
                double avgDist = 0;
                double count = 0;
                for (int i = 0; i < depth; i++) {
                    if (skel[currentId][i] != null && skel[color][i] != null) {
                        double a = skel[currentId][i].x - skel[color][i].x;
                        double b = skel[currentId][i].y - skel[color][i].y;
                        double dist = Math.sqrt(a * a + b * b);
                        avgDist += dist;
                        count++;
                    }
                }
                if (count > 0) avgDist /= count;
                System.out.println("Overlaps " + count);
                System.out.println("Avgdist " + avgDist);
                if (avgDist < 50 || count == 0) {
                    System.out.println("Merging " + color + " with " + (currentId));
                    control.releaseFiberId(color);
                    for (int z = 0; z < depth; z++) {
                        short[] slice = control.getSlice(z);
                        for (int x = 1; x < width - 1; x++) {
                            for (int y = 1; y < height - 1; y++) {
                                int index = x + y * width;
                                if (slice[index] == color) {
                                    slice[index] = currentId;
                                }
                            }
                        }
                        skel[currentId][z] = ImageFunctions.getAveragePoint(slice, width, height, currentId);
                        skel[color][z] = null;
                    }
                } else {
                    System.out.println("Rejected merge!");
                }
            }
        }
    }

    @Override
    public void reset() {
    }

    @Override
    public void stop() {
    }

    private final class FindAllCandidatesAction implements Runnable {

        public void run() {
            int depth = control.getStack().getDepth();
            String s = JOptionPane.showInputDialog("How many slices to skip?", "10");
            try {
                final int num = Integer.parseInt(s);
                for (int z = 0; z < depth; z += num) {
                    control.setProgress(z);
                    findCandidates(z);
                }
            } catch (NumberFormatException ee) {
            }
        }
    }

    private final class FindCandidatesAction implements Runnable {

        public void run() {
            findCandidates(control.currentSlice());
        }
    }

    private final class AutosegmentAction implements Runnable {

        public void run() {
            automerge = (1 == control.getPreferences().getInt("autoMerge", 0));
            autoSegment();
        }
    }
}
