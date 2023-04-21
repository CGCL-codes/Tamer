package lab.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class DoubleMpiMessage extends MpiMessage<Double> {

    public static final int size = 12;

    public DoubleMpiMessage(Double val, int index) {
        super(val, index);
    }

    public DoubleMpiMessage() {
        super(Double.NaN, -1);
    }

    @Override
    protected MpiMessage createInstance(Double value, int i) {
        MpiMessage<Double> msg = new DoubleMpiMessage(value, i);
        return msg;
    }

    @Override
    protected Double readValue(DataInputStream in) throws IOException {
        return in.readDouble();
    }

    @Override
    protected void writeValue(DataOutputStream out, Double value) throws IOException {
        out.writeDouble(value);
    }

    @Override
    protected Double readValue(ByteBuffer bb) {
        return bb.getDouble();
    }

    @Override
    protected void writeValue(ByteBuffer bb, Double value) {
        bb.putDouble(value);
    }
}
