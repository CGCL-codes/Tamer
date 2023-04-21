package de.shandschuh.jaolt.core.exception;

import de.shandschuh.jaolt.core.Directory;

public class NoAuctionPlatformFoundException extends Exception {

    /** Default serial version uid */
    private static final long serialVersionUID = 1l;

    public NoAuctionPlatformFoundException() {
        super("No auction platforms are found. If you try to run the program out of ecplise, please run the build.xml first for one time.\n\n" + Directory.AUCTIONPLATFORMS_DIR + ", " + System.getProperty("java.class.path"));
    }
}
