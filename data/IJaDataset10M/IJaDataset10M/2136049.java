package jacky.lanlan.song.util.sp;

import static org.junit.Assert.*;
import jacky.lanlan.song.closure.Judgment;
import jacky.lanlan.song.closure.ClosureUtils;
import jacky.lanlan.song.util.sp.Selector;
import org.junit.*;

public class SelectorTest {

    @Before
    public void inti() {
    }

    @Test
    public void testEncodeSelector() throws Exception {
        Judgment<String> notEmptyString = ClosureUtils.reverse(Judgment.EMPTY_STRING);
        assertEquals(0, Selector.encodeSelector(notEmptyString, "", "", ""));
        assertEquals(1, Selector.encodeSelector(notEmptyString, "a", "", ""));
        assertEquals(2, Selector.encodeSelector(notEmptyString, "", "a", null));
        assertEquals(4, Selector.encodeSelector(notEmptyString, "", "", "a"));
        assertEquals(3, Selector.encodeSelector(notEmptyString, "a", "a", null));
        assertEquals(5, Selector.encodeSelector(notEmptyString, "a", "", "a"));
        assertEquals(6, Selector.encodeSelector(notEmptyString, "", "a", "a"));
        assertEquals(7, Selector.encodeSelector(notEmptyString, "a", "a", "a"));
        assertEquals(9, Selector.encodeSelector(notEmptyString, "a", "", null, "a"));
    }

    @Test
    public void testDecodeSelector() throws Exception {
        boolean[] reValue = this.prepareArray(4, 0);
        this.compareBooleanArray(reValue, Selector.decodeSelector(4, 1 + 0 + 0 + 0));
        reValue = this.prepareArray(4, 1);
        this.compareBooleanArray(reValue, Selector.decodeSelector(4, 0 + 2 + 0 + 0));
        reValue = this.prepareArray(8, 2, 6);
        this.compareBooleanArray(reValue, Selector.decodeSelector(8, (1 << 2) + (1 << 6)));
        reValue = this.prepareArray(6);
        this.compareBooleanArray(reValue, Selector.decodeSelector(6, 0 + 0 + 0 + 0 + 0 + 0));
        reValue = this.prepareArray(6, 3, 5);
        this.compareBooleanArray(reValue, Selector.decodeSelector(6, (1 << 3) + (1 << 5)));
    }

    private boolean[] prepareArray(int arraySize, int... truePos) {
        boolean[] array = new boolean[arraySize];
        for (int i = 0; i < truePos.length; i++) {
            array[truePos[i]] = true;
        }
        return array;
    }

    private void compareBooleanArray(boolean[] expected, boolean[] actual) {
        for (int i = 0; i < actual.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @After
    public void destory() {
    }
}
