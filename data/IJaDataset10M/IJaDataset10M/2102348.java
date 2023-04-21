package net.sf.openforge.util;

public interface ForgeDebug {

    /** Used for older debug statements you wish to keep, but not display.
     * If no level is specified, these will not display.
     */
    public static final long OLD = GlobalDebug.OLD;

    public static final long LEGACY = OLD;

    /** Default set of levels enabled by default ;-)
     */
    public static final long DEFAULT_LEVELS = GlobalDebug.DEFAULT_LEVELS;

    public static final boolean _cmodel = false;

    public static final GlobalDebug cmodel = new GlobalDebug("CModel", _cmodel, DEFAULT_LEVELS);

    public static final boolean _clinker = false;

    public static final GlobalDebug clinker = new GlobalDebug("CLinker", _clinker, DEFAULT_LEVELS);

    public static final boolean _gapp = false;

    public static final GlobalDebug gapp = new GlobalDebug("Global App", _gapp, DEFAULT_LEVELS);
}
