package jacky.lanlan.song.closure;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ClosureUtilsTest {

    @Test
    public void testReverse() throws Exception {
        Judgment<String> notEmptyStr = ClosureUtils.reverse(Judgment.EMPTY_STRING);
        assertTrue("非空字符串应该可以通过", notEmptyStr.canPass("sdfdsf"));
        assertFalse("空字符串应该不能通过", notEmptyStr.canPass(""));
        Judgment<Double> not0 = ClosureUtils.reverse(Judgment.ZERO);
        assertTrue("非0值应该可以通过", not0.canPass(12.2));
        assertFalse("0应该不能通过", not0.canPass(0d));
    }
}
