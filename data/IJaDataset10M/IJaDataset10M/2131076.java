package org.crap4j;

import org.crap4j.MethodComplexity;
import org.crap4j.MethodCrap;
import junit.framework.TestCase;

public class MethodCrapTest extends TestCase {

    public void testNumbers1() throws Exception {
        int[] complexity = { 1, 4, 5, 8, 9, 16, 17 };
        float[] coverage = { 0.0f, 0.01f, 0.2f, 0.21f, 0.4f, 0.41f, 0.6f, 0.61f, 0.8f, 0.81f, 0.99f, 1.0f };
        float[][] crapResults = { { 2.0f, 20.0f, 30.0f, 72.0f, 90.0f, 272.0f, 306.0f }, { 1.970299f, 19.524784f, 29.257475f, 70.099136f, 87.594219f, 264.396544f, 297.416411f }, { 1.512f, 12.192f, 17.8f, 40.768f, 50.472f, 147.072f, 164.968f }, { 1.493039f, 11.888624f, 17.325975f, 39.554496f, 48.936159f, 142.217984f, 159.488271f }, { 1.216f, 7.456f, 10.4f, 21.824f, 26.496f, 71.296f, 79.424f }, { 1.205379f, 7.286064f, 10.134475f, 21.144256f, 25.635699f, 68.577024f, 76.354531f }, { 1.064f, 5.024f, 6.6f, 12.096f, 14.184f, 32.384f, 35.496f }, { 1.059319f, 4.949104f, 6.482975f, 11.796416f, 13.804839f, 31.185664f, 34.143191f }, { 1.008f, 4.128f, 5.2f, 8.512f, 9.648f, 18.048f, 19.312f }, { 1.006859f, 4.109744f, 5.171475f, 8.438976f, 9.555579f, 17.755904f, 18.982251f }, { 1.000001f, 4.000016f, 5.000025f, 8.000064f, 9.000081f, 16.000256f, 17.000289f }, { 1.0f, 4.0f, 5.0f, 8.0f, 9.0f, 16.0f, 17.0f } };
        System.out.println("Coverage   Complexity Crap");
        for (int i = 0; i < coverage.length; i++) {
            for (int j = 0; j < complexity.length; j++) {
                MethodCrap crapActual = new MethodCrap(null, coverage[i], new MethodComplexity("foo", "bar", "baz", "biz", "buz", complexity[j], "foo()"));
                assertEquals(i + " " + j, crapResults[i][j], crapActual.getCrap(), 0.01);
            }
        }
    }
}
