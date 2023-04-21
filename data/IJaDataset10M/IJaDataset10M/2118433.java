package corrective.FloatEqualComparision.CorrectFloatEqualComparision.in;

/**
 * @violations 8
 */
public class TestCorrectComparision {

    float var_float1 = 1;

    float var_float2 = 2;

    double var_double1 = 1;

    double var_double2 = 2;

    int var_int1 = 1;

    int var_int2 = 2;

    public void test1() {
        if (Math.abs(var_float2 - var_float2) < 0.0001) {
            var_float1 = 1;
        }
    }

    public void test2() {
        if (Math.abs(var_double1 - var_float2) < 0.0001) {
            var_float1 = 1;
        }
    }

    public void test3() {
        if (Math.abs(var_float1 - var_double2) < 0.0001) {
            var_float1 = 1;
        }
    }

    public void test4() {
        if (Math.abs(var_float1 - var_int2) < 0.0001) {
            var_float1 = 1;
        }
    }

    public void test5() {
        if (var_int1 == var_int2) {
            var_float1 = 1;
        }
    }

    public void test6() {
        float a = 5;
        float b = 3;
        int g = 2;
        if (Math.abs((g * (a - b)) - g) < 0.0001) {
            g = 3;
        }
    }

    public void test7() {
        float a = 5;
        float b = 3;
        int g = 2;
        if (Math.abs(returnsFloat() - (a + b)) < 0.0001) {
            g = 3;
        }
    }

    public void test8() {
        float a = 5;
        TestCorrectComparision first = new TestCorrectComparision();
        if (Math.abs((TestCorrectComparision.returnsFloat() + a) - (a + returnsFloat())) < 0.0001) {
            a = 3;
        }
    }

    public void test9() {
        float a = 5.5;
        float b = 6.6;
        if (Math.abs((a = 4.4) - b) < 0.0001) {
            a = 3;
        }
    }

    public float returnsFloat() {
        float a = 7.5;
        return a;
    }
}
