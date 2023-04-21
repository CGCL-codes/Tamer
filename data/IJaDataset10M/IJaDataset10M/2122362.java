package gp.utils.arrays;

/**
 *
 * @author Gwenhael Pasquiers
 */
public class BitwiseOrArray extends OperationArray {

    public BitwiseOrArray(Array array1, Array array2) {
        super(array1, array2);
    }

    @Override
    public byte operation(byte byte1, byte byte2) {
        return (byte) ((byte1 | byte2) & 0xFF);
    }
}
