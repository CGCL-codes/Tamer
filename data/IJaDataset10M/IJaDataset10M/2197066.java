package de.grogra.blocks;

import javax.vecmath.Tuple2f;
import javax.vecmath.Tuple3f;
import de.grogra.xl.lang.FloatToFloat;

public class ModeList {

    public static float function(int i, int n, float value1, float value2, FloatToFloat func) {
        float ii = i * value1 + (n - i) * value2;
        return func.evaluateFloat(ii);
    }

    public static float function(int i, int n, float value1, float value2, FloatToFloat func, Tuple2f ids, Tuple3f nutrientsValues, Tuple2f h, float d) {
        float ii = i * value1 + (n - i) * value2;
        if (func instanceof CustomFunction) {
            ((CustomFunction) func).setValues(ids, nutrientsValues, h, d);
        }
        return func.evaluateFloat(ii);
    }

    public static float function(int i, float delta, FloatToFloat func, Tuple2f ids, Tuple3f nutrientsValues, Tuple2f h, float d) {
        float ii = i * delta;
        if (func instanceof CustomFunction) {
            ((CustomFunction) func).setValues(ids, nutrientsValues, h, d);
        }
        return func.evaluateFloat(ii);
    }
}
