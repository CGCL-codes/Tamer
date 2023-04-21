package sketch.issta11.specifyvalues;

import sketch.ounit.Values;
import sketch.specs.annotation.TestSketch;

public class Z_ChooseSubsetValue_Example {

    @TestSketch
    public void testChooseSubsetValue_foo() {
        Integer[] a = Values.subset(1, 2);
        foo(a);
    }

    @TestSketch
    public void testChooseSubset_foo_bar() {
        Integer[] a = Values.subset(1, 2);
        foo(a);
        Integer[] b = Values.subset(3, 4);
        bar(b);
    }

    @TestSketch
    public void testChooseSubset_mix_one_() {
        Integer[] a = Values.subset(1, 2);
        foo(a);
        Integer i = Values.choose(3, 4);
        foo_1(i);
    }

    public void foo(Integer[] x) {
    }

    public void bar(Integer[] y) {
    }

    public void foo_1(int x) {
    }

    ;
}
