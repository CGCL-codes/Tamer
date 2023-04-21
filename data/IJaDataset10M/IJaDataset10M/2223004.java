package mecca.lcms;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class ScoDataFactory {

    public static ScoData get() throws Exception {
        return new ScoDb();
    }
}
