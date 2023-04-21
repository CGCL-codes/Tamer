package org.ucdetector.example.fields;

/**
 * [ 2290711 ] suggestion: read-only fields
 * <p>
 * I've been using your tool on some of my older projects - what a pleasure!
 * It's dug out all sorts of cruft that I'm very glad to get rid off,
 * including lots of stuff that I'd completely forgotten about :-)
 * <p>
 * 
 * One thing that would be useful would be to enhance the detector to spot
 * fields that are never assigned to, but only read from.
 */
public class FieldNeverRead {

    @SuppressWarnings("unused")
    private int i = 1;

    public static String NEVER_READ_FIELD = "NEVER_READ_FIELD";

    public String neverReadField = "neverReadField";

    public FieldNeverRead() {
        i = 2;
    }
}
