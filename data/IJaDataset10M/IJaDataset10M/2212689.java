package net.sf.jaer.eventprocessing.filter;

import net.sf.jaer.chip.AEChip;
import net.sf.jaer.event.*;
import net.sf.jaer.eventprocessing.EventFilter2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import net.sf.jaer.Description;

/**
 * Does an event-based spatial highpass filter, so that only small objects pass through.
 
 * @author tobi
 */
@Description("Does an event-based spatial highpass filter, so that only small objects pass through.")
public class SpatialBandpassFilter extends EventFilter2D implements Observer {

    private int centerRadius = getPrefs().getInt("SpatialBandpassFilter.centerRadius", 0);

    private int surroundRadius = getPrefs().getInt("SpatialBandpassFilter.surroundRadius", 1);

    int sizex, sizey;

    /** the time in timestamp ticks (1us at present) that a spike in surround
     will inhibit a spike from center passing through.
     */
    private int dtSurround = getPrefs().getInt("SpatialBandpassFilter.dtSurround", 8000);

    int[][] surroundTimestamps, centerTimestamps;

    /**
     * Creates a new instance of SpatialBandpassFilter
     */
    public SpatialBandpassFilter(AEChip c) {
        super(c);
        chip.addObserver(this);
    }

    public synchronized void setFilterEnabled(boolean yes) {
        super.setFilterEnabled(yes);
        if (!yes) {
            surroundTimestamps = null;
            centerTimestamps = null;
        } else {
            initFilter();
        }
    }

    public Object getFilterState() {
        return null;
    }

    public void resetFilter() {
        initFilter();
    }

    public void update(Observable o, Object arg) {
        initFilter();
    }

    public synchronized void initFilter() {
        computeOffsets();
    }

    void checkMaps() {
        if (surroundTimestamps == null || surroundTimestamps.length != chip.getSizeX() || surroundTimestamps[0].length != chip.getSizeY()) {
            allocateMaps();
        }
    }

    void allocateMaps() {
        sizex = chip.getSizeX() - 1;
        sizey = chip.getSizeY() - 1;
        surroundTimestamps = new int[sizex + 1][sizey + 1];
        centerTimestamps = new int[sizex + 1][sizey + 1];
    }

    final class Offset {

        int x, y;

        Offset(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    Offset[] centerOffsets, surroundOffsets;

    synchronized void computeOffsets() {
        ArrayList<Offset> surList = new ArrayList<Offset>();
        ArrayList<Offset> cenList = new ArrayList<Offset>();
        for (int x = -surroundRadius; x <= surroundRadius; x++) {
            for (int y = -surroundRadius; y <= surroundRadius; y++) {
                if ((x <= centerRadius && x >= -centerRadius && y <= centerRadius && y >= -centerRadius)) {
                    cenList.add(new Offset(x, y));
                } else {
                    surList.add(new Offset(x, y));
                }
            }
        }
        centerOffsets = new Offset[1];
        centerOffsets = (Offset[]) cenList.toArray(centerOffsets);
        surroundOffsets = new Offset[1];
        surroundOffsets = (Offset[]) surList.toArray(surroundOffsets);
    }

    public int getDtSurround() {
        return dtSurround;
    }

    /** sets the time in timestamp ticks (1us at present) that a spike in surround
     will inhibit a spike from center passing through.
     @param dtSurround the time in us
     */
    public void setDtSurround(int dtSurround) {
        this.dtSurround = dtSurround;
        getPrefs().putInt("SpatialBandpassFilter.dtSurround", dtSurround);
    }

    public int getCenterRadius() {
        return centerRadius;
    }

    /** sets the center radius, 0 meaning a single pixel. This value is clipped to min 0.
     @param centerRadius the radius in pixels for a square area. 0 is 1 pixel, 1 is 9 pixels (3x3), etc.
     */
    public synchronized void setCenterRadius(int centerRadius) {
        if (centerRadius < 0) centerRadius = 0; else if (centerRadius >= surroundRadius) centerRadius = surroundRadius - 1;
        this.centerRadius = centerRadius;
        getPrefs().putInt("SpatialBandpassFilter.centerRadius", centerRadius);
        computeOffsets();
    }

    public int getSurroundRadius() {
        return surroundRadius;
    }

    /** sets the surround radius. This value is clipped to be at least the center radius plus 1.
     @param surroundRadius the radius in pixels for a square area. 1 is 9 pixels (3x3), etc.
     */
    public synchronized void setSurroundRadius(int surroundRadius) {
        if (surroundRadius < centerRadius + 1) surroundRadius = centerRadius + 1;
        this.surroundRadius = surroundRadius;
        getPrefs().putInt("SpatialBandpassFilter.surroundRadius", surroundRadius);
        computeOffsets();
    }

    public synchronized EventPacket filterPacket(EventPacket in) {
        if (in == null) return null;
        if (!filterEnabled) return in;
        if (enclosedFilter != null) in = enclosedFilter.filterPacket(in);
        checkOutputPacketEventType(in);
        checkMaps();
        int n = in.getSize();
        if (n == 0) return in;
        OutputEventIterator o = out.outputIterator();
        for (Object obj : in) {
            PolarityEvent i = (PolarityEvent) obj;
            if ((i.timestamp - surroundTimestamps[i.x][i.y]) > dtSurround) {
                o.nextOutput().copyFrom(i);
            }
            writeSurround(i);
        }
        return out;
    }

    final void writeSurround(PolarityEvent i) {
        for (int k = 0; k < surroundOffsets.length; k++) {
            Offset d = surroundOffsets[k];
            int kx = i.x + d.x;
            if (kx < 0 || kx > sizex) continue;
            int ky = i.y + d.y;
            if (ky < 0 || ky > sizey) continue;
            surroundTimestamps[kx][ky] = i.timestamp;
        }
    }
}
