package com.xenoage.zong.core.music.beam;

import com.xenoage.pdlib.PMap;
import com.xenoage.pdlib.PSet;
import com.xenoage.util.annotations.Untested;
import com.xenoage.zong.core.music.chord.Chord;

/**
 * Set of beams belonging to chords. A single instance of this
 * class should be used for the whole score.
 * 
 * Given a chord, its beam can be queried in hash lookup time.
 * Adding or removing a beam is done in near hash lookup time
 * (actually in linear time regarding the number of chords
 * within the beam).
 * 
 * @author Andreas Wenger
 */
public final class Beams {

    public final PSet<Beam> beams;

    public final PMap<Chord, Beam> chordsMap;

    /** An empty set of beams */
    public static final Beams empty = new Beams(new PSet<Beam>(), new PMap<Chord, Beam>());

    /**
	 * Creates a set of beams with the given chord-to-beam mapping.
	 */
    private Beams(PSet<Beam> beams, PMap<Chord, Beam> chordsMap) {
        this.beams = beams;
        this.chordsMap = chordsMap;
    }

    /**
	 * Adds a beam. No chord of the beam may already belong to another beam,
	 * otherwise an {@link IllegalStateException} is thrown.
	 */
    @Untested
    public Beams plus(Beam beam) {
        for (BeamWaypoint wp : beam.getWaypoints()) {
            if (chordsMap.containsKey(wp.getChord())) {
                throw new IllegalStateException("Chord is already beamed");
            }
        }
        PSet<Beam> beams = this.beams.plus(beam);
        PMap<Chord, Beam> chordsMap = this.chordsMap;
        for (BeamWaypoint wp : beam.getWaypoints()) {
            chordsMap = chordsMap.plus(wp.getChord(), beam);
        }
        return new Beams(beams, chordsMap);
    }

    /**
	 * Gets the beam belonging to the given chord, or null if there is none.
	 */
    public Beam get(Chord chord) {
        return chordsMap.get(chord);
    }

    /**
	 * Removes a beam.
	 */
    @Untested
    public Beams minus(Beam beam) {
        PSet<Beam> beams = this.beams.minus(beam);
        PMap<Chord, Beam> chordsMap = this.chordsMap;
        for (BeamWaypoint wp : beam.getWaypoints()) {
            chordsMap = chordsMap.minus(wp.getChord());
        }
        return new Beams(beams, chordsMap);
    }

    /**
	 * Replaces the beam (if any) at the given old chord by an equal new beam
	 * at the given new chord.
	 */
    @Untested
    public Beams replaceChord(Chord oldChord, Chord newChord) {
        Beam beam = get(oldChord);
        if (beam != null) {
            Beams beams = this;
            beams = beams.minus(beam);
            beam = beam.replaceChord(oldChord, newChord);
            beams = beams.plus(beam);
            return beams;
        } else {
            return this;
        }
    }
}
