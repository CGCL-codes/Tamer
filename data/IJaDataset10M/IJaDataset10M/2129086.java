package com.frinika.versionmanagement;

/**
 * Trying to follow this version scheme:
 * major.minor[.build[.revision]]
 * 
 * A version number returned from this class is numeric, and will 
 * reserve 100 values (0-99) for every field other than the major (which is not limited)
 * e.g.:
 * 
 * version 0.5.1 transfers to: 050100 (not that revision is omitted and then = 00)
 * 
 * let's say we created a version 0.5.1.1 - that would be: 050101
 * Or version 1.1.3.1 -> 1010301
 * 
 * For more info see here:
 * http://en.wikipedia.org/wiki/Software_versioning
 */
public class FrinikaVersionManagerBean implements FrinikaVersionManager {

    int latestFrinikaVersion_major = 0;

    int latestFrinikaVersion_minor = 7;

    int latestFrinikaVersion_build = 0;

    int latestFrinikaVersion_revision = 0;

    /**
	 * Get latest version of Frinika
	 * @return
	 */
    public int getLatestFrinikaVersion() {
        return latestFrinikaVersion_major * 1000000 + latestFrinikaVersion_minor * 10000 + latestFrinikaVersion_build * 100 + latestFrinikaVersion_revision * 1;
    }
}
