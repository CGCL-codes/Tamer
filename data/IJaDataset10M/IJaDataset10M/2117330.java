package org.eiichiro.jazzmaster;

/**
 * {@code Version} represents the serial version for current Jazzmaster build.
 *
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class Version {

    private Version() {
    }

    /** Serial major version for this build. */
    public static final int MAJOR = 1;

    /** Serial miner version for this build. */
    public static final int MINER = 5;

    /** Serial build version for this build. */
    public static final int BUILD = 1;
}
