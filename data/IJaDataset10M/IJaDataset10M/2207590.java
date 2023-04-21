package org.jmol.shape;

import org.jmol.util.Escape;
import org.jmol.util.Logger;
import org.jmol.g3d.Graphics3D;
import org.jmol.geodesic.EnvelopeCalculation;
import org.jmol.modelset.Atom;
import java.util.BitSet;
import java.util.Hashtable;

public class Dots extends AtomShape {

    public EnvelopeCalculation ec;

    public boolean isSurface = false;

    static final float SURFACE_DISTANCE_FOR_CALCULATION = 10f;

    BitSet bsOn = new BitSet();

    private BitSet bsSelected, bsIgnore;

    static int MAX_LEVEL = EnvelopeCalculation.MAX_LEVEL;

    int thisAtom;

    float thisRadius;

    int thisArgb;

    short lastMad = 0;

    float lastSolventRadius = 0;

    long timeBeginExecution;

    long timeEndExecution;

    int getExecutionWalltime() {
        return (int) (timeEndExecution - timeBeginExecution);
    }

    public void initShape() {
        super.initShape();
        translucentAllowed = false;
        ec = new EnvelopeCalculation(viewer, atomCount, mads);
    }

    public void setProperty(String propertyName, Object value, BitSet bs) {
        if (Logger.isActiveLevel(Logger.LEVEL_DEBUG)) {
            Logger.debug("Dots.setProperty: " + propertyName + " " + value);
        }
        if ("init" == propertyName) {
            initialize();
            return;
        }
        if ("translucency" == propertyName) {
            if (!translucentAllowed) return;
        }
        if ("ignore" == propertyName) {
            bsIgnore = (BitSet) value;
            return;
        }
        if ("select" == propertyName) {
            bsSelected = (BitSet) value;
            return;
        }
        if ("radius" == propertyName) {
            thisRadius = ((Float) value).floatValue();
            return;
        }
        if ("colorRGB" == propertyName) {
            thisArgb = ((Integer) value).intValue();
            return;
        }
        if ("atom" == propertyName) {
            thisAtom = ((Integer) value).intValue();
            atoms[thisAtom].setShapeVisibility(myVisibilityFlag, true);
            ec.allocDotsConvexMaps(atomCount);
            return;
        }
        if ("dots" == propertyName) {
            isActive = true;
            ec.setFromBits(thisAtom, (BitSet) value);
            atoms[thisAtom].setShapeVisibility(myVisibilityFlag, true);
            if (mads == null) {
                ec.setMads(null);
                mads = new short[atomCount];
                for (int i = 0; i < atomCount; i++) if (atoms[i].isShapeVisible(myVisibilityFlag)) mads[i] = (short) (ec.getAppropriateRadius(i) * 1000);
                ec.setMads(mads);
            }
            mads[thisAtom] = (short) (thisRadius * 1000f);
            if (colixes == null) {
                colixes = new short[atomCount];
                paletteIDs = new byte[atomCount];
            }
            colixes[thisAtom] = Graphics3D.getColix(thisArgb);
            bsOn.set(thisAtom);
            return;
        }
        super.setProperty(propertyName, value, bs);
    }

    void initialize() {
        bsSelected = null;
        bsIgnore = null;
        isActive = false;
        if (ec == null) ec = new EnvelopeCalculation(viewer, atomCount, mads);
    }

