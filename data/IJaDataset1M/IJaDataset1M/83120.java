package luz.dsexplorer.datastructures.simple;

import luz.dsexplorer.datastructures.DSType;
import com.sun.jna.Memory;

public class ByteArray extends DefaultDatastructure {

    public ByteArray() {
        super();
        name = getClass().getSimpleName();
        byteCount = 32;
        byteCountFix = false;
    }

    ;

    @Override
    public Object eval(Memory buffer) {
        return buffer.getByteArray(0, byteCount);
    }

    @Override
    public DSType getType() {
        return DSType.ByteArray;
    }

    @Override
    public String valueToString(Object value) {
        if (value == null) return null;
        byte[] array = (byte[]) value;
        StringBuilder sb = new StringBuilder();
        for (byte b : array) sb.append(String.format("%1$02X", b));
        return sb.toString();
    }
}
