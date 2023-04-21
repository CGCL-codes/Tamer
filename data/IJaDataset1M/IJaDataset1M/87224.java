package org.bff.slimserver.test;

import org.bff.slimserver.SlimServer;
import org.bff.slimserver.exception.SlimConnectionException;

/**
 *
 * @author Music
 */
class TestController {

    private static final String SLIM_IP = "localhost";

    private SlimServer slimServer;

    private static TestController instance;

    private String testArtist1 = "Guns n' Roses";

    private String testAlbum1 = "Chinese Democracy";

    private String testAlbum2Disc1 = "Merry Axemas - A Guitar Christmas (Disc 1)";

    private String testAlbum2Disc2 = "Merry Axemas - A Guitar Christmas (Disc 2)";

    private String testAlbum3 = "We Wish You A Metal X-Mas And A Headbanging New Year!";

    private String testGenre1 = "Heavy Metal";

    private String testComment1 = "Test Comment";

    private String testYear1 = "2008";

    private String[] testTracks1 = new String[] { "Chinese Democracy", "Shackler's Revenge", "Better", "Street Of Dreams", "If The World", "There Was A Time", "Catcher In The Rye", "Scraped", "Riad N' The Bedouins", "Sorry", "I.R.S", "Madagascar", "This I Love", "Prostitute" };

    /**
     * @return the testArtist1
     */
    public String getTestArtist1() {
        return testArtist1;
    }

    /**
     * @param aTestArtist1 the testArtist1 to set
     */
    public void setTestArtist1(String aTestArtist1) {
        testArtist1 = aTestArtist1;
    }

    /**
     * @return the testAlbum1
     */
    public String getTestAlbum1() {
        return testAlbum1;
    }

    /**
     * @param aTestAlbum1 the testAlbum1 to set
     */
    public void setTestAlbum1(String aTestAlbum1) {
        testAlbum1 = aTestAlbum1;
    }

    private TestController() throws SlimConnectionException {
        setSlimServer(new SlimServer(SLIM_IP));
    }

    public static TestController getInstance() throws SlimConnectionException {
        if (instance == null) {
            instance = new TestController();
        }
        return instance;
    }

    /**
     * @return the slimServer
     */
    public SlimServer getSlimServer() {
        return slimServer;
    }

    /**
     * @param slimServer the slimServer to set
     */
    public void setSlimServer(SlimServer slimServer) {
        this.slimServer = slimServer;
    }

    /**
     * @return the testGenre1
     */
    public String getTestGenre1() {
        return testGenre1;
    }

    /**
     * @param testGenre1 the testGenre1 to set
     */
    public void setTestGenre1(String testGenre1) {
        this.testGenre1 = testGenre1;
    }

    /**
     * @return the testComment1
     */
    public String getTestComment1() {
        return testComment1;
    }

    /**
     * @param testComment1 the testComment1 to set
     */
    public void setTestComment1(String testComment1) {
        this.testComment1 = testComment1;
    }

    /**
     * @return the testYear1
     */
    public String getTestYear1() {
        return testYear1;
    }

    /**
     * @param testYear1 the testYear1 to set
     */
    public void setTestYear1(String testYear1) {
        this.testYear1 = testYear1;
    }

    /**
     * @return the testTracks1
     */
    public String[] getTestTracks1() {
        return testTracks1;
    }

    /**
     * @param testTracks1 the testTracks1 to set
     */
    public void setTestTracks1(String[] testTracks1) {
        this.testTracks1 = testTracks1;
    }
}
