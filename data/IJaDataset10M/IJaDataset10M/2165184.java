package mini.java.fa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import mini.java.TestHelperV2;
import mini.java.fa.NFAClosure;
import mini.java.fa.NFAState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class NFAClosureGetSourceClosureTest {

    private TestHelperV2 _helper;

    private Set<NFAState> _expected;

    public NFAClosureGetSourceClosureTest(String states_, String transitions_, String closure_) {
        _helper = new TestHelperV2();
        _helper.addNFAStates(states_);
        _helper.addTransitions(transitions_);
        _expected = new HashSet<NFAState>();
        for (Character c : closure_.toCharArray()) {
            _expected.add(_helper.getNFAState(c));
        }
    }

    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] { { "A", "", "A" }, { "A", "AA", "A" }, { "AB", "AB", "AB" }, { "ABC", "AB,AC", "ABC" }, { "ABC", "AB,BC", "ABC" }, { "AB", "AB,BA", "AB" }, { "AB", "AA,AB", "AB" }, { "AB", "AB,BB", "AB" }, { "ABCD", "AB,AC,BD", "ABCD" }, { "ABCD", "AB,BC,BD", "ABCD" }, { "ABCD", "AB,BC,CD", "ABCD" } });
    }

    @Test
    public void testFindClosure() {
        NFAState initialState = _helper.getNFAState(TestHelperV2.INITIAL_STATE);
        NFAClosure closure = new NFAClosure(initialState);
        assertNotNull(closure);
        assertEquals(_expected, closure.getStates());
    }
}
