package ttpasi.jgenere.chaocipher;

import static ttpasi.jgenere.chaocipher.testutils.AssertUtils.*;
import java.util.Arrays;
import java.util.List;
import junitx.runners.ParameterizedWithDescription;
import junitx.runners.ParameterizedWithDescription.ParametersWithDescriptions;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Bruce [com.gmail @ ProgrammerBruce]
 */
@RunWith(ParameterizedWithDescription.class)
public class ChaocipherTranslationsWithVariedNadirTest {

    private final String _left;

    private final String _right;

    private final int _nadir;

    private final String _plaintextMsg;

    private final String _ciphertextMsg;

    public ChaocipherTranslationsWithVariedNadirTest(String left, String right, int nadir, String plaintextMsg, String ciphertextMsg) {
        this._left = left;
        this._right = right;
        this._nadir = nadir;
        this._plaintextMsg = plaintextMsg;
        this._ciphertextMsg = ciphertextMsg;
    }

    @ParametersWithDescriptions(fixtureDescription = "{2}")
    public static List<Object[]> chaocipherCreationData() {
        return Arrays.asList(new Object[][] { { "ALLGOODDOGS:12 -> PXHPZCKLEAE", "XUCZVAMDSLKPEFJRIGTWOBNYQH", "TLNBQDEOYSFAVZKGJRIHWXUMCP", 12, "ALLGOODDOGS", "PXHPZCKLEAE" }, { "GED:3 -> BDC", "BCDEFGHA", "GFEDCBAH", 3, "GED", "BDC" }, { "DAAGE:3 -> EFHHC", "BCDEFGHA", "GFEDCBAH", 3, "DAAGE", "EFHHC" }, { "FEAD:5 -> CCEA", "BCDEFGHA", "GFEDCBAH", 5, "FEAD", "CCEA" }, { "FEAD:4 -> CCEB", "BCDEFGHA", "GFEDCBAH", 4, "FEAD", "CCEB" } });
    }

    @Test
    public void shouldEncipher() {
        assertEncipher(_left, _right, _nadir, _plaintextMsg, _ciphertextMsg);
    }

    @Test
    public void shouldDecipher() {
        assertDecipher(_left, _right, _nadir, _ciphertextMsg, _plaintextMsg);
    }
}
