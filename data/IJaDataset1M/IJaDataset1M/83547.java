package jvmTestCases;

/**
 * <p>Title: </p>
 * <p>Description:
 * To test variable slot sizes.
 * In takatuka we can have smaller slots sizes. The aim of this class it to test it,
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 */
public class VariableSlotSizes {

    private static final VariableSlotSizes myObj = new VariableSlotSizes();

    private static final float SUCCESS = 1025;

    private VariableSlotSizes() {
    }

    public static VariableSlotSizes getInstanceOf() {
        return myObj;
    }

    public float checkSlotSizes() {
        long array[] = { 423 };
        byte byteInput = 1;
        long longInput = byteInput;
        short shortInput = byteInput;
        float result = foo(false, longInput, shortInput, byteInput, 0f, longInput, array, 1, 1, byteInput);
        result += foo(true, 1, shortInput, byteInput, 0, 1, array, 1, 55, byteInput);
        result += foo(true, 1, shortInput, byteInput, shortInput, byteInput, array, byteInput, shortInput, byteInput);
        if (result == SUCCESS) {
        } else {
            System.out.println("******* Error in slot sizes");
            System.out.println(result);
        }
        return result;
    }

    private float foo(boolean boolInput, long longInput, short shortInput, byte byteInput, float flotInput, long longInput2, long array[], int intInput, long longInput3, byte byteInput2) {
        short localShort = 55;
        long localLong = localShort;
        if (boolInput) {
            return longInput + shortInput + byteInput + flotInput + longInput2 + array[0] + intInput + byteInput2 + localShort + longInput3;
        } else {
            return longInput * shortInput * byteInput * flotInput * longInput2 * array[0] * intInput * byteInput2 * localLong * longInput3;
        }
    }

    public static void main(String args[]) {
        System.out.println(getInstanceOf().checkSlotSizes());
    }
}
