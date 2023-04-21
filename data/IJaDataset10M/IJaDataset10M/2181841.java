package it.tukano.jps.math;

/**
 * Functions on segment objects
 */
public class Segments {

    /**
     * Factory method
     * @return a new Segments instance.
     */
    public static Segments newInstance() {
        return new Segments();
    }

    /**
     * Default no arg constructor
     */
    protected Segments() {
    }

    /**
     * Translates a segment
     * @param source the segment to translate
     * @param translation the amount of translation to apply
     * @param JPSDefaults.vectors3d() the vector functions to use
     * @return a translated version of the given segment
     */
    public NSegment translate(NSegment source, NTuple3 translation) {
        final NTuple3 v0 = JPSDefaults.vectors3d().add(source.getV0(), translation);
        final NTuple3 v1 = JPSDefaults.vectors3d().add(source.getV1(), translation);
        return new NSegment(v0, v1);
    }
}
