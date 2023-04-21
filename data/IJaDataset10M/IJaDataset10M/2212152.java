package com.xenoage.zong.io.musicxml.in.util;

import static com.xenoage.pdlib.PMap.pmap;
import static com.xenoage.pdlib.PVector.pvec;
import com.xenoage.pdlib.PMap;
import com.xenoage.pdlib.PVector;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.direction.Wedge;

/**
 * Open wedges, beams, slurs and ties.
 * 
 * @author Andreas Wenger
 */
public final class OpenElements {

    private final PVector<Wedge> openWedges;

    private final PVector<PVector<Chord>> openBeams;

    private final PVector<OpenCurvedLine> openSlurs;

    private final PVector<OpenCurvedLine> openTies;

    private final PMap<Pitch, OpenCurvedLine> openUnnumberedTies;

    @SuppressWarnings("unchecked")
    public OpenElements() {
        this.openWedges = pvec(null, null, null, null, null, null);
        this.openBeams = pvec(null, null, null, null, null, null);
        this.openSlurs = pvec(null, null, null, null, null, null);
        this.openTies = pvec(null, null, null, null, null, null);
        this.openUnnumberedTies = pmap();
    }

    private OpenElements(PVector<Wedge> openWedges, PVector<PVector<Chord>> openBeams, PVector<OpenCurvedLine> openSlurs, PVector<OpenCurvedLine> openTies, PMap<Pitch, OpenCurvedLine> openUnnumberedTies) {
        this.openWedges = openWedges;
        this.openBeams = openBeams;
        this.openSlurs = openSlurs;
        this.openTies = openTies;
        this.openUnnumberedTies = openUnnumberedTies;
    }

    public PVector<Wedge> getOpenWedges() {
        return openWedges;
    }

    public OpenElements withOpenWedges(PVector<Wedge> openWedges) {
        return new OpenElements(openWedges, openBeams, openSlurs, openTies, openUnnumberedTies);
    }

    public PVector<PVector<Chord>> getOpenBeams() {
        return openBeams;
    }

    public OpenElements withOpenBeams(PVector<PVector<Chord>> openBeams) {
        return new OpenElements(openWedges, openBeams, openSlurs, openTies, openUnnumberedTies);
    }

    public PVector<OpenCurvedLine> getOpenSlurs() {
        return openSlurs;
    }

    public OpenElements withOpenSlurs(PVector<OpenCurvedLine> openSlurs) {
        return new OpenElements(openWedges, openBeams, openSlurs, openTies, openUnnumberedTies);
    }

    public PVector<OpenCurvedLine> getOpenTies() {
        return openTies;
    }

    public OpenElements withOpenTies(PVector<OpenCurvedLine> openTies) {
        return new OpenElements(openWedges, openBeams, openSlurs, openTies, openUnnumberedTies);
    }

    public PMap<Pitch, OpenCurvedLine> getOpenUnnumberedTies() {
        return openUnnumberedTies;
    }

    public OpenElements withOpenUnnumberedTies(PMap<Pitch, OpenCurvedLine> openUnnumberedTies) {
        return new OpenElements(openWedges, openBeams, openSlurs, openTies, openUnnumberedTies);
    }
}
