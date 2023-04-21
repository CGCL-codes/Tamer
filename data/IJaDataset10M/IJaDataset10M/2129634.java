package net.sf.bt747.j2se.app.filefilters;

/**
 * @author Mario
 * 
 */
public final class NMEAFileFilter extends ListFileFilter {

    /**
     * Lower case list of accepted extensions.
     */
    private static final String[] extensions = { ".nmea", ".txt", ".log", ".nme", ".nma" };

    private static final String description = "NMEA_FilterDescription";

    /**
     * 
     */
    public NMEAFileFilter() {
        super(extensions, description);
    }
}
