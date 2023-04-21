package sketch.issta11.specifystatements;

import sketch.ounit.Values;
import sketch.specs.annotation.TestSketch;
import junit.framework.TestCase;

public class Z_RepeatStatement_Example extends TestCase {

    @TestSketch
    public void testSimpleRepeat() {
        int i = Values.choose(1, 2, 3, 4);
        {
            foo(i);
        }
    }

    @TestSketch
    public void testMultiRepeatValues() {
        int v1 = Values.choose(1, 2);
        int v2 = Values.choose(3, 4, 5);
        {
            foo_2(v1, v2);
        }
    }

    @TestSketch
    public void testMultiStatements() {
        int v1 = Values.choose(1, 2);
        int v2 = Values.choose(3, 4, 5);
        {
            foo(v1);
            foo_2(v1, v2);
        }
    }

    @TestSketch
    public void testMutliStatements_Array() {
        Integer[] array = Values.exhaust(1, 2);
        int v = Values.choose(2, 3, 4);
        {
            foo_array(array);
            foo(v);
        }
    }

    public void foo(int i) {
    }

    public void foo_array(Integer[] is) {
    }

    public void foo_2(int i, int j) {
    }
}
