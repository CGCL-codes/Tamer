package mini.java.fa.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import mini.java.TestHelperV2;
import mini.java.fa.NFAState;
import mini.java.fa.helper.Helper;
import org.junit.Test;

public final class HelperDumpTest extends DFAComparisonData {

    private static final String STATES = "ABCDEFabc";

    private NFAState _A, _B;

    private boolean _result;

    private static NFAState buildNFAState(String rep_) {
        TestHelperV2 helper = new TestHelperV2();
        helper.addNFAStates(STATES);
        helper.addTransitions(rep_);
        return helper.getNFAState(rep_.charAt(0));
    }

    public HelperDumpTest(String A_, String B_, boolean result_) {
        _A = buildNFAState(A_);
        _B = buildNFAState(B_);
        _result = result_;
    }

    @Test
    public final void testDumpString() {
        String A = Helper.dump(_A);
        String B = Helper.dump(_B);
        if (_result) {
            assertEquals(A, B);
        } else {
            assertFalse(("" + A).equals("" + B));
        }
    }
}
