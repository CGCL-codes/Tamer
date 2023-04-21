package gov.ca.modeling.maps.elevation.client;

import com.google.gwt.core.client.JavaScriptObject;

public class ArrayUtils {

    public static JavaScriptObject toJsArray(int[] array) {
        JavaScriptObject result = createArray();
        for (int element : array) {
            pushArray(result, element);
        }
        return result;
    }

    ;

    public static JavaScriptObject toJsArray(double[] array) {
        JavaScriptObject result = createArray();
        for (double element : array) {
            pushArray(result, element);
        }
        return result;
    }

    ;

    public static JavaScriptObject toJsArray(Object[] array) {
        JavaScriptObject result = createArray();
        for (Object element : array) {
            pushArray(result, element);
        }
        return result;
    }

    ;

    public static native JavaScriptObject createArray();

    public static native void pushArray(JavaScriptObject array, int i);

    public static native void pushArray(JavaScriptObject array, double d);

    public static native void pushArray(JavaScriptObject array, Object o);

    public static native int lengthArray(JavaScriptObject array);

    public static native JavaScriptObject getElementAt(JavaScriptObject array, int i);
}