    public void setSize(int size, BitSet bsSelected) {
        if (this.bsSelected != null) bsSelected = this.bsSelected;
        if (Logger.isActiveLevel(Logger.LEVEL_DEBUG)) {
            Logger.debug("Dots.setSize " + size);
        }
        boolean isVisible = true;
        float addRadius = Float.MAX_VALUE;
        float setRadius = Float.MAX_VALUE;
        boolean useVanderwaalsRadius = true;
        float scale = 1;
        isActive = true;
        short mad = (short) size;
        if (mad < 0) {
            useVanderwaalsRadius = false;
        } else if (mad == 0) {
            isVisible = false;
        } else if (mad == 1) {
        } else if (mad <= 1001) {
            scale = (mad - 1) / 100f;
        } else if (mad <= 11002) {
            useVanderwaalsRadius = false;
            setRadius = (mad - 1002) / 1000f;
        } else if (mad <= 13002) {
            addRadius = (mad - 11002) / 1000f;
            scale = 1;
        }
        float maxRadius = !useVanderwaalsRadius ? setRadius : modelSet.getMaxVanderwaalsRadius();
        float solventRadius = viewer.getCurrentSolventProbeRadius();
        if (addRadius == Float.MAX_VALUE) addRadius = (solventRadius != 0 ? solventRadius : 0);
        timeBeginExecution = System.currentTimeMillis();
        boolean newSet = (lastSolventRadius != addRadius || mad != 0 && mad != lastMad || ec.getDotsConvexMax() == 0);
        if (isVisible) {
            for (int i = atomCount; --i >= 0; ) if (bsSelected.get(i) && !bsOn.get(i)) {
                bsOn.set(i);
                newSet = true;
            }
        } else {
            for (int i = atomCount; --i >= 0; ) if (bsSelected.get(i)) bsOn.set(i, false);
        }
        for (int i = atomCount; --i >= 0; ) {
            atoms[i].setShapeVisibility(myVisibilityFlag, bsOn.get(i));
        }
        if (newSet) {
            mads = null;
            ec.newSet();
            lastSolventRadius = addRadius;
        }
        int[][] dotsConvexMaps = ec.getDotsConvexMaps();
        if (isVisible && dotsConvexMaps != null) {
            for (int i = atomCount; --i >= 0; ) if (bsOn.get(i)) {
                dotsConvexMaps[i] = null;
            }
        }
        if (isVisible) {
            lastMad = mad;
            if (dotsConvexMaps == null) {
                colixes = new short[atomCount];
                paletteIDs = new byte[atomCount];
            }
            boolean disregardNeighbors = (viewer.getDotSurfaceFlag() == false);
            boolean onlySelectedDots = (viewer.getDotsSelectedOnlyFlag() == true);
            ec.calculate(addRadius, setRadius, scale, maxRadius, bsOn, bsIgnore, useVanderwaalsRadius, disregardNeighbors, onlySelectedDots, isSurface, true);
        }
        timeEndExecution = System.currentTimeMillis();
        if (Logger.isActiveLevel(Logger.LEVEL_DEBUG)) {
            Logger.debug("dots generation time = " + getExecutionWalltime());
        }
    }

    public void setModelClickability() {
        for (int i = atomCount; --i >= 0; ) {
            Atom atom = atoms[i];
            if ((atom.getShapeVisibilityFlags() & myVisibilityFlag) == 0 || modelSet.isAtomHidden(i)) continue;
            atom.setClickable(myVisibilityFlag);
        }
    }

    public String getShapeState() {
        int[][] dotsConvexMaps = ec.getDotsConvexMaps();
        if (dotsConvexMaps == null || ec.getDotsConvexMax() == 0) return "";
        StringBuffer s = new StringBuffer();
        Hashtable temp = new Hashtable();
        int atomCount = viewer.getAtomCount();
        String type = (isSurface ? "geoSurface " : "dots ");
        for (int i = 0; i < atomCount; i++) {
            if (dotsConvexMaps[i] == null || !bsOn.get(i)) continue;
            if (bsColixSet != null && bsColixSet.get(i)) setStateInfo(temp, i, getColorCommand(type, paletteIDs[i], colixes[i]));
            BitSet bs = new BitSet();
            int[] map = dotsConvexMaps[i];
            int iDot = map.length << 5;
            int n = 0;
            while (--iDot >= 0) if (EnvelopeCalculation.getBit(map, iDot)) {
                n++;
                bs.set(iDot);
            }
            if (n > 0) {
                float r = ec.getAppropriateRadius(i);
                appendCmd(s, type + i + " radius " + r + " " + Escape.escape(bs));
            }
        }
        s.append(getShapeCommands(temp, null, atomCount));
        return s.toString();
    }
}
