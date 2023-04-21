package org.xith3d;

import org.jagatoo.util.versioning.Version;

/**
 * This class offers static methods for API information about the Xith3D-Core.<br>
 * <br>
 * Please note, that the version information is not guaranteed to be updated
 * with each SVN commit. So the information might be outdated for
 * development versions. Especially the revision-number will be outdated most
 * of the time. Only releases are guaranteed to carry valid and correct
 * version information.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public final class Xith3D {

    private static Version version = null;

    /**
     * @return a version information object about xith3d core.
     */
    public static Version getVersion() {
        if (version == null) version = new Version(0, 9, 7, "dev", 1831);
        return (version);
    }

    private Xith3D() {
    }
}
