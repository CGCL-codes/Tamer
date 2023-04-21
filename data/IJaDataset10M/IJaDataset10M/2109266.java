package org.tranche.hash;

import org.tranche.util.TrancheTestCase;

/**
 * Some simple tests to show that the Base64 cleanup code works for the ways that Tranche hashes are commonly munged.
 * @author Jayson Falkner - jfalkner@umich.edu
 */
public class Base64UtilTest extends TrancheTestCase {

    /**
     *Test if the clean up code is working properly.
     */
    public void testCleanUpCode() throws Exception {
        String expected = "siQG5oyk956YTBLsV2RhNKPy6/kQOeHZot3V6qRN4/fp3AdMnZXP2PThHuudUtBcTjA9VIeent0gT3mBv1l751pIai0AAAAAAABKZw==";
        String whitespace = "siQG5oyk956 YTBLsV2RhNKPy6/kQOeHZ  ot3V6qRN4/fp3AdMnZXP2PThHuudUtBcTjA9VIeent0gT3mBv1l751pIai0AAAAAAABKZw==";
        String email = "> siQG5oyk956YTBLsV2RhNKPy6/kQOeHZot3V6qRN4/fp3AdMnZXP2PThHuudUtBcTjA9VIeent0g\n> T3mBv1l751pIai0AAAAAAABKZw==";
        String hyphen = "siQG5oyk956YTBLsV2RhNKPy6/kQOeHZot3V6qRN-4/fp3AdMnZXP2PThHuudUtBcTjA9VIeent0g--T3mBv1l751pIai0AAAAAAABKZw==";
        assertEquals("Expected normal hashes to stay the same.", expected, Base64Util.cleanUpBase64(expected));
        assertEquals("Expected to clean up whitespace.", expected, Base64Util.cleanUpBase64(whitespace));
        assertEquals("Expected to clean up e-mail characters.", expected, Base64Util.cleanUpBase64(email));
        assertEquals("Expected clean up hyphens.", expected, Base64Util.cleanUpBase64(hyphen));
    }
}
